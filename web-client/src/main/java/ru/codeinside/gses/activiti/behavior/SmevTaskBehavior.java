/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import com.google.common.base.Predicate;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.behavior.TaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.activiti.engine.impl.jobexecutor.TimerDeclarationType;
import org.activiti.engine.impl.jobexecutor.TimerExecuteNestedActivityJobHandler;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TimerEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.util.xml.Element;
import org.activiti.engine.impl.util.xml.Parse;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.database.SmevTaskStrategy;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Iterables.getOnlyElement;

final public class SmevTaskBehavior extends TaskActivityBehavior implements TaskListener, ExecutionListener {

  final private SmevTaskConfig config;

  public SmevTaskBehavior(SmevTaskConfig config) {
    this.config = config;
  }

  // TODO: нужны ли эти события?
  @Override
  public void notify(DelegateExecution execution) throws Exception {
    logger().info("NOTIFY for execution: " + execution.getId());
  }

  // TODO: нужны ли эти события?
  @Override
  public void notify(DelegateTask delegateTask) {
    logger().info("NOTIFY for task: " + delegateTask.getId());
  }

  /**
   * Проверка исходящих BPMN переходов. Должно быть три перехода {reject*, result*, error*}
   */
  public void validateTransitions(ActivityImpl activity, Parse parse) {
    List<PvmTransition> outgoingTransitions = activity.getOutgoingTransitions();
    // общие требования
    for (PvmTransition outgoingTransition : outgoingTransitions) {
      Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
      if (condition != null) {
        parse.addError(
          String.format(
            "В блоке СМЭВ {%s} переход {%s} с условием не применим",
            activity.getId(), outgoingTransition.getId()),
          findElement(outgoingTransition.getId(), "sequenceFlow", parse));
      }
      String name = StringUtils.trimToNull((String) outgoingTransition.getProperty("name"));
      if (name == null) {
        parse.addError(String.format(
            "В блоке СМЭВ {%s} пропущено название перехода {%s}", activity.getId(), outgoingTransition.getId()),
          findElement(outgoingTransition.getId(), "sequenceFlow", parse));
      }
    }
    validatePrefix(activity, "reject", outgoingTransitions, parse);
    validatePrefix(activity, "error", outgoingTransitions, parse);
    validatePrefix(activity, "result", outgoingTransitions, parse);
  }

  private void validatePrefix(ActivityImpl activity, String prefix, Collection<PvmTransition> outgoingTransitions, Parse parse) {
    Collection<PvmTransition> transitions = filter(outgoingTransitions, withPrefix(prefix));
    if (transitions.size() != 1) {
      parse.addError(String.format(
          "Для блока СМЭВ {%s} должен быть один переход с префиксом {%s}", activity.getId(), prefix),
        findElement(activity.getId(), "serviceTask", parse));
    }
  }

  private Element findElement(String id, String tag, Parse parse) {
    Element rootElement = parse.getRootElement();
    return findElement(id, tag, rootElement);
  }

  private Element findElement(String id, String tag, Element element) {
    for (Element child : element.elements()) {
      if (tag.equals(child.getTagName()) && id.equals(child.attribute("id"))) {
        return child;
      }
      Element deep = findElement(id, tag, child);
      if (deep != null) {
        return deep;
      }
    }
    return null;
  }

  private Predicate<PvmTransition> withPrefix(final String prefix) {
    return new Predicate<PvmTransition>() {
      @Override
      public boolean apply(PvmTransition transition) {
        return transition.getId().toLowerCase().startsWith(prefix);
      }
    };
  }

  public void execute(ActivityExecution execution) throws Exception {
    execution.setActive(true);
    SmevInteraction interaction = new SmevInteraction(execution, config);
    interaction.initialize();
    if (!interaction.isFinished() || interaction.task.getStrategy() == SmevTaskStrategy.REQUEST) {
      interaction.nextStage();
      try {
        interaction.process();
      } catch (Exception e) {
        interaction.registerException(e);
      }
    }
    interaction.store();
    if (interaction.isFinished()) {
      //TODO: добавить состояния, не требующие действия пользователя
      if (interaction.isSuccess()) {
        leaveTo(execution, "result", interaction);
      } else {
        interaction.task.setNeedUserReaction(true);
        logger().info("Требуется решение исполнителя для этапа СМЭВ " + getFullId(execution));
//        execution.inactivate();
      }
    } else {
      execution.inactivate();
      scheduleNextStage(
        execution,
        interaction.isPool() ? interaction.getPingDelay() : interaction.getRecoveryDelay()
      );
    }
  }

  public void doUserAction(ActivityExecution execution, boolean repeat)  throws Exception {
    SmevInteraction interaction = new SmevInteraction(execution, config);
    interaction.updateVariables(repeat);
    interaction.store();
    if (repeat) {
      execute(execution);
    } else {
      leave(execution, interaction);
    }
  }

  // ------------ internals ------------

  private void leave(ActivityExecution execution, SmevInteraction interaction) {
    if (interaction.isSuccess())  {
      throw new IllegalStateException();
    }
    if (interaction.isReject()) {
      leaveTo(execution, "reject", interaction);
    } else {
      leaveTo(execution, "error", interaction);
    }
  }

  private void scheduleNextStage(ActivityExecution execution, Expression delay) {
    execution.setConcurrent(true);
    TimerDeclarationImpl timerDeclaration = new TimerDeclarationImpl(
      delay,
      TimerDeclarationType.DATE,
      TimerExecuteNestedActivityJobHandler.TYPE
    );
    logger().log(Level.INFO, "scheduleNextStage " + new Date() + " delay: " + delay.getExpressionText());
    timerDeclaration.setRetries(1);
    TimerEntity timer = timerDeclaration.prepareTimerEntity((ExecutionEntity)execution);
    timer.setJobHandlerConfiguration(execution.getCurrentActivityId());
    timer.setExclusive(true);
    Context
      .getCommandContext()
      .getJobManager()
      .schedule(timer);
  }

  private Logger logger() {
    return Logger.getLogger(getClass().getName());
  }


  private void leaveTo(ActivityExecution execution, String prefix, SmevInteraction interaction) {
    if (interaction.task.getFailure() != null) {
      execution.setVariable("smevError", interaction.task.getFailure());
    }
    PvmTransition active = getOnlyElement(filter(execution.getActivity().getOutgoingTransitions(), withPrefix(prefix)));
    execution.take(active);
    interaction.removeTask();
  }


  private String getFullId(ActivityExecution execution) {
    return execution.getProcessDefinitionId() + ":" + execution.getProcessInstanceId() + ":" + execution.getCurrentActivityId();
  }

}


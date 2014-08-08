/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import org.activiti.engine.ActivitiException;
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
import org.activiti.engine.impl.persistence.entity.TimerEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

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
   * Проверка исходящих BPMN переходов
   *
   * @param activity
   */
  public void validateTransitions(ActivityImpl activity) {
    List<PvmTransition> outgoingTransitions = activity.getOutgoingTransitions();
    for (PvmTransition outgoingTransition : outgoingTransitions) {
      Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
      if (condition != null) {
        throw new IllegalArgumentException(String.format(
          "Для блока СМЭВ {%s} обнаружен переход {%s} с условием!", activity.getId(), outgoingTransition.getId()
        ));
      }
    }
    if (outgoingTransitions.isEmpty()) {
      throw new IllegalArgumentException(String.format(
        "Для блока СМЭВ {%s} не обнаружено не одного перехода!", activity.getId()
      ));
    }
  }

  public void execute(ActivityExecution execution) throws Exception {
    SmevInteraction interaction = new SmevInteraction(execution, config);
    interaction.initialize();
    if (!interaction.isFinished()) {
      interaction.nextStage();
      try {
        interaction.process();
      } catch (Exception e) {
        interaction.registerException(e);
      }
    }
    interaction.store();
    if (interaction.isFinished()) {
      leave(execution, interaction);
    } else {
      scheduleNextStage(
        execution,
        interaction.isPool() ? interaction.getPingDelay() : interaction.getRecoveryDelay()
      );
    }
  }

  // ------------ internals ------------

  private void leave(ActivityExecution execution, SmevInteraction interaction) {
    boolean left;
    if (interaction.isSuccess()) {
      left = leaveTo(execution, "result", false);
    } else if (interaction.isReject()) {
      left = leaveTo(execution, "reject", false);
    } else if (interaction.isFailure()) {
      left = leaveTo(execution, "error", false);
    } else {
      throw new ActivitiException("На этапе СМЭВ " + getFullId(execution) + " неизвестный статус " + interaction.getResponseStatus());
    }
    if (!left) {
      logger().info("Требуется решение исполнителя для этапа СМЭВ " + getFullId(execution));
    }
    // TODO: удалить SmevTask ?
  }

  private void scheduleNextStage(ActivityExecution execution, Expression delay) {
    TimerDeclarationImpl timerDeclaration = new TimerDeclarationImpl(
      delay,
      TimerDeclarationType.DURATION,
      TimerExecuteNestedActivityJobHandler.TYPE
    );
    timerDeclaration.setRetries(1);
    TimerEntity timer = timerDeclaration.prepareTimerEntity(null);
    timer.setExecutionId(execution.getId());
    timer.setProcessInstanceId(execution.getProcessInstanceId());
    timer.setJobHandlerConfiguration(execution.getCurrentActivityId());
    Context
      .getCommandContext()
      .getJobManager()
      .schedule(timer);
  }

  private Logger logger() {
    return Logger.getLogger(getClass().getName());
  }


  private boolean leaveTo(ActivityExecution execution, String start, boolean required) {
    List<PvmTransition> transitionsToTake = new ArrayList<PvmTransition>();
    List<PvmTransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
    for (PvmTransition outgoingTransition : outgoingTransitions) {
      if (outgoingTransition.getId().startsWith(start)) {
        transitionsToTake.add(outgoingTransition);
      }
    }
    if (transitionsToTake.isEmpty()) {
      if (required) {
        throw new ActivitiException("Исходящий поток " + start + "* не найден!");
      }
      return false;
    }
    if (transitionsToTake.size() == 1) {
      execution.take(transitionsToTake.get(0));
      return true;
    }

    if (true) {
      throw fireConcurrentTransitions(execution, transitionsToTake);
    }
    execution.inactivate();
    List<ActivityExecution> noJoin = Collections.emptyList();
    execution.takeAll(transitionsToTake, noJoin);
    return true;
  }

  private ActivitiException fireConcurrentTransitions(ActivityExecution execution, List<PvmTransition> transitions) {
    StringBuilder msg = new StringBuilder("Обнаружено несколько исходящих потоков управления для СМЭВ блока ");
    msg.append(getFullId(execution));
    msg.append(": ");
    boolean first = true;
    for (PvmTransition transition : transitions) {
      if (first) {
        first = false;
      } else {
        msg.append(", ");
      }
      msg.append(transition.getId());
    }
    return new ActivitiException(msg.toString());
  }

  private String getFullId(ActivityExecution execution) {
    return execution.getProcessDefinitionId() + ":" + execution.getProcessInstanceId() + ":" + execution.getCurrentActivityId();
  }

}


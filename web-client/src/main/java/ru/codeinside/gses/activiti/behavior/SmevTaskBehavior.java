/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.behavior.TaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.util.xml.Element;
import org.activiti.engine.impl.util.xml.Parse;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Collections2.filter;

final public class SmevTaskBehavior extends TaskActivityBehavior {

  final private SmevTaskConfig config;

  public SmevTaskBehavior(SmevTaskConfig config) {
    this.config = config;
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
    Collection<PvmTransition> transitions = filter(outgoingTransitions, Transitions.withPrefix(prefix));
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

  public void execute(ActivityExecution execution) throws Exception {
    new SmevInteraction(execution, config).robotAction();
  }

  public void doUserAction(ActivityExecution execution, boolean repeat) throws Exception {
    new SmevInteraction(execution, config).humanAction(repeat);
  }
}


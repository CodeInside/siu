/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.listeners;

import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.util.xml.Element;
import ru.codeinside.gses.activiti.ReceiptEnsurance;

final public class GsesBpmnParseListener extends AbstractBpmnParseListener {

  private final ReceiptEnsurance receiptEnsurance;

  public GsesBpmnParseListener(ReceiptEnsurance receiptEnsurance) {
    this.receiptEnsurance = receiptEnsurance;
  }

  public void parseProcess(Element processElement, ProcessDefinitionEntity processDefinition) {
    processDefinition.addExecutionListener(ExecutionListener.EVENTNAME_END, new EndProcessListener(false, receiptEnsurance), 0);
    processDefinition.addExecutionListener(ExecutionListener.EVENTNAME_START, new StartProcessListener(), 0);
    processDefinition.addExecutionListener(ExecutionListener.EVENTNAME_TAKE, new TakeProcessListener(), 0);
  }

  public void parseEndEvent(Element endEventElement, ScopeImpl scope, ActivityImpl activity) {
    activity.addExecutionListener(ExecutionListener.EVENTNAME_END, new EndProcessListener(true, receiptEnsurance), 0);
  }

  public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
    ActivityBehavior activitybehaviour = activity.getActivityBehavior();
    if (activitybehaviour instanceof UserTaskActivityBehavior) {
      UserTaskActivityBehavior userTaskActivity = (UserTaskActivityBehavior) activitybehaviour;
      TaskProcessListener taskListener = new TaskProcessListener();
      userTaskActivity.getTaskDefinition().addTaskListener(TaskListener.EVENTNAME_CREATE, taskListener);
      userTaskActivity.getTaskDefinition().addTaskListener(TaskListener.EVENTNAME_COMPLETE, taskListener);
      userTaskActivity.getTaskDefinition().addTaskListener(TaskListener.EVENTNAME_ASSIGNMENT, taskListener);
    }
  }
}
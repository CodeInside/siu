/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

public class GetFormHandlerCommand implements Command<FullFormHandler> {

  final String processDefinitionId;
  final String taskId;
  final String login;
  final boolean withExecution;

  public GetFormHandlerCommand(boolean withExecution, String processDefinitionId, String taskId, String login) {
    this.withExecution = withExecution;
    this.processDefinitionId = processDefinitionId;
    this.taskId = taskId;
    this.login = login;
  }

  @Override
  public FullFormHandler execute(final CommandContext commandContext) {
    if (processDefinitionId != null) {
      final ProcessDefinitionEntity processDefinition = Context
        .getProcessEngineConfiguration()
        .getDeploymentCache()
        .findDeployedProcessDefinitionById(processDefinitionId);
      if (processDefinition == null) {
        throw new ActivitiException("No process definition found for id '" + processDefinitionId + "'");
      }
      final StartFormHandler startFormHandler = processDefinition.getStartFormHandler();
      if (startFormHandler == null) {
        throw new ActivitiException("No startFormHandler defined in process '" + processDefinitionId + "'");
      }
      return new FullFormHandler(startFormHandler, null);
    }
    final TaskEntity task = commandContext.getTaskManager().findTaskById(taskId);
    if (task == null) {
      throw new TaskGoneException(false);
    }
    if (!login.equals(task.getAssignee())) {
      throw new TaskGoneException(true);
    }
    if (task.getTaskDefinition() == null) {
      throw new IllegalStateException("No definition");
    }
    final TaskFormHandler taskFormHandler = task.getTaskDefinition().getTaskFormHandler();
    if (taskFormHandler == null) {
      throw new ActivitiException("No taskFormHandler specified for task '" + taskId + "'");
    }
    return new FullFormHandler(taskFormHandler, withExecution ? task.getExecution() : null);
  }

}

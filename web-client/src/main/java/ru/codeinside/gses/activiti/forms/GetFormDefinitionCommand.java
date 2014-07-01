/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import ru.codeinside.gses.activiti.forms.api.definitions.FormDefinitionProvider;
import ru.codeinside.gses.webui.form.TaskGoneException;

final class GetFormDefinitionCommand implements Command<FormDefinition> {

  final FormID id;
  final String login;

  public GetFormDefinitionCommand(FormID id, String login) {
    this.id = id;
    this.login = login;
  }

  @Override
  public FormDefinition execute(CommandContext commandContext) {
    ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
    configuration.getIdentityService().setAuthenticatedUserId(login);

    FormDefinition def = new FormDefinition();

    if (id.processDefinitionId != null) {
      def.processDefinition = configuration.getRepositoryService().createProcessDefinitionQuery()
        .processDefinitionId(id.processDefinitionId)
          //.startableByUser(login) не используем identityLink в Activiti
        .active()
        .singleResult();

      // есть права и описатель активен
      if (def.processDefinition != null) {
        StartFormHandler startFormHandler = configuration.getDeploymentCache()
          .findDeployedProcessDefinitionById(id.processDefinitionId).getStartFormHandler();
        def.propertyTree = ((FormDefinitionProvider) startFormHandler).getPropertyTree();
      } else {
        throw new ActivitiException("No process definition found for id '" + id.processDefinitionId + "'");
      }

    } else {
      def.task = configuration.getTaskService().createTaskQuery().taskAssignee(login).taskId(id.taskId).singleResult();
      // не исполнена и есть права
      if (def.task != null) {
        def.processDefinition = configuration.getRepositoryService().createProcessDefinitionQuery()
          .processDefinitionId(def.task.getProcessDefinitionId())
          .singleResult();
        TaskEntity taskEntity = commandContext.getTaskManager().findTaskById(id.taskId);
        def.execution = taskEntity.getExecution();
        TaskFormHandler taskFormHandler = taskEntity.getTaskDefinition().getTaskFormHandler();
        def.propertyTree = ((FormDefinitionProvider) taskFormHandler).getPropertyTree();
      } else {
        TaskEntity taskEntity = commandContext.getTaskManager().findTaskById(id.taskId);
        if (taskEntity == null) {
          throw new TaskGoneException(false);
        }
        throw new TaskGoneException(true);
      }
    }
    return def;
  }

}

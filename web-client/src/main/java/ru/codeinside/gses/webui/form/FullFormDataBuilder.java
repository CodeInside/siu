/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.form.FormData;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.activiti.FormDecorator;
import ru.codeinside.gses.activiti.FormID;
import ru.codeinside.gses.activiti.forms.CloneTree;
import ru.codeinside.gses.activiti.forms.CloneTreeProvider;
import ru.codeinside.gses.activiti.forms.PropertyTree;
import ru.codeinside.gses.activiti.forms.PropertyTreeProvider;
import ru.codeinside.gses.activiti.history.VariableFormData;
import ru.codeinside.gses.activiti.history.VariableSnapshot;

import java.util.Map;

/**
 * Формирование полного описателя:
 * - этап исполнения
 * - процесс исполнения
 * - декоратор со списком свойствам и карторй переменных.
 */
final public class FullFormDataBuilder {

  private final FormID id;
  private final String login;

  public FullFormDataBuilder(final FormID id, String login) {
    this.id = id;
    this.login = login;
  }

  public FullFormData build(final ProcessEngine engine) {
    final Task task;
    final ProcessDefinition processDefinition;
    final FormData formData;

    engine.getIdentityService().setAuthenticatedUserId(login);
    if (id.taskId == null) {
      task = null;
      processDefinition = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(id.processDefinitionId).singleResult();
      formData = engine.getFormService().getStartFormData(id.processDefinitionId);
    } else {
      task = engine.getTaskService().createTaskQuery()
        .taskAssignee(login).taskId(id.taskId)
        .singleResult();
      // нет прав либо уже исполнена?
      if (task == null) {
        return null;
      }
      processDefinition = engine.getRepositoryService().createProcessDefinitionQuery()
        .processDefinitionId(task.getProcessDefinitionId())
        .singleResult();
      formData = engine.getFormService().getTaskFormData(id.taskId);
    }
    if (formData == null) {
      return null;
    }

    final PropertyTree nodeMap = ((PropertyTreeProvider) formData).getPropertyTree();
    final Map<String, VariableSnapshot> map;
    if (id.taskId != null) {
      CloneTree cloneTree = ((CloneTreeProvider) formData).getCloneTree();
      map = ((ServiceImpl) engine.getFormService())
        .getCommandExecutor()
        .execute(new GetVariableSnapshotsCommand(task.getId(), cloneTree.handlers));
    } else {
      map = ImmutableMap.of();
    }
    return new FullFormData(task, processDefinition, new FormDecorator(id, new VariableFormData(formData, map, nodeMap)));
  }

}

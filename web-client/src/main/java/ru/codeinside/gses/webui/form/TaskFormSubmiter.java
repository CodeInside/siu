/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.activiti.ActivitiFormProperties;
import ru.codeinside.gses.activiti.forms.CloneSupport;
import ru.codeinside.gses.activiti.forms.TypeTree;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.Flash;

import java.util.List;

class TaskFormSubmiter implements PF<Boolean> {
  private static final long serialVersionUID = 1L;

  private final String taskId;
  private final List<Form> forms;

  TaskFormSubmiter(String taskId, List<Form> forms) {
    this.taskId = taskId;
    this.forms = forms;
  }

  public Boolean apply(ProcessEngine engine) {
    final String login = Flash.login();

    engine.getIdentityService().setAuthenticatedUserId(login);

    Task task = engine.getTaskService().createTaskQuery().taskAssignee(login)
      .taskId(taskId).singleResult();
    if (task == null) {
      return false;
    }

    final CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
    final FullFormHandler fullFormHandler = commandExecutor.execute(new GetFormHandlerCommand(false, null, taskId, login));
    TypeTree typeTree = ((CloneSupport) fullFormHandler.formHandler).getTypeTree();

    ActivitiFormProperties properties = ActivitiFormProperties.createForTypeTree(typeTree, forms);
    properties.createAttachments(engine, taskId, task.getProcessInstanceId());
    engine.getFormService().submitTaskFormData(taskId, properties.formPropertyValues);
    return true;
  }
}

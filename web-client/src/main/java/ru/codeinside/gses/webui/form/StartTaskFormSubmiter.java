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
import ru.codeinside.gses.activiti.ActivitiFormProperties;
import ru.codeinside.gses.activiti.forms.CloneSupport;
import ru.codeinside.gses.activiti.forms.TypeTree;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.Flash;

import java.util.List;

final public class StartTaskFormSubmiter implements PF<String> {
  private static final long serialVersionUID = 1L;

  private final String processDefinitionId;
  private final List<Form> forms;

  StartTaskFormSubmiter(String processDefinitionId, List<Form> forms) {
    this.processDefinitionId = processDefinitionId;
    this.forms = forms;
  }

  public String apply(ProcessEngine engine) {
    String login = Flash.login();
    engine.getIdentityService().setAuthenticatedUserId(login);

    final CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
    final FullFormHandler fullFormHandler = commandExecutor.execute(new GetFormHandlerCommand(false, processDefinitionId, null, login));
    TypeTree typeTree = ((CloneSupport) fullFormHandler.formHandler).getTypeTree();

    ActivitiFormProperties properties = ActivitiFormProperties.createForTypeTree(typeTree, forms);
    return Flash.flash().getDeclarantService().createProcess(engine, processDefinitionId, properties, login);
  }
}

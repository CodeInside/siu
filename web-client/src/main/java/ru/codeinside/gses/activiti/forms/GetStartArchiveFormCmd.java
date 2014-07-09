/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import ru.codeinside.gses.activiti.forms.api.definitions.FormDefinitionProvider;
import ru.codeinside.gses.activiti.forms.api.values.FormValue;
import ru.codeinside.gses.activiti.forms.values.PropertyValuesBuilder;

import java.io.Serializable;
import java.util.Map;

public class GetStartArchiveFormCmd implements Command<FormValue>, Serializable {

  private static final long serialVersionUID = 1L;
  private final Map<String, String> historyValues;
  protected String processDefinitionId;

  public GetStartArchiveFormCmd(String processDefinitionId, Map<String, String> historyValues) {
    this.processDefinitionId = processDefinitionId;
    this.historyValues = historyValues;
  }

  public FormValue execute(CommandContext commandContext) {

    ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();

    FormDefinition def = new FormDefinition();


      def.processDefinition = configuration.getRepositoryService().createProcessDefinitionQuery()
        .processDefinitionId(processDefinitionId)
        .singleResult();

      if (def.processDefinition != null) {
        StartFormHandler startFormHandler = configuration.getDeploymentCache()
          .findDeployedProcessDefinitionById(processDefinitionId).getStartFormHandler();
        def.propertyTree = ((FormDefinitionProvider) startFormHandler).getPropertyTree();
      } else {
        throw new ActivitiException("No process definition found for id '" + processDefinitionId + "'");
      }

    return new PropertyValuesBuilder(null, null, historyValues).build(def.propertyTree, null, def.processDefinition);

  }
}
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.executor;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;

import java.io.Serializable;
import java.util.Map;

public class GetStartArchiveFormCmd implements Command<StartFormData>, Serializable {

  private static final long serialVersionUID = 1L;
  private final Map<String, String> historyValues;
  protected String processDefinitionId;

  public GetStartArchiveFormCmd(String processDefinitionId, Map<String, String> historyValues) {
    this.processDefinitionId = processDefinitionId;
    this.historyValues = historyValues;
  }

  public StartFormData execute(CommandContext commandContext) {
    ProcessDefinitionEntity processDefinition = Context
      .getProcessEngineConfiguration()
      .getDeploymentCache()
      .findDeployedProcessDefinitionById(processDefinitionId);
    if (processDefinition == null) {
      throw new ActivitiException("No process definition found for id '" + processDefinitionId + "'");
    }

    StartFormHandler startFormHandler = processDefinition.getStartFormHandler();
    if (startFormHandler == null) {
      throw new ActivitiException("No startFormHandler defined in process '" + processDefinitionId + "'");
    }
    throw new UnsupportedOperationException("return ((CustomStartFormHandler)startFormHandler).createStartFormData(processDefinition, historyValues)");
  }
}
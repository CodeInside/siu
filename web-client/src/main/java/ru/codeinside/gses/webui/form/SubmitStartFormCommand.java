/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cmd.CreateAttachmentCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSession;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Map;

import static com.google.common.base.Objects.equal;

public class SubmitStartFormCommand implements Command<ProcessInstance>, Serializable {

  private static final long serialVersionUID = 1L;

  private final String processDefinitionId;
  private final Map<String, String> properties;
  private final Map<String, FileValue> files;
  private final boolean fromSmev;

  //TODO продумать использование не публичного конструктора?
  public SubmitStartFormCommand(String processDefinitionId, Map<String, String> properties, Map<String, FileValue> files) {
    this.processDefinitionId = processDefinitionId;
    this.properties = properties;
    this.files = files;
    this.fromSmev = false;
  }

  public SubmitStartFormCommand(String processDefinitionId, Map<String, String> properties, Map<String, FileValue> files, boolean fromSmev) {
    this.processDefinitionId = processDefinitionId;
    this.properties = properties;
    this.files = files;
    this.fromSmev = fromSmev;
  }

  @Override
  public ProcessInstance execute(CommandContext commandContext) {
    ProcessDefinitionEntity processDefinition = Context.getProcessEngineConfiguration().getDeploymentCache()
      .findDeployedProcessDefinitionById(processDefinitionId);
    if (processDefinition == null) {
      throw new ActivitiException("No process definition found for id = '" + processDefinitionId + "'");
    }

    ExecutionEntity processInstance = processDefinition.createProcessInstance();

    int countFiles = 0;
    int countSignFiles = 0;
    // <!-- вставка значения свойства вложений
    for (final String propertyId : properties.keySet()) {
      if (files.containsKey(propertyId)) {
        final FileValue fileValue = files.get(propertyId);
        final CreateAttachmentCmd createAttachmentCmd = new CreateAttachmentCmd(//
          fileValue.getMimeType(), // attachmentType
          null, // taskId
          processInstance.getProcessInstanceId(), // processInstanceId
          fileValue.getFileName(), // attachmentName
          null, // attachmentDescription
          new ByteArrayInputStream(fileValue.getContent()), // content
          null // url
        );
        final Attachment attachment = createAttachmentCmd.execute(commandContext);
        properties.put(propertyId, AttachmentFFT.stringValue(attachment));

        final HistoricDbSqlSession session = (HistoricDbSqlSession) commandContext.getSession(DbSqlSession.class);
        boolean added = session.addSignaturesBySmevFileValue(processDefinitionId, propertyId, fileValue);
        countSignFiles += (added ? 1 : 0);
        countFiles += 1;
      }
    }

    // -->

    int historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
    if (historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_ACTIVITY) {
      DbSqlSession dbSqlSession = commandContext.getSession(DbSqlSession.class);

      if (historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {
        for (String propertyId : properties.keySet()) {
          String propertyValue = properties.get(propertyId);
          HistoricFormPropertyEntity historicFormProperty = new HistoricFormPropertyEntity(processInstance,
            propertyId, propertyValue);
          dbSqlSession.insert(historicFormProperty);
        }
      }
    }
    if (fromSmev) {
      StartFormData startFormData = Context.getProcessEngineConfiguration().getFormService().getStartFormData(processDefinitionId);
      for (FormProperty formProperty : startFormData.getFormProperties()) {
        if (formProperty.getType() != null && equal("signature", formProperty.getType().getName())) {
          if (countFiles == 0 || countFiles == countSignFiles) {
            properties.put(formProperty.getId(), "1"); //fake value
          }
        }
      }
    }

    StartFormHandler startFormHandler = processDefinition.getStartFormHandler();
    startFormHandler.submitFormProperties(properties, processInstance);

    processInstance.start();

    return processInstance;
  }

}

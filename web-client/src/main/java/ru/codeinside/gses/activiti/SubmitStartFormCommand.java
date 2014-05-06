/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cmd.CreateAttachmentCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.activiti.engine.task.Attachment;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.BidStatus;
import ru.codeinside.adm.database.BidWorkers;
import ru.codeinside.adm.database.Directory;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.Service;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSession;
import ru.codeinside.gses.service.BidID;
import ru.codeinside.gses.service.DeclarantService;

import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Objects.equal;

final public class SubmitStartFormCommand implements Command<BidID>, Serializable {

  private static final long serialVersionUID = 1L;

  private final String requestIdRef;
  private final String componentName;
  private final String processDefinitionId;
  private EntityManager em_;
  private final Map<String, String> properties;
  private final Map<String, FileValue> files;
  private final String declarer;
  private final String tag;

  public SubmitStartFormCommand(
    String requestIdRef, String componentName,
    String processDefinitionId,
    Map<String, String> properties, Map<String, FileValue> files,
    String declarer, String tag, EntityManager em) {

    this.requestIdRef = requestIdRef;
    this.componentName = componentName;
    this.processDefinitionId = processDefinitionId;
    em_ = em;
    this.properties = new LinkedHashMap<String, String>(properties);
    this.files = files;
    this.declarer = declarer;
    this.tag = tag == null ? "" : tag;
  }

  @Override
  public BidID execute(CommandContext commandContext) {
    identityService().setAuthenticatedUserId(requestIdRef == null ? null : declarer);

    ProcessDefinitionEntity processDefinition = deploymentCache().findDeployedProcessDefinitionById(processDefinitionId);
    if (processDefinition == null) {
      throw new ActivitiException("No process definition found for id = '" + processDefinitionId + "'");
    }

    EntityManager em = em_ == null ? entityManger(commandContext) : em_;
    ProcedureProcessDefinition procedureDef = em.find(ProcedureProcessDefinition.class, processDefinitionId);
    if (procedureDef == null) {
      throw new ActivitiException("No procedure found for id = '" + processDefinitionId + "'");
    }
    addCustomProperties(em, procedureDef);


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

    int historyLevel = historyLevel();
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

    if (requestIdRef != null) {
      StartFormData startFormData = formService().getStartFormData(processDefinitionId);
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

    Bid bid = createBid(em, procedureDef, processInstance);

    processInstance.start();

    return new BidID(bid.getId(), Long.parseLong(bid.getProcessInstanceId()));
  }


  private Bid createBid(EntityManager em, ProcedureProcessDefinition procedureDef, ExecutionEntity processInstance) {
    Employee employee = requestIdRef == null ? em.find(Employee.class, declarer) : null;

    Bid bid = new Bid();
    bid.setTag(tag);
    bid.setDeclarant(declarer == null ? "" : declarer);
    bid.setStatus(BidStatus.New);
    bid.setProcessInstanceId(processInstance.getProcessInstanceId());
    bid.setEmployee(employee);

    bid.setProcedureProcessDefinition(procedureDef);
    if (procedureDef != null) {
      Procedure procedure = procedureDef.getProcedure();
      bid.setProcedure(procedure);
      bid.setVersion(procedure.getVersion());
    }
    em.persist(bid);

    if (requestIdRef != null) {
      List<ExternalGlue> glues = em
        .createQuery("select e from ExternalGlue e where e.requestIdRef=:ref", ExternalGlue.class)
        .setParameter("ref", requestIdRef).getResultList();
      ExternalGlue externalGlue;
      if (glues.isEmpty()) {
        externalGlue = new ExternalGlue();
        externalGlue.setName(componentName);
        externalGlue.setRequestIdRef(requestIdRef);
        externalGlue.setId(bid.getId());
        em.persist(externalGlue);
      } else {
        externalGlue = glues.get(0);
      }
      bid.setGlue(externalGlue);
      externalGlue.getBids().add(bid);
      em.persist(bid);
      em.persist(externalGlue);
    }

    if (employee != null) {
      em.persist(new BidWorkers(bid, employee));
    }

    em.flush();

    return bid;
  }


  private void addCustomProperties(EntityManager em, ProcedureProcessDefinition procedureDef) {
    Procedure procedure = procedureDef.getProcedure();
    Service service = procedure.getService();

    properties.put(DeclarantService.VAR_PROCEDURE_TYPE_NAME, Integer.toString(procedure.getType().ordinal()));
    properties.put(DeclarantService.VAR_PROCEDURE_ID, procedure.getId());
    if (componentName == null) {
      properties.put(DeclarantService.VAR_REQUESTER_LOGIN, declarer);
    }
    if (service != null) {
      properties.put(DeclarantService.VAR_SERVICE_ID, Long.toString(service.getId()));
      for (String dt : service.getDeclarantTypes()) {
        Directory directory = em.find(Directory.class, DeclarantService.DECLARANT_TYPES);
        final String val;
        if (directory != null && directory.getValues().get(dt) != null) {
          val = directory.getValues().get(dt);
        } else {
          val = dt;
        }
        properties.put(dt, val);
      }
    }
  }

  private ProcessEngineConfigurationImpl config() {
    return Context.getProcessEngineConfiguration();
  }

  private FormService formService() {
    return config().getFormService();
  }

  private int historyLevel() {
    return config().getHistoryLevel();
  }

  private DeploymentCache deploymentCache() {
    return config().getDeploymentCache();
  }

  private IdentityService identityService() {
    return config().getIdentityService();
  }

  private EntityManager entityManger(CommandContext commandContext) {
    return commandContext.getSession(EntityManagerSession.class).getEntityManager();
  }

}

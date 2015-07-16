/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.variable.EntityManagerSession;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.BidStatus;
import ru.codeinside.adm.database.BidWorkers;
import ru.codeinside.adm.database.Directory;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.InfoSystem;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.Service;
import ru.codeinside.adm.database.SmevChain;
import ru.codeinside.gses.activiti.forms.CustomStartFormHandler;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.activiti.forms.SubmitFormDataCmd;
import ru.codeinside.gses.activiti.forms.api.definitions.FormDefinitionProvider;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.api.duration.DurationPreference;
import ru.codeinside.gses.activiti.forms.api.duration.LazyCalendar;
import ru.codeinside.gses.service.BidID;
import ru.codeinside.gses.service.DeclarantService;
import ru.codeinside.gses.webui.form.DataAccumulator;
import ru.codeinside.gses.webui.form.ProcessInstanceAttachmentConverter;
import ru.codeinside.gses.webui.form.SignatureType;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitStartFormCommand implements Command<BidID>, Serializable {

  private static final long serialVersionUID = 1L;

  private final SmevChain smevChain;
  private final String componentName;
  private final String processDefinitionId;
  private final Map<SignatureType, Signatures> signatures;
  private final DataAccumulator accumulator;
  private final Map<String, Object> properties;
  private final String declarer;
  private final String tag;

  public SubmitStartFormCommand(
    SmevChain smevChain, String componentName,
    String processDefinitionId,
    Map<String, Object> properties,
    Map<SignatureType, Signatures> signatures,
    String declarer, String tag,
    DataAccumulator accumulator) {

    this.smevChain = smevChain;
    this.componentName = componentName;
    this.processDefinitionId = processDefinitionId;
    this.signatures = signatures;
    this.accumulator = accumulator;
    this.properties = new HashMap<String, Object>(properties);
    this.declarer = declarer;
    this.tag = tag == null ? "" : tag;
  }

  @Override
  public BidID execute(CommandContext commandContext) {
    identityService().setAuthenticatedUserId(smevChain == null ? declarer : null);

    ProcessDefinitionEntity processDefinition = deploymentCache().findDeployedProcessDefinitionById(processDefinitionId);
    if (processDefinition == null) {
      throw new ActivitiException("No process definition found for id = '" + processDefinitionId + "'");
    }

    EntityManager em = entityManger(commandContext);
    ProcedureProcessDefinition procedureDef = em.find(ProcedureProcessDefinition.class, processDefinitionId);
    if (procedureDef == null) {
      throw new ActivitiException("No procedure found for id = '" + processDefinitionId + "'");
    }
    addCustomProperties(em, procedureDef);

    ExecutionEntity processInstance = processDefinition.createProcessInstance();
    StartFormHandler startFormHandler = processDefinition.getStartFormHandler();
    PropertyTree propertyTree = ((FormDefinitionProvider) startFormHandler).getPropertyTree();
    new SubmitFormDataCmd(
        propertyTree,
        processInstance,
        properties,
        signatures,
        new ProcessInstanceAttachmentConverter(processInstance.getProcessInstanceId()),
        accumulator).execute(commandContext);

    Bid bid = createBid(em, procedureDef, processInstance);

    processInstance.start();

    return new BidID(bid.getId(), Long.parseLong(bid.getProcessInstanceId()));
  }


  private Bid createBid(EntityManager em, ProcedureProcessDefinition procedureDef, ExecutionEntity processInstance) {
    Employee employee = smevChain == null ? em.find(Employee.class, declarer) : null;

    Bid bid = new Bid();
    setExecutionDates(bid, processInstance);
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

    if (smevChain != null) {
      List<ExternalGlue> glues = em
        .createQuery("select e from ExternalGlue e where e.requestIdRef=:ref", ExternalGlue.class)
        .setParameter("ref", smevChain.originRequestIdRef).getResultList();
      ExternalGlue externalGlue;
      if (glues.isEmpty()) {
        externalGlue = new ExternalGlue();
        externalGlue.setName(componentName);
        externalGlue.setRequestIdRef(smevChain.originRequestIdRef);
        externalGlue.setId(bid.getId());
        {
          ru.codeinside.gws.api.InfoSystem senderSystem = smevChain.sender;
          if (senderSystem != null) {
            InfoSystem sender = em.find(InfoSystem.class, senderSystem.code);
            if (sender == null) {
              sender = new InfoSystem(senderSystem.code, senderSystem.name);
              em.persist(sender);
            }
            externalGlue.setSender(sender);
          }
          ru.codeinside.gws.api.InfoSystem recipientSystem = smevChain.recipient;
          if (recipientSystem != null) {
            InfoSystem recipient = em.find(InfoSystem.class, recipientSystem.code);
            if (recipient == null) {
              recipient = new InfoSystem(recipientSystem.code, recipientSystem.name);
              em.persist(recipient);
            }
            externalGlue.setRecipient(recipient);
          }
        }
        {
          ru.codeinside.gws.api.InfoSystem originSystem = smevChain.originator;
          if (originSystem != null) {
            InfoSystem origin = em.find(InfoSystem.class, originSystem.code);
            if (origin == null) {
              origin = new InfoSystem(originSystem.code, originSystem.name);
              em.persist(origin);
            }
            externalGlue.setOrigin(origin);
          }
        }
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

  void setExecutionDates(Bid bid, ExecutionEntity processInstance) {
    ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) processInstance.getProcessDefinition();
    CustomStartFormHandler startFormHandler = (CustomStartFormHandler) processDefinition.getStartFormHandler();
    DurationPreference durationPreference = startFormHandler.getPropertyTree().getDurationPreference();
    durationPreference.initializeProcessDates(bid, new LazyCalendar());
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

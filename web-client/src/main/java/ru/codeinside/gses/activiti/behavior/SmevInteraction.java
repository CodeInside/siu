/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import com.sun.xml.ws.client.ClientTransportException;
import commons.Exceptions;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.FixedValue;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.activiti.engine.impl.jobexecutor.TimerDeclarationType;
import org.activiti.engine.impl.jobexecutor.TimerExecuteNestedActivityJobHandler;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TimerEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.variable.EntityManagerSession;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.ClientRequestEntity;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.adm.database.SmevRequestType;
import ru.codeinside.adm.database.SmevResponseType;
import ru.codeinside.adm.database.SmevTask;
import ru.codeinside.adm.database.SmevTaskStrategy;
import ru.codeinside.gses.API;
import ru.codeinside.gses.beans.Smev;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.form.FormOvSignatureSeq;
import ru.codeinside.gses.webui.form.ProtocolUtils;
import ru.codeinside.gses.webui.form.TaskGoneException;
import ru.codeinside.gses.webui.osgi.LogCustomizer;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientLog;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Iterables.getOnlyElement;

final public class SmevInteraction {

  final Logger logger = Logger.getLogger(getClass().getName());

  /**
   * Аннотация тут лишь как подсказка для IDEA, в каком модуле искать сущности
   */
  @PersistenceContext(unitName = "myPU")
  final EntityManager em;

  final ActivityExecution execution;
  final SmevTaskConfig config;

  SmevStage stage;
  SmevTask task;

  SmevRequestType lastRequestType;
  SmevResponseType lastResponseStatus;

  public SmevInteraction(ActivityExecution execution, SmevTaskConfig config) {
    this.execution = execution;
    this.config = config;
    em = Context.getCommandContext().getSession(EntityManagerSession.class).getEntityManager();
    stage = SmevStage.ENTER;
  }

  public void robotAction() {
    List<SmevTask> tasks = findSmevTask();
    if (tasks.isEmpty()) {
      task = new SmevTask();
      task.setTaskId(execution.getCurrentActivityId());
      task.setExecutionId(execution.getId());
      task.setProcessInstanceId(execution.getProcessInstanceId());
      task.setErrorMaxCount(config.retryCount.getValue(execution));
      task.setErrorDelay(config.retryInterval.getValue(execution));
      task.setPingMaxCount(config.pingCount.getValue(execution));
      task.setPingDelay(config.pingInterval.getValue(execution));
      task.setConsumer(config.consumer.getValue(execution));
      task.setStrategy(config.strategy.getValue(execution));
      task.setGroups(config.candidateGroup.getValue(execution));
      task.setBid(AdminServiceProvider.get().getBidByProcessInstanceId(execution.getProcessInstanceId()));
      String login = Authentication.getAuthenticatedUserId();
      if (login != null) {
        task.setEmployee(em.find(Employee.class, login));
      }
    } else {
      task = tasks.get(0);
      lastResponseStatus = getResponseStatus();
      lastRequestType = getRequestStatus();
    }
    execute();
  }

  void humanAction(boolean repeat) {
    List<SmevTask> tasks = findSmevTask();
    if (tasks.isEmpty()) {
      throw new TaskGoneException(false);
    }
    task = tasks.get(0);
    lastResponseStatus = getResponseStatus();
    lastRequestType = getRequestStatus();
    if (repeat) {
      task.setNeedUserReaction(false);
      task.setPingCount(0);
      task.setErrorCount(0);
      execute();
    } else {
      if (isFailure()) {
        leaveTo("error");
      } else {
        leaveTo("reject");
      }
    }
  }

  private List<SmevTask> findSmevTask() {
    List<SmevTask> tasks = em.createQuery("select t from SmevTask t where " +
        "t.taskId=:taskId and t.executionId=:executionId and t.processInstanceId=:processId", SmevTask.class)
        .setParameter("taskId", execution.getCurrentActivityId())
        .setParameter("executionId", execution.getId())
        .setParameter("processId", execution.getProcessInstanceId())
        .getResultList();
    if (tasks.size() > 1) {
      throw new IllegalStateException("Duplicate smevTask " +
          execution.getProcessInstanceId() + ":" + execution.getId() + ":" + execution.getCurrentActivityId()
      );
    }
    return tasks;
  }

  private String createSoapFaultMessage(SOAPFaultException failure) {
    SOAPFault fault = failure.getFault();
    StringBuilder message = new StringBuilder("Ошибка взаимодействия с поставщиком услуги");
    Name code = fault.getFaultCodeAsName();
    if (code != null) {
      message.append(" (").append(code.getLocalName()).append(')');
    }
    message.append(":\n");
    message.append(fault.getFaultString());
    return message.toString();
  }

  private SmevResponseType getResponseStatus() {
    return task.getResponseType();
  }

  private SmevRequestType getRequestStatus() {
    return task.getRequestType();
  }

  private boolean isSuccess() {
    SmevResponseType responseStatus = getResponseStatus();
    return SmevResponseType.RESULT == responseStatus || SmevResponseType.STATE == responseStatus;
  }

  private boolean isReject() {
    SmevResponseType responseStatus = getResponseStatus();
    return SmevResponseType.REJECT == responseStatus;
  }

  private boolean isFailure() {
    SmevResponseType responseStatus = getResponseStatus();
    return SmevResponseType.INVALID == responseStatus || SmevResponseType.FAILURE == responseStatus;
  }

  private boolean isPool() {
    SmevResponseType responseStatus = getResponseStatus();
    return SmevResponseType.ACCEPT == responseStatus || SmevResponseType.PROCESS == responseStatus;
  }


  // TODO: точка использования сервисов OSGI - сервисы могут быть НЕ доступны, и их нужно освобождать!
  private String processNextStage() {
    ClientRequest request;
    ClientResponse response;
    Smev smev;
    InfoSystemService service;
    Client client;
    ClientLog clientLog = null;
    String servicePort;
    Revision serviceRevision;
    ClientExchangeContext gwsContext;
    URL serviceWsdl;
    Bid bid;

    stage = SmevStage.REQUEST_PREPARE;

    {
      bid = task.getBid();
      if (bid == null) {
        throw new IllegalStateException("Нет заявки для процесса {" + execution.getProcessInstanceId() + "}");
      }
      ProcessEngineConfigurationImpl cfg = Context.getProcessEngineConfiguration();
      smev = (Smev) cfg.getExpressionManager().createExpression("#{smev}").getValue(execution);
      service = smev.validateAndGetService(task.getConsumer());
      ru.codeinside.adm.database.InfoSystem sender = service.getSource();
      if (sender == null) {
        sender = smev.getDefaultSender();
      }
      if (sender == null) {
        throw new IllegalStateException("Ошибка в конфигурации, не задана основная ифнормационнця система");
      }

      client = smev.findByNameAndVersion(task.getConsumer(), service.getSversion()); // OSGI - ресурс!
      serviceWsdl = client.getWsdlUrl();
      if (serviceWsdl == null) {
        throw new IllegalStateException("Ошибка в реализации потребителя, не задан WSDL");
      }
      serviceRevision = client.getRevision();
      if (serviceRevision == null) {
        throw new IllegalStateException("Ошибка в реализации потребителя, не задана ревизия СМЭВ");
      }

      gwsContext = new ClientExchangeContext(execution, task.getConsumer());
      gwsContext.setOriginRequestId(task.getOriginId());
      gwsContext.setRequestId(task.getRequestId());
      ru.codeinside.adm.database.InfoSystem origin = null;
      ExternalGlue glue = bid.getGlue();
      if (glue != null) {
        origin = glue.getOrigin(); // первоисточник, если есть
        if (origin == null) {
          origin = glue.getSender(); // оправитель прямого запроса
        }
      }
      // TODO: не нужно - клиент эту переменную ЗАПИСЫВАЕТ а не читает
      gwsContext.setPool(
          task.getStrategy() == SmevTaskStrategy.PING &&
              (lastRequestType == SmevRequestType.PING || lastRequestType == SmevRequestType.REQUEST)
      );

      stage = SmevStage.REQUEST;

      servicePort = Fn.trimToNull(service.getAddress());

      //serviceName нужен, чтобы в случае параллельного выполнения отличались имена переменных в разных потоках
      String serviceName = service.getSname();
      Long requestId = (Long) gwsContext.getVariable(serviceName + FormOvSignatureSeq.REQUEST_ID);
      boolean isDataFlow = (requestId != null);

      if (isDataFlow && !ProtocolUtils.isPing(gwsContext)) {
        ClientRequestEntity entity = AdminServiceProvider.get().getClientRequestEntity(requestId);
        request = smev.createClientRequest(entity, gwsContext, execution.getId(), "");
      } else {
        ProtocolUtils.writeInfoSystemsToContext(service, gwsContext);
        request = client.createClientRequest(gwsContext);
        if (request == null || request.packet == null) {
          throw new IllegalStateException("Ошибка в реализации потребителя, нет пакета данных");
        }
        task.setRequestType(SmevRequestType.fromStatus(request.packet.status));
        //TODO: использование logger вместо обработки IllegalStateException
        if (task.getStrategy() == SmevTaskStrategy.PING) {
          if (lastRequestType == null && task.getRequestType() != SmevRequestType.REQUEST ||
              lastRequestType != null && (task.getRequestType() != SmevRequestType.PING && lastResponseStatus != null)) {
            logger.warning("Ошибка в реализации потребителя " + task.getConsumer() + ", ошибка в типе запроса " + task.getRequestType());
          }
        }
        if (request.packet.status == Packet.Status.PING) {
          task.setPingCount(task.getPingCount() + 1);
        }

        if (servicePort != null) {
          request.portAddress = servicePort;
        }

      }
      
      fillRequestPacket(request, service, sender, origin);
    }

    stage = SmevStage.LOG;
    {
      boolean logEnabled = AdminServiceProvider.getBoolProperty(API.ENABLE_CLIENT_LOG) && service.isLogEnabled();
      if (logEnabled || AdminServiceProvider.getBoolProperty(API.LOG_ERRORS)) {
        boolean logErrors = AdminServiceProvider.getBoolProperty(API.LOG_ERRORS);
        String logStatus = AdminServiceProvider.get().getSystemProperty(API.LOG_STATUS);
        Set<String> remote = smev.parseRemote(servicePort);
        clientLog = LogCustomizer.createClientLog(bid.getId(), task.getConsumer(), execution.getProcessInstanceId(),
            logEnabled, logErrors, logStatus, remote);
      }
    }

    stage = SmevStage.NETWORK;
    try {
      response = smev.createProtocol(serviceRevision).send(serviceWsdl, request, clientLog); // OSGI ресурс!
    } catch (RuntimeException e) {
      stage = SmevStage.NETWORK_ERROR;
      smev.storeUnavailable(service);
      RuntimeException e2 = smev.processFailure(client, gwsContext, clientLog, e);
      if (e2 == null) {
        throw e;
      }
      throw e2;
    } finally {
      if (clientLog != null) {
        clientLog.close();
      }
    }
    stage = SmevStage.RESPONSE;
    if (response.verifyResult.error != null) {
      throw new IllegalStateException("Verification error: " + response.verifyResult.error);
    }
    if (task.getOriginId() == null) {
      task.setOriginId(response.packet.originRequestIdRef);
    }
    if (response.routerPacket != null && response.routerPacket.messageId != null) {
      task.setRequestId(response.routerPacket.messageId);
    } else {
      task.setRequestId(UUID.randomUUID().toString());
    }
    client.processClientResponse(response, gwsContext);
    task.setResponseType(SmevResponseType.fromStatus(response.packet.status));
    stage = SmevStage.LEAVE;
    return gwsContext.getSmevError();
  }

  private void fillRequestPacket(
      ClientRequest request,
      InfoSystemService service,
      ru.codeinside.adm.database.InfoSystem sender,
      ru.codeinside.adm.database.InfoSystem origin) {
    ru.codeinside.adm.database.InfoSystem recipient = service.getInfoSystem();
    if (request.packet.recipient == null) {
      request.packet.recipient = new InfoSystem(recipient.getCode(), recipient.getName());
    }
    if (request.packet.sender == null) {
      request.packet.sender = new InfoSystem(sender.getCode(), sender.getName());
    }
    if (origin != null && request.packet.originator == null) {
      request.packet.originator = new InfoSystem(origin.getCode(), origin.getName());
    }
    if (request.packet.requestIdRef == null) {
      request.packet.requestIdRef = task.getRequestId();
    }
    if (request.packet.originRequestIdRef == null) {
      request.packet.originRequestIdRef = task.getOriginId();
    }
    if (AdminServiceProvider.getBoolProperty(API.PRODUCTION_MODE)) {
      request.packet.testMsg = null;
    }
    if (request.packet.date == null) {
      request.packet.date = new Date();
    }
  }


  private void execute() {
    final boolean processRequired;
    boolean errorDetected = false;
    if (isSuccess() || isReject()) {
      logger.fine("success or reject");
      processRequired = false;
    } else if (isPool()) {
      logger.fine("pooling");
      processRequired = task.canProcess();
    } else if (isFailure()) {
      logger.fine("failure");
      processRequired = task.canProcess();
    } else {
      logger.fine("internals");
      processRequired = task.canProcess();
    }

    String smevError = null;

    if (processRequired) {
      task.setRevision(task.getRevision() + 1);
      task.setRequestType(null);
      task.setResponseType(null);
      task.setFailure(null);

      // контекст блока не относится к пользователю!
      String userId = Authentication.getAuthenticatedUserId();
      Authentication.setAuthenticatedUserId(null);
      try {
        smevError = processNextStage();
      } catch (Exception e) {
        StringBuilder sb = new StringBuilder().append(stage).append(":\n");
        if (e instanceof ClientTransportException) {
          sb.append(e.getMessage());
        } else if (e instanceof IllegalStateException) {
          sb.append(e.getMessage());
        } else if (e instanceof SOAPFaultException) {
          sb.append(createSoapFaultMessage((SOAPFaultException) e));
        } else {
          sb.append(Exceptions.toString(e));
        }
        errorDetected = true;
        task.registerFailure(sb.toString());
        // сохранять предыдущий тип запроса при ошибке формирования текущего
        if (task.getRequestType() == null) {
          task.setRequestType(lastRequestType);
        }
      } finally {
        Authentication.setAuthenticatedUserId(userId);
      }
    }

    final boolean leave;
    final boolean needHuman;
    if (isSuccess() || isReject()) {
      if (SmevResponseType.REJECT == task.getResponseType()) {
        task.setFailure(Fn.trimToNull(smevError));
      }
      leave = true;
      needHuman = false;
    } else if (isPool()) {
      leave = false;
      needHuman = task.needHumanReaction();
    } else if (isFailure()) {
      if (!errorDetected) {
        task.registerFailure(task.getResponseType().name + getReason(smevError));
      }
      leave = false;
      needHuman = task.needHumanReaction();
    } else {
      if (!errorDetected) {
        task.registerFailure(stage + ": " + task.getResponseType() + getReason(smevError));
      }
      leave = false;
      needHuman = task.needHumanReaction();
    }

    task.setNeedUserReaction(needHuman);
    task.setLastChange(new Date());
    em.persist(task);
    em.flush();

    if (needHuman) {
      logger.info("Требуется решение человека для {" +
              execution.getProcessDefinitionId() + ":" +
              execution.getProcessInstanceId() + ":" +
              execution.getCurrentActivityId() + "}"
      );

    } else if (leave) {
      if (isSuccess()) {
        leaveTo("result");
      } else if (isReject()) {
        leaveTo("reject");
      } else {
        leaveTo("error");
      }

    } else {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.SECOND, isPool() ? task.getPingDelay() : task.getErrorDelay());
      FixedValue nextRun = new FixedValue(calendar.getTime());
      logger.fine("scheduleNextStage at " + nextRun.getExpressionText());
      TimerDeclarationImpl timerDeclaration = new TimerDeclarationImpl(
          nextRun, TimerDeclarationType.DATE, TimerExecuteNestedActivityJobHandler.TYPE);
      timerDeclaration.setRetries(1);
      TimerEntity timer = timerDeclaration.prepareTimerEntity((ExecutionEntity) execution);
      timer.setJobHandlerConfiguration(execution.getCurrentActivityId());
      timer.setExclusive(true);
      Context.getCommandContext().getJobManager().schedule(timer);
    }
  }

  /**
   * Обоснование ошибки от поставщика, предоставленное потребителем:
   */
  private String getReason(String smevError) {
    smevError = Fn.trimToNull(smevError);
    return smevError == null ? "" : ("\nОбоснование: " + smevError);
  }

  void leaveTo(String prefix) {
    if (task.getFailure() != null || execution.hasVariable("smevError")) { // заменяем переменную!
      execution.setVariable("smevError", task.getFailure());
    }
    PvmTransition active = getOnlyElement(filter(execution.getActivity().getOutgoingTransitions(), Transitions.withPrefix(prefix)));
    em.remove(task);
    em.flush();
    execution.take(active);
  }


}

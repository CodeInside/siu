/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.FixedValue;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
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
import ru.codeinside.gses.webui.form.TaskGoneException;
import ru.codeinside.gses.webui.osgi.LogCustomizer;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientLog;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Revision;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

final public class SmevInteraction {

  final Logger logger = Logger.getLogger(getClass().getName());

  @PersistenceContext(unitName = "myPU") // для IDEA
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

  void initialize() {
    List<SmevTask> tasks = em.createQuery("select t from SmevTask t where " +
      "t.taskId=:taskId and t.executionId=:executionId and t.processInstanceId=:processId", SmevTask.class)
      .setParameter("taskId", execution.getCurrentActivityId())
      .setParameter("executionId", execution.getId())
      .setParameter("processId", execution.getProcessInstanceId())
      .getResultList();
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
  }

  void updateVariables(boolean repeat) {
    List<SmevTask> tasks = em.createQuery("select t from SmevTask t where " +
      "t.taskId=:taskId and t.executionId=:executionId and t.processInstanceId=:processId", SmevTask.class)
      .setParameter("taskId", execution.getCurrentActivityId())
      .setParameter("executionId", execution.getId())
      .setParameter("processId", execution.getProcessInstanceId())
      .getResultList();
    if (tasks.isEmpty()) {
      throw new TaskGoneException(false);
    }
    task = tasks.get(0);
    lastResponseStatus = getResponseStatus();
    lastRequestType = getRequestStatus();
    task.setNeedUserReaction(false);
    if (repeat && task.getStrategy() == SmevTaskStrategy.PING) {
      task.setPingCount(0);
      task.setErrorCount(0);
    }
  }

  public void registerException(Exception e) {
    StringBuilder sb = new StringBuilder("Стадия {" + stage + "} ");
    if (e instanceof IllegalStateException) {
      sb.append(e.getMessage());
    } else if (e instanceof SOAPFaultException) {
      sb.append(createSoapFaultMessage((SOAPFaultException) e));
      // есть ли смысл?
      //} else if (e instanceof WebServiceException) {
      //  sb.append(e.getMessage());
    } else {
      StringWriter sw = new StringWriter();
      Fn.trim(e).printStackTrace(new PrintWriter(sw));
      sb.append(sw.getBuffer());
    }
    task.setFailure(sb.toString());
  }

  private String createSoapFaultMessage(SOAPFaultException failure) {
    SOAPFault fault = failure.getFault();
    StringBuilder message = new StringBuilder("Ошибка поставщика");
    Name code = fault.getFaultCodeAsName();
    if (code != null) {
      message.append(" (").append(code.getLocalName()).append(')');
    }
    message.append(": ");
    message.append(fault.getFaultString());
    return message.toString();
  }

  public boolean isFinished() {
    if (task.getRevision() == 0) { // читый лист
      return false;
    }
    if (isSuccess() || isReject()) {
      return true;
    }
    if (task.getStrategy() == SmevTaskStrategy.REQUEST) {
      return true;
    }
    if (isPool()) {
      return task.getPingCount() >= task.getPingMaxCount();
    }
    if (isFailure()) {
      return task.getErrorCount() >= task.getErrorMaxCount();
    }
    return true;
  }

  public boolean isSuccess() {
    SmevResponseType responseStatus = getResponseStatus();
    return SmevResponseType.RESULT == responseStatus || SmevResponseType.STATE == responseStatus;
  }


  public SmevResponseType getResponseStatus() {
    return task.getResponseType();
  }

  public SmevRequestType getRequestStatus() {
    return task.getRequestType();
  }

  public boolean isReject() {
    SmevResponseType responseStatus = getResponseStatus();
    return SmevResponseType.REJECT == responseStatus;
  }

  public boolean isFailure() {
    SmevResponseType responseStatus = getResponseStatus();
    return (responseStatus == null && getRequestStatus() != null) ||
      SmevResponseType.INVALID == responseStatus ||
      SmevResponseType.FAILURE == responseStatus;
  }

  public boolean isPool() {
    SmevResponseType responseStatus = getResponseStatus();
    return SmevResponseType.ACCEPT == responseStatus || SmevResponseType.PROCESS == responseStatus;
  }

  public Expression getPingDelay() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, task.getPingDelay());
    return new FixedValue(calendar.getTime());
  }

  public Expression getRecoveryDelay() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, task.getErrorDelay());
    return new FixedValue(calendar.getTime());
  }


  // TODO: точка использования сервисов OSGI - сервисы могут быть НЕ доступны, и их нужно освобождать!
  void process() {
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
      ru.codeinside.adm.database.InfoSystem origin = null;
      String originRequestId = null;
      ExternalGlue glue = bid.getGlue();
      if (glue != null) {
        originRequestId = glue.getOriginRequestIdRef(); // например, через Портал гос-услуг.
        if (originRequestId == null) {
          originRequestId = glue.getRequestIdRef(); // прямой запрос к СИУ
        }
        origin = glue.getOrigin(); // первоисточник, если есть
        if (origin == null) {
          origin = glue.getSender(); // оправитель прямого запроса
        }
        gwsContext.setOriginRequestId(originRequestId);
      }
      if (task.getRequestId() != null) {
        gwsContext.setRequestId(task.getRequestId());
      }
      // TODO: не нужно - клиент эту переменную ЗАПИСЫВАЕТ а не читает
      gwsContext.setPool(
        task.getStrategy() == SmevTaskStrategy.PING &&
          (lastRequestType == SmevRequestType.PING || lastRequestType == SmevRequestType.REQUEST)
      );

      stage = SmevStage.REQUEST;
      request = client.createClientRequest(gwsContext);
      if (request == null || request.packet == null) {
        throw new IllegalStateException("Ошибка в реализации потребителя, нет пакета данных");
      }
      task.setRequestType(SmevRequestType.fromStatus(request.packet.status));
      if (task.getStrategy() == SmevTaskStrategy.PING) {
        if (lastRequestType == null && task.getRequestType() != SmevRequestType.REQUEST ||
          lastRequestType != null && (task.getRequestType() != SmevRequestType.PING && lastResponseStatus != null)) {
          throw new IllegalStateException("Ошибка в реализации потребителя, ошибка в типе запроса!");
        }
      }
      servicePort = StringUtils.trimToNull(service.getAddress());
      if (servicePort != null) {
        request.portAddress = servicePort;
      }
      ru.codeinside.adm.database.InfoSystem recipient = service.getInfoSystem();
      request.packet.recipient = new InfoSystem(recipient.getCode(), recipient.getName());
      request.packet.sender = new InfoSystem(sender.getCode(), sender.getName());
      if (glue != null) {
        origin = glue.getOrigin();
      }
      if (origin != null) {
        request.packet.originator = new InfoSystem(origin.getCode(), origin.getName());
      }
      request.packet.requestIdRef = task.getRequestId();
      if (originRequestId != null) {
        request.packet.originRequestIdRef = originRequestId;
      }
      if (AdminServiceProvider.getBoolProperty(API.PRODUCTION_MODE)) {
        request.packet.testMsg = null;
      }
      if (request.packet.date == null) {
        request.packet.date = new Date();
      }
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
      registerException(e);
      smev.storeUnavailable(service);
      e = smev.processFailure(client, gwsContext, clientLog, e);
      if (e == null) {
        return;
      }
      throw e;
    } finally {
      if (clientLog != null) {
        clientLog.close();
      }
    }
    stage = SmevStage.RESPONSE;
    if (task.getRequestId() == null) {
      task.setRequestId(response.packet.requestIdRef);
    }
    task.setResponseType(SmevResponseType.fromStatus(response.packet.status));
    client.processClientResponse(response, gwsContext);
  }

  public void nextStage() {
    task.setRevision(task.getRevision() + 1);
    if (isPool()) {
      task.setPingCount(task.getPingCount() + 1);
    }
    if (isFailure()) {
      task.setErrorCount(task.getErrorCount() + 1);
    }
    task.setRequestType(null);
    task.setResponseType(null);
    task.setFailure(null);
  }

  public void store() {
    task.setLastChange(new Date());
    em.persist(task);
    em.flush();
  }

  public void removeTask() {
    em.remove(task);
  }
}

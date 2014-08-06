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
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.adm.database.SmevTask;
import ru.codeinside.gses.API;
import ru.codeinside.gses.beans.ActivitiExchangeContext;
import ru.codeinside.gses.beans.Smev;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.osgi.LogCustomizer;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientLog;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

final public class SmevInteraction {

  final Logger logger = Logger.getLogger(getClass().getName());
  final EntityManager em;
  final ActivityExecution execution;
  final SmevTaskConfig config;
  final Variables variables;

  String state;
  SmevTask task;

  Packet.Status lastResponseStatus;
  Packet.Status lastRequestStatus;

  public SmevInteraction(ActivityExecution execution, SmevTaskConfig config) {
    state = "Создание";
    this.execution = execution;
    this.config = config;
    variables = new Variables(execution);
    em = Context.getCommandContext().getSession(EntityManagerSession.class).getEntityManager();
  }

  void initialize() {
    logger.info("SMEV task: " +
      execution.getCurrentActivityId() + "/" +
      execution.getId() + "/" +
      execution.getProcessInstanceId());
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
      task.setErrorMaxCount(variables.integer(config.retryCount, 5));
      task.setErrorDelay(variables.integer(config.retryIntervalSec, 600));
      task.setPingMaxCount(variables.integer(config.pingCount, 10));
      task.setPingDelay(variables.integer(config.pingIntervalSec, 60));
    } else {
      task = tasks.get(0);
      lastResponseStatus = getResponseStatus();
      lastRequestStatus = getRequestStatus();
    }
    state = "Инициализация";
  }

  public void registerException(Exception e) {
    StringBuilder sb = new StringBuilder("Режим " + state + "\n");
    StringWriter sw = new StringWriter();
    Fn.trim(e).printStackTrace(new PrintWriter(sw));
    sb.append(sw.getBuffer());
    task.setFailure(sb.toString());
  }

  public boolean isFinished() {
    if (task.getRevision() == 0) { // читый лист
      return false;
    }
    if (isSuccess() || isReject()) {
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
    Packet.Status responseStatus = getResponseStatus();
    return Packet.Status.RESULT == responseStatus ||
      Packet.Status.STATE == responseStatus;
  }


  public Packet.Status getResponseStatus() {
    String responseStatus = task.getResponseStatus();
    return responseStatus == null ? null : Packet.Status.valueOf(responseStatus);
  }

  public Packet.Status getRequestStatus() {
    String responseStatus = task.getRequestStatus();
    return responseStatus == null ? null : Packet.Status.valueOf(responseStatus);
  }

  public boolean isReject() {
    Packet.Status responseStatus = getResponseStatus();
    return Packet.Status.CANCEL == responseStatus ||
      Packet.Status.REJECT == responseStatus;
  }

  public boolean isFailure() {
    Packet.Status responseStatus = getResponseStatus();
    return responseStatus == null || // не получен!
      Packet.Status.INVALID == responseStatus ||
      Packet.Status.FAILURE == responseStatus;
  }

  public boolean isPool() {
    Packet.Status responseStatus = getResponseStatus();
    return Packet.Status.ACCEPT == responseStatus ||
      Packet.Status.PROCESS == responseStatus;
  }

  public Expression getPingDelay() {
    return new FixedValue("PT" + task.getPingDelay() + "S");
  }

  public Expression getRecoveryDelay() {
    return new FixedValue("PT" + task.getErrorDelay() + "S");
  }


  // TODO: точка использования сервисов OSGI - сервисы могут быть НЕ доступны, и их нужно освобождать!
  void process() {
    ClientRequest request;
    ClientResponse response;
    Smev smev;
    InfoSystemService service;
    Client client;
    ClientLog clientLog = null;
    String consumer;
    String servicePort;
    Revision serviceRevision;
    ExchangeContext gwsContext;
    URL serviceWsdl;

    state = "Создание запроса";
    {
      consumer = variables.string(config.consumer);
      ProcessEngineConfigurationImpl cfg = Context.getProcessEngineConfiguration();
      smev = (Smev) cfg.getExpressionManager().createExpression("#{smev}").getValue(execution);
      service = smev.validateAndGetService(consumer);
      ru.codeinside.adm.database.InfoSystem sender = service.getSource();
      if (sender == null) {
        sender = smev.getDefaultSender();
      }
      if (sender == null) {
        throw new IllegalStateException("Не задан источник для потребителя '" + consumer + "'");
      }

      client = smev.findByNameAndVersion(consumer, service.getSversion()); // OSGI - ресурс!
      serviceWsdl = client.getWsdlUrl();
      if (serviceWsdl == null) {
        throw new IllegalStateException("Нет WSDL для потребителя '" + consumer + "'");
      }
      serviceRevision = client.getRevision();
      if (serviceRevision == null) {
        throw new IllegalStateException("Нет ревизии для потребителя '" + consumer + "'");
      }

      gwsContext = new ActivitiExchangeContext(execution);
      request = client.createClientRequest(gwsContext); // отказ в клиенте !
      servicePort = StringUtils.trimToNull(service.getAddress());
      if (servicePort != null) {
        request.portAddress = servicePort;
      }
      ru.codeinside.adm.database.InfoSystem recipient = service.getInfoSystem();
      request.packet.recipient = new InfoSystem(recipient.getCode(), recipient.getName());
      request.packet.originator = request.packet.sender = new InfoSystem(sender.getCode(), sender.getName());

      if (AdminServiceProvider.getBoolProperty(API.PRODUCTION_MODE)) {
        request.packet.testMsg = null;
      }
      task.setRequestStatus(request.packet.status.name());
    }

    state = "Создание журнала";
    {
      boolean logEnabled = AdminServiceProvider.getBoolProperty(API.ENABLE_CLIENT_LOG) && service.isLogEnabled();
      if (logEnabled || AdminServiceProvider.getBoolProperty(API.LOG_ERRORS)) {
        Bid bid = AdminServiceProvider.get().getBidByProcessInstanceId(execution.getProcessInstanceId());
        if (bid == null) {
          throw new IllegalStateException("Нет заявки для процесса '" + execution.getProcessInstanceId() + "'");
        }
        boolean logErrors = AdminServiceProvider.getBoolProperty(API.LOG_ERRORS);
        String logStatus = AdminServiceProvider.get().getSystemProperty(API.LOG_STATUS);
        Set<String> remote = smev.parseRemote(servicePort);
        clientLog = LogCustomizer.createClientLog(bid.getId(), consumer, execution.getProcessInstanceId(),
          logEnabled, logErrors, logStatus, remote);
      }
    }

    state = "Сетевое взаимодействие";
    try {
      response = smev.createProtocol(serviceRevision).send(serviceWsdl, request, clientLog); // OSGI ресурс!
    } catch (RuntimeException failure) {
      state = "Обработка ошибки";
      registerException(failure);
      smev.storeUnavailable(service);
      failure = smev.processFailure(client, gwsContext, clientLog, failure);
      if (failure == null) {
        return;
      }
      throw failure;
    } finally {
      if (clientLog != null) {
        clientLog.close();
      }
    }
    state = "Обработка результата";
    task.setResponseStatus(response.packet.status.name());
    client.processClientResponse(response, gwsContext);
  }

  public void nextStage() {
    logger.info("next stage");
    task.setRevision(task.getRevision() + 1);
    if (isPool()) {
      task.setPingCount(task.getPingCount() + 1);
    }
    if (isFailure()) {
      task.setErrorCount(task.getErrorCount() + 1);
    }
    task.setRequestStatus(null);
    task.setResponseStatus(null);
    task.setFailure(null);
  }

  public void store() {
    task.setLastChange(new Date());
    em.persist(task);
  }
}

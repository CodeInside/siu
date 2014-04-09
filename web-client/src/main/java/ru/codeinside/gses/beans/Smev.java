/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.apache.commons.lang.StringUtils;
import org.glassfish.osgicdi.OSGiService;
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.AuditValue;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.ClientRequestEntity;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.adm.database.ServiceResponseEntity;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.activiti.ReceiptEnsurance;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSession;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.gws.ClientRefRegistry;
import ru.codeinside.gses.webui.gws.ServiceRefRegistry;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gses.webui.osgi.LogCustomizer;
import ru.codeinside.gws.api.AppData;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientFailureAware;
import ru.codeinside.gws.api.ClientLog;
import ru.codeinside.gws.api.ClientProtocol;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.ProtocolFactory;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerRejectAware;
import ru.codeinside.gws.api.ServerResponse;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.namespace.QName;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.lang.StringUtils.defaultString;

@Named("smev")
@Singleton
public class Smev implements ReceiptEnsurance {

  final static String CLIENT_BPMN_ERROR = "client_bpmn_error"; // ошибки до отпрвки в смэв
  final static String SERVER_BPMN_ERROR = "server_bpmn_error"; // ошибки при отправке в смэв
  final static String SUDDENLY_BPMN_ERROR = "suddenly_bpmn_error"; //не предполагаемые ошибки(например ошибки разработчиков сервисов)
  final static Logger logger = Logger.getLogger(Smev.class.getName());

  @Inject
  ClientRefRegistry registry;

  @Inject
  AdminService adminService;

  @Inject
  ServiceRefRegistry serviceRegistry;

  @Inject
  @OSGiService(dynamic = true)
  CryptoProvider cryptoProvider;

  @Inject
  @OSGiService(dynamic = true)
  ProtocolFactory protocolFactory;

  // TODO не передавать DelegateExecution как параметер
  public void call(DelegateExecution execution, String serviceName) {
    innerCall(execution, serviceName, false);
  }

  private void innerCall(DelegateExecution execution, String serviceName, boolean wrapErrors) {
    ExchangeContext context = new ActivitiExchangeContext(execution);
    InfoSystemService service = validateAndGetService(serviceName);
    Client client = findByNameAndVersion(serviceName, service.getSversion());
    ClientRequest clientRequest = client.createClientRequest(context);
    callGws(execution.getProcessInstanceId(), serviceName, client, context, clientRequest, service, wrapErrors);
  }

  //минимальный список ошибок
  //org.glassfish.osgicdi.ServiceUnavailableException
  //BpmnError
  //UnsupportedOperationException
  //IllegalStateException
  //IllegalArgumentException
    /*
    Возможные варианты кодов bmpnerror
        client_bpmn_error - ошибки до отправки в смэв
        server_bpmn_error - ошибки при отправке в смэв
        suddenly_bpmn_error - не предполагаемые ошибки(например ошибки разработчиков сервисов)
        * - ожидаемые bpmn ошибки
     */
  public void managedCall(DelegateExecution execution, String serviceName) {
    try {
      innerCall(execution, serviceName, true);
    } catch (BpmnError e) {
      registerError(execution, serviceName, e, "managedCall");
      throw e;
    } catch (org.glassfish.osgicdi.ServiceUnavailableException e) {
      registerError(execution, serviceName, e, "managedCall");
      throw new BpmnError(CLIENT_BPMN_ERROR);
    } catch (UnsupportedOperationException e) {
      registerError(execution, serviceName, e, "managedCall");
      throw new BpmnError(CLIENT_BPMN_ERROR);
    } catch (IllegalStateException e) {
      registerError(execution, serviceName, e, "managedCall");
      throw new BpmnError(CLIENT_BPMN_ERROR);
    } catch (IllegalArgumentException e) {
      registerError(execution, serviceName, e, "managedCall");
      throw new BpmnError(CLIENT_BPMN_ERROR);
    } catch (Throwable th) {
      registerError(execution, serviceName, th, "managedCall");
      throw new BpmnError(SUDDENLY_BPMN_ERROR);
    }
  }

  @SuppressWarnings("unused") // Do NOT remove (used in BPMN API)
  public void infoCall(DelegateExecution execution, String serviceName) {
    try {
      managedCall(execution, serviceName);
    } catch (BpmnError bpmnError) {
      registerError(execution, serviceName, bpmnError, "infoCall");
    } catch (Throwable th) {
      registerError(execution, serviceName, th, "infoCall");
    }
  }

  private void registerError(DelegateExecution execution, String serviceName, Throwable th, String variant) {
    StringBuilder sb = new StringBuilder();
    sb.append(serviceName).append(" ").append(variant).append(" error\n");
    StringWriter sw = new StringWriter();
    Fn.trim(th).printStackTrace(new PrintWriter(sw));
    sb.append(sw.getBuffer());
    String value = sb.toString();
    if (value.length() <= 4000) {
      execution.setVariable("call_error", value);
    } else {
      logger.info("text overflow 4000 chars!");
      try {
        execution.setVariable("call_error", value.getBytes("UTF-8"));
      } catch (UnsupportedEncodingException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  private void callGws(
    String processInstanceId, String componentName,
    Client client, ExchangeContext context, ClientRequest clientRequest,
    InfoSystemService curService, boolean wrapErrors) {
    final Revision revision = client.getRevision();
    if (revision == Revision.rev110801) {
      throw new UnsupportedOperationException("Revision " + revision + " not supported");
    }
    ru.codeinside.adm.database.InfoSystem sender = curService.getSource();
    if (sender == null) {
      sender = adminService.getMainInfoSystem();
    }
    if (sender == null) {
      throw new IllegalStateException("Не задана основная информационная система");
    }
    String address = StringUtils.trimToNull(curService.getAddress());
    if (address != null) {
      clientRequest.portAddress = address;
    }
    final ru.codeinside.adm.database.InfoSystem infoSystem = curService.getInfoSystem();
    clientRequest.packet.recipient = new InfoSystem(infoSystem.getCode(), infoSystem.getName());
    clientRequest.packet.originator = clientRequest.packet.sender = new InfoSystem(sender.getCode(), sender.getName());
    final ClientProtocol protocol = protocolFactory.createClientProtocol(revision);

    if (AdminServiceProvider.getBoolProperty(API.PRODUCTION_MODE)) {
      // Реальный контур СМЭВ не принимает тестовые сообщения.
      clientRequest.packet.testMsg = null;
    }

    ClientLog clientLog = null;
    final ClientResponse response;
    try {
      if (AdminServiceProvider.getBoolProperty(API.ENABLE_CLIENT_LOG) && curService.isLogEnabled()) {
        Bid bid = AdminServiceProvider.get().getBidByProcessInstanceId(processInstanceId);
        clientLog = LogCustomizer.createClientLog(bid.getId(), componentName, processInstanceId);
      }
      response = protocol.send(client.getWsdlUrl(), clientRequest, clientLog);
    } catch (RuntimeException failure) {
      adminService.saveServiceUnavailable(curService);
      failure = processFailure(client, context, clientLog, failure);
      if (failure == null) {
        return;
      }
      throw wrapErrors ? Fn.trim(new SmevBpmnError(SERVER_BPMN_ERROR, failure)) : failure;
    } finally {
      if (clientLog != null) {
        clientLog.close();
      }
    }
    if (wrapErrors) {
      try {
        client.processClientResponse(response, context);
      } catch (BpmnError e) {
        throw Fn.trim(e); // пользовательские ошибки
      } catch (Throwable th) {
        throw Fn.trim(new SmevBpmnError(SUDDENLY_BPMN_ERROR, th));
      }
    } else {
      client.processClientResponse(response, context);
    }
  }

  private RuntimeException processFailure(Client client, ExchangeContext context,
                                          ClientLog clientLog, RuntimeException failure) {
    failure = Fn.trim(failure);
    if (client instanceof ClientFailureAware) {
      try {
        ((ClientFailureAware) client).processFailure(context, failure);
        failure = null;
      } catch (RuntimeException nested) {
        if (setRootCause(nested, failure) && clientLog != null) {
          clientLog.log(nested);
        }
        failure = nested;
      }
    }
    return failure;
  }

  /**
   * Внедрение первопричины.
   */
  private boolean setRootCause(RuntimeException nested, RuntimeException root) {
    Throwable cause = Fn.trim(nested);
    boolean rootFound = false;
    while (cause.getCause() != null) {
      if (cause == root) {
        rootFound = true;
      }
      cause = Fn.trim(cause.getCause());
    }
    if (!rootFound && cause != root) {
      try {
        Field causeFiled = Throwable.class.getDeclaredField("cause");
        causeFiled.setAccessible(true);
        causeFiled.set(cause, root);
        return true;
      } catch (NoSuchFieldException e) {
        logger.log(Level.INFO, "can't set cause", e);
      } catch (IllegalAccessException e) {
        logger.log(Level.INFO, "can't set cause", e);
      }
    }
    return false;
  }

  public ClientRequestEntity prepare(DelegateExecution execution, String serviceName, String variableName) {
    InfoSystemService service = validateAndGetService(serviceName);
    final Client client = findByNameAndVersion(serviceName, service.getSversion());

    final ExchangeContext context = new ActivitiExchangeContext(execution);
    final ClientRequest clientRequest = client.createClientRequest(context);
    final ClientRequestEntity entity = createClientRequestEntity(serviceName, clientRequest, client.getRevision());

    // Activiti не вызывает сохраниение значений JPA сущноестей,
    // это нужня сделать явно.
    Activiti.getEm().persist(entity);
    logger.fine("Persist CRE " + entity.getId());
    if (execution.hasVariable(variableName)) {
      execution.removeVariable(variableName);
    }
    execution.setVariable(variableName, entity);
    putEnclosureToContext(clientRequest, context, variableName, execution);
    return entity;
  }

  private void putEnclosureToContext(ClientRequest clientRequest, ExchangeContext context, String variableName, DelegateExecution execution) {
    if (clientRequest.enclosures == null) return;
    List<String> enclosureVariableNames = new LinkedList<String>();
    for (int idx = 0; idx < clientRequest.enclosures.length; idx++) {
      final String enclosureVarName = variableName + "_enclosure_to_sign_" + idx;
      if (!execution.hasVariable(enclosureVarName)) {
        if (clientRequest.enclosures[idx].signature == null) { // вложение еще не подписано
          context.addEnclosure(enclosureVarName, clientRequest.enclosures[idx]);
          enclosureVariableNames.add(enclosureVarName);
        }
      } else {
        cleanEnclosureVarName(enclosureVariableNames, execution);
        throw new IllegalStateException("Невозможно подготовить вложения для подписи. Переменная " + enclosureVarName + " уже используется в контексте.");
      }
    }
    if (!enclosureVariableNames.isEmpty()) {
      execution.setVariable(buildVariableNameForStoreEnclosureVars(variableName), Joiner.on(';').join(enclosureVariableNames));
    }
  }

  private Enclosure[] getEnclosuresFromContext(ExchangeContext context, String variableName) {
    final String variableForStoreDynamicEnclosures = buildVariableNameForStoreEnclosureVars(variableName);
    final String dynamicEnclosuresVars = (String) context.getVariable(variableForStoreDynamicEnclosures);
    ImmutableList<String> dynamicEnclosureList = ImmutableList.copyOf(
      Splitter.on(';')
        .trimResults()
        .omitEmptyStrings()
        .split(defaultString(dynamicEnclosuresVars))
    );
    Enclosure[] result = new Enclosure[dynamicEnclosureList.size()];
    int idx = 0;
    for (String enclosureVarName : dynamicEnclosureList) {
      result[idx++] = context.getEnclosure(enclosureVarName);
    }
    return result;
  }

  private String buildVariableNameForStoreEnclosureVars(String variableName) {
    return variableName + "_enclosure_to_sign_vars";
  }

  private void cleanEnclosureVarName(List<String> enclosureVariableNames, DelegateExecution execution) {
    for (String varName : enclosureVariableNames) {
      execution.removeVariable(varName);
    }
  }

  // TODO убрать serviceName
  public void done(DelegateExecution execution, String serviceName, String variableName) {
    final ExchangeContext context = new ActivitiExchangeContext(execution);
    final ClientRequestEntity entity = getAndValidateClientRequestEntity(serviceName, variableName, context);
    logger.fine("Load CRE " + entity.getId());
    final ClientRequest clientRequest = createClientRequest(entity, context, execution.getId(), variableName);
    InfoSystemService service = validateAndGetService(entity.name);
    final Client client = findByNameAndVersion(entity.name, service.getSversion());
    callGws(execution.getProcessInstanceId(), serviceName, client, context, clientRequest, service, false);
  }

  public void result(DelegateExecution execution) {
    result(execution, "resultMessage");
  }

  public void result(DelegateExecution execution, String message) {
    ExternalGlue glue = getExternalGlue(execution);
    if (glue == null) {
      throw new BpmnError("Нет связи с внешней услугой");
    }
    TRef<Server> ref = serviceRegistry.getServerByName(glue.getName());
    if (ref == null) {
      throw new BpmnError("Услуга не найдена: имя " + glue.getName());
    }
    Server service = ref.getRef();

    ActivitiReceiptContext exchangeContext = new ActivitiReceiptContext(execution);
    ServerResponse response = service.processResult(message, exchangeContext);
    adminService.saveServiceResponse(
      new ServiceResponseEntity(glue.getBidId(), response),
      response.attachmens,
      exchangeContext.getUsedEnclosures());
  }

  public void completeReceipt(DelegateExecution delegateExecution, String rejectReason) {
    final ExternalGlue glue = getExternalGlue(delegateExecution);
    if (glue != null && adminService.countOfServerResponseByBidIdAndStatus(glue.getBidId(), Packet.Status.RESULT.name()) == 0) {
      logger.info("Complete Receipt " + delegateExecution.getProcessInstanceId() +
        " for " + glue.getName() + "/" + glue.getBidId() + "/" + glue.getRequestIdRef());
      TRef<Server> ref = serviceRegistry.getServerByName(glue.getName());
      Server service = ref.getRef();
      ActivitiReceiptContext exchangeContext = new ActivitiReceiptContext(delegateExecution);
      ServerResponse response;
      if (rejectReason != null && service instanceof ServerRejectAware) {
        response = ((ServerRejectAware) service).processReject(rejectReason, exchangeContext);
      } else {
        String msg = rejectReason == null ? "Исполнено" : ("Удалено: " + rejectReason);
        response = service.processResult(msg, exchangeContext);
      }
      if (response == null) {
        throw new BpmnError("В smev.completeReceipt при вызове метода processResult сервер " + service.toString() + " вернул null");
      }
      adminService.saveServiceResponse(
        new ServiceResponseEntity(glue.getBidId(), response),
        response.attachmens,
        exchangeContext.getUsedEnclosures());
    }
  }

  public void status(DelegateExecution execution, String statusValue) {
    ExternalGlue glue = getExternalGlue(execution);

    if (glue == null) {
      throw new BpmnError("Нет связи с внешней услугой");
    }
    TRef<Server> ref = serviceRegistry.getServerByName(glue.getName());
    if (ref == null) {
      throw new BpmnError("Услуга не найдена: имя " + glue.getName());
    }
    Server service = ref.getRef();

    ActivitiReceiptContext exchangeContext = new ActivitiReceiptContext(execution);
    ServerResponse response = service.processStatus(statusValue, exchangeContext);
    adminService.saveServiceResponse(
      new ServiceResponseEntity(glue.getBidId(), response),
      response.attachmens,
      exchangeContext.getUsedEnclosures());
  }

  private ExternalGlue getExternalGlue(DelegateExecution execution) {
    ExternalGlue externalGlue = adminService.getGlueByProcessInstanceId(execution.getProcessInstanceId());
    if (externalGlue == null) {
      Object glueId = execution.getVariable("glueId");
      externalGlue = adminService.getGlueById(glueId == null ? null : Long.parseLong(glueId + ""));
    }
    return externalGlue;
  }

  private ClientRequestEntity getAndValidateClientRequestEntity(String serviceName, String variableName, ExchangeContext context) {
    final Object object = context.getVariable(variableName);
    if (object == null) {
      throw new BpmnError("СМЭВ запрос " + variableName + " не найден");
    }
    if (!(object instanceof ClientRequestEntity)) {
      throw new BpmnError("Переменная процесса " + variableName + " не является СМЭВ запросом");
    }
    final ClientRequestEntity entity = (ClientRequestEntity) object;
    if (!entity.name.equals(serviceName)) {
      throw new BpmnError("СМЭВ запрос  " + variableName + " сформирован для сервиса " + entity.name);
    }
    return entity;
  }

  private ClientRequest createClientRequest(ClientRequestEntity entity, ExchangeContext context, String executionId, String variableName) {
    final ClientRequest request = new ClientRequest();
    if (entity.action != null || entity.actionNs != null) {
      request.action = new QName(entity.actionNs, entity.action);
    }
    if (entity.port != null || entity.portNs != null) {
      request.port = new QName(entity.portNs, entity.port);
    }
    if (entity.service != null || entity.serviceNs != null) {
      request.port = new QName(entity.serviceNs, entity.service);
    }
    if (!entity.signRequired) {
      request.appData = entity.appData;
    } else {
      final CommandContext ctx = Context.getCommandContext();
      final HistoricDbSqlSession session = (HistoricDbSqlSession) ctx.getDbSqlSession();
      final AuditValue auditValue = session.getAuditSnapshotValue(Long.parseLong(executionId), variableName);
      if (auditValue == null || auditValue.getSign() == null || auditValue.getCert() == null) {
        logger.log(Level.WARNING, "no required signature in execution " + executionId + " for " + variableName);
        throw new IllegalStateException("Отсутсвуют данные для подписи");
      }
      final byte[] cert = auditValue.getCert();
      final byte[] sign = auditValue.getSign();
      try {
        List<QName> namespaces = new ArrayList<QName>();
        namespaces.add(new QName("http://smev.gosuslugi.ru/rev111111", "smev"));
        AppData appData = new AppData(entity.appData.getBytes("UTF8"), entity.digest.getBytes("UTF8"));
        X509Certificate x509 = X509.decode(cert);
        request.appData = cryptoProvider.inject(namespaces, appData, x509, sign);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    request.enclosures = getEnclosuresFromContext(context, variableName);
    final Packet packet = new Packet();
    request.packet = packet;
    packet.typeCode = Packet.Type.valueOf(entity.gservice);
    packet.status = Packet.Status.valueOf(entity.status);
    packet.date = entity.date;
    packet.exchangeType = entity.exchangeType;
    packet.requestIdRef = entity.requestIdRef;
    packet.originRequestIdRef = entity.originRequestIdRef;
    packet.serviceCode = entity.serviceCode;
    packet.caseNumber = entity.caseNumber;
    packet.testMsg = entity.testMsg;
    request.enclosureDescriptor = entity.enclosureDescriptor;
    return request;
  }


  private ClientRequestEntity createClientRequestEntity(String serviceName, ClientRequest request, Revision revision) {
    final ClientRequestEntity entity = new ClientRequestEntity();
    entity.name = serviceName;
    if (request.action != null) {
      entity.action = request.action.getLocalPart();
      entity.actionNs = request.action.getNamespaceURI();
    }
    if (request.port != null) {
      entity.port = request.port.getLocalPart();
      entity.portNs = request.port.getNamespaceURI();
    }
    if (request.service != null) {
      entity.service = request.service.getLocalPart();
      entity.serviceNs = request.service.getNamespaceURI();
    }
    if (request.appData != null) {
      if (request.signRequired) {
        List<QName> namespaces = new ArrayList<QName>();
        //TODO: убрать в протокол?
        if (revision == Revision.rev111111) {
          namespaces.add(new QName("http://smev.gosuslugi.ru/rev111111", "smev"));
        }
        final AppData normalize = cryptoProvider.normalize(namespaces, request.appData);
        try {
          entity.appData = new String(normalize.content, "UTF8");
          entity.digest = new String(normalize.digest, "UTF8");
        } catch (UnsupportedEncodingException e) {
          throw new IllegalStateException(e);
        }
      } else {
        entity.appData = request.appData;
      }
    }
    final Packet packet = request.packet;
    entity.gservice = packet.typeCode.name();
    entity.status = packet.status.name();
    entity.date = new Date();
    entity.exchangeType = packet.exchangeType;
    entity.requestIdRef = packet.requestIdRef;
    entity.originRequestIdRef = packet.originRequestIdRef;
    entity.serviceCode = packet.serviceCode;
    entity.caseNumber = packet.caseNumber;
    entity.testMsg = packet.testMsg;
    entity.signRequired = request.signRequired;
    entity.enclosureDescriptor = request.enclosureDescriptor;
    return entity;
  }

  private Client findByNameAndVersion(String serviceName, String sversion) {
    final TRef<Client> clientRef = registry.getClientByNameAndVersion(serviceName, sversion);
    if (clientRef == null) {
      throw new IllegalStateException("Client not found! [" + serviceName + "]");
    }
    return clientRef.getRef();
  }

  private InfoSystemService validateAndGetService(String serviceName) {
    List<InfoSystemService> services = adminService.getInfoSystemServiceBySName(serviceName);
    if (services == null || services.isEmpty()) {
      throw new IllegalStateException("Нет сервиса с именем " + serviceName);
    }
    return getServiceWithMaxVersion(services);
  }

  private InfoSystemService getServiceWithMaxVersion(List<InfoSystemService> services) {
    InfoSystemService curService = null;
    for (InfoSystemService s : services) {
      if (curService == null) {
        curService = s;
      }
      if (s.getSversion().compareTo(curService.getSversion()) >= 0) {
        curService = s;
      }
    }
    return curService;
  }

}

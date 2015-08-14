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
import commons.Exceptions;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.lang.StringUtils;
import org.glassfish.osgicdi.OSGiService;
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.ClientRequestEntity;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.adm.database.ServiceResponseEntity;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.activiti.ReceiptEnsurance;
import ru.codeinside.gses.webui.form.FormOvSignatureSeq;
import ru.codeinside.gses.webui.form.ProtocolUtils;
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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.lang.StringUtils.defaultString;

@Named("smev")
@Singleton
public class Smev implements ReceiptEnsurance {
  public static final String SERVER_RESPONSE_ID = "ServerResponseEntityId";

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
    ClientRequest clientRequest;

    //serviceName нужен, чтобы в случае параллельного выполнения отличались имена переменных в разных потоках
    Long requestId = (Long) context.getVariable(serviceName + FormOvSignatureSeq.REQUEST_ID);
    boolean isDataFlow = (requestId != null);

    if (isDataFlow && !ProtocolUtils.isPing(context)) {
      ClientRequestEntity entity = AdminServiceProvider.get().getClientRequestEntity(requestId);
      clientRequest = createClientRequest(entity, context, execution.getId(), "");//TODO VariableName?
    } else {
      ProtocolUtils.writeInfoSystemsToContext(service, context);
      clientRequest = client.createClientRequest(context);
    }

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
    sb.append(Exceptions.trimToString(th));
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
    String address = StringUtils.trimToNull(curService.getAddress());
    if (address != null) {
      clientRequest.portAddress = address;
    }

    if (clientRequest.requestMessage != null) {
      ProtocolUtils.fillClientRequestFromSoapMessage(clientRequest);
    }

    ProtocolUtils.fillServiceRequestPacket(clientRequest, curService);

    final ClientProtocol protocol = protocolFactory.createClientProtocol(revision);

    if (AdminServiceProvider.getBoolProperty(API.PRODUCTION_MODE)) {
      // Реальный контур СМЭВ не принимает тестовые сообщения.
      clientRequest.packet.testMsg = null;
    }

    ClientLog clientLog = null;
    final ClientResponse response;
    try {
      boolean logEnabled = AdminServiceProvider.getBoolProperty(API.ENABLE_CLIENT_LOG) && curService.isLogEnabled();
      if (logEnabled || AdminServiceProvider.getBoolProperty(API.LOG_ERRORS)) {
        Bid bid = AdminServiceProvider.get().getBidByProcessInstanceId(processInstanceId);
        if (bid == null) {
          throw new IllegalStateException("Нет номера заявки для процесса '" + processInstanceId + "'");
        }
        boolean logErrors = AdminServiceProvider.getBoolProperty(API.LOG_ERRORS);
        String logStatus = AdminServiceProvider.get().getSystemProperty(API.LOG_STATUS);
        Set<String> remote = parseRemote(address);
        clientLog = LogCustomizer.createClientLog(bid.getId(), componentName, processInstanceId,
            logEnabled, logErrors, logStatus, remote);
      }

      response = protocol.send(client.getWsdlUrl(), clientRequest, clientLog);

    } catch (RuntimeException failure) {
      adminService.saveServiceUnavailable(curService);
      failure = processFailure(client, context, clientLog, failure);
      if (failure == null) {
        return;
      }
      throw wrapErrors ? Exceptions.trim(new SmevBpmnError(SERVER_BPMN_ERROR, failure)) : failure;
    } finally {
      if (clientLog != null) {
        clientLog.close();
      }
    }
    if (wrapErrors) {
      try {
        client.processClientResponse(response, context);
      } catch (BpmnError e) {
        throw Exceptions.trim(e); // пользовательские ошибки
      } catch (Throwable th) {
        throw Exceptions.trim(new SmevBpmnError(SUDDENLY_BPMN_ERROR, th));
      }
    } else {
      client.processClientResponse(response, context);
    }
  }

  public Set<String> parseRemote(String address) {
    Set<String> remote = new HashSet<String>();
    try {
      String host = new URL(address).getHost();
      remote.add(host);
      InetAddress _address = InetAddress.getByName(host);
      String ip = _address.getHostAddress();
      remote.add(ip);
    } catch (UnknownHostException ignore) {
      // будет ошибка снова, сейчас игнорируем
    } catch (MalformedURLException ignore) {
      // будет ошибка снова, сейчас игнорируем
    }
    return remote;
  }

  public RuntimeException processFailure(Client client, ExchangeContext context,
                                         ClientLog clientLog, RuntimeException failure) {
    failure = Exceptions.trim(failure);
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
    Throwable cause = Exceptions.trim(nested);
    boolean rootFound = false;
    while (cause.getCause() != null) {
      if (cause == root) {
        rootFound = true;
      }
      cause = Exceptions.trim(cause.getCause());
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

    ProtocolUtils.writeInfoSystemsToContext(service, context);
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
    List<String> enclosureVariableNames = new LinkedList<String>();
    Enclosure[] enclosures = clientRequest.enclosures;
    if (enclosures != null) {
      for (int idx = 0; idx < enclosures.length; idx++) {
        Enclosure enclosure = enclosures[idx];
        String variable = null;
        // code может использоваться как имя переменной!
        if (enclosure.code != null) {
          Enclosure check = context.getEnclosure(enclosure.code);
          if (check != null && Arrays.equals(check.content, enclosure.content)) {
            variable = enclosure.code;
          }
        }
        if (variable == null) {
          variable = variableName + "_enclosure_to_sign_" + idx;
          context.addEnclosure(variable, enclosure);
        }
        enclosureVariableNames.add(variable);
      }
    }
    execution.setVariable(buildVariableNameForStoreEnclosureVars(variableName), Joiner.on(';').join(enclosureVariableNames));
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
      Enclosure enclosure = context.getEnclosure(enclosureVarName);
      result[idx++] = enclosure;
    }
    return result;
  }

  private String buildVariableNameForStoreEnclosureVars(String variableName) {
    return variableName + "_enclosure_to_sign_vars";
  }

  // TODO убрать serviceName
  public void done(DelegateExecution execution, String serviceName, String variableName) {
    ExchangeContext context = new ActivitiExchangeContext(execution);
    ClientRequestEntity entity = getAndValidateClientRequestEntity(serviceName, variableName, context);
    ClientRequest clientRequest = createClientRequest(entity, context, execution.getId(), variableName);
    InfoSystemService service = validateAndGetService(entity.name);
    Client client = findByNameAndVersion(entity.name, service.getSversion());

    callGws(execution.getProcessInstanceId(), serviceName, client, context, clientRequest, service, false);
  }

  public void result(DelegateExecution execution) {
    result(execution, "resultMessage");
  }

  public void result(DelegateExecution execution, String message) {
    Object serverResponseEntityId = execution.getVariable(SERVER_RESPONSE_ID);
    if (serverResponseEntityId != null && !serverResponseEntityId.toString().isEmpty()) {
      return;
    }

    Bid bid = getBid(execution);
    ExternalGlue glue = bid.getGlue();
    if (glue == null) {
      throw new BpmnError("Нет связи с внешней услугой");
    }
    TRef<Server> ref = serviceRegistry.getServerByName(glue.getName());
    if (ref == null) {
      throw new BpmnError("Услуга не найдена: имя " + glue.getName());
    }
    Server service = ref.getRef();
    ActivitiReceiptContext exchangeContext = new ActivitiReceiptContext(execution, bid.getId());
    ServerResponse response = service.processResult(message, exchangeContext);
    adminService.saveServiceResponse(
        new ServiceResponseEntity(bid, response),
        response.attachmens,
        exchangeContext.getUsedEnclosures());
  }

  public void completeReceipt(DelegateExecution delegateExecution, String rejectReason) {
    Bid bid = getBid(delegateExecution);
    ExternalGlue glue = bid.getGlue();
    if (glue != null && adminService.countOfServerResponseByBidIdAndStatus(bid.getId(), Packet.Status.RESULT.name()) == 0) {
      TRef<Server> ref = serviceRegistry.getServerByName(glue.getName());
      Server service = ref.getRef();
      ActivitiReceiptContext exchangeContext = new ActivitiReceiptContext(delegateExecution, bid.getId());
      ServerResponse response;
      if (rejectReason != null && service instanceof ServerRejectAware) {
        response = ((ServerRejectAware) service).processReject(rejectReason, exchangeContext);
      } else {
        String msg = rejectReason == null ? "Исполнено" : ("Удалено: " + rejectReason);
        response = service.processResult(msg, exchangeContext);
      }
      if (response == null) {
        throw new BpmnError(SUDDENLY_BPMN_ERROR, "Поставщик " + glue.getName() + " при вызове метода processResult вернул null");
      }
      adminService.saveServiceResponse(
          new ServiceResponseEntity(bid, response),
          response.attachmens,
          exchangeContext.getUsedEnclosures());
    }
  }

  public void status(DelegateExecution execution, String statusValue) {
    Bid bid = getBid(execution);
    ExternalGlue glue = bid.getGlue();
    if (glue == null) {
      throw new BpmnError("Нет связи с внешней услугой");
    }
    TRef<Server> ref = serviceRegistry.getServerByName(glue.getName());
    if (ref == null) {
      throw new BpmnError("Услуга не найдена: имя " + glue.getName());
    }
    Server service = ref.getRef();

    ActivitiReceiptContext exchangeContext = new ActivitiReceiptContext(execution, bid.getId());
    ServerResponse response = service.processStatus(statusValue, exchangeContext);
    adminService.saveServiceResponse(new ServiceResponseEntity(bid, response),
        response.attachmens,
        exchangeContext.getUsedEnclosures());
  }

  private Bid getBid(DelegateExecution execution) {
    return adminService.getBidByProcessInstanceId(execution.getProcessInstanceId());
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

  public ClientRequest createClientRequest(ClientRequestEntity entity, ExchangeContext context, String executionId, String variableName) {
    final ClientRequest request = new ClientRequest();
    if (entity.action != null || entity.actionNs != null) {
      request.action = new QName(entity.actionNs, entity.action);
    }
    if (entity.port != null || entity.portNs != null) {
      request.port = new QName(entity.portNs, entity.port);
    }
    if (entity.service != null || entity.serviceNs != null) {
      request.service = new QName(entity.serviceNs, entity.service);
    }
    request.portAddress = entity.portAddress;
    request.requestMessage = entity.requestMessage;
    request.appData = entity.appData;
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
    entity.portAddress = request.portAddress;
    entity.requestMessage = request.requestMessage;

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

  public Client findByNameAndVersion(String serviceName, String sversion) {
    final TRef<Client> clientRef = registry.getClientByNameAndVersion(serviceName, sversion);
    if (clientRef == null) {
      throw new IllegalStateException("Client not found! [" + serviceName + "]");
    }
    return clientRef.getRef();
  }

  public InfoSystemService validateAndGetService(String serviceName) {
    List<InfoSystemService> services = adminService.getInfoSystemServiceBySName(serviceName);
    if (services == null || services.isEmpty()) {
      throw new IllegalStateException("Нет модуля потребителя СМЭВ с именем '" + serviceName + "'");
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

  public ru.codeinside.adm.database.InfoSystem getDefaultSender() {
    return adminService.getMainInfoSystem();
  }

  public ClientProtocol createProtocol(Revision revision) {
    return protocolFactory.createClientProtocol(revision);
  }

  public void storeUnavailable(InfoSystemService service) {
    adminService.saveServiceUnavailable(service);
  }
}

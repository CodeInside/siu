/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3970c;

import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientFailureAware;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.XmlTypes;
import ru.codeinside.gws3970c.types.AppData;
import ru.codeinside.gws3970c.types.data.DataRow;
import ru.codeinside.gws3970c.types.data.Result;
import ru.codeinside.gws3970c.types.data.SystemParams;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

final public class UniversalClient implements Client, ClientFailureAware {

  public static final QName UPDATE_STATUS = new QName("http://mvv.oep.com/", "updateStatus");
  public static final QName PUT_DATA = new QName("http://mvv.oep.com/", "putData");
  public static final String SMEV_REQUEST_ID = "smevRequestId";
  public static final String SMEV_ORIGIN_REQUEST_ID = "smevOriginRequestId";
  public static final String APP_ID = "app_id";
  public static final String SMEV_POOL = "smevPool";
  public static final String SMEV_REJECT = "smevReject";

  @Override
  public Revision getRevision() {
    return Revision.rev120315;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("gws3970/mvvact.wsdl");
  }

  @Override
  public ClientRequest createClientRequest(ExchangeContext ctx) {
    final Packet packet = new Packet();

    packet.originRequestIdRef = (String) ctx.getVariable(SMEV_ORIGIN_REQUEST_ID);
    packet.requestIdRef = (String) ctx.getVariable(SMEV_REQUEST_ID);
    boolean pooling = packet.originRequestIdRef != null || packet.requestIdRef != null;

    packet.recipient = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    packet.typeCode = Packet.Type.SERVICE;
    packet.date = new Date();
    packet.serviceName = "PENZUniversalMVV";
    packet.exchangeType = "1"; // 1 - Запрос на оказание услуги
    packet.testMsg = (String) ctx.getVariable("smevTest");
    packet.status = pooling ? Packet.Status.PING : Packet.Status.REQUEST;


    final ClientRequest request = new ClientRequest();
    request.signRequired = true;
    request.packet = packet;
    request.appData = createAppData(ctx, pooling);
    request.action = pooling ? UPDATE_STATUS : PUT_DATA;

    if (!pooling) {
      final List<Enclosure> enclosures = new ArrayList<Enclosure>();
      for (String name : ctx.getVariableNames()) {
        if (name.startsWith("appData_")) {
          if (ctx.isEnclosure(name)) {
            final Enclosure enclosure = ctx.getEnclosure(name);
            enclosure.code = name;
            enclosures.add(enclosure);
          }
        }
      }
      final Object requestCode = ctx.getVariable("smevRequestCode");
      request.enclosureDescriptor = requestCode != null ? requestCode.toString() : "metadata";
      request.enclosures = enclosures.toArray(new Enclosure[enclosures.size()]);
    }
    return request;
  }

  private String createAppData(ExchangeContext ctx, boolean pooled) {
    final Result result = new Result();
    SystemParams params = new SystemParams();
    Long appId = (Long) ctx.getVariable(APP_ID);
    if (appId != null) {
      params.setAppId(appId);
    }
    GregorianCalendar c = new GregorianCalendar();
    try {
      params.setStatusDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
    } catch (DatatypeConfigurationException e) {
    }
    result.setParams(params);
    List<DataRow> dataRowList = new ArrayList<DataRow>();

    result.setDataRow(dataRowList);

    Set<String> variableNames = new LinkedHashSet<String>();
    for (String name : ctx.getVariableNames()) {
      if (name.startsWith("appData_") || name.startsWith("+appData_")) {
        variableNames.add(name);
      }
    }
    variableNames.add("flowName");
    variableNames.add("procedureCode");
    if (pooled) {
      variableNames.add("internalRequestId");
    }
    for (String variable : variableNames) {
      if (!ctx.isEnclosure(variable) && ctx.getVariable(variable) != null) {
        DataRow dataRow = new DataRow();
        dataRow.setName(variable);
        if (ctx.getVariable(variable) instanceof Date) {
          String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(ctx.getVariable(variable));
          dataRow.setValue(formattedDate);
        } else {
          dataRow.setValue(ctx.getVariable(variable).toString());
        }
        dataRowList.add(dataRow);
      }
    }
    return new XmlTypes(Result.class).toXml(result);
  }

  @Override
  public void processClientResponse(ClientResponse response, ExchangeContext context) {
    if (response.verifyResult.error != null) {
      context.setVariable(SMEV_POOL, false);
      context.setVariable(SMEV_REJECT, true);
      context.setVariable("status_code", "ЭЦП:" + response.verifyResult.error);
      return;
    }

    boolean pooled = context.getVariable(SMEV_ORIGIN_REQUEST_ID) != null ||
      context.getVariable(SMEV_REQUEST_ID) != null;

    if (!pooled) {
      boolean isAccepted = response.packet.status == Packet.Status.ACCEPT;
      context.setVariable(SMEV_POOL, isAccepted);
      if (isAccepted) {
        updateRequestChain(response, context, true);
      }
      context.setVariable(SMEV_REJECT, !isAccepted);
    } else {
      boolean isProcess = response.packet.status == Packet.Status.PROCESS;
      context.setVariable(SMEV_POOL, isProcess);
      if (isProcess) {
        updateRequestChain(response, context, false);
      } else {
        context.setVariable(SMEV_REJECT, response.packet.status == Packet.Status.REJECT);
      }
    }
    final AppData appData = XmlTypes.elementToBean(response.appData, AppData.class);
    for (final Result result : appData.getResult()) {
      for (final DataRow row : result.getDataRow()) {
        context.setVariable(row.getName(), row.getValue());
      }
      final SystemParams systemParams = result.getParams();
      context.setVariable(APP_ID, systemParams.getAppId());
      context.setVariable("form_id", systemParams.getFormId());
      context.setVariable("org_id", systemParams.getOrgId());
      context.setVariable("status_code", systemParams.getStatusCode());
      context.setVariable("status_pgu", systemParams.getStatusPgu());
      context.setVariable("status_title", systemParams.getStatusTitle());
      context.setVariable("status_date", systemParams.getStatusDate());
    }
    if (response.enclosures != null) {
      for (final Enclosure enclosure : response.enclosures) {
        String name = enclosure.code;
        if (name == null) {
          name = "enclosure_" + enclosure.zipPath.replace('/', '_');
        }
        context.addEnclosure(name, enclosure);
      }
    }
  }

  private void updateRequestChain(ClientResponse response, ExchangeContext context, boolean start) {
    if (start) {
      context.setVariable(SMEV_ORIGIN_REQUEST_ID, response.packet.originRequestIdRef);
    }
    if (response.routerPacket != null) {
      context.setVariable(SMEV_REQUEST_ID, response.routerPacket.messageId);
    } else {
      context.setVariable(SMEV_REQUEST_ID, UUID.randomUUID().toString());
    }
  }

  @Override
  public void processFailure(ExchangeContext ctx, RuntimeException failure) {

    if (failure instanceof SOAPFaultException) {
      ctx.setVariable(SMEV_POOL, false);
      ctx.setVariable(SMEV_REJECT, true);
      ctx.setVariable("status_code", createSoapFaultMessage((SOAPFaultException) failure));
      return;
    }

    if (failure instanceof WebServiceException) {
      ctx.setVariable(SMEV_POOL, false);
      ctx.setVariable(SMEV_REJECT, true);
      ctx.setVariable("status_code", "Ошибка связи с поставщиком: " + failure.getMessage());
      return;
    }

    throw failure;
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
}

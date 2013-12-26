/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3970c;

import ru.codeinside.gws.api.Client;
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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

final public class UniversalClient implements Client {

  public static final QName UPDATE_STATUS = new QName("http://mvv.oep.com/", "updateStatus");
  public static final QName PUT_DATA = new QName("http://mvv.oep.com/", "putData");
  public static final String SMEV_REQUEST_ID = "smevRequestId";
  public static final String SMEV_ORIGIN_REQUEST_ID = "smevOriginRequestId";
  public static final String APP_ID = "app_id";
  public static final String SMEV_POOL = "smevPool";
  private Logger logger = Logger.getLogger(getClass().getName());

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
    final boolean pooling = Boolean.TRUE == ctx.getVariable(SMEV_POOL);

    final Packet packet = new Packet();

    packet.recipient = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    packet.typeCode = Packet.Type.SERVICE;
    packet.date = new Date();
    packet.serviceName = "PENZUniversalMVV";
    packet.exchangeType = "1"; // 1 - Запрос на оказание услуги
    packet.testMsg = (String) ctx.getVariable("smevTest");
    packet.status = pooling ? Packet.Status.PING : Packet.Status.REQUEST;

    if (pooling) {
      packet.originRequestIdRef = (String) ctx.getVariable(SMEV_ORIGIN_REQUEST_ID);
      packet.requestIdRef = (String) ctx.getVariable(SMEV_REQUEST_ID);
    }

    final ClientRequest request = new ClientRequest();
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
    boolean pooled = Boolean.TRUE == context.getVariable(SMEV_POOL);
    String smevError = "smevError";
    if (response.verifyResult.error != null) {
      context.setVariable(SMEV_POOL, false);
      context.setVariable(smevError, response.verifyResult.error);
    } else {
      if (!pooled) {
        boolean isAccepted = response.packet.status == Packet.Status.ACCEPT;
        context.setVariable(SMEV_POOL, isAccepted);
        if (isAccepted) {
          updateRequestChain(response, context, true);
        }
      } else {
        boolean isProcess = response.packet.status == Packet.Status.PROCESS;
        context.setVariable(SMEV_POOL, isProcess);
        if (isProcess) {
          updateRequestChain(response, context, false);
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

}

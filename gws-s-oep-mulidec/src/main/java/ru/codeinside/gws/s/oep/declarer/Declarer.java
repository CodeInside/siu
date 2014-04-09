/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.s.oep.declarer;

import ru.codeinside.gws.api.DeclarerContext;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.ReceiptContext;
import ru.codeinside.gws.api.RequestContext;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerRejectAware;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.XmlTypes;
import ru.codeinside.gws.s.oep.declarer.app.AppData;
import ru.codeinside.gws.s.oep.declarer.data.DataRow;
import ru.codeinside.gws.s.oep.declarer.data.Result;
import ru.codeinside.gws.s.oep.declarer.data.SystemParams;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

public class Declarer implements Server, ServerRejectAware {


  final private Logger logger = Logger.getLogger(getClass().getName());

  @Override
  public Revision getRevision() {
    return Revision.rev120315;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("mvvact/mvvact.wsdl");
  }

  @Override
  public ServerResponse processRequest(final RequestContext ctx) {
    if (!ctx.isFirst()) {
      return onPing(ctx);
    }
    return onSubmit(ctx);
  }

  private ServerResponse onSubmit(RequestContext ctx) {
    final ServerRequest request = ctx.getRequest();
    if (request.packet.status != Packet.Status.REQUEST) {
      throw new IllegalStateException("Illegal status " + request.packet.status);
    }
    AppData requestWrapper = XmlTypes.elementToBean(request.appData, AppData.class);
    Result requestData = requestWrapper.getResult().get(0);
    String procedureCodeStr = requestData.getRowValue("procedureCode");
    long procedureCode = procedureCodeStr == null ? 1L : Long.parseLong(procedureCodeStr);

    List<String> bids = new ArrayList<String>(2);
    for (int i = 0; i < 2; i++) {
      final DeclarerContext declarerContext = ctx.getDeclarerContext(procedureCode);

      final List<DataRow> rows = requestData.getDataRow();
      for (DataRow raw : rows) {
        final String propertyName = raw.getName();
        declarerContext.setValue(propertyName, raw.getValue());
      }
      if (request.attachmens != null) {
        for (Enclosure enclosure : request.attachmens) {
          String name = enclosure.code;
          if (name == null) {
            name = enclosure.zipPath.replace('/', '_');
          }
          declarerContext.addEnclosure(name, enclosure);
        }
      }
      bids.add(declarerContext.declare());
    }
    return createResponse(request, bids, "Принято к обработке", Packet.Status.ACCEPT);
  }

  private ServerResponse onPing(RequestContext ctx) {
    final ServerRequest request = ctx.getRequest();

    if ("putData".equals(request.action.getLocalPart())) {
      if (request.packet.status != Packet.Status.PING) {
        throw new IllegalStateException("Illegal status " + request.packet.status);
      }

      boolean complete = true;
      ServerResponse lastResult = null;
      for (String bid : ctx.getBids()) {
        lastResult = ctx.getResult(bid);
        if (lastResult == null) {
          complete = false;
        }
      }
      if (!complete) {
        return createResponse(request, Arrays.asList(ctx.getBid()), "В обработке", Packet.Status.STATE);
      }
      return lastResult;
    }

    switch (request.packet.status) {
      case REQUEST:
      case PING:
        // проверка состояние это либо запрос либо опрос.
        break;

      default:
        throw new IllegalStateException("Illegal status " + request.packet.status);
    }

    for (String bid : ctx.getBids()) {
      final ServerResponse result = ctx.getResult(bid);
      if (result != null) {
        return result;
      }
      final ServerResponse state = ctx.getState(bid);
      if (state != null) {
        return state;
      }
    }

    return createResponse(request, Arrays.asList(ctx.getBid()), "В обработке", Packet.Status.PROCESS);
  }

  private ServerResponse createResponse(ServerRequest request, List<String> bids, String value, Packet.Status status) {

    final SystemParams systemParams = new SystemParams();
    systemParams.setAppId(0);
    systemParams.setStatusCode(value);
    systemParams.setStatusDate(getCurrentXmlDate());

    Result res1 = new Result();
    res1.setParams(systemParams);

    for (String bid : bids) {
      DataRow r = new DataRow();
      r.setName("internalRequestId");
      r.setValue(bid);
      res1.getDataRow().add(r);
    }


    ServerResponse response = new ServerResponse();
    response.packet = new Packet();
    response.packet.status = status;
    response.packet.exchangeType = "1";
    response.packet.typeCode = Packet.Type.SERVICE;
    response.packet.serviceName = "UniversalMVV";
    response.action = request.action;
    response.appData = XmlTypes.beanToXml(res1);
    return response;
  }

  private XMLGregorianCalendar getCurrentXmlDate() {
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
    } catch (DatatypeConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ServerResponse processStatus(String statusMessage, ReceiptContext receiptContext) {
    Result res1 = new Result();
    final SystemParams systemParams = new SystemParams();
    systemParams.setAppId(0);
    systemParams.setStatusCode(statusMessage);
    systemParams.setStatusDate(getCurrentXmlDate());
    res1.setParams(systemParams);
    ServerResponse response = new ServerResponse();
    response.packet = new Packet();
    response.packet.status = Packet.Status.STATE;
    response.packet.exchangeType = "1";
    response.packet.typeCode = Packet.Type.SERVICE;
    response.appData = XmlTypes.beanToXml(res1);
    response.action = new QName("http://mvv.oep.com/", "updateStatus");
    return response;
  }

  @Override
  public ServerResponse processResult(String resultMessage, ReceiptContext receiptContext) {
    final SystemParams systemParams = new SystemParams();
    systemParams.setAppId(0);
    systemParams.setStatusCode(resultMessage);
    systemParams.setStatusDate(getCurrentXmlDate());
    final Result result = new Result();
    result.setParams(systemParams);
    final TreeSet<String> vars = new TreeSet<String>(receiptContext.getPropertyNames());
    for (final String var : vars) {
      final DataRow row = new DataRow();
      row.setName(var);
      Object value = receiptContext.getVariable(var);
      if (value instanceof Date) {
        String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(value);
        row.setValue(formattedDate);
      } else {
        row.setValue("" + receiptContext.getVariable(var));
      }
      result.getDataRow().add(row);
    }
    ServerResponse response = new ServerResponse();
    response.packet = new Packet();
    response.packet.status = Packet.Status.RESULT;
    response.packet.exchangeType = "1";
    response.packet.typeCode = Packet.Type.SERVICE;
    response.appData = XmlTypes.beanToXml(result);
    response.action = new QName("http://mvv.oep.com/", "updateStatus");

    final Set<String> enclosureNames = receiptContext.getEnclosureNames();
    if (enclosureNames != null) {
      final List<Enclosure> enclosures = new ArrayList<Enclosure>();
      for (final String name : enclosureNames) {
        final Enclosure enclosure = receiptContext.getEnclosure(name);
        enclosure.code = name;
        enclosures.add(enclosure);
        logger.info("add enclosure " + name + " (" + enclosure.zipPath + ")");
      }
      if (!enclosures.isEmpty()) {
        response.attachmens = enclosures;
      }
    }
    return response;
  }

  @Override
  public ServerResponse processReject(String reason, ReceiptContext context) {
    SystemParams params = new SystemParams();
    params.setAppId(0);
    params.setStatusCode(reason);
    params.setStatusDate(getCurrentXmlDate());
    Result result = new Result();
    result.setParams(params);
    ServerResponse response = new ServerResponse();
    response.packet = new Packet();
    response.packet.status = Packet.Status.REJECT;
    response.packet.exchangeType = "1";
    response.packet.typeCode = Packet.Type.SERVICE;
    response.appData = XmlTypes.beanToXml(result);
    response.action = new QName("http://mvv.oep.com/", "updateStatus");
    return response;
  }
}

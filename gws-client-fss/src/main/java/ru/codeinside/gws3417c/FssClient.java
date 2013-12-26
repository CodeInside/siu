/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3417c;

import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.XmlTypes;
import ru.codeinside.gws3417c.types.AppData;
import ru.codeinside.gws3417c.types.data.Curator;
import ru.codeinside.gws3417c.types.data.Request;
import ru.codeinside.gws3417c.types.data.Response;
import ru.codeinside.gws3417c.types.data.ResponseType;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

final public class FssClient implements Client {

  private Packet createPacket(final Packet.Status status) {
    final Packet packet = new Packet();
    packet.recipient = new InfoSystem("FSSR01001", "ФСС России");
    packet.typeCode = Packet.Type.SERVICE;
    packet.status = status;
    packet.date = new Date();
    packet.exchangeType = "1"; // 1 - Запрос на оказание услуги
    return packet;
  }

  @Override
  public ru.codeinside.gws.api.Revision getRevision() {
    return ru.codeinside.gws.api.Revision.rev111111;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("gws3417/SvedRegisterNoPosob.wsdl");
  }

  @Override
  public ClientRequest createClientRequest(ExchangeContext ctx) {
    final String originRequestId = (String) ctx.getVariable("smevOriginRequestId");
    final String requestId = (String) ctx.getVariable("smevRequestId");
    final Boolean smevPool = (Boolean) ctx.getVariable("smevPool");


    final Packet packet = new Packet();
    packet.recipient = new InfoSystem("FSSR01001", "ФСС России");
    packet.typeCode = Packet.Type.SERVICE;
    packet.date = new Date();
    packet.exchangeType = "1"; // 1 - Запрос на оказание услуги
    packet.originRequestIdRef = originRequestId;
    packet.testMsg = (String) ctx.getVariable("smevTest");

    final ClientRequest request = new ClientRequest();
    request.packet = packet;
    request.action = new QName("http://fss.ru/SvedRegisterNoPosob", "request");

    if (Boolean.TRUE == smevPool) {
      packet.status = Packet.Status.PING;
      packet.requestIdRef = requestId;
    } else {
      packet.status = Packet.Status.REQUEST;
      request.appData = createAppData(ctx);
    }
    return request;
  }

  private String createAppData(ExchangeContext ctx) {
    final Request request = new Request();
    request.setRegionFrom((String) ctx.getVariable("regionFrom"));
    request.setNameOrganizationFrom((String) ctx.getVariable("nameOrganizationFrom"));
    request.setINameCiv((String) ctx.getVariable("iNameCiv"));
    request.setFNameCiv((String) ctx.getVariable("fNameCiv"));
    request.setMNameCiv((String) ctx.getVariable("mNameCiv"));
    request.setCodeKind((String) ctx.getVariable("codeKind"));
    request.setSeriesNumber((String) ctx.getVariable("seriesNumber"));
    request.setInn((String) ctx.getVariable("inn"));
    request.setSnils((String) ctx.getVariable("snils"));
    request.setDocDatCiv(XmlTypes.date((String) ctx.getVariable("docDatCiv")));
    request.setStatus(Curator.find((String) ctx.getVariable("status")));
    request.setINameKind((String) ctx.getVariable("iNameKind"));
    request.setFNameKind((String) ctx.getVariable("fNameKind"));
    request.setMNameKind((String) ctx.getVariable("mNameKind"));
    request.setDocDatKind(XmlTypes.date((String) ctx.getVariable("docDatKind")));
    request.setSbDoc((String) ctx.getVariable("sbDoc"));
    request.setNbDoc((String) ctx.getVariable("nbDoc"));
    request.setStartDate(XmlTypes.date((String) ctx.getVariable("startDate")));
    request.setEndDate(XmlTypes.date((String) ctx.getVariable("endDate")));
    return new XmlTypes(Request.class).toXml(request);
  }

  @Override
  public void processClientResponse(ClientResponse response, ExchangeContext context) {
    Boolean pooled = (Boolean) context.getVariable("smevPool");
    if (response.verifyResult.error != null) {
      context.setVariable("smevPool", false);
      context.setVariable("smevError", response.verifyResult.error);
    } else if (!new QName("http://fss.ru/SvedRegisterNoPosob/request", "requestResponse").equals(response.action)) {
      context.setVariable("smevPool", false);
      context.setVariable("smevError", "Неизвестный ответ " + response.action);
    } else {
      if (response.packet.status == Packet.Status.ACCEPT) {
        context.setVariable("smevPool", true);
        if (Boolean.TRUE != pooled) {
          context.setVariable("smevRequestId", response.packet.requestIdRef);
          context.setVariable("smevOriginRequestId", response.packet.originRequestIdRef);
        }
      } else {
        context.setVariable("smevPool", false);
        AppData appData = (AppData) XmlTypes.elementToBean(response.appData, AppData.class);
        ResponseType responseType = appData.getТипОтвета();
        context.setVariable("hint", appData.getПримечание());
        if (responseType == ResponseType.ОШИБКА_В_ЗАПРОСЕ) {
          context.setVariable("smevError", "Ошибка в запросе: " + appData.getПримечание());
          context.setVariable("status", "Ошибка");
        } else if (responseType == ResponseType.ОТВЕТ) {
          context.setVariable("status", "Данные найдены");

          final Response r = appData.getОтвет();

          context.setVariable("regionTo_response", r.getRegionTo());
          context.setVariable("nameOrganizationTo_response", r.getNameOrganizationTo());
          context.setVariable("regionToPay_response", r.getRegionToPay());
          context.setVariable("iNameCiv_response", r.getINameCiv());
          context.setVariable("fNameCiv_response", r.getFNameCiv());
          context.setVariable("mNameCiv_response", r.getMNameCiv());
          context.setVariable("docDatCiv_response", date(r.getDocDatCiv()));
          context.setVariable("codeKind_response", r.getCodeKind());
          context.setVariable("seriesNumber_response", r.getSeriesNumber());
          context.setVariable("inn_response", r.getInn());
          context.setVariable("snils_response", r.getSnils());
          if (r.getStatus() != null) {
            context.setVariable("status_response", r.getStatus().value());
          }
          context.setVariable("iNameKind_response", r.getINameKind());
          context.setVariable("fNameKind_response", r.getFNameKind());
          context.setVariable("mNameKind_response", r.getMNameKind());
          context.setVariable("docDatKind_response", date(r.getDocDatKind()));
          context.setVariable("sbDoc_response", r.getSbDoc());
          context.setVariable("nbDoc_response", r.getNbDoc());
          context.setVariable("startDate_response", date(r.getStartDate()));
          context.setVariable("endDate_response", date(r.getEndDate()));
          context.setVariable("registering_response", r.isRegistering());
          context.setVariable("obtainingGrants1_response", r.isObtainingGrants1());
          context.setVariable("obtainingGrants2_response", r.isObtainingGrants2());
          context.setVariable("monthsNumber_response", r.getMonthsNumber());

        } else {
          context.setVariable("status", "Данные не найдены");
        }
      }
    }
  }

  private String date(final XMLGregorianCalendar calendar) {
    if (calendar == null) {
      return null;
    }
    return new SimpleDateFormat("dd.MM.yyyy").format(calendar.toGregorianCalendar().getTime());
  }

}

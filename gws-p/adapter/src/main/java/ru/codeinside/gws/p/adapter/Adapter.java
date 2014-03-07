/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.adapter;

import ru.codeinside.gws.api.*;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.*;
import javax.xml.ws.soap.SOAPBinding;
import java.util.Date;


@ServiceMode(Service.Mode.MESSAGE)
@WebServiceProvider
@BindingType(value = SOAPBinding.SOAP11HTTP_MTOM_BINDING)
public class Adapter implements Provider<SOAPMessage> {

  final ProviderEntry entry;

  public Adapter(ProviderEntry entry) {
    this.entry = entry;
  }

  @Override
  public SOAPMessage invoke(final SOAPMessage request) {
    String marker = getMarker(request);

    final ServerRequest serverRequest = entry.protocol.processRequest(request, entry.wsService, entry.wsPortDef);

    if (entry.logService != null) {
      entry.logService.log(marker, serverRequest);
    }

    final ServerResponse serverResponse;
    try {
      serverResponse = entry.declarant.processRequest(serverRequest, entry.name);
    } catch (RuntimeException e) {
      if (entry.logService != null) {
        entry.logService.log(marker, false, e.getStackTrace());
      }
      throw e;
    }

    if (entry.logService != null) {
      entry.logService.log(marker, serverResponse);
    }

    final Packet resp = serverResponse.packet;
    final Packet req = serverRequest.packet;

    // TODO: перенести всё в протокол!!!

    // это вообще убрать из типа ответа
    if (serverResponse.action == null) {
      serverResponse.action = serverRequest.action;
    }

    // перевернём отправителя и получателя
    resp.sender = req.recipient;
    resp.recipient = req.sender;
    resp.originator = req.originator;

    // дата обработки
    if (resp.date == null) {
      resp.date = new Date();
    }

    // начало цепочки запросов (обычно поставщик должен обеспечить!)
    if (resp.originRequestIdRef == null) {
      if (req.originRequestIdRef != null) {
        // связываем с запросом
        resp.originRequestIdRef = req.originRequestIdRef;
      } else if (serverRequest.routerPacket != null) {
        // связываем с ID присвоенным роутером
        resp.originRequestIdRef = serverRequest.routerPacket.messageId;
      } else {
        // без роутера используем ID запроса.
        resp.originRequestIdRef = req.requestIdRef;
      }
    }
    // цепочка запросов
    if (resp.requestIdRef == null) {
      if (serverRequest.routerPacket != null) {
        // связываем с ID присвоенным роутером
        resp.requestIdRef = serverRequest.routerPacket.messageId;
      } else {
        // без роутера используем ID запроса.
        resp.requestIdRef = req.requestIdRef;
      }
    }

    // тип ответа
    if (resp.exchangeType == null) {
      resp.exchangeType = req.exchangeType;
    }

    return entry.protocol.processResponse(serverResponse, entry.wsService, entry.wsPortDef);
  }

  private String getMarker(SOAPMessage request) {
    String[] markerIds = request.getMimeHeaders().getHeader("serverMarker");
    if (markerIds == null || markerIds.length <= 0) {
      return "";
    }
    return markerIds[0];
  }
}

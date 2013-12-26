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
  private final LogServiceProvider provider;

  public Adapter(final ProviderEntry entry, LogServiceProvider logProvider) {
    this.entry = entry;
    this.provider = logProvider;
  }

  @Override
  public SOAPMessage invoke(final SOAPMessage request) {
    final QName wsService = entry.wsService;
    final ServiceDefinition.Port wsPortDef = entry.wsPortDef;
    final String wsName = entry.name;
    final ServerProtocol wsProtocol = entry.protocol;
    final Declarant wsDeclarant = entry.declarant;

    String marker = getMarker(request);

    final ServerRequest serverRequest = wsProtocol.processRequest(request, wsService, wsPortDef);

    getLogService().log(marker, serverRequest);

    final ServerResponse serverResponse;
    try {
      serverResponse = wsDeclarant.processRequest(serverRequest, wsName);
    }catch (RuntimeException e){
      getLogService().log(marker, false, e.getStackTrace());
      throw e;
    }

    getLogService().log(marker, serverResponse);

    final Packet resp = serverResponse.packet;
    final Packet req = serverRequest.packet;

    // TODO: перенести всё в протокол!!!

    // это вообще убрать их типа ответа
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

    return wsProtocol.processResponse(serverResponse, wsService, wsPortDef);
  }

  private LogService getLogService() {
    if(provider == null){
        System.out.println("adapter provider is null");
        return LogServiceFake.fakeLog();
    }
    return provider.get();
  }

  private String getMarker(SOAPMessage request) {
    String[] markerIds = request.getMimeHeaders().getHeader("serverMarker");
    if(markerIds == null || markerIds.length <= 0){
        return "";
    }
    return markerIds[0];
  }
}

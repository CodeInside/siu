/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.adapter;

import ru.codeinside.gws.api.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPBinding;
import java.util.Date;


@ServiceMode(Service.Mode.MESSAGE)
@WebServiceProvider
@BindingType(value = SOAPBinding.SOAP11HTTP_MTOM_BINDING)
public class Adapter implements Provider<SOAPMessage> {

  final ProviderEntry entry;

  @Resource
  WebServiceContext context;

  public Adapter(ProviderEntry entry) {
    this.entry = entry;
  }

  @Override
  public SOAPMessage invoke(final SOAPMessage request) {

    final ServerLog serverLog = getServerLog();

    final ServerRequest serverRequest = entry.protocol.processRequest(request, entry.wsService, entry.wsPortDef);
    if (serverLog != null) {
      serverLog.logRequest(serverRequest);
    }

    final ServerResponse serverResponse;
    try {
      serverResponse = entry.declarant.processRequest(serverRequest, entry.name);
    } catch (RuntimeException e) {
      if (serverLog != null) {
        serverLog.log(e);
      }
      throw e;
    }

    return entry.protocol.processResponse(serverRequest, serverResponse, entry.wsService, entry.wsPortDef, serverLog);
  }

  private ServerLog getServerLog() {
    final HttpServletRequest httpServletRequest = (HttpServletRequest) context
      .getMessageContext()
      .get(MessageContext.SERVLET_REQUEST);
    return (ServerLog) httpServletRequest.getAttribute(ServerLog.class.getName());
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api.impl;

import org.junit.Test;
import ru.codeinside.gws.core.ExceptionProducer;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.ServerLog;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FileTest {

  @Test
  public void test() throws IOException {
    File logs = new File(new File("target"), "logs");
    LogSettings.setLogRoot(logs);
    LogServiceFileImpl logServiceFile = new LogServiceFileImpl();

    InfoSystem sender = new InfoSystem("sender", "some name");
    InfoSystem recipient = new InfoSystem("recipient", "other side");

    Packet sendPacket = new Packet();
    sendPacket.originator = sender;
    sendPacket.recipient = recipient;
    sendPacket.sender = sender;

    Packet responsePacket = new Packet();
    responsePacket.originator = sender;
    responsePacket.recipient = sender;
    responsePacket.sender = recipient;


    FileClientLog clientLog = (FileClientLog) logServiceFile.createClientLog(1L, "client", "1", true, false, null);
    ClientRequest request = new ClientRequest();
    request.packet = sendPacket;
    clientLog.log(ExceptionProducer.fire("foo"));
    clientLog.logRequest(request);
    ClientResponse response = new ClientResponse();
    response.packet = responsePacket;
    clientLog.logResponse(response);
    clientLog.getHttpInStream().write("in\n".getBytes());
    clientLog.getHttpOutStream().write("out\n".getBytes());
    clientLog.close();


    assertEquals("java.lang.RuntimeException: foo\n" +
      "\tat ru.codeinside.gws.core.ExceptionProducer.fire(ExceptionProducer.java:12)\n", clientLog.metadata.error);

    logServiceFile.setServerLogEnabled(true);
    logServiceFile.setServerLogEnabled("server", true);
    ServerLog serverLog = logServiceFile.createServerLog("server");
    ServerRequest serverRequest = new ServerRequest();
    serverRequest.packet = sendPacket;
    serverLog.logRequest(serverRequest);
    ServerResponse serverResponse = new ServerResponse();
    serverResponse.packet = responsePacket;
    serverLog.logResponse(serverResponse);
    serverLog.close();
  }

}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api.impl;

import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.ServerLog;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.core.ExceptionProducer;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileTest {

  final InfoSystem sender = new InfoSystem("sender", "some name");
  final InfoSystem recipient = new InfoSystem("recipient", "other side");
  final File logs = new File(new File("target"), "logs");
  final LogServiceFileImpl logServiceFile;

  {
    LogSettings.setLogRoot(logs);
    logServiceFile = new LogServiceFileImpl();
  }

  @Test
  public void testExceptionText() throws IOException {
    FileClientLog log = createClient(1L, "client", "1", true, false, null);
    log.log(ExceptionProducer.fire("foo"));
    assertEquals("java.lang.RuntimeException: foo\n" +
      "\tat ru.codeinside.gws.core.ExceptionProducer.fire(ExceptionProducer.java:12)\n", log.metadata.error);
  }

  @Test
  public void testExceptionSave() throws IOException {
    FileClientLog log = createClient(2L, "client", "2", false, true, null);
    log.log(ExceptionProducer.fire("foo2"));
    File logDir = createLogDir(log);
    assertFalse(logDir.exists());
    log.close();
    assertTrue(logDir.exists());
  }

  @Test
  public void testExceptionNotSave() throws IOException {
    FileClientLog log = createClient(3L, "client", "3", false, false, null);
    log.log(ExceptionProducer.fire("foo3"));
    log.close();
    assertFalse(createLogDir(log).exists());
  }


  @Test
  public void testServer() throws IOException {
    logServiceFile.setServerLogEnabled(true);
    logServiceFile.setServerLogEnabled("server", true);

    ServerLog serverLog = logServiceFile.createServerLog("server");
    Packet sendPacket = new Packet();
    sendPacket.originator = sender;
    sendPacket.recipient = recipient;
    sendPacket.sender = sender;
    ServerRequest serverRequest = new ServerRequest();
    serverRequest.packet = sendPacket;
    serverLog.logRequest(serverRequest);


    Packet responsePacket = new Packet();
    responsePacket.originator = sender;
    responsePacket.recipient = sender;
    responsePacket.sender = recipient;
    ServerResponse serverResponse = new ServerResponse();
    serverResponse.packet = responsePacket;
    serverLog.logResponse(serverResponse);

    serverLog.close();
  }

  File createLogDir(FileLog log) {
    String logDir = log.getDirName();
    int len = logDir.length();
    return new File(logs, "smev/" + logDir.charAt(len - 2) + "/" + logDir.charAt(len - 1) + "/" + logDir);
  }


  FileClientLog createClient(
    long bidId, String component, String pid, boolean logEnabled, boolean logErrors, String status
  )
    throws IOException {

    FileClientLog clientLog = (FileClientLog) logServiceFile.createClientLog(
      bidId, component, pid, logEnabled, logErrors, status
    );

    Packet sendPacket = new Packet();
    sendPacket.originator = sender;
    sendPacket.recipient = recipient;
    sendPacket.sender = sender;
    ClientRequest request = new ClientRequest();
    request.packet = sendPacket;

    clientLog.logRequest(request);

    Packet responsePacket = new Packet();
    responsePacket.originator = sender;
    responsePacket.recipient = sender;
    responsePacket.sender = recipient;
    ClientResponse response = new ClientResponse();
    response.packet = responsePacket;
    clientLog.logResponse(response);

    clientLog.getHttpInStream().write("in\n".getBytes());

    clientLog.getHttpOutStream().write("out\n".getBytes());

    return clientLog;
  }
}

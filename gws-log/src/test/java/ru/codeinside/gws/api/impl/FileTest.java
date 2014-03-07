/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api.impl;

import org.junit.Test;
import ru.codeinside.gws.api.ClientLog;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;

import java.io.File;
import java.io.IOException;

public class FileTest {

  @Test
  public void test() throws IOException {
    LogSettings.setLogRoot(new File(new File("target"), "logs"));

    LogServiceFileImpl logServiceFile = new LogServiceFileImpl();
    String marker = logServiceFile.generateMarker();

    logServiceFile.log(marker, "text", true, true);

    InfoSystem infoSystem = new InfoSystem("first", "some name");
    Packet packet = new Packet();
    packet.originator = infoSystem;
    packet.recipient = infoSystem;
    packet.sender = infoSystem;


    ClientLog clientLog = logServiceFile.createClientLog("client", "1");
    ClientRequest request = new ClientRequest();
    request.packet = packet;
    clientLog.logRequest(request);
    ClientResponse response = new ClientResponse();
    response.packet = packet;
    clientLog.logResponse(response);
    clientLog.getHttpInStream().write("in\n".getBytes());
    clientLog.getHttpOutStream().write("out\n".getBytes());
    clientLog.close();

    ServerRequest serverRequest = new ServerRequest();
    serverRequest.packet = packet;

    logServiceFile.log(marker, serverRequest);

    ServerResponse serverResponse = new ServerResponse();
    serverResponse.packet = packet;

    logServiceFile.log(marker, serverResponse);
  }

}

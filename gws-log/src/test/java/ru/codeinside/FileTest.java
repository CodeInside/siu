/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.codeinside.gws.api.*;
import ru.codeinside.gws.api.impl.LogServiceFileImpl;

public class FileTest {

    @Test
    @Ignore
    public void test() {
        LogServiceFileImpl logServiceFile = new LogServiceFileImpl();
        String marker = logServiceFile.generateMarker();

        logServiceFile.log(marker, "text", true, true);

        InfoSystem infoSystem = new InfoSystem("first", "some name");
        Packet packet = new Packet();
        packet.originator = infoSystem;
        packet.recipient = infoSystem;
        packet.sender = infoSystem;


        ClientRequest request = new ClientRequest();
        request.packet = packet;

        logServiceFile.log(marker, request);

        ClientResponse response = new ClientResponse();
        response.packet = packet;

        logServiceFile.log(marker, response);

        ServerRequest serverRequest = new ServerRequest();
        serverRequest.packet = packet;

        logServiceFile.log(marker, serverRequest);

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.packet = packet;

        logServiceFile.log(marker, serverResponse);
    }

}

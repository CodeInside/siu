/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3417c;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.VerifyResult;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;

public class FssClientTest extends Assert {

  @Test
  public void testCreateClientRequest() throws Exception {

    DummyContext ctx = new DummyContext();
    ctx.setVariable("smevTest", "Первичный запрос");
    ctx.setVariable("regionFrom", "01");
    ctx.setVariable("nameOrganizationFrom", "ФСС");
    ctx.setVariable("iNameCiv", "Иванов");
    ctx.setVariable("fNameCiv", "Иван");
    ctx.setVariable("mNameCiv", "Иванович");
    ctx.setVariable("codeKind", "21");
    ctx.setVariable("seriesNumber", "0000000000");
    ctx.setVariable("inn", "000000000000");
    ctx.setVariable("snils", "00000000000");
    ctx.setVariable("docDatCiv", "01.01.1970");
    ctx.setVariable("status", "Отец");
    ctx.setVariable("iNameKind", "Иванов");
    ctx.setVariable("fNameKind", "Иван");
    ctx.setVariable("mNameKind", "Иванович");
    ctx.setVariable("docDatKind", "01.01.2000");
    ctx.setVariable("sbDoc", "00");
    ctx.setVariable("nbDoc", "0000");
    ctx.setVariable("startDate", "01.01.2000");
    ctx.setVariable("endDate", "01.01.2001");

    FssClient fss = new FssClient();
    ClientRequest request = fss.createClientRequest(ctx);
    assertTrue(request.appData.startsWith("<запрос "));
  }

  @Test
  public void testResponse() throws SOAPException, IOException {
    InputStream is = getClass().getClassLoader().getResourceAsStream("response.xml");
    SOAPMessage message = MessageFactory.newInstance().createMessage(null, is);


    NodeList nodes = message.getSOAPPart().getElementsByTagNameNS("http://smev.gosuslugi.ru/rev111111", "AppData");
    assertEquals(1, nodes.getLength());

    FssClient fss = new FssClient();
    DummyContext ctx = new DummyContext();
    ClientResponse response = new ClientResponse();
    response.verifyResult = new VerifyResult(null, null, null);
    response.action = new QName("http://fss.ru/SvedRegisterNoPosob/request", "requestResponse");
    response.packet = new Packet();
    response.packet.status = Packet.Status.RESULT;
    response.appData = (Element) nodes.item(0);
    fss.processClientResponse(response, ctx);


  }
}

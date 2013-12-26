/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3572c;


import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.VerifyResult;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class ExportReceiptResponseTest {
  private ExchangeContext ctx;
  private GMPClient3572 gmpClient3572;

  @Before
  public void setUp() throws Exception {
    ctx = new DummyContext();
    gmpClient3572 = new GMPClient3572();
  }

  @Test
  public void testSuccessResponse() throws SOAPException, IOException, ParserConfigurationException {
    InputStream is = getClass().getClassLoader().getResourceAsStream("gmp/export_receipt/response.xml");
    SOAPMessage message = MessageFactory.newInstance().createMessage(null, is);

    SOAPPart soapPart = message.getSOAPPart();
    NodeList nodes = soapPart.getElementsByTagNameNS("http://smev.gosuslugi.ru/rev111111", "AppData");
    assertEquals(1, nodes.getLength());
    ctx.setVariable("operationType", "exportData");
    ctx.setVariable("exportRequestType", "QUITTANCE");
    gmpClient3572.processClientResponse(getClientResponse(nodes), ctx);
    assertEquals(true, ctx.getVariable("responseSuccess"));
    assertEquals(1L, ctx.getVariable("quittance"));
    //assertEquals("", ctx.getVariable("-quittance"));
    assertEquals("18810XГ50АК586032ZZ0", ctx.getVariable("quittanceSupplierBillID_1"));
    assertEquals("5417150774572399", ctx.getVariable("quittancePayerIdentifier_1"));
    assertEquals(0L, ctx.getVariable("quittanceBalance_1"));
    assertEquals("1", ctx.getVariable("quittanceBillStatus_1"));
    assertEquals("f51e3aa5-ca9a-455d-a109-e441a5f13063", ctx.getVariable("quittanceSystemIdentifier_1"));
    assertEquals("044525219", ctx.getVariable("quittanceBankBIK_1"));
  }

  @Test
  public void testErrorResponse() throws SOAPException, IOException, ParserConfigurationException {
    InputStream is = getClass().getClassLoader().getResourceAsStream("gmp/export_receipt/gmp_import_quittance_error.xml");
    SOAPMessage message = MessageFactory.newInstance().createMessage(null, is);

    SOAPPart soapPart = message.getSOAPPart();
    NodeList nodes = soapPart.getElementsByTagNameNS("http://smev.gosuslugi.ru/rev111111", "AppData");
    assertEquals(1, nodes.getLength());
    ctx.setVariable("operationType", "exportData");
    ctx.setVariable("exportRequestType", "QUITTANCE");
    gmpClient3572.processClientResponse(getClientResponse(nodes), ctx);
    assertEquals(false, ctx.getVariable("responseSuccess"));
    assertEquals(false, ctx.getVariable("smevPool"));
}

  private ClientResponse getClientResponse(NodeList nodes) throws ParserConfigurationException {
    ClientResponse response = new ClientResponse();
    response.verifyResult = new VerifyResult(null, null, null);
    response.action = new QName("http://roskazna.ru/SmevUnifoService/", "UnifoTransferMsg");
    response.packet = new Packet();
    response.packet.status = Packet.Status.RESULT;

    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    final Document doc = dbf.newDocumentBuilder().newDocument();
    Element appData = (Element) nodes.item(0);
    appData = (Element) doc.importNode(appData, true);
    response.appData = appData;
    return response;
  }
}

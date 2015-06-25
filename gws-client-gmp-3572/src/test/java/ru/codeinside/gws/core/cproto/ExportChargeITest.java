/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.VerifyResult;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.stubs.DummyContext;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;
import ru.codeinside.gws3572c.GMPClient3572;
import xmltype.R;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.logging.LogManager;

@Ignore
public class ExportChargeITest extends Assert {

  static {
    final InputStream is = ExportChargeITest.class.getClassLoader().getResourceAsStream("logging.properties");
    try {
      if (is != null) {
        LogManager.getLogManager().readConfiguration(is);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private GMPClient3572 client;
  public static final String SERVICE_ADDRESS = "http://188.254.16.92:7777/gateway/services/SID0003218?wsdl";
  private InfoSystem pnzr01581;
  private ClientRev111111 rev111111;

  private DummyContext createContext() throws ParseException {
    DummyContext ctx = new DummyContext();
    ctx.setVariable("operationType", "exportData");
    ctx.setVariable("postBlockTimeStamp", DateUtils.parseDate("2001-12-17 09:30:47", new String[]{"yyyy-MM-dd HH:mm:ss"}));  // не обязательно
    ctx.setVariable("postBlockId", "1538442"); // генерируется
    ctx.setVariable("postBlockSenderIdentifier", "002811");


    ctx.setVariable("SupplierBillIDBlock", 1L);
    ctx.setVariable("SupplierBillID_1", "Ъ0028112706131146519");
    ctx.setVariable("-SupplierBillIDBlock", "");
    return ctx;
  }


  @Before
  public void setUp() throws Exception {
    pnzr01581 = new InfoSystem("8201", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    CryptoProvider cryptoProvider = new CryptoProvider();
    XmlNormalizer xmlNormalizer = new XmlNormalizerImpl();
    rev111111 = new ClientRev111111(new ServiceDefinitionParser(), cryptoProvider, xmlNormalizer, null);
    client = new GMPClient3572();
    // client.bindCryptoProvider (cryptoProvider);
    HttpTransportPipe.dump = true;
    ClientRev111111.validate = true;
  }

  @Test
  public void testExportChargeStatus() throws Exception {

    ExchangeContext ctx = createContext();
    ctx.setVariable("smevTest", "Первичный запрос");
    ctx.setVariable("exportRequestType", "CHARGESTATUS");

    ClientRequest request = client.createClientRequest(ctx);
    System.out.print(request.appData);

    request.portAddress = SERVICE_ADDRESS;
    request.packet.sender = request.packet.originator = pnzr01581;


    ClientResponse response = rev111111.send(client.getWsdlUrl(), request, null);
    client.processClientResponse(response, ctx);

    assertEquals(true, ctx.getVariable("responseSuccess"));
    assertEquals(1L, ctx.getVariable("charge"));
    // assertEquals("", ctx.getVariable("-quittance"));
    assertEquals("18810XГ50АК586032ZZ0", ctx.getVariable("quittanceSupplierBillID_1"));
    assertEquals("5417150774572399", ctx.getVariable("quittancePayerIdentifier_1"));
    assertEquals(0L, ctx.getVariable("quittanceBalance_1"));
    assertEquals("1", ctx.getVariable("quittanceBillStatus_1"));
    assertEquals("f51e3aa5-ca9a-455d-a109-e441a5f13063", ctx.getVariable("quittanceSystemIdentifier_1"));
    assertEquals("044525219", ctx.getVariable("quittanceBankBIK_1"));

  }

  @Test
  public void testExportChargeNotFullMatchedStatus() throws Exception {

    ExchangeContext ctx = createContext();
    ctx.setVariable("smevTest", "Первичный запрос");
    ctx.setVariable("exportRequestType", "CHARGENOTFULLMATCHED");

    ClientRequest request = client.createClientRequest(ctx);
    System.out.print(request.appData);

    request.portAddress = SERVICE_ADDRESS;
    request.packet.sender = request.packet.originator = pnzr01581;


    ClientResponse response = rev111111.send(client.getWsdlUrl(), request, null);
    client.processClientResponse(response, ctx);

    assertEquals(true, ctx.getVariable("responseSuccess"));
    assertEquals(1L, ctx.getVariable("charge"));
    // assertEquals("", ctx.getVariable("-quittance"));
    assertEquals("18810XГ50АК586032ZZ0", ctx.getVariable("quittanceSupplierBillID_1"));
    assertEquals("5417150774572399", ctx.getVariable("quittancePayerIdentifier_1"));
    assertEquals(0L, ctx.getVariable("quittanceBalance_1"));
    assertEquals("1", ctx.getVariable("quittanceBillStatus_1"));
    assertEquals("f51e3aa5-ca9a-455d-a109-e441a5f13063", ctx.getVariable("quittanceSystemIdentifier_1"));
    assertEquals("044525219", ctx.getVariable("quittanceBankBIK_1"));
  }

  @Test
  public void testParseSampleRequest() throws Exception {
    SOAPMessage soapResource = R.getSoapResource("gmp/export_charge/sample_request.xml");
    ClientResponse response = new ClientResponse();
    response.action = new QName("http://roskazna.ru/SmevUnifoService/", "UnifoTransferMsg");
    response.verifyResult = new VerifyResult(null, null, null);
    response.appData = (Element) soapResource.getSOAPBody().getElementsByTagNameNS("http://smev.gosuslugi.ru/rev111111", "AppData").item(0);
    assertNotNull(response.appData);
    DummyContext context = createContext();
    client.processClientResponse(response, context);
    assertNotNull(context.getVariable("BillDate_1"));
  }
}
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.stubs.DummyContext;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;
import ru.codeinside.gws3572c.GMPClient3572;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.logging.LogManager;
@Ignore
public class DoAcknowledgmentRequestITest extends Assert {

  static {
    final InputStream is = DoAcknowledgmentRequestITest.class.getClassLoader().getResourceAsStream("logging.properties");
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

  private DummyContext createContextForErrorRequest() throws ParseException {
    DummyContext ctx = new DummyContext();
    ctx.setVariable("postBlockIdRequest", "13454"); // идентификатор запроса
    ctx.setVariable("postBlockSenderIdentifier", "002811"); // идентификатор отправителя
    ctx.setVariable("SupplierBillID", "FK800122222222222221"); // идентификатор начисления
    ctx.setVariable("paymentBlock", "1");
    ctx.setVariable("paymentSystemId_1", "1"); // идентификатор платежа для квитирования

    ctx.setVariable("operationType", "DoAcknowledgmentRequest");
    return ctx;
  }


  @Before
  public void setUp() throws Exception {
    pnzr01581 = new InfoSystem("8201", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    CryptoProvider cryptoProvider = new CryptoProvider();
    XmlNormalizer xmlNormalizer = new XmlNormalizerImpl();
    rev111111 = new ClientRev111111(new ServiceDefinitionParser(), cryptoProvider, xmlNormalizer, null);
    client = new GMPClient3572();
    client.bindCryptoProvider (cryptoProvider);
    HttpTransportPipe.dump = true;
    ClientRev111111.validate = true;
  }

  @Test
  public void testDoAcknowledgmentRequest() throws Exception {

    ExchangeContext ctx = createContextForErrorRequest();
    ctx.setVariable("smevTest", "Первичный запрос");

    ClientRequest request = client.createClientRequest(ctx);

    request.portAddress = SERVICE_ADDRESS;
    request.packet.sender = request.packet.originator = pnzr01581;


    ClientResponse response = rev111111.send(client.getWsdlUrl(), request, null);
    client.processClientResponse(response, ctx);

//    Assert.assertEquals("9500", ctx.getVariable("ticketPostBlockSenderIdentifier"));
    Assert.assertNotNull( ctx.getVariable("requestProcessResultErrorCode"));
  }



}
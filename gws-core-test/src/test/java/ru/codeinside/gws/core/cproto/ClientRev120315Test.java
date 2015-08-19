/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import org.junit.Test;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.stubs.DummyContext;
import ru.codeinside.gws.stubs.DummyProvider;
import ru.codeinside.gws.stubs.R;
import ru.codeinside.gws.stubs.TestServer;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;
import ru.codeinside.gws3970c.UniversalClient;

import javax.xml.namespace.QName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/*
 * логика работы сервиса:
 * 1. если переменная smevPool не установлена
 *     1.1 отправить запрос putData с параметрами
 *     1.2 получить от сервера ответ:
 *         - ошибка
 *         - запрос принят на обработку
 *           1.3 установить переменную smevPool
 *           1.4 установить параметр internalRequestId в контекст
 * 2. если переменная smevPool установлена
 *    2.1 отправить запрос updateStatus с параметром internalRequestId
 *    2.2 получить от сервера ответ:
 *        - ошибка
 *        - получить все параметры из ответа и установить их в контекст с их именами
 */
public class ClientRev120315Test {

  static {
    R.init();
    ClientRev120315.validate = false;// в универсальном клиенет cхема чуть не по СМЭВ.
  }

  @Test
  public void testGetRevision() throws Exception {
    ClientRev120315 rev120315 = new ClientRev120315(new ServiceDefinitionParser(), new DummyProvider(),
            new XmlNormalizerImpl(), null);
    assertEquals(Revision.rev120315, rev120315.getRevision());
  }

  @Test
  public void testPutData() throws Exception {
    final int PORT = 7777;
    final String PORT_ADDRES = "http://127.0.0.1:" + PORT;
    final TestServer testServer = new TestServer();
    testServer.start(PORT);
    UniversalClient universalClient = new UniversalClient();
    try {
      ClientRev120315 rev120315 = new ClientRev120315(new ServiceDefinitionParser(), new DummyProvider(),
              new XmlNormalizerImpl(), null);
      DummyContext ctx = new DummyContext();

      //отправить первый запрос
      ClientRequest request = createRequest(PORT_ADDRES, universalClient, ctx);
      {
        Enclosure enc = new Enclosure("req_xxx.txt", "12345".getBytes());
        request.enclosureDescriptor = "123";
        request.enclosures = new Enclosure[]{enc};
      }
      assertEquals(new QName("http://mvv.oep.com/", "putData"), request.action);
      testServer.setResponseBody("mvvact/putData/response.xml");
      ClientResponse response = rev120315.send(universalClient.getWsdlUrl(), request, null);
      universalClient.processClientResponse(response, ctx);

      //проверить реакцию на успешный ответ на вызов putData
      assertEquals(true, ctx.getVariable("smevPool"));
      assertEquals("506d40ab5532d72a4003b511", ctx.getVariable("internalRequestId"));
      assertEquals(0L, ctx.getVariable("app_id"));
      assertEquals("В очереди", ctx.getVariable("status_title"));
      assertEquals("1", ctx.getVariable("status_pgu"));
      assertEquals("040", ctx.getVariable("status_code"));

      //отправить следующий запрос
      testServer.setResponseBody("mvvact/updateStatus/UpdateStatus_response.xml");
      request = createRequest(PORT_ADDRES, universalClient, ctx);
      assertEquals(new QName("http://mvv.oep.com/", "updateStatus"), request.action);
      response = rev120315.send(universalClient.getWsdlUrl(), request, null);
      universalClient.processClientResponse(response, ctx);

      // ответ с вложением
      testServer.setResponseBody("mvvact/updateStatus/response2.xml");
      request = createRequest(PORT_ADDRES, universalClient, ctx);
      assertEquals(new QName("http://mvv.oep.com/", "updateStatus"), request.action);
      response = rev120315.send(universalClient.getWsdlUrl(), request, null);
      assertEquals("xxxxx", response.enclosureDescriptor);
      assertEquals(1, response.enclosures.length);
      Enclosure enclosure = response.enclosures[0];
      assertEquals("req_f73251fa-6030-4ca0-b944-9394be769c3a.xml", enclosure.fileName);
      assertEquals(2332, enclosure.content.length);
      assertNotNull(enclosure.signature);
      assertTrue(enclosure.signature.valid);
      assertEquals(64, enclosure.signature.sign.length);
      assertEquals("T=Генеральный директор, CN=Семенкин Максим Викторович, O=ООО КодИнсайд, " +
        "L=Пенза, ST=58 Пензенская область, C=RU, EMAILADDRESS=maxim.semenkin@gmail.ru, " +
        "OID.1.2.643.3.131.1.1=5837040135, OID.1.2.643.100.1=1095837000929", enclosure.signature.certificate.getSubjectDN().getName());
      universalClient.processClientResponse(response, ctx);

    } finally {
      testServer.stop();
    }
  }

  private ClientRequest createRequest(String port, Client client, ExchangeContext ctx) {
    ClientRequest request = client.createClientRequest(ctx);
    request.portAddress = port;
    request.packet.sender = request.packet.originator = new InfoSystem("test", "test");
    return request;
  }

}

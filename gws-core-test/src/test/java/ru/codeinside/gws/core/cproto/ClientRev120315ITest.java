/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import org.junit.Ignore;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.stubs.DummyContext;
import ru.codeinside.gws.stubs.DummyProvider;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;
import ru.codeinside.gws3970c.UniversalClient;

@Ignore("Пока ручной тест")
public class ClientRev120315ITest {

  @Test
  public void testIteraction() throws Exception {
    InfoSystem pnzr01581 = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    String ADDRESS = "http://localhost:8080/smev/mvvact";

    final UniversalClient universalClient = new UniversalClient();
    final ClientRev120315 rev120315 = new ClientRev120315(new ServiceDefinitionParser(), new DummyProvider(),
            new XmlNormalizerImpl(), null);
    final DummyContext ctx = new DummyContext();
    ctx.setVariable("smevTest", "Первичный запрос");
    HttpTransportPipe.dump = true;

    while (true) {
      ClientRequest request = universalClient.createClientRequest(ctx);
      request.portAddress = ADDRESS;
      request.packet.sender = request.packet.originator = pnzr01581;
      ClientResponse response = rev120315.send(universalClient.getWsdlUrl(), request, null);
      universalClient.processClientResponse(response, ctx);
      if (Boolean.TRUE != ctx.getVariable("smevPool")) {
        break;
      }
      System.out.println("Ждём 30 секунд...");
      Thread.sleep(30000);
      ctx.setVariable("smevTest", "Повторный запрос");
    }
  }
}

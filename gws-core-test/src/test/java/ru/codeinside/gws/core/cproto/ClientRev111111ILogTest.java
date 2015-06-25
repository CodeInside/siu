/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.stubs.DummyContext;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;
import ru.codeinside.gws3417c.FssClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

@Ignore
public class ClientRev111111ILogTest extends Assert {

  static {
    final InputStream is = ClientRev111111ILogTest.class.getClassLoader().getResourceAsStream("logging.properties");
    try {
      if (is != null) {
        LogManager.getLogManager().readConfiguration(is);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void test() throws Exception {
    InfoSystem pnzr01581 = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    // 1) http://smevtest.fss.ru/fss/SvedRegisterNoPosob
    // 2) http://188.254.16.92:7777/gateway/services/SID0003123
    String FSS_ADDRESS = "http://smevtest.fss.ru/fss/SvedRegisterNoPosob";

    CryptoProvider cryptoProvider = new CryptoProvider();
    ClientRev111111 rev111111 = new ClientRev111111(new ServiceDefinitionParser(), cryptoProvider,
            new XmlNormalizerImpl(), null);
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

    FssClient fssClient = new FssClient();
    ClientRequest request = fssClient.createClientRequest(ctx);

    request.portAddress = FSS_ADDRESS;
    request.packet.sender = request.packet.originator = pnzr01581;

    HttpTransportPipe.dump = true;
    ClientResponse response = rev111111.send(fssClient.getWsdlUrl(), request, null);
    fssClient.processClientResponse(response, ctx);

    while (Boolean.TRUE == ctx.getVariable("smevPool")) {
      System.out.println("Ждём 30 секунд...");
      Thread.sleep(30000);
      ctx.setVariable("smevTest", "Повторный запрос");
      request = fssClient.createClientRequest(ctx);
      request.portAddress = FSS_ADDRESS;
      request.packet.sender = request.packet.originator = pnzr01581;
      response = rev111111.send(fssClient.getWsdlUrl(), request, null);
      fssClient.processClientResponse(response, ctx);
    }
    System.out.println("ФСС: " + response.verifyResult.actor.getSubjectDN());
    assertNull("Ответ проходит через Ростелеком", response.verifyResult.recipient);
    assertEquals(Boolean.FALSE, ctx.getVariable("registering_response"));
    assertEquals(Boolean.TRUE, ctx.getVariable("obtainingGrants1_response"));
    assertEquals(Boolean.TRUE, ctx.getVariable("obtainingGrants2_response"));
  }

}
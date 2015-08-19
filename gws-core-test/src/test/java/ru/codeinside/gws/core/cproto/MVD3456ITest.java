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
import org.junit.Ignore;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.stubs.DummyContext;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;
import ru.codeinside.gws3456c.MvdClient3456;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;


@Ignore
public class MVD3456ITest extends Assert {

  static {
    final InputStream is = MVD3456ITest.class.getClassLoader().getResourceAsStream("logging.properties");
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
    String SERVICE_ADDRESS = "http://188.254.16.92:7777/gateway/services/SID0003058?wsdl";

    CryptoProvider cryptoProvider = new CryptoProvider();
    ClientRev111111 rev111111 = new ClientRev111111(new ServiceDefinitionParser(), cryptoProvider,
            new XmlNormalizerImpl(), null);
    ClientRev111111.validate = true;
    DummyContext ctx = new DummyContext();
    ctx.setVariable("smevTest", "Первичный запрос");

    ctx.setVariable("SecName", "Иванов");
    ctx.setVariable("FirstName", "Иван");
    ctx.setVariable("FathersName", "Иванович");
    ctx.setVariable("SNILS", "000-000-000 00");
    ctx.setVariable("DateOfBirth", DateUtils.parseDate("07.10.1917", new String[]{"dd.MM.yyyy"}));

    ctx.setVariable("Region", "058"); // брать из справочника
    ctx.setVariable("RegistrationPlace", "г. Пенза ул. Попова 32 кв 1");
    ctx.setVariable("TypeRegistration", "MЖ"); //для места пребывания – МП,  для места проживания - МЖ

    ctx.setVariable("MsgVid", "conviction_doc");  // в зависимости от типа запроса нужно выставлять разные параметры

    ctx.setVariable("OriginatorFio", "Ковалевская И.А., тел. (351) 232-3456");
    ctx.setVariable("OriginatorName", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    ctx.setVariable("OriginatorRegion", "058");
    ctx.setVariable("OriginatorCode", "PNZR01581");

    ctx.setVariable("PlaceOfBirth", "Пенза");
    ctx.setVariable("BirthRegionCode", "058"); // брать из справочникаПри месте рождения вне перечня регионов РФ – должно принимать значение "077"
    ctx.setVariable("Reason", "Тестирование системы"); // правовые основания предоставления услуги

    MvdClient3456 client = new MvdClient3456();
    ClientRequest request = client.createClientRequest(ctx);

    request.portAddress = SERVICE_ADDRESS;
    request.packet.sender = request.packet.originator = pnzr01581;

    HttpTransportPipe.dump = true;
    ClientResponse response = rev111111.send(client.getWsdlUrl(), request, null);
    client.processClientResponse(response, ctx);

    while (Boolean.TRUE == ctx.getVariable("smevPool")) {
      System.out.println("Ждём 30 секунд...");
      Thread.sleep(30000);
      ctx.setVariable("smevTest", "Повторный запрос");
      request = client.createClientRequest(ctx);
      request.portAddress = SERVICE_ADDRESS;
      request.packet.sender = request.packet.originator = pnzr01581;
      response = rev111111.send(client.getWsdlUrl(), request, null);
      client.processClientResponse(response, ctx);
    }
   /* System.out.println("ФСС: " + response.verifyResult.actor.getSubjectDN());
    assertNull("Ответ проходит через Ростелеком", response.verifyResult.recipient);
    assertEquals(Boolean.FALSE, ctx.getVariable("registering_response"));
    assertEquals(Boolean.TRUE, ctx.getVariable("obtainingGrants1_response"));
    assertEquals(Boolean.TRUE, ctx.getVariable("obtainingGrants2_response")); */
  }

}
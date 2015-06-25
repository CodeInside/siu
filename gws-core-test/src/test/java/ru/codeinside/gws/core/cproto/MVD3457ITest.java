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
import ru.codeinside.gws3457c.MvdClient3457;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

@Ignore
public class MVD3457ITest extends Assert {

    static {
        final InputStream is = MVD3457ITest.class.getClassLoader().getResourceAsStream("logging.properties");
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
        String SERVICE_ADDRESS = "http://188.254.16.92:7777/gateway/services/SID0003221";

        CryptoProvider cryptoProvider = new CryptoProvider();
        ClientRev111111 rev111111 = new ClientRev111111(new ServiceDefinitionParser(), cryptoProvider,
                new XmlNormalizerImpl(), null);
        ClientRev111111.validate = true;
        DummyContext ctx = new DummyContext();
        ctx.setVariable("smevTest", "Первичный запрос");

        ctx.setVariable("secondName", "Иванов");
        ctx.setVariable("firstName", "Иван");
        ctx.setVariable("patronymic", "Иванович");
        ctx.setVariable("snils", "000-000-000 00");
        ctx.setVariable("DateOfBirth", DateUtils.parseDate("07.10.1917", new String[]{"dd.MM.yyyy"}));
        ctx.setVariable("requestMomentDate", DateUtils.parseDate("07.10.2002", new String[]{"dd.MM.yyyy"}));
        ctx.setVariable("documentNumber", "1234567890");
        ctx.setVariable("dptcod", "58"); // брать из справочника
        ctx.setVariable("typeQuery", "ЗАПРОС_СВЕДЕНИЙ_О_ПРЕКРАЩЕНИИ_ВЫПЛАТЫ");
        ctx.setVariable("MsgVid", "PENSION3");

        ctx.setVariable("OriginatorFio", "Иванов Иван Иванович");
        ctx.setVariable("OriginatorRegion", "58");



        MvdClient3457 client = new MvdClient3457();
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
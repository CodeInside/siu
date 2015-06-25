/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.stubs.DummyContext;
import ru.codeinside.gws.stubs.DummyProvider;
import ru.codeinside.gws.stubs.MultiPartItem;
import ru.codeinside.gws.stubs.MultiPartServer;
import ru.codeinside.gws.stubs.R;
import ru.codeinside.gws.stubs.TestServer;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;
import ru.codeinside.gws3417c.FssClient;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ru.codeinside.gws.stubs.R.getRequiredResourceStream;
import static ru.codeinside.gws.stubs.Streams.toBytes;

public class ClientRev111111Test extends Assert {

    static {
        R.init();
    }

    @Test
    public void testGetRevision() throws Exception {
        ClientRev111111 rev111111 = new ClientRev111111(new ServiceDefinitionParser(), new DummyProvider(),
                new XmlNormalizerImpl(), null);
        assertEquals(Revision.rev111111, rev111111.getRevision());
    }

    @Test
    public void testBadPort() throws Exception {
        ClientRev111111 rev111111 = new ClientRev111111(new ServiceDefinitionParser(), new DummyProvider(),
                new XmlNormalizerImpl(), null);
        DummyContext ctx = new DummyContext();
        FssClient fssClient = new FssClient();
        ClientRequest request = fssClient.createClientRequest(ctx);
        request.portAddress = "http://127.0.0.1:99999";
        request.packet.sender = request.packet.originator = new InfoSystem("test", "test");
        try {
            ClientResponse response = rev111111.send(fssClient.getWsdlUrl(), request, null);
            fail("Порт не правильный");
        } catch (IllegalArgumentException e) {
            assertEquals("port out of range:99999", e.getMessage());
        }
    }

    @Test
    public void test() throws Exception {
        final TestServer testServer = new TestServer();
        testServer.start(7777);
        try {
            ClientRev111111 rev111111 = new ClientRev111111(new ServiceDefinitionParser(), new DummyProvider(),
                    new XmlNormalizerImpl(), null);
            DummyContext ctx = new DummyContext();
            FssClient fssClient = new FssClient();
            ClientRequest request = fssClient.createClientRequest(ctx);
            request.portAddress = "http://127.0.0.1:7777";
            request.packet.sender = request.packet.originator = new InfoSystem("test", "test");
            request.enclosureDescriptor = "xxx123";
            List<Enclosure> enclosures = new ArrayList<Enclosure>();
            Enclosure enclosure = new Enclosure("path1.txt", "12345".getBytes());
            enclosure.fileName = "text.txt";
            enclosure.mimeType = "text/pain; charset=utf-8";
            enclosure.code = "1";
            enclosure.id = "1";
            enclosures.add(enclosure);
            request.enclosures = enclosures.toArray(new Enclosure[enclosures.size()]);
            testServer.setResponseBody("fss1.xml");
            ClientResponse response = rev111111.send(fssClient.getWsdlUrl(), request, null);
        } finally {
            testServer.stop();
        }


        Map<String, List<String>> headers = testServer.getHeaderMap();
        assertEquals("text/xml; charset=UTF-8", headers.get("Content-Type").get(0));
        assertEquals("\"\"", headers.get("SOAPAction").get(0));
        assertNotNull(testServer.getRequestBody());
        assertTrue(testServer.getParameterMap().isEmpty());
    }

    @Test
    public void testFaultContent() throws Exception {
        final TestServer testServer = new TestServer();
        testServer.start(7778);
        try {
            ClientRev111111 rev111111 = new ClientRev111111(new ServiceDefinitionParser(), new DummyProvider(),
                    new XmlNormalizerImpl(), null);
            DummyContext ctx = new DummyContext();
            FssClient fssClient = new FssClient();
            ClientRequest request = fssClient.createClientRequest(ctx);
            request.portAddress = "http://127.0.0.1:7778";
            request.packet.sender = request.packet.originator = new InfoSystem("test", "test");
            testServer.setResponseBody("logback.xml");
            //testServer.setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
            ClientResponse response = rev111111.send(fssClient.getWsdlUrl(), request, null);
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("unexpected XML tag"));
            // TODO: тут должен быть assert на то что реально было в ответе !!!
        } finally {
            testServer.stop();
        }
    }

    @Test
    public void testAttachment() throws Exception {
        final TestServer testServer = new TestServer();
        testServer.start(7779);
        try {
            ClientRev111111 rev111111 = new ClientRev111111(new ServiceDefinitionParser(), new DummyProvider(),
                    new XmlNormalizerImpl(), null);
            DummyContext ctx = new DummyContext();
            FssClient fssClient = new FssClient();
            ClientRequest request = fssClient.createClientRequest(ctx);
            request.portAddress = "http://127.0.0.1:7779";
            request.packet.sender = request.packet.originator = new InfoSystem("test", "test");
            testServer.setResponseBody("rr2-response.xml");
            ClientResponse response = rev111111.send(fssClient.getWsdlUrl(), request, null);
            assertNull("Нет ответа роутера", response.routerPacket);
            assertEquals("req_ee0b4ef0-f1b3-4353-993a-368a33bc6435", response.enclosureDescriptor);
            assertEquals(1, response.enclosures.length);
            Enclosure enclosure = response.enclosures[0];
            assertEquals(18152, enclosure.content.length);
            assertEquals("doc197264.xml", enclosure.fileName);
            assertTrue(new String(enclosure.content).contains("Region_Cadastr_Vidimus_KP"));
        } finally {
            testServer.stop();
        }
    }

    @Test
    public void testReject() throws Exception {
        final TestServer testServer = new TestServer();
        testServer.start(7771);
        try {
            ClientRev111111 rev111111 = new ClientRev111111(new ServiceDefinitionParser(), new DummyProvider(),
                    new XmlNormalizerImpl(), null);
            DummyContext ctx = new DummyContext();
            FssClient fssClient = new FssClient();
            ClientRequest request = fssClient.createClientRequest(ctx);
            request.portAddress = "http://127.0.0.1:7771";
            request.packet.sender = request.packet.originator = new InfoSystem("test", "test");
            testServer.setResponseBody("fss-response-2.xml");
            ClientResponse response = rev111111.send(fssClient.getWsdlUrl(), request, null);
            assertNotNull(response.appData);
            fssClient.processClientResponse(response, ctx);
            assertEquals("Ошибка", ctx.getVariable("status"));
            assertEquals("Запрос с таким origRequestIdRef не найден!", ctx.getVariable("hint"));
            assertEquals("Ошибка в запросе: Запрос с таким origRequestIdRef не найден!", ctx.getVariable("smevError"));


        } finally {
            testServer.stop();
        }
    }

    @Test
    public void testMtomAttachment() throws Exception {
        MultiPartItem msg = new MultiPartItem(toBytes(getRequiredResourceStream("rr2-response-mtom.xml")));
        msg.addHeader("Content-Type", "text/xml");

        MultiPartItem zip = new MultiPartItem(toBytes(getRequiredResourceStream("enclosure.zip")));
        zip.addHeader("Content-Type", "application/zip");
        zip.addHeader("Content-Transfer-Encoding", "binary");
        zip.addHeader("Content-Id", "<CHANGES_EGRUL_2011-06-09.ZIP>");

        MultiPartServer server = new MultiPartServer();
        server.setResponseItems(Arrays.asList(msg, zip));
        server.start(7770);
        try {
            ClientRev111111 rev111111 = new ClientRev111111(new ServiceDefinitionParser(), new DummyProvider(),
                    new XmlNormalizerImpl(), null);
            DummyContext ctx = new DummyContext();
            FssClient fssClient = new FssClient();
            ClientRequest request = fssClient.createClientRequest(ctx);
            request.portAddress = "http://127.0.0.1:7770";
            request.packet.sender = request.packet.originator = new InfoSystem("test", "test");
            ClientResponse response = rev111111.send(fssClient.getWsdlUrl(), request, null);
            assertNull("Нет ответа роутера", response.routerPacket);
            assertEquals("Не проверяем целостность", null, response.verifyResult.error);
            assertEquals("req_ee0b4ef0-f1b3-4353-993a-368a33bc6435", response.enclosureDescriptor);
            assertEquals(1, response.enclosures.length);
            Enclosure enclosure = response.enclosures[0];
            assertEquals(18152, enclosure.content.length);
            assertEquals("doc197264.xml", enclosure.fileName);
            assertTrue(new String(enclosure.content).contains("Region_Cadastr_Vidimus_KP"));
        } finally {
            server.stop();
        }
    }

    @Test
    public void testHash() throws Exception {
        MessageDigest digest = MessageDigest.getInstance("GOST3411");
        digest.update(DatatypeConverter.printBase64Binary(toBytes(getRequiredResourceStream("enclosure.zip"))).getBytes());
        String base64 = DatatypeConverter.printBase64Binary(digest.digest());
        assertEquals("qV9FKT+mNKS15wbsg0i0N6PzB5zfR3rpCiuaH+6Q0Dk=", base64);
    }
}

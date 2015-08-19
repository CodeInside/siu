/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.sproto;

import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.core.R;
import ru.codeinside.gws.core.Support;
import ru.codeinside.gws.core.cproto.DummyProvider;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import java.io.FileOutputStream;
import java.util.Date;

import static org.mockito.Mockito.mock;

public class R120315Test extends Assert {

  final static Support.PortDefinition mvvPort = Support.getServicePort("mvvact/wsdl/mvvact.wsdl");

  @Test
  public void testRevision() {
    assertEquals(Revision.rev120315, new R120315(new DummyProvider(), null, null).getRevision());
  }

  // TODO: Для сервиса c WSDL это бесполезный тест, так как движок JWS обработает раньше.
  @Test
  public void request_invalidOperation() throws Exception {
    R120315 r120315 = new R120315(new DummyProvider(), null, null);
    try {
      r120315.processRequest(R.getSoapResource("fss-request-1.xml"), mvvPort.service, mvvPort.portDef);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(
        "Invalid operation {http://fss.ru/SvedRegisterNoPosob/request}request" +
          " for port {http://mvv.oep.com/}MVVAct" +
          " in service {http://mvv.oep.com/}MVVActService",
        e.getMessage());
    }
  }

  @Test
  public void request_mvv_putData() throws Exception {
    CryptoProvider cryptoProvider = new ru.codeinside.gws.crypto.cryptopro.CryptoProvider();
    R120315 r120315 = new R120315(cryptoProvider, null, null);
    ServerRequest request = r120315.processRequest(R.getSoapResource("mvvact/putData/request.xml"), mvvPort.service, mvvPort.portDef);
    assertNull(request.routerPacket);
    assertEquals(new QName("http://mvv.oep.com/", "putData"), request.action);
    assertNotNull(request.verifyResult.actor);
    assertNull(request.verifyResult.recipient);

    assertEquals("UniversalMVV", request.packet.serviceName);
    assertEquals(Packet.Type.SERVICE, request.packet.typeCode);
    assertEquals(Packet.Status.REQUEST, request.packet.status);
    assertEquals("Test", request.packet.exchangeType);
    assertEquals("111111111111", request.packet.requestIdRef);
    assertEquals("111111111111", request.packet.originRequestIdRef);
    assertEquals("111111111111", request.packet.serviceCode);
    assertEquals("111111111111", request.packet.caseNumber);
    assertNull(request.attachmens);
    assertNull(request.docRequestCode);

    assertEquals("AppData", request.appData.getLocalName());
    assertEquals("smev", request.appData.getPrefix());
    assertEquals("http://smev.gosuslugi.ru/rev120315", request.appData.getNamespaceURI());
  }

  @Test
  public void response_mvv_putData() throws Exception {
    CryptoProvider cryptoProvider = mock(CryptoProvider.class);
    R120315 r120315 = new R120315(cryptoProvider, null, null);

    ServerResponse response = new ServerResponse();
    response.action = new QName("http://mvv.oep.com/", "putData");
    Packet p = new Packet();
    response.packet = p;
    p.exchangeType = "Test";
    p.serviceCode = "111111111111";
    p.requestIdRef = "111111111111";
    p.originRequestIdRef = "111111111111";
    p.caseNumber = "111111111111";
    p.typeCode = Packet.Type.SERVICE;
    p.status = Packet.Status.PROCESS;
    p.recipient = p.sender = new InfoSystem("PNZR01581", "111111111");
    p.date = new Date();

    SOAPMessage message = r120315.processResponse(null, response, mvvPort.service, mvvPort.portDef, null);

    FileOutputStream fos = new FileOutputStream("target/response.xml");
    message.writeTo(fos);
  }

  @Test
  public void request_mvv_like_gosUslugiRu() throws Exception {
    CryptoProvider cryptoProvider = new ru.codeinside.gws.crypto.cryptopro.CryptoProvider();
    R120315 r120315 = new R120315(cryptoProvider, null, null);
    ServerRequest request = r120315.processRequest(R.getSoapResource("mvvact/putData/request-2.xml"), mvvPort.service, mvvPort.portDef);
    assertNull(request.routerPacket);
    assertEquals(new QName("http://mvv.oep.com/", "putData"), request.action);
    assertNotNull(request.verifyResult.actor);
    assertNull(request.verifyResult.recipient);

    assertEquals("UniversalMVV", request.packet.serviceName);
    assertEquals(Packet.Type.SERVICE, request.packet.typeCode);
    assertEquals(Packet.Status.REQUEST, request.packet.status);
    assertEquals("Test", request.packet.exchangeType);

    assertNull(request.packet.requestIdRef);
    assertNull(request.packet.originRequestIdRef);

    assertEquals("111111111111", request.packet.serviceCode);
    assertEquals("111111111111", request.packet.caseNumber);
    assertNull(request.attachmens);
    assertNull(request.docRequestCode);

    assertEquals("AppData", request.appData.getLocalName());
    assertEquals("smev", request.appData.getPrefix());
    assertEquals("http://smev.gosuslugi.ru/rev120315", request.appData.getNamespaceURI());
  }


}

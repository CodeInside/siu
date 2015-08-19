/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.s.oep.declarer;

import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.api.XmlTypes;
import ru.codeinside.gws.core.sproto.R120315;
import ru.codeinside.gws.s.oep.declarer.data.Result;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import static org.mockito.Mockito.mock;

public class DeclarerTest extends Assert {

  static final QName serviceName;
  static final QName portName;
  static final ServiceDefinition.Port portDef;

  static {
    ServiceDefinition definition = new ServiceDefinitionParser().parseServiceDefinition(DeclarerTest.class.getClassLoader().getResource("mvvact/mvvact.wsdl"));
    serviceName = definition.services.keySet().iterator().next();
    ServiceDefinition.Service service = definition.services.get(serviceName);
    portName = service.ports.keySet().iterator().next();
    portDef = service.ports.get(portName);
  }

  @Test
  public void testGetRevision() throws Exception {
    assertEquals(Revision.rev120315, new Declarer().getRevision());
  }

  @Test
  public void testGetWsdlUrl() throws Exception {
    final URL wsdlUrl = new Declarer().getWsdlUrl();
    assertNotNull("Ссылка на wsdl обяательна", wsdlUrl);
    assertTrue("WSDL должен быть ресурсом", wsdlUrl.toExternalForm().contains("/target/classes/"));
  }

  @Test
  public void putData_ping() throws Exception {
    Declarer declarer = new Declarer();
    DummyRequestContext requestContext = new DummyRequestContext();
    requestContext.bid = "1";
    requestContext.results.put("1", new ServerResponse());
    requestContext.request = new ServerRequest();
    requestContext.request.packet = new Packet();
    requestContext.request.packet.status = Packet.Status.PING;
    requestContext.request.action = new QName("http://smev.gosuslugi.ru/rev120315", "putData");
    ServerResponse response = declarer.processRequest(requestContext);
    assertSame(requestContext.results.get("1"), response);
  }

  @Test
  public void putData_other() throws Exception {
    Declarer declarer = new Declarer();
    DummyRequestContext requestContext = new DummyRequestContext();
    requestContext.request = new ServerRequest();
    requestContext.request.packet = new Packet();
    requestContext.request.packet.status = Packet.Status.REQUEST;
    requestContext.request.action = new QName("http://smev.gosuslugi.ru/rev120315", "putData");
    try {
      declarer.processRequest(requestContext);
      fail();
    } catch (IllegalStateException e) {
      assertEquals("Illegal status REQUEST", e.getMessage());
    }
  }

  @Test
  public void updateStatus_request() throws Exception {
    Declarer declarer = new Declarer();
    DummyRequestContext requestContext = new DummyRequestContext();
    requestContext.bid = "2";
    requestContext.states.put("2", new ServerResponse());
    requestContext.request = new ServerRequest();
    requestContext.request.packet = new Packet();
    requestContext.request.packet.status = Packet.Status.REQUEST;
    requestContext.request.action = new QName("http://smev.gosuslugi.ru/rev120315", "updateStatus");
    ServerResponse response = declarer.processRequest(requestContext);
    assertSame(requestContext.states.get("2"), response);
  }

  @Test
  public void updateStatus_other() throws Exception {
    Declarer declarer = new Declarer();
    DummyRequestContext requestContext = new DummyRequestContext();
    requestContext.bid = "3";
    requestContext.states.put("3", new ServerResponse());
    requestContext.request = new ServerRequest();
    requestContext.request.packet = new Packet();
    requestContext.request.packet.status = Packet.Status.STATE;
    requestContext.request.action = new QName("http://smev.gosuslugi.ru/rev120315", "updateStatus");
    try {
      declarer.processRequest(requestContext);
      fail();
    } catch (IllegalStateException e) {
      assertEquals("Illegal status STATE", e.getMessage());
    }
  }

  @Test
  public void testProcessStatus() throws Exception {
    Declarer declarer = new Declarer();
    DummyContext ctx = new DummyContext();
    ServerResponse response = declarer.processStatus("x1x", ctx);
    assertEquals(new QName("http://mvv.oep.com/", "updateStatus"), response.action);
    assertTrue(response.appData.startsWith("<oep:result xmlns:oep=\"http://oep-penza.ru/com/oep\"><oep:params>"));
    Result result = new XmlTypes(Result.class).fromXml(Result.class, response.appData);
    assertEquals("x1x", result.getParams().getStatusCode());
    assertMessage(response);
  }

  @Test
  public void testProcessResult() throws Exception {
    Declarer declarer = new Declarer();
    DummyContext ctx = new DummyContext();
    ctx.vars.put("r1x", "zxx");
    ServerResponse response = declarer.processResult("xxx", ctx);
    assertEquals(new QName("http://mvv.oep.com/", "updateStatus"), response.action);
    assertTrue(response.appData.startsWith(
      "<oep:result xmlns:oep=\"http://oep-penza.ru/com/oep\">" +
        "<oep:dataRow><oep:name>r1x</oep:name><oep:value>zxx</oep:value></oep:dataRow>"
    ));
    Result result = new XmlTypes(Result.class).fromXml(Result.class, response.appData);
    assertEquals("xxx", result.getParams().getStatusCode());
    assertMessage(response);
  }

  @Test
  public void testRealRequest() throws SOAPException, IOException {
    Declarer declarer = new Declarer();
    DummyRequestContext requestContext = new DummyRequestContext();
    requestContext.request = parseRequest("request-1.xml");
    requestContext.first = true;
    requestContext.procedureCode = 1L;
    requestContext.declarerContext = new DeclarerContextStub();
    ServerResponse response = declarer.processRequest(requestContext);
    assertMessage(response);
  }

  private ServerRequest parseRequest(String name) throws IOException, SOAPException {
    final InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
    final SOAPMessage message = MessageFactory.newInstance().createMessage(null, stream);
    final CryptoProvider provider = mock(CryptoProvider.class);
    final R120315 r120315 = new R120315(provider, null, null);
    return r120315.processRequest(message, serviceName, portDef);
  }

  private void assertMessage(ServerResponse response) {
    CryptoProvider provider = mock(CryptoProvider.class);
    R120315 r120315 = new R120315(provider, null, null);
    response.packet.sender = response.packet.recipient = new InfoSystem("x", "y");
    response.packet.date = new Date();
    assertNotNull(r120315.processResponse(null, response, serviceName, portDef, null));
  }

}

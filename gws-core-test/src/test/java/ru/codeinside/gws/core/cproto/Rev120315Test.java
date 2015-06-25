/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.core.sproto.R120315;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.s.oep.declarer.Declarer;
import ru.codeinside.gws.stubs.DeclarerContextStub;
import ru.codeinside.gws.stubs.DummyContext;
import ru.codeinside.gws.stubs.DummyReceiptContext;
import ru.codeinside.gws.stubs.DummyRequestContext;
import ru.codeinside.gws.stubs.R;
import ru.codeinside.gws.stubs.Support;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;
import ru.codeinside.gws3970c.UniversalClient;

import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingType;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.spi.Provider;

public class Rev120315Test extends Assert {

  static {
    R.init();
    ClientRev120315.validate = false;// в универсальном клиенет cхема чуть не по СМЭВ.
    ClientRev120315.dumping = false;
    HttpTransportPipe.dump = false;
  }

  final static Support.PortDefinition mvvPort = Support.getServicePort("mvvact/mvvact.wsdl");

  @Test
  public void testPutData() throws Exception {
    ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
    CryptoProvider cryptoProvider = new CryptoProvider();
    final int PORT = 7770;
    final String PORT_ADDRES = "http://127.0.0.1:" + PORT + "/";
    Adapter adapter = new Adapter(cryptoProvider);
    Endpoint endpoint = Provider.provider().createEndpoint(null, adapter);
    endpoint.publish(PORT_ADDRES);
    try {
      ClientRev120315 c120315 = new ClientRev120315(definitionParser, cryptoProvider,
              new XmlNormalizerImpl(), null);

      DummyContext context = new DummyContext();
      context.setVariable("appData_FIO", "Иванов Иван Иванович");
      context.setVariable("appData_birthDay", "12.01.1956");
      context.setVariable("appData_addressRegister", "г Пенза улица Попова 36");
      context.setVariable("appData_toOrganizationName", "Codeinside");
      context.setVariable("appData_phone", "8908432422");
      context.setVariable("flowName", "RegisterForImproveLivingArea");
      context.setVariable("procedureCode", "123");
      context.setVariable("app_id", 52L);

      context.setVariable("smevRequestCode", 987);
      context.addEnclosure("appData_e1", new Enclosure("e1.txt", "12345".getBytes()));
      context.addEnclosure("appData_e2", new Enclosure("e2.txt", "54321".getBytes()));

      UniversalClient client = new UniversalClient();
      ClientRequest request = createRequest(PORT_ADDRES, client, context);
      adapter.requestContext.procedureCode = 123L;
      DeclarerContextStub dc = adapter.requestContext.declarerContext;
      dc.id = "1234567";
      adapter.requestContext.first = true;
      ClientResponse response = c120315.send(client.getWsdlUrl(), request, null);
      client.processClientResponse(response, context);
      assertNull(adapter.requestContext.request.verifyResult.error);
      assertTrue(dc.values.containsKey("appData_e1"));
      assertTrue(dc.values.containsKey("appData_e2"));

      DummyReceiptContext rc = new DummyReceiptContext();
      rc.vars.put("x", "y");
      rc.enclosures.put("z", new Enclosure("e3.txt", "14325".getBytes()));
      adapter.requestContext.first = false;
      adapter.requestContext.result = adapter.declarer.processResult("OK", rc);

      request = createRequest(PORT_ADDRES, client, context);
      response = c120315.send(client.getWsdlUrl(), request, null);
      client.processClientResponse(response, context);

      assertEquals("metadata", response.enclosureDescriptor);
      assertEquals(2, response.enclosures.length);
      assertEquals("metadata.xml", response.enclosures[0].zipPath);
      assertEquals("e3.txt", response.enclosures[1].zipPath);
      assertTrue(context.isEnclosure("z"));

    } finally {
      endpoint.stop();
    }

  }

  private ClientRequest createRequest(String port, Client client, ExchangeContext ctx) {
    ClientRequest request = client.createClientRequest(ctx);
    request.portAddress = port;
    request.packet.sender = request.packet.originator = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    return request;
  }

  //@SchemaValidation
  @ServiceMode(Service.Mode.MESSAGE)
  @WebServiceProvider(
    targetNamespace = "http://mvv.oep.com/",
    serviceName = "MVVActService", portName = "MVVActPort",
    wsdlLocation = "mvvact/mvvact.wsdl")
  @BindingType(value = SOAPBinding.SOAP11HTTP_MTOM_BINDING)
  public class Adapter implements javax.xml.ws.Provider<SOAPMessage> {
    final R120315 r120315;
    final Declarer declarer = new Declarer();
    final DummyRequestContext requestContext = new DummyRequestContext();

    Adapter(CryptoProvider cryptoProvider) {
      r120315 = new R120315(cryptoProvider, null, null);
    }

    @Override
    public SOAPMessage invoke(SOAPMessage in) {
      ServerRequest request = r120315.processRequest(in, mvvPort.service, mvvPort.portDef);
      requestContext.request = request;
      ServerResponse response = declarer.processRequest(requestContext);
      SOAPMessage out = r120315.processResponse(request, response, mvvPort.service, mvvPort.portDef, null);
      return out;
    }
  }
}

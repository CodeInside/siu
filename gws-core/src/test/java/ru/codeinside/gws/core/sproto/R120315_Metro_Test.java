/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.sproto;


import com.sun.xml.ws.developer.SchemaValidation;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.core.R;
import ru.codeinside.gws.core.Support;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingType;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.SOAPBinding;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.mock;

public class R120315_Metro_Test extends Assert {

  final static Support.PortDefinition mvvPort = Support.getServicePort("mvvact/wsdl/mvvact.wsdl");

  @Test
  public void testRequestParsing() throws IOException {
    final URL addr = new URL("http://127.0.0.1:7771/");
    final AtomicReference<ServerRequest> request = new AtomicReference<ServerRequest>();
    Endpoint endpoint = Endpoint.publish("http://127.0.0.1:7771/", new Router(new Invoker() {
      @Override
      public SOAPMessage invoke(SOAPMessage in, WebServiceContext ctx) {
        CryptoProvider cryptoProvider = mock(CryptoProvider.class);
        R120315 r120315 = new R120315(cryptoProvider, null, null);
        request.set(r120315.processRequest(in, mvvPort.service, mvvPort.portDef));

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

        return r120315.processResponse(null, response, mvvPort.service, mvvPort.portDef, null);
      }
    }));
    try {
      assertTrue(endpoint.isPublished());
      HttpURLConnection con = (HttpURLConnection) addr.openConnection();
      con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
      con.setDoOutput(true);
      con.setDoInput(true);
      IOUtils.copy(R.getRequiredResourceStream("mvvact/putData/request.xml"), con.getOutputStream());
      String result = IOUtils.toString(con.getInputStream(), "UTF8");
      assertNotNull(result);

      ServerRequest req = request.get();

      assertNull(req.routerPacket);
      assertEquals(new QName("http://mvv.oep.com/", "putData"), req.action);
      assertEquals("UniversalMVV", req.packet.serviceName);
      assertEquals(Packet.Type.SERVICE, req.packet.typeCode);
      assertEquals(Packet.Status.REQUEST, req.packet.status);
      assertEquals("Test", req.packet.exchangeType);
      assertEquals("111111111111", req.packet.requestIdRef);
      assertEquals("111111111111", req.packet.originRequestIdRef);
      assertEquals("111111111111", req.packet.serviceCode);
      assertEquals("111111111111", req.packet.caseNumber);
      assertNull(req.attachmens);
      assertNull(req.docRequestCode);
    } finally {
      endpoint.stop();
    }
  }

  @Test
  public void testValidationIn() throws IOException {
    String portAddr = "http://127.0.0.1:7772/";
    Endpoint endpoint = Endpoint.publish(portAddr, new Router(null));
    try {
      assertTrue(endpoint.isPublished());
      HttpURLConnection con = (HttpURLConnection) new URL(portAddr).openConnection();
      con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
      con.setDoOutput(true);
      con.setDoInput(true);
      IOUtils.copy(R.getRequiredResourceStream("fss-request-1.xml"), con.getOutputStream());
      assertEquals(500, con.getResponseCode());
      String error = IOUtils.toString(con.getErrorStream(), "UTF8");
      assertTrue(error.contains("Cannot find the declaration of element 'ws:request'"));
    } finally {
      endpoint.stop();
    }
  }

  @Test
  public void testValidationOut() throws IOException {
    final AtomicReference<ServerRequest> request = new AtomicReference<ServerRequest>();
    String portAddr = "http://127.0.0.1:7773/";
    Endpoint endpoint = Endpoint.publish(portAddr, new Router(new Invoker() {
      @Override
      public SOAPMessage invoke(SOAPMessage in, WebServiceContext ctx) {
        CryptoProvider cryptoProvider = mock(CryptoProvider.class);
        R120315 r120315 = new R120315(cryptoProvider, null, null);
        request.set(r120315.processRequest(in, mvvPort.service, mvvPort.portDef));
        try {
          return R.getSoapResource("fss-response-2.xml");
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }));
    try {
      assertTrue(endpoint.isPublished());
      HttpURLConnection con = (HttpURLConnection) new URL(portAddr).openConnection();
      con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
      con.setDoOutput(true);
      con.setDoInput(true);
      IOUtils.copy(R.getRequiredResourceStream("mvvact/updateStatus/UpdateStatus_request.xml"), con.getOutputStream());
      assertEquals(500, con.getResponseCode());
      String error = IOUtils.toString(con.getErrorStream(), "UTF8");
      assertTrue(error.contains("Cannot find the declaration of element 'ns3:requestResponse'"));

      ServerRequest req = request.get();
      assertNull(req.routerPacket);
      assertEquals(new QName("http://mvv.oep.com/", "updateStatus"), req.action);
      assertNull(req.packet.serviceName);
      assertEquals(Packet.Type.SERVICE, req.packet.typeCode);
      assertEquals(Packet.Status.REQUEST, req.packet.status);
      assertEquals("Test", req.packet.exchangeType);
      assertEquals("11111111111", req.packet.requestIdRef);
      assertEquals("111111111111", req.packet.originRequestIdRef);
      assertEquals("1111111111", req.packet.serviceCode);
      assertEquals("1111111111111", req.packet.caseNumber);
      assertNull(req.attachmens);
      assertNull(req.docRequestCode);

    } finally {
      endpoint.stop();
    }
  }

  @SchemaValidation(inbound = true, outbound = true)
  @ServiceMode(Service.Mode.MESSAGE)
  @MTOM
  @WebServiceProvider(targetNamespace = "http://mvv.oep.com/", serviceName = "MVVActService", portName = "MVVActPort",
    wsdlLocation = "mvvact/wsdl/mvvact.wsdl")
  @BindingType(value = SOAPBinding.SOAP11HTTP_MTOM_BINDING)
  public class Router implements Provider<SOAPMessage> {
    final Invoker invoker;
    @Resource
    WebServiceContext wsContext;

    Router(Invoker invoker) {
      this.invoker = invoker;
    }

    @Override
    public SOAPMessage invoke(SOAPMessage in) {
      return invoker.invoke(in, wsContext);
    }
  }

}

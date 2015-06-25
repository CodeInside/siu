/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.s.oep.dict;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Internals;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.core.sproto.R120315;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class DictionaryTest extends Assert {

  static final QName serviceName;
  static final QName portName;
  static final ServiceDefinition.Port portDef;

  static {
    ServiceDefinition definition = new ServiceDefinitionParser().parseServiceDefinition(new Dictionary().getWsdlUrl());
    serviceName = definition.services.keySet().iterator().next();
    ServiceDefinition.Service service = definition.services.get(serviceName);
    portName = service.ports.keySet().iterator().next();
    portDef = service.ports.get(portName);
  }


  @Test
  public void testGetRevision() throws Exception {
    assertEquals(Revision.rev120315, new Dictionary().getRevision());
  }

  @Test
  public void testGetWsdlUrl() throws Exception {
    final URL wsdlUrl = new Dictionary().getWsdlUrl();
    assertNotNull("Ссылка на wsdl обяательна", wsdlUrl);
    assertTrue("WSDL должен быть ресурсом", wsdlUrl.toExternalForm().contains("/target/classes/"));
  }

  @Test
  public void testRealRequest() throws Exception {
    Internals internals = mock(Internals.class);
    Dictionary dictionary = new Dictionary();
    dictionary.bindInternals(internals);

    Map<String, String> dict = new LinkedHashMap<String, String>();
    dict.put("NikRai", "ЖЖЖ"); // фильтруемое поле!
    dict.put("xYz", "z_Z");
    Mockito.when(internals.getDictionary("detail_city_region")).thenReturn(dict);

    ServerRequest serverRequest = parseRequest("request.xml");
    ServerResponse response = dictionary.processRequest(serverRequest);
    assertMessage(response);
    assertEquals(
      "<oep:result xmlns:oep=\"http://oep-penza.ru/com/oep\">" +
        "<oep:dataRow><oep:name>xYz</oep:name><oep:value>z_Z</oep:value></oep:dataRow>" +
        "</oep:result>",
      response.appData);
  }

  private ServerRequest parseRequest(String name) throws IOException, SOAPException {
    final InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
    assertNotNull(name, stream);
    final SOAPMessage message = MessageFactory.newInstance().createMessage(null, stream);
    final CryptoProvider provider = mock(CryptoProvider.class);
    final R120315 r120315 = new R120315(provider, null, null);
    return r120315.processRequest(message, serviceName, portDef);
  }

  private void assertMessage(ServerResponse response) throws Exception {
    CryptoProvider provider = mock(CryptoProvider.class);
    R120315 r120315 = new R120315(provider, null, null);
    response.packet.sender = response.packet.recipient = new InfoSystem("x", "y");
    response.packet.date = new Date();
    SOAPMessage message = r120315.processResponse(null, response, serviceName, portDef, null);
    message.writeTo(new FileOutputStream("target" + File.separatorChar + "/soap_" + System.currentTimeMillis() + ".xml"));
    assertNotNull(message);
  }

}

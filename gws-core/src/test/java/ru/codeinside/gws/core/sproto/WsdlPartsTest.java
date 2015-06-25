package ru.codeinside.gws.core.sproto;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.ReceiptContext;
import ru.codeinside.gws.api.RequestContext;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.mockito.Mockito.mock;

public class WsdlPartsTest extends Assert {

  static class SocialSupport implements Server {

    @Override
    public Revision getRevision() {
      return Revision.rev120315;
    }

    @Override
    public URL getWsdlUrl() {
      return getClass().getClassLoader().getResource("social-support-1021/toc.wsdl");
    }

    @Override
    public ServerResponse processRequest(RequestContext requestContext) {
      return new ServerResponse();
    }

    @Override
    public ServerResponse processStatus(String statusMessage, ReceiptContext exchangeContext) {
      return new ServerResponse();
    }

    @Override
    public ServerResponse processResult(String resultMessage, ReceiptContext exchangeContext) {
      return new ServerResponse();
    }
  }

  @Test
  public void testSocialSupport() throws SOAPException, IOException {
    SocialSupport socialSupport = new SocialSupport();
    ServiceDefinition definition = new ServiceDefinitionParser().parseServiceDefinition(socialSupport.getWsdlUrl());
    QName serviceName = definition.services.keySet().iterator().next();
    ServiceDefinition.Service service = definition.services.get(serviceName);
    QName portName = service.ports.keySet().iterator().next();


    InputStream stream = getClass().getClassLoader().getResourceAsStream("social-support-1021/request_1.xml");
    SOAPMessage message = MessageFactory.newInstance().createMessage(null, stream);

    ServerRequest request = new R120315(mock(CryptoProvider.class), null, null).processRequest(message, serviceName, service.ports.get(portName));
    assertEquals(new QName("http://sum-soc-help.skmv.rstyle.com", "SumSocHelpRequestMessage"), request.action);
    Node requestData = request.appData.getChildNodes().item(1);
    assertEquals("http://smev.gosuslugi.ru/rev120315", requestData.getNamespaceURI());
    assertEquals("rev:sumSocHelpRequest", requestData.getNodeName());
  }

}

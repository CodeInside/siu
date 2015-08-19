package server.mcsv1002;

import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.core.sproto.R111111;
import ru.codeinside.gws.core.sproto.R120315;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

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
    assertEquals(Revision.rev111111, new Declarer().getRevision());
  }

  @Test
  public void testGetWsdlUrl() throws Exception {
    final URL wsdlUrl = new Declarer().getWsdlUrl();
    assertNotNull("Ссылка на wsdl обязательна", wsdlUrl);
    assertTrue("WSDL должен быть ресурсом", wsdlUrl.toExternalForm().contains("/target/classes/"));
  }

  @Test
  public void putData_ping() throws Exception {
    Declarer declarer = new Declarer();
    DummyRequestContext requestContext = new DummyRequestContext();
    requestContext.result = new ServerResponse();
    requestContext.result.packet = new Packet();
    requestContext.request = new ServerRequest();
    requestContext.request.packet = new Packet();
    requestContext.request.packet.oktmo = "71875000";
    requestContext.request.packet.status = Packet.Status.PING;
    requestContext.request.action = new QName("http://canonicalRequests.gov.ru", "processCanonicalService");
    ServerResponse response = declarer.processRequest(requestContext);
    assertSame(requestContext.result, response);
    assertEquals("71875000", response.packet.oktmo);
  }

  @Test
  public void putData_other() throws Exception {
    Declarer declarer = new Declarer();
    DummyRequestContext requestContext = new DummyRequestContext();
    requestContext.request = new ServerRequest();
    requestContext.request.packet = new Packet();
    requestContext.request.packet.status = Packet.Status.REQUEST;
    requestContext.request.action = new QName("http://canonicalRequests.gov.ru", "processCanonicalService");
    requestContext.request.packet.oktmo = "test";
    try {
      assertSame(requestContext.request.packet.oktmo, declarer.processRequest(requestContext).packet.oktmo);
      fail();
    } catch (IllegalStateException e) {
      assertEquals("Illegal status REQUEST", e.getMessage());
    }
  }


  @Test
  public void updateStatus_other() throws Exception {
    Declarer declarer = new Declarer();
    DummyRequestContext requestContext = new DummyRequestContext();
    requestContext.state = new ServerResponse();
    requestContext.request = new ServerRequest();
    requestContext.request.packet = new Packet();
    requestContext.request.packet.status = Packet.Status.STATE;
    requestContext.request.action = new QName("http://canonicalRequests.gov.ru", "processCanonicalService");
    requestContext.request.packet.oktmo = "test";
    try {
      assertSame(requestContext.request.packet.oktmo, declarer.processRequest(requestContext).packet.oktmo);
      fail();
    } catch (IllegalStateException e) {
      assertEquals("Illegal status STATE", e.getMessage());
    }
  }

//  @Test
//  public void testProcessStatus() throws Exception {
//    Declarer declarer = new Declarer();
//    DummyContext ctx = new DummyContext();
//    ServerResponse response = declarer.processStatus("x1x", ctx);
//    assertEquals(new QName("http://mvv.oep.com/", "updateStatus"), response.action);
//    assertTrue(response.appData.startsWith("<oep:result xmlns:oep=\"http://oep-penza.ru/com/oep\"><oep:params>"));
//    Result result = new XmlTypes(Result.class).fromXml(Result.class, response.appData);
//    assertEquals("x1x", result.getParams().getStatusCode());
//    assertMessage(response);
//  }

//  @Test
//  public void testProcessResult() throws Exception {
//    Declarer declarer = new Declarer();
//    DummyContext ctx = new DummyContext();
//    ctx.vars.put("r1x", "zxx");
//    ServerResponse response = declarer.processResult("xxx", ctx);
//    assertEquals(new QName("http://mvv.oep.com/", "updateStatus"), response.action);
//    assertTrue(response.appData.startsWith(
//      "<oep:result xmlns:oep=\"http://oep-penza.ru/com/oep\">" +
//        "<oep:dataRow><oep:name>r1x</oep:name><oep:value>zxx</oep:value></oep:dataRow>"));
//    Result result = new XmlTypes(Result.class).fromXml(Result.class, response.appData);
//    assertEquals("xxx", result.getParams().getStatusCode());
//    assertMessage(response);
//  }

  @Test
  public void testRealRequest() throws SOAPException, IOException {
    Declarer declarer = new Declarer();
    DummyRequestContext context = new DummyRequestContext();
    context.request = parseRequest("request-1.xml");
    context.first = true;
    context.procedureCode = 5000000000000001002L;
    final DeclarerContextStub declarerContextStub = new DeclarerContextStub();
    context.declarerContext = declarerContextStub;
    ServerResponse response = declarer.processRequest(context);
    assertMessage(response);
    assertEquals("71875000", response.packet.oktmo);
    assertEquals("4000", declarerContextStub.values.get("result_regionCode"));
    assertEquals("Санкт-Петербург", declarerContextStub.values.get("result_objectCity"));
    assertEquals("Ленинский пр.", declarerContextStub.values.get("result_objectStreet"));
    assertEquals("140", declarerContextStub.values.get("result_objectHouse"));
    assertEquals("2", declarerContextStub.values.get("result_objectCorpus"));
    assertEquals("А", declarerContextStub.values.get("result_objectBuilding"));
    assertEquals("16", declarerContextStub.values.get("result_objectFlat"));
    assertEquals("09:06:0120221:50", declarerContextStub.values.get("result_objectCadastrNumber"));
  }

  private ServerRequest parseRequest(String name) throws IOException, SOAPException {
    final InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
    final SOAPMessage message = MessageFactory.newInstance().createMessage(null, stream);
    CryptoProvider cryptoProvider = new CryptoProvider();
    final R111111 r111111 = new R111111(cryptoProvider, null, null);
    return r111111.processRequest(message, serviceName, portDef);
  }

  private void assertMessage(ServerResponse response) {
    CryptoProvider cryptoProvider = new CryptoProvider();
    R120315 r120315 = new R120315(cryptoProvider,null, null);
    response.packet.sender = response.packet.recipient = new InfoSystem("x", "y");
    response.packet.date = new Date();
    assertNotNull(r120315.processResponse(null, response, serviceName, portDef, null));
  }

}

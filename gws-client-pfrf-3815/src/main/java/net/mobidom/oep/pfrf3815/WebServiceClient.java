package net.mobidom.oep.pfrf3815;

import java.net.URL;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.XmlTypes;

public class WebServiceClient implements Client {

  static Logger log = Logger.getLogger(WebServiceClient.class.getName());

  @Override
  public Revision getRevision() {
    return Revision.rev120315;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("pfrf3815/SID0003578_1.wsdl");
  }

  @Override
  public ClientRequest createClientRequest(ExchangeContext ctx) {

    // create packet
    Packet packet = new Packet();
    packet.typeCode = Packet.Type.SERVICE;
    packet.date = new Date();
    packet.exchangeType = "2";
    packet.sender = new InfoSystem("PFRF01001", "Пенсионный фонд РФ");
    packet.recipient = new InfoSystem("PFRF01001", "Пенсионный фонд РФ");
    packet.originator = new InfoSystem("PFRF01001", "Пенсионный фонд РФ");
    packet.serviceName = "DATA_BY_SNILS";
    packet.testMsg = "Test";

    // packet.originRequestIdRef = ori

    // setup request
    ClientRequest clientRequest = new ClientRequest();
    clientRequest.packet = packet;
    clientRequest.action = new QName("http://data-by-snils.skmv.rstyle.com", "DataBySnilsRequest");

    // define status
    packet.status = Packet.Status.REQUEST;

    // create appdata
    clientRequest.appData = createAppDate(ctx);

    return clientRequest;
  }

  private String createAppDate(ExchangeContext ctx) {
    String snisNumber = (String) ctx.getVariable("snils_number");
    JAXBElement<String> element = new JAXBElement<String>(new QName("http://smev.gosuslugi.ru/rev120315", "snils"), String.class,
        snisNumber);
    String appData = XmlTypes.beanToXml(element);
    log.info("appData = " + appData);
    return appData;
  }

  @Override
  public void processClientResponse(ClientResponse clientResponse, ExchangeContext ctx) {

    Element appDataElement = clientResponse.appData;

    NodeList childs = appDataElement.getChildNodes();
    if (childs.getLength() == 0) {
      ctx.setVariable("snils_request_fault", "Не удалось обработать запрос.");
      // TODO webdom
      return;
    }

    for (int i = 0; i < childs.getLength(); i++) {
      Node node = childs.item(i);
      String name = node.getLocalName();
      if (name.equals("result")) {
        ctx.setVariable("snils_request_result", node);
      } else if (name.equals("fault")) {
        ctx.setVariable("snils_request_fault", node);
      }
    }
  }
}
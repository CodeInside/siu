package net.mobidom.oep.midrf3894;

import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
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
import ru.codeinside.gws.api.Packet.Status;
import ru.codeinside.gws.api.Packet.Type;
import ru.codeinside.gws.api.Revision;
import ru.kdmid.SmevreqAppData;

public class MIDRF3894Client implements Client {

  static Logger log = Logger.getLogger(MIDRF3894Client.class.getName());

  @Override
  public Revision getRevision() {
    return Revision.rev111111;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("midrf3894/wsdl_1.wsdl");
  }

  @Override
  public ClientRequest createClientRequest(ExchangeContext ctx) {
    log.info("create client request");

    Packet packet = new Packet();
    packet.typeCode = Type.SERVICE;
    packet.date = new Date();
    packet.exchangeType = "2";
    packet.recipient = new InfoSystem("MIDR09001", "МИД России");
    packet.status = Status.REQUEST;
    // TODO webdom check system environment
    packet.testMsg = "Тест";

    // TODO webdom разобраться как там вызывающий работает с исключениями
    ClientRequest clientRequest = null;
    try {
      clientRequest = new ClientRequest();
      clientRequest.appData = createAppData(ctx);
      clientRequest.packet = packet;
      clientRequest.service = new QName("http://www.kdmid.ru/", "Service1");
      clientRequest.port = new QName("http://www.kdmid.ru/", "TranslationSoap");
      clientRequest.action = new QName("http://www.kdmid.ru/", "GetTranslation");
    } catch (Exception e) {
      log.log(Level.SEVERE, "can't create ClientRequest instance", e);
      throw new RuntimeException(e);
    }

    return clientRequest;
  }

  private String createAppData(ExchangeContext ctx) throws Exception {
    if (!ctx.getVariableNames().contains("transcrib_params")) {
      throw new IllegalArgumentException("'transcrib_params' variable noit found in ctx");
    }

    Map<String, String> transcribParams = (Map<String, String>) ctx.getVariable("transcrib_params");

    SmevreqAppData params = new SmevreqAppData();

    if (transcribParams.containsKey("LAT_SURNAME")) {
      params.setLATSURNAME(transcribParams.get("LAT_SURNAME"));
    }

    if (transcribParams.containsKey("LAT_FIRSTNAME")) {
      params.setLATFIRSTNAME(transcribParams.get("LAT_FIRSTNAME"));
    }

    if (transcribParams.containsKey("COUNTRY_CODE")) {
      params.setCOUNTRYCODE(transcribParams.get("COUNTRY_CODE"));
    }

    // SmevreqAppData.class is not @javax.xml.bind.annotation.XmlRootElement.
    // creating root manually
    QName rootQName = new QName("http://www.kdmid.ru/", "Request");
    JAXBElement<SmevreqAppData> root = new JAXBElement<SmevreqAppData>(rootQName, SmevreqAppData.class, params);

    String appDataContent = null;

    StringWriter sw = new StringWriter();
    JAXBContext jaxbContext = JAXBContext.newInstance(SmevreqAppData.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    jaxbMarshaller.marshal(root, sw);
    appDataContent = sw.toString();

    return appDataContent;
  }

  @Override
  public void processClientResponse(ClientResponse clientResponse, ExchangeContext ctx) {
    log.info("process client response");

    Element appDataElement = clientResponse.appData;

    NodeList childs = appDataElement.getChildNodes();
    if (childs.getLength() == 0) {
      ctx.setVariable("transcrib_request_fault", "Не удалось обработать запрос.");
      // TODO webdom
      return;
    }

    for (int i = 0; i < childs.getLength(); i++) {
      Node node = childs.item(i);
      String name = node.getLocalName();
      if (name.equals("Response")) {
        ctx.setVariable("transcrib_request_result", node);
      }
    }

  }

}

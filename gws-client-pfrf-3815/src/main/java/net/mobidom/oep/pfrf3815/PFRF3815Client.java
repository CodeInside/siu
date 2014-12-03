package net.mobidom.oep.pfrf3815;

import java.net.URL;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import net.mobidom.bp.beans.request.DocumentRequest;

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

import com.rstyle.skmv.data_by_snils.DataBySnilsOut;
import com.rstyle.skmv.pfr.PFRFAULT;

public class PFRF3815Client implements Client {

  static Logger log = Logger.getLogger(PFRF3815Client.class.getName());

  @Override
  public Revision getRevision() {
    return Revision.rev120315;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("pfrf3815/SID0003578_1.wsdl");
  }

  private DocumentRequest getDocumentRequest(ExchangeContext ctx) {
    DocumentRequest documentRequest = (DocumentRequest) ctx.getVariable("REQUEST_OBJECT");
    if (documentRequest == null) {
      throw new IllegalStateException("Context have no parameter 'REQUEST_OBJECT'");
    }

    return documentRequest;
  }

  @Override
  public ClientRequest createClientRequest(ExchangeContext ctx) {

    DocumentRequest documentRequest = getDocumentRequest(ctx);

    // create packet
    Packet packet = new Packet();
    packet.typeCode = Packet.Type.SERVICE;
    packet.date = new Date();
    packet.exchangeType = "2";
    packet.recipient = new InfoSystem("PFRF01001", "Пенсионный фонд РФ");
    packet.serviceName = "DATA_BY_SNILS";
    packet.status = Packet.Status.REQUEST;

    if (documentRequest.getTestMessage() != null) {
      packet.testMsg = documentRequest.getTestMessage();
    }

    // setup request
    ClientRequest clientRequest = new ClientRequest();
    clientRequest.packet = packet;
    clientRequest.action = new QName("http://data-by-snils.skmv.rstyle.com", "DataBySnilsRequest");

    // create appdata
    clientRequest.appData = createAppData(documentRequest);

    return clientRequest;
  }

  private String createAppData(DocumentRequest documentRequest) {
    String snisNumber = (String) documentRequest.getRequestParam("snils_number");
    JAXBElement<String> element = new JAXBElement<String>(new QName("http://smev.gosuslugi.ru/rev120315", "snils"), String.class, snisNumber);
    String appData = XmlTypes.beanToXml(element);
    log.info("appData = " + appData);
    return appData;
  }

  @Override
  public void processClientResponse(ClientResponse clientResponse, ExchangeContext ctx) {

    NodeList childs = clientResponse.appData.getChildNodes();
    if (childs.getLength() == 0) {
      return;
    }

    JAXBContext jaxbContext = null;
    try {
      jaxbContext = JAXBContext.newInstance(DataBySnilsOut.class);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }

    for (int i = 0; i < childs.getLength(); i++) {
      Node node = childs.item(i);
      String name = node.getLocalName();
      if (name.equals("result")) {
        try {
          JAXBElement<DataBySnilsOut> appDataElement = jaxbContext.createUnmarshaller().unmarshal(node, DataBySnilsOut.class);
          DataBySnilsOut result = appDataElement.getValue();

          log.info(result.toString());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      } else if (name.equals("fault")) {
        try {
          JAXBElement<PFRFAULT> appDataElement = jaxbContext.createUnmarshaller().unmarshal(node, PFRFAULT.class);
          PFRFAULT fault = appDataElement.getValue();

          log.info(fault.toString());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    // clientResponse.appData;

    // NodeList childs = appDataElement.getChildNodes();
    // if (childs.getLength() == 0) {
    // ctx.setVariable("snils_request_fault", "Не удалось обработать запрос.");
    // // TODO webdom
    // return;
    // }
    //
    // for (int i = 0; i < childs.getLength(); i++) {
    // Node node = childs.item(i);
    // String name = node.getLocalName();
    // if (name.equals("result")) {
    // ctx.setVariable("snils_request_result", node);
    // } else if (name.equals("fault")) {
    // ctx.setVariable("snils_request_fault", node);
    // }
    // }
  }
}
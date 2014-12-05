package net.mobidom.oep.fns3777;

import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.request.DocumentRequestType;

import org.w3c.dom.Element;

import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.XmlTypes;
import unisoft.ws.fns2ndflws.sendquery.Документ;
import unisoft.ws.fns2ndflws.sendquery.Документ.СвНА;
import unisoft.ws.fns2ndflws.sendquery.Документ.СвНА.СвНАФЛ;
import unisoft.ws.fns2ndflws.sendquery.ФИОТип;

public class FNS3777Client implements Client {

  static Logger log = Logger.getLogger(FNS3777Client.class.getName());

  @Override
  public Revision getRevision() {
    return Revision.rev120315;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("fns3777/FNS2NDFLWS_1.wsdl");
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
    if (documentRequest.getRequestType() == null) {
      documentRequest.setRequestType(DocumentRequestType.ЗАПРОС_ДОКУМЕНТА);
    }

    ClientRequest clientRequest = new ClientRequest();

    if (documentRequest.getRequestType() == DocumentRequestType.ЗАПРОС_ДОКУМЕНТА) {
      // create packet
      Packet packet = new Packet();
      packet.typeCode = Packet.Type.SERVICE;
      packet.date = new Date();
      packet.exchangeType = "2";
      packet.recipient = new InfoSystem("FNS001001", "ФНС России");
      packet.sender = new InfoSystem("FNS001001", "ФНС России");
      packet.originator = new InfoSystem("FNS001001", "ФНС России");
      packet.serviceName = "FNS2NDFLWS";
      packet.status = Packet.Status.REQUEST;

      if (documentRequest.getTestMessage() != null) {
        packet.testMsg = documentRequest.getTestMessage();
      }

      // setup request
      clientRequest.packet = packet;
      clientRequest.action = new QName("http://ws.unisoft/", "sendQuery");

      // create appdata
      clientRequest.appData = createFirstRequestAppData(documentRequest);
      clientRequest.needEnvelopedSignatureForAppData = true;

    } else if (documentRequest.getRequestType() == DocumentRequestType.ПРОВЕРКА_ВЫПОЛНЕНИЯ) {

    }

    return clientRequest;
  }

  private String createFirstRequestAppData(DocumentRequest documentRequest) {

    Map<String, Object> params = documentRequest.getRequestParams();

    try {

      Документ документ = new Документ();
      документ.setВерсФорм(String.valueOf(params.get("ВерсФорм")));
      документ.setИдЗапросП(String.valueOf(params.get("ИдЗапрос")));
      документ.setОтчетГод(XmlTypes.date(String.valueOf(params.get("ОтчетГод"))));
      документ.setТипЗапросП(String.valueOf(params.get("ТипЗапроса")));

      ФИОТип фио = new ФИОТип();
      фио.setИмя(String.valueOf(params.get("Имя")));
      фио.setОтчество(String.valueOf(params.get("Отчество")));
      фио.setФамилия(String.valueOf(params.get("Фамилия")));

      СвНАФЛ свНАФЛ = new СвНАФЛ();
      свНАФЛ.setСНИЛС(String.valueOf(params.get("Снилс")));
      свНАФЛ.setФИО(фио);

      СвНА свНА = new СвНА();
      свНА.setСвНАФЛ(свНАФЛ);

      документ.setСвНА(свНА);

      JAXBContext jaxbCtx = JAXBContext.newInstance(Документ.class);
      Marshaller marshaller = jaxbCtx.createMarshaller();

      marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

      StringWriter sw = new StringWriter();
      marshaller.marshal(документ, sw);

      String line = sw.toString();

      return line;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void processClientResponse(ClientResponse clientResponse, ExchangeContext ctx) {

    Element appDataElement = clientResponse.appData;

    try {

      javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
      javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(new java.io.StringWriter());
      javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(appDataElement);
      transformer.transform(source, result);
      String xmlString = result.getWriter().toString();

      log.info(xmlString);

    } catch (Exception e) {
      log.log(Level.SEVERE, "", e);
      throw new RuntimeException(e);
    }
  }

}
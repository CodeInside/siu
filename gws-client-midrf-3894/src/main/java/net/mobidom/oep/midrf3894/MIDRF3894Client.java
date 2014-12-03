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
import javax.xml.transform.dom.DOMResult;

import net.mobidom.bp.beans.XmlContentWrapper;
import net.mobidom.bp.beans.Документ;
import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.request.DocumentRequestType;

import org.w3c.dom.Document;

import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Packet.Status;
import ru.codeinside.gws.api.Packet.Type;
import ru.codeinside.gws.api.Revision;
import ru.gosuslugi.smev.rev111111.SmevAppData;
import ru.kdmid.ObjectFactory;
import ru.kdmid.SmevreqAppData;
import ru.kdmid.SmevresAppData;

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

  private DocumentRequest getDocumentRequest(ExchangeContext ctx) {
    DocumentRequest documentRequest = (DocumentRequest) ctx.getVariable("REQUEST_OBJECT");
    if (documentRequest == null) {
      throw new IllegalStateException("Context have no parameter 'REQUEST_OBJECT'");
    }

    return documentRequest;
  }

  @Override
  public ClientRequest createClientRequest(ExchangeContext ctx) {
    log.info("create client request");

    DocumentRequest documentRequest = getDocumentRequest(ctx);
    if (documentRequest.getRequestType() == null) {
      documentRequest.setRequestType(DocumentRequestType.ЗАПРОС_ДОКУМЕНТА);
    } else if (documentRequest.getRequestType() != DocumentRequestType.ЗАПРОС_ДОКУМЕНТА) {
      throw new IllegalStateException(String.format("Unknown DocumentRequestType for client midrf3894'%s'", documentRequest.getRequestType()));
    }

    Packet packet = new Packet();
    packet.typeCode = Type.SERVICE;
    packet.date = new Date();
    packet.exchangeType = "2";
    packet.recipient = new InfoSystem("MIDR09001", "МИД России");
    packet.status = Status.REQUEST;

    if (documentRequest.getTestMessage() != null) {
      packet.testMsg = documentRequest.getTestMessage();
    }

    // TODO webdom разобраться как там вызывающий работает с исключениями
    ClientRequest clientRequest = null;
    try {
      clientRequest = new ClientRequest();
      clientRequest.appData = createAppData(documentRequest);
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

  private String createAppData(DocumentRequest documentRequest) throws Exception {

    Map<String, Object> params = documentRequest.getRequestParams();
    SmevreqAppData appDataParam = new SmevreqAppData();

    if (params.containsKey("LAT_SURNAME")) {
      appDataParam.setLATSURNAME((String) params.get("LAT_SURNAME"));
    }

    if (params.containsKey("LAT_FIRSTNAME")) {
      appDataParam.setLATFIRSTNAME((String) params.get("LAT_FIRSTNAME"));
    }

    if (params.containsKey("COUNTRY_CODE")) {
      appDataParam.setCOUNTRYCODE((String) params.get("COUNTRY_CODE"));
    }

    // SmevreqAppData.class is not @javax.xml.bind.annotation.XmlRootElement.
    // creating root manually
    QName rootQName = new QName("http://www.kdmid.ru/", "Request");
    JAXBElement<SmevreqAppData> root = new JAXBElement<SmevreqAppData>(rootQName, SmevreqAppData.class, appDataParam);

    String appDataContent = null;

    StringWriter sw = new StringWriter();
    JAXBContext jaxbContext = JAXBContext.newInstance(SmevreqAppData.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    jaxbMarshaller.marshal(root, sw);
    appDataContent = sw.toString();

    return appDataContent;
  }

  @Override
  public void processClientResponse(ClientResponse clientResponse, ExchangeContext ctx) {
    log.info("process client response");

    DocumentRequest documentRequest = getDocumentRequest(ctx);

    JAXBContext jaxbContext = null;
    try {
      jaxbContext = JAXBContext.newInstance(SmevAppData.class);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }

    SmevresAppData response = null;
    try {
      JAXBElement<SmevAppData> appDataElement = jaxbContext.createUnmarshaller().unmarshal(clientResponse.appData, SmevAppData.class);
      SmevAppData appData = appDataElement.getValue();
      if (appData.getResponse() == null) {
        throw new Exception("unable to parse response appData");
      }
      response = appData.getResponse();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    int errorCode = response.getERRCODE();
    if (errorCode != 0) {
      documentRequest.setFault(String.format("ERROR_CODE: '%s'\n ERROR_DESCR: '%s'", response.getERRCODE(), response.getERRDESCRIPTION()));
    } else {
      Документ doc = new Документ();
      XmlContentWrapper xmlContentWrapper = new XmlContentWrapper();

      DOMResult domResult = new DOMResult();
      try {
        JAXBElement<SmevresAppData> responseElement = new ObjectFactory().createResponse(response);
        jaxbContext.createMarshaller().marshal(responseElement, domResult);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }

      xmlContentWrapper.setXmlContent(((Document) domResult.getNode()).getDocumentElement());
      doc.setXmlContent(xmlContentWrapper);
      doc.setDocumentType(documentRequest.getType());
      documentRequest.setДокумент(doc);
      documentRequest.setReady(true);
    }

  }
}

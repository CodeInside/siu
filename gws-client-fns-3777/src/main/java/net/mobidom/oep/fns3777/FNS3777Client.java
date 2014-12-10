package net.mobidom.oep.fns3777;

import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.request.DocumentRequestType;
import net.mobidom.bp.beans.request.ResponseType;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

    Map<String, Serializable> params = documentRequest.getRequestParams();

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

  // TODO webdom add detail to error handlers
  @Override
  public void processClientResponse(ClientResponse clientResponse, ExchangeContext ctx) {

    DocumentRequest documentRequest = getDocumentRequest(ctx);

    Element appDataElement = clientResponse.appData;
    
    try {

      JAXBContext jaxbContext = JAXBContext.newInstance(unisoft.ws.fns2ndflws.sendqueryresponse.Документ.class, 
                                                        unisoft.ws.fns2ndflws.getresultresponse.Документ.class);
  
      if (documentRequest.getRequestType() == DocumentRequestType.ЗАПРОС_ДОКУМЕНТА) {
        
        NodeList childs = appDataElement.getChildNodes();
        if (childs.getLength() == 0) {
          documentRequest.setResponseType(ResponseType.SYSTEM_ERROR);
          throw new RuntimeException("AppData content is empty");
        }

        Element docElementRaw = (Element) childs.item(0);
        if (!docElementRaw.getTagName().equals("Документ")) {
          documentRequest.setResponseType(ResponseType.SYSTEM_ERROR);
          throw new RuntimeException("undefuned content inside AppData");
        }
        
        JAXBElement<unisoft.ws.fns2ndflws.sendqueryresponse.Документ> docElement = jaxbContext.createUnmarshaller().unmarshal(docElementRaw,
                                                                                          unisoft.ws.fns2ndflws.sendqueryresponse.Документ.class);
        
        unisoft.ws.fns2ndflws.sendqueryresponse.Документ result = docElement.getValue();

        String responseCode = result.getКодОбр();
        if (responseCode != null && !responseCode.isEmpty()) {
          // response code here! something went wrong!
          String responseMsg = SEND_QUERY_RESPONSE_CODE.get(responseCode);
          if (responseMsg != null && !responseMsg.isEmpty()) {
            documentRequest.setFault(createErrorMessage(responseCode, responseMsg));
          } else {
            documentRequest.setFault(createErrorMessage(responseCode, "undefined response code"));
          }
          
          documentRequest.setResponseType(ResponseType.DATA_ERROR);
          return;
        }
        
        BigInteger resultId = result.getИдЗапросФ();
        if (resultId == null) {
          documentRequest.setFault("не определен ИдЗапросФ в ответе сервиса");
          documentRequest.setResponseType(ResponseType.DATA_ERROR);
          return;
        }
        
        documentRequest.setRequestId(resultId.toString());
        documentRequest.setRequestType(DocumentRequestType.ПРОВЕРКА_ВЫПОЛНЕНИЯ);

      } else if (documentRequest.getRequestType() == DocumentRequestType.ПРОВЕРКА_ВЫПОЛНЕНИЯ) {
        
        
                
      }
      
    } catch (JAXBException e) {
      log.log(Level.SEVERE, "cant use JaxbContext", e);
      throw new RuntimeException("cant use JaxbContext", e);
    }  
  }
  
  public static String createErrorMessage(String code, String message) {
    return String.format("RESPONSE_CODE: '%s'\n RESPONSE_MESSAGE: '%s'", code, message);
  }
  
  public static Map<String, String> SEND_QUERY_RESPONSE_CODE = new HashMap<String, String>() {
    private static final long serialVersionUID = -8060800710002968444L;
    {
      put("01", "запрашиваемые сведения не найдены");
      put("52", "ответ не готов");
      put("83", "отсутствует запрос с указанным идентификатором запроса");
      put("85", "неверный объектный идентификатор в сертификате ключа подписи");
      put("99", "системная ошибка");
    }
  };

}
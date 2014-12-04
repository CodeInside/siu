package net.mobidom.oep.pfrf3814;

import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMResult;

import net.mobidom.bp.beans.XmlContentWrapper;
import net.mobidom.bp.beans.Документ;
import net.mobidom.bp.beans.Пол;
import net.mobidom.bp.beans.request.DocumentRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;

import com.rstyle.skmv.pfr.FIO;
import com.rstyle.skmv.pfr.GENDER;
import com.rstyle.skmv.pfr.PFRFAULT;
import com.rstyle.skmv.snils_by_data.ObjectFactory;
import com.rstyle.skmv.snils_by_data.SnilsByDataIn;
import com.rstyle.skmv.snils_by_data.SnilsByDataOut;

public class PFRF3814Client implements Client {

  static Logger log = Logger.getLogger(PFRF3814Client.class.getName());

  static SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");

  @Override
  public Revision getRevision() {
    return Revision.rev120315;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("pfrf3814/wsdl_1.wsdl");
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
    packet.serviceName = "SNILS_BY_DATA";
    packet.status = Packet.Status.REQUEST;

    if (documentRequest.getTestMessage() != null) {
      packet.testMsg = documentRequest.getTestMessage();
    }

    // setup request
    ClientRequest clientRequest = new ClientRequest();
    clientRequest.packet = packet;
    clientRequest.action = new QName("http://snils-by-data.skmv.rstyle.com", "SnilsByDataRequest");

    // create appdata
    clientRequest.appData = createAppData(documentRequest);

    return clientRequest;
  }

  private String createAppData(DocumentRequest documentRequest) {

    Map<String, Object> params = documentRequest.getRequestParams();

    SnilsByDataIn in = new SnilsByDataIn();

    in.setBirthDate(SDF.format((Date) params.get("birthdate")));

    FIO fio = new FIO();
    fio.setFirstName(String.valueOf(params.get("name")));
    fio.setLastName(String.valueOf(params.get("surname")));
    fio.setPatronymic(String.valueOf(params.get("patronymic")));
    in.setFio(fio);

    Пол genderLoc = (Пол) params.get("gender");
    GENDER gender = genderLoc == Пол.ЖЕНСКИЙ ? GENDER.F : GENDER.M;
    in.setGender(gender);

    JAXBElement<SnilsByDataIn> appDataElement = new JAXBElement<SnilsByDataIn>(new QName("http://smev.gosuslugi.ru/rev120315", "request"), SnilsByDataIn.class, in);

    StringWriter sw = new StringWriter();

    try {
      JAXBContext jaxbCtx = JAXBContext.newInstance(SnilsByDataIn.class);
      Marshaller marshaller = jaxbCtx.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
      marshaller.marshal(appDataElement, sw);
    } catch (Exception e) {
      throw new RuntimeException("cant use JaxbContext", e);
    }

    String appData = sw.toString();

    log.info("appData = " + appData);
    return appData;
  }

  @Override
  public void processClientResponse(ClientResponse clientResponse, ExchangeContext ctx) {

    DocumentRequest documentRequest = getDocumentRequest(ctx);

    NodeList childs = clientResponse.appData.getChildNodes();
    if (childs.getLength() == 0) {
      return;
    }

    JAXBContext jaxbContext = null;
    try {
      jaxbContext = JAXBContext.newInstance(SnilsByDataOut.class, PFRFAULT.class);
    } catch (Exception e) {
      throw new IllegalStateException("cant init jaxbcontext", e);
    }

    for (int i = 0; i < childs.getLength(); i++) {
      Node node = childs.item(i);
      String name = node.getLocalName();
      if (name.equals("result")) {
        try {
          JAXBElement<SnilsByDataOut> appDataElement = jaxbContext.createUnmarshaller().unmarshal(node, SnilsByDataOut.class);
          SnilsByDataOut result = appDataElement.getValue();

          Документ doc = new Документ();
          XmlContentWrapper xmlContentWrapper = new XmlContentWrapper();

          DOMResult domResult = new DOMResult();
          try {
            JAXBElement<SnilsByDataOut> responseElement = new ObjectFactory().createResult(result);
            jaxbContext.createMarshaller().marshal(responseElement, domResult);
          } catch (Exception e) {
            throw new IllegalStateException(e);
          }

          xmlContentWrapper.setXmlContent(((Document) domResult.getNode()).getDocumentElement());
          doc.setXmlContent(xmlContentWrapper);
          doc.setDocumentType(documentRequest.getType());
          documentRequest.setДокумент(doc);
          documentRequest.setReady(true);

          return;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      } else if (name.equals("fault")) {
        try {
          JAXBElement<PFRFAULT> appDataElement = jaxbContext.createUnmarshaller().unmarshal(node, PFRFAULT.class);
          PFRFAULT fault = appDataElement.getValue();

          String errorMessage = String.format("ERROR_CODE: '%s'\nERROR_MESSAGE: '%s'\nSTACK_TRACE: '%s'", 
                                                    fault.getCode(), fault.getMessage(), fault.getStackTrace());
          documentRequest.setFault(errorMessage);

          return;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
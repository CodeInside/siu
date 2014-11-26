package net.mobidom.oep.fns3777;

import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;

import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.CryptoProvider;
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

  private CryptoProvider cryptoProvider;

  @Override
  public Revision getRevision() {
    return Revision.rev120315;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("fns3777/FNS2NDFLWS_1.wsdl");
  }

  @Override
  public ClientRequest createClientRequest(ExchangeContext ctx) {

    // create packet
    Packet packet = new Packet();
    packet.typeCode = Packet.Type.SERVICE;
    packet.date = new Date();
    packet.exchangeType = "2";
    packet.recipient = new InfoSystem("FNS001001", "ФНС России");
    packet.sender = new InfoSystem("FNS001001", "ФНС России");
    packet.originator = new InfoSystem("FNS001001", "ФНС России");;
    packet.serviceName = "FNS2NDFLWS";
    packet.status = Packet.Status.REQUEST;

    // TODO webdom check system environment
    packet.testMsg = "";

    // setup request
    ClientRequest clientRequest = new ClientRequest();
    clientRequest.packet = packet;
    clientRequest.action = new QName("http://ws.unisoft/", "sendQuery");

    // create appdata
    clientRequest.appData = createAppData(ctx);
    clientRequest.needEnvelopedSignatureForAppData = true;

    return clientRequest;
  }

  private String createAppData(ExchangeContext ctx) {

    try {

      Документ документ = new Документ();
      документ.setВерсФорм("4.01");
      документ.setИдЗапросП("2012");
      документ.setОтчетГод(XmlTypes.date("01.01.2012"));
      документ.setТипЗапросП("1");

      ФИОТип фиоТип = new ФИОТип();
      фиоТип.setИмя("НАТАЛЬЯ");
      фиоТип.setОтчество("ВЛАДИМИРОВНА");
      фиоТип.setФамилия("ЕВСЕЕВА");

      СвНАФЛ свНАФЛ = new СвНАФЛ();
      свНАФЛ.setСНИЛС("12345678901234");
      свНАФЛ.setФИО(фиоТип);

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

      log.info("appdata content = \n" + line);

      return line;

    } catch (Exception e) {

      log.log(Level.SEVERE, "can't create Документ for sendQuery", e);

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

    // NodeList childs = appDataElement.getChildNodes();
    // if (childs.getLength() == 0) {
    // // ctx.setVariable("snilsbydata_request_fault",
    // // "Не удалось обработать запрос.");
    // // TODO webdom
    // return;
    // }

    //
    // for (int i = 0; i < childs.getLength(); i++) {
    //
    // Node node = childs.item(i);
    //
    // String name = node.getLocalName();
    //
    //
    // // if (name.equals("result")) {
    // // ctx.setVariable("snilsbydata_request_result", node);
    // // } else if (name.equals("fault")) {
    // // ctx.setVariable("snilsbydata_request_fault", node);
    // // }
    // }
    //
  }

  public void bindCryptoProvider(CryptoProvider cryptoProvider) {
    this.cryptoProvider = cryptoProvider;
  }

  public void unbindCryptoProvider(CryptoProvider cryptoProvider) {
    this.cryptoProvider = null;
  }

}
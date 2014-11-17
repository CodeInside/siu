package net.mobidom.oep.pfrf3814;

import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
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
import ru.codeinside.gws.api.Revision;

import com.rstyle.skmv.pfr.FIO;
import com.rstyle.skmv.pfr.GENDER;
import com.rstyle.skmv.snils_by_data.SnilsByDataIn;

public class WebServiceClient implements Client {

  static Logger log = Logger.getLogger(WebServiceClient.class.getName());

  @Override
  public Revision getRevision() {
    return Revision.rev120315;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("pfrf3814/wsdl_1.wsdl");
  }

  @Override
  public ClientRequest createClientRequest(ExchangeContext ctx) {

    // create packet
    Packet packet = new Packet();
    packet.typeCode = Packet.Type.SERVICE;
    packet.date = new Date();
    packet.exchangeType = "2";
    packet.recipient = new InfoSystem("PFRF01001", "Пенсионный фонд РФ");
    packet.serviceName = "SNILS_BY_DATA";
    packet.status = Packet.Status.REQUEST;

    // TODO webdom check system environment
    packet.testMsg = "Test";

    // setup request
    ClientRequest clientRequest = new ClientRequest();
    clientRequest.packet = packet;
    clientRequest.action = new QName("http://snils-by-data.skmv.rstyle.com", "SnilsByDataRequest");

    // create appdata
    clientRequest.appData = createAppData(ctx);

    return clientRequest;
  }

  private String createAppData(ExchangeContext ctx) {

    Map<String, String> params = (Map<String, String>) ctx.getVariable("snilsbydata_params");
    try {

      SnilsByDataIn in = new SnilsByDataIn();

      in.setBirthDate(params.get("birthdate"));

      FIO fio = new FIO();
      fio.setFirstName(params.get("name"));
      fio.setLastName(params.get("surname"));
      fio.setPatronymic(params.get("patronymic"));
      in.setFio(fio);

      in.setGender(GENDER.fromValue(params.get("gender")));

      JAXBContext jaxbCtx = JAXBContext.newInstance(SnilsByDataIn.class);
      Marshaller marshaller = jaxbCtx.createMarshaller();

      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

      StringWriter sw = new StringWriter();

      marshaller.marshal(in, sw);

      return sw.toString();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void processClientResponse(ClientResponse clientResponse, ExchangeContext ctx) {

    Element appDataElement = clientResponse.appData;

    NodeList childs = appDataElement.getChildNodes();
    if (childs.getLength() == 0) {
      ctx.setVariable("snilsbydata_request_fault", "Не удалось обработать запрос.");
      // TODO webdom
      return;
    }

    for (int i = 0; i < childs.getLength(); i++) {
      Node node = childs.item(i);
      String name = node.getLocalName();
      if (name.equals("result")) {
        ctx.setVariable("snilsbydata_request_result", node);
      } else if (name.equals("fault")) {
        ctx.setVariable("snilsbydata_request_fault", node);
      }
    }
  }
}
package net.mobidom.oep.ensi3407;

import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import net.mobidom.bp.beans.request.DocumentRequest;

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
import ru.codeinside.gws.api.Packet.Status;
import ru.codeinside.gws.api.Packet.Type;

public class ENSI3407Client implements Client {

  static Logger log = Logger.getLogger(ENSI3407Client.class.getName());

  @Override
  public Revision getRevision() {
    return Revision.rev111111;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("ensi3407/wsdl_1.wsdl");
  }

  private DocumentRequest getDocumentRequest(ExchangeContext ctx) {
    DocumentRequest documentRequest = (DocumentRequest) ctx.getVariable("REQUEST_OBJECT");
    if (documentRequest == null) {
      throw new IllegalStateException("Context have no parameter 'REQUEST_OBJECT'");
    }

    return documentRequest;
  }

  @Override
  public ClientRequest createClientRequest(ExchangeContext exchangeContext) {

    DocumentRequest documentRequest = getDocumentRequest(exchangeContext);

    Packet packet = new Packet();
    packet.typeCode = Type.SERVICE;
    packet.date = new Date();
    packet.exchangeType = "2";
    packet.recipient = new InfoSystem("NSI_CODE", "НСИ");
    packet.status = Status.REQUEST;

    if (documentRequest.getTestMessage() != null) {
      packet.testMsg = documentRequest.getTestMessage();
    }

    ClientRequest clientRequest = null;
    try {
      clientRequest = new ClientRequest();
      clientRequest.appData = createAppData(documentRequest);
      clientRequest.packet = packet;
    } catch (Exception e) {
      log.log(Level.SEVERE, "can't create ClientRequest instance", e);
      throw new RuntimeException(e);
    }
    return null;
  }

  private String createAppData(DocumentRequest documentRequest) {
    return null;
  }

  @Override
  public void processClientResponse(ClientResponse response, ExchangeContext context) {
    // TODO Auto-generated method stub

  }

}
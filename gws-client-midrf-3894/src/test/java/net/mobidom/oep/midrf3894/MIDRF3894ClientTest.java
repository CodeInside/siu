package net.mobidom.oep.midrf3894;

import java.util.logging.Logger;

import net.mobidom.bp.beans.request.DocumentRequest;

import org.junit.Ignore;
import org.junit.Test;

import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.core.cproto.ClientRev111111;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;

public class MIDRF3894ClientTest {

  static Logger log = Logger.getLogger(MIDRF3894ClientTest.class.getName());

//  @Ignore
  @Test
  public void serviceInstanceInvoke() throws Exception {
    InfoSystem pnzr01581 = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    // String SERVICE_ADDRESS =
    // "http://smev-mvf.test.gosuslugi.ru:7777/gateway/services/SID0003634/1.00";
    String SERVICE_ADDRESS = "http://213.208.170.40:9081/Translationservice.asmx";

    CryptoProvider cryptoProvider = new CryptoProvider();
    ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
    MIDRF3894Client client = new MIDRF3894Client();

    ClientRev111111 rev111111 = new ClientRev111111(definitionParser, cryptoProvider);

    DocumentRequest documentRequest = new DocumentRequest();
    documentRequest.addRequestParam("LAT_SURNAME", "LABBEE");
    documentRequest.addRequestParam("LAT_FIRSTNAME", "GABRIELLE CECILIA");
    documentRequest.addRequestParam("COUNTRY_CODE", "USA");
    documentRequest.setTestMessage("Test");

    ExchangeContext ctx = new DummyContext();
    ctx.setVariable("REQUEST_OBJECT", documentRequest);

    ClientRequest request = client.createClientRequest(ctx);
    request.packet.sender = pnzr01581;
    request.packet.originator = pnzr01581;

    request.portAddress = SERVICE_ADDRESS;
    HttpTransportPipe.dump = true;

    ClientResponse response = rev111111.send(client.getWsdlUrl(), request, null);

    client.processClientResponse(response, ctx);

    log.info("==== END ====");

  }

}

package net.mobidom.oep.ensi3407;

import java.util.logging.Logger;

import net.mobidom.bp.beans.request.DocumentRequest;

import org.junit.Test;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;

import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.core.cproto.ClientRev120315;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;

public class ENSI3407ClientTest {

  private static Logger log = Logger.getLogger(ENSI3407ClientTest.class.getName());

  @Test
  public void serviceInstanceInvoke() throws Exception {
    InfoSystem pnzr01581 = new InfoSystem("PNZR01581",
        "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");

    String SERVICE_ADDRESS = "http://smev-mvf.test.gosuslugi.ru:7777/gateway/services/SID0003155/1.00";

    CryptoProvider cryptoProvider = new CryptoProvider();
    ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
    ENSI3407Client client = new ENSI3407Client();

    ClientRev120315 clientProtocol = new ClientRev120315(definitionParser, cryptoProvider);

    DocumentRequest documentRequest = new DocumentRequest();
    documentRequest.addRequestParam("", "");
    documentRequest.setTestMessage("Test");

    ExchangeContext ctx = new DummyContext();
    ctx.setVariable("REQUEST_OBJECT", documentRequest);

    ClientRequest request = client.createClientRequest(ctx);
    request.packet.sender = pnzr01581;
    request.packet.originator = pnzr01581;

    request.portAddress = SERVICE_ADDRESS;
    HttpTransportPipe.dump = true;

    ClientResponse response = clientProtocol.send(client.getWsdlUrl(), request, null);

    client.processClientResponse(response, ctx);

    log.info("==== END ====");

  }

}

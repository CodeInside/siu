package net.mobidom.oep.pfrf3815;

import java.util.logging.Logger;

import net.mobidom.bp.beans.request.DocumentRequest;

import org.junit.Test;

import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.core.cproto.ClientRev120315;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;

public class PFRF3815ClientTest {
  static Logger log = Logger.getLogger(PFRF3815ClientTest.class.getName());

  @Test
  public void serviceInstanceInvoke() throws Exception {
    InfoSystem pnzr01581 = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    
    String SERVICE_ADDRESS = "http://smev-mvf.test.gosuslugi.ru:7777/gateway/services/SID0003578/1.00";

    CryptoProvider cryptoProvider = new CryptoProvider();
    ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
    PFRF3815Client client = new PFRF3815Client();

    ClientRev120315 clientProtocol = new ClientRev120315(definitionParser, cryptoProvider);

    DocumentRequest documentRequest = new DocumentRequest();
    documentRequest.addRequestParam("snils_number", "027-733-198 62");
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

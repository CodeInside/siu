package net.mobidom.oep.fns3777;

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

public class FNS3777ClientTest {

  static Logger log = Logger.getLogger(FNS3777ClientTest.class.getName());

  @Test
  public void serviceInstanceInvoke() throws Exception {
    InfoSystem pnzr01581 = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");

    String SERVICE_ADDRESS = "http://localhost:8088/mock";
    // String SERVICE_ADDRESS = "http://smev-mvf.test.gosuslugi.ru:7777/gateway/services/SID0003490/1.00";
    // String SERVICE_ADDRESS = "http://94.125.90.50:6336/FNS2NDFLWS";

    CryptoProvider cryptoProvider = new CryptoProvider();
    ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
    FNS3777Client client = new FNS3777Client();

    ClientRev120315 clientProtocol = new ClientRev120315(definitionParser, cryptoProvider);

    DocumentRequest documentRequest = new DocumentRequest();
    documentRequest.setTestMessage("");

    documentRequest.addRequestParam("ВерсФорм", "4.01");
    documentRequest.addRequestParam("ИдЗапрос", "2012");
    documentRequest.addRequestParam("ОтчетГод", "01.01.2012");
    documentRequest.addRequestParam("ТипЗапроса", "1");
    documentRequest.addRequestParam("Имя", "НАТАЛЬЯ");
    documentRequest.addRequestParam("Отчество", "ВЛАДИМИРОВНА");
    documentRequest.addRequestParam("Фамилия", "ЕВСЕЕВА");
    documentRequest.addRequestParam("Снилс", "12345678901234");

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

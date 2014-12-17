package ru.codeinside.gws3970c;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet.Status;
import ru.codeinside.gws.core.cproto.ClientRev120315;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;

public class MfcDeclarerTest extends Assert {

  @Ignore
  @Test
  public void test_startBP() throws Exception {

    InfoSystem pnzr01581 = new InfoSystem("PNZR01581",
        "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");

    String SERVICE_ADDRESS = "http://localhost:8080/smev/mvvact";

    CryptoProvider cryptoProvider = new CryptoProvider();
    ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
    UniversalClient client = new UniversalClient();

    ClientRev120315 clientProtocol = new ClientRev120315(definitionParser, cryptoProvider);

    ExchangeContext ctx = new DummyContext();
    ctx.setVariable("procedureCode", "333");

    Enclosure xmlEnclosure = new Enclosure("50.xml", loadFile("c:/work/siu/_test_documents/50/50.xml"));
    ctx.addEnclosure("appData_50.xml", xmlEnclosure);
    Enclosure signatureEnclosure = new Enclosure("50.signature", loadFile("c:/work/siu/_test_documents/50/50.signature"));
    ctx.addEnclosure("appData_50.signature", signatureEnclosure);

    ClientRequest request = client.createClientRequest(ctx);
    request.packet.sender = pnzr01581;
    request.packet.originator = pnzr01581;

    request.portAddress = SERVICE_ADDRESS;
    HttpTransportPipe.dump = true;

    ClientResponse response = clientProtocol.send(client.getWsdlUrl(), request, null);

    client.processClientResponse(response, ctx);

    System.out.println("========================================================");
    System.out.println("response.packet.originRequestIdRef = " + response.packet.originRequestIdRef);
    System.out.println("started process. internalRequestId = " + ctx.getVariable("internalRequestId"));
  }

  private byte[] loadFile(String string) throws Exception {
    return FileUtils.readFileToByteArray(new File(string));
  }

  @Ignore
  @Test
  public void test_updateStatusBP() throws Exception {
    
    InfoSystem pnzr01581 = new InfoSystem("PNZR01581",
        "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");

    String SERVICE_ADDRESS = "http://localhost:8080/smev/mvvact";

    CryptoProvider cryptoProvider = new CryptoProvider();
    ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
    UniversalClient client = new UniversalClient();

    ClientRev120315 clientProtocol = new ClientRev120315(definitionParser, cryptoProvider);

    ExchangeContext ctx = new DummyContext();
    ctx.setVariable("internalRequestId", "659");
    ctx.setVariable("smevOriginRequestId", "a1e08c9c-caf2-4289-88da-47ef1826de3c");

    ClientRequest request = client.createClientRequest(ctx);
    request.packet.status = Status.CANCEL;
    request.packet.sender = pnzr01581;
    request.packet.originator = pnzr01581;

    request.portAddress = SERVICE_ADDRESS;
    HttpTransportPipe.dump = true;

    ClientResponse response = clientProtocol.send(client.getWsdlUrl(), request, null);

    client.processClientResponse(response, ctx);

    System.out.println("========================================================");

    for (String name : ctx.getVariableNames()) {
      System.out.println(name + " = " + ctx.getVariable(name));
    }

  }

  @Test
  public void test_cancelsBP() throws Exception {

    InfoSystem pnzr01581 = new InfoSystem("PNZR01581",
        "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");

    String SERVICE_ADDRESS = "http://localhost:8080/smev/mvvact";

    CryptoProvider cryptoProvider = new CryptoProvider();
    ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
    UniversalClient client = new UniversalClient();

    ClientRev120315 clientProtocol = new ClientRev120315(definitionParser, cryptoProvider);

    ExchangeContext ctx = new DummyContext();
    ctx.setVariable("internalRequestId", "659");
    ctx.setVariable("smevOriginRequestId", "a1e08c9c-caf2-4289-88da-47ef1826de3c");

    ClientRequest request = client.createClientRequest(ctx);
//    request.packet.status = Status.RE;
    request.packet.sender = pnzr01581;
    request.packet.originator = pnzr01581;

    request.portAddress = SERVICE_ADDRESS;
    HttpTransportPipe.dump = true;

    ClientResponse response = clientProtocol.send(client.getWsdlUrl(), request, null);

    client.processClientResponse(response, ctx);

    System.out.println("========================================================");

    for (String name : ctx.getVariableNames()) {
      System.out.println(name + " = " + ctx.getVariable(name));
    }

  }

}

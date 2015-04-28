package ru.codeinside.gws.core.cproto;

import junit.framework.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientProtocol;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.stubs.DummyContext;
import ru.codeinside.gws.stubs.TestServer;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;
import ru.codeinside.gws3970c.UniversalClient;

import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;

public class NormalizedBodyTest extends Assert {
    @Test
    public void test_normalizedBody() throws Exception {
        final int PORT = 7777;
        final String PORT_ADDRESS = "http://127.0.0.1:" + PORT;
        final TestServer testServer = new TestServer();
        testServer.start(PORT);
        try {
            Enclosure myEnclosure = new Enclosure("file.txt", "file", new byte[] {1,2,3,4,5,6,7,8,9,0});
            DummyContext ctx = new DummyContext();
            ctx.addEnclosure("appData_myEnclosure", myEnclosure);
            ctx.setVariable("appData_myVar", "-*-MyValue-*-");

            UniversalClient universalClient = new UniversalClient();
            ClientRequest clientRequest = createRequest(PORT_ADDRESS, universalClient, ctx);

            ClientProtocol clientProtocol = createClientProtocol();
            ByteArrayOutputStream normalizedBody = new ByteArrayOutputStream();
            SOAPMessage soapMessage =
                    clientProtocol.createMessage(universalClient.getWsdlUrl(), clientRequest, null, normalizedBody);

            assertTrue(normalizedBody.toString()
                    .startsWith("<SOAP-ENV:Body xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\""));
        } finally {
            testServer.stop();
        }
    }

    private ClientProtocol createClientProtocol() {
        ServiceDefinitionParser parser = new ServiceDefinitionParser();
        CryptoProvider cryptoProvider = new CryptoProvider();
        XmlNormalizer xmlNormalizer = new XmlNormalizerImpl();
        return new ClientRev120315(parser, cryptoProvider, xmlNormalizer);
    }

    private ClientRequest createRequest(String port, Client client, ExchangeContext ctx) {
        ClientRequest request = client.createClientRequest(ctx);
        request.portAddress = port;
        request.packet.sender = request.packet.originator = new InfoSystem("test", "test");
        return request;
    }
}

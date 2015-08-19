package ru.codeinside.gws.core.cproto;

import junit.framework.Assert;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientProtocol;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Signature;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.api.XmlSignatureInjector;
import ru.codeinside.gws.signature.injector.XmlSignatureInjectorImp;
import ru.codeinside.gws.stubs.DummyContext;
import ru.codeinside.gws.stubs.DummyProvider;
import ru.codeinside.gws.stubs.TestServer;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;
import ru.codeinside.gws3970c.UniversalClient;

import javax.security.auth.x500.X500Principal;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.util.Date;

public class NormalizedBodyTest extends Assert {

  @BeforeClass
  public static void setUp() {
    Security.addProvider(new BouncyCastleProvider());
  }

  @Test
  public void test_normalizedBody() throws Exception {
    final int PORT = 7777;
    final String PORT_ADDRESS = "http://127.0.0.1:" + PORT;
    final TestServer testServer = new TestServer();
    testServer.start(PORT);
    try {
      Enclosure myEnclosure = new Enclosure("file.txt", "file", new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
      DummyContext ctx = new DummyContext();
      ctx.addEnclosure("appData_myEnclosure", myEnclosure);
      ctx.setVariable("appData_myVar", "-*-MyValue-*-");

      UniversalClient universalClient = new UniversalClient();
      ClientRequest clientRequest = createRequest(PORT_ADDRESS, universalClient, ctx);

      ClientProtocol clientProtocol = createClientProtocol();
      ByteArrayOutputStream normalizedSignedInfo = new ByteArrayOutputStream();
      SOAPMessage soapMessage =
          clientProtocol.createMessage(universalClient.getWsdlUrl(), clientRequest, null, normalizedSignedInfo);

      assertTrue(normalizedSignedInfo.toString()
          .startsWith("<ds:SignedInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">" +
              "<ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></ds:CanonicalizationMethod>" +
              "<ds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411\"></ds:SignatureMethod>" +
              "<ds:Reference URI=\"#body\">" +
              "<ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></ds:Transform></ds:Transforms>" +
              "<ds:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr3411\"></ds:DigestMethod><ds:DigestValue>"));

      byte[] content = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
      byte[] sign = new byte[]{11, 21, 31, 41, 51, 61, 71, 81, 91, 101};
      byte[] digest = new byte[]{12, 22, 32, 42, 52, 62, 72, 82, 92, 102};
      Signature signature = new Signature(genCertificate(genKeyPair()), content, sign, digest, true);

      XmlSignatureInjector injector = new XmlSignatureInjectorImp();
      injector.injectOvToSoapHeader(soapMessage, signature);

      validateSignatureInHeader(soapMessage);

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      soapMessage.writeTo(bos);

      clientRequest.requestMessage = bos.toByteArray();

      testServer.setResponseBody("mvvact/putData/response.xml");
      ClientResponse response = clientProtocol.send(universalClient.getWsdlUrl(), clientRequest, null);
      assertTrue("PNZR01581".equals(response.packet.recipient.code));
    } finally {
      testServer.stop();
    }
  }

  private void validateSignatureInHeader(SOAPMessage header) throws XPathExpressionException, IOException, SOAPException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    header.writeTo(os);
    assertTrue("Отсутствует блок 'Signature.'", os.toString().contains("<ds:Signature "));
  }

  private ClientProtocol createClientProtocol() {
    ServiceDefinitionParser parser = new ServiceDefinitionParser();
    DummyProvider cryptoProvider = new DummyProvider();
    XmlNormalizer xmlNormalizer = new XmlNormalizerImpl();
    XmlSignatureInjector injector = new XmlSignatureInjectorImp();
    return new ClientRev120315(parser, cryptoProvider, xmlNormalizer, injector);
  }

  private ClientRequest createRequest(String port, Client client, ExchangeContext ctx) {
    ClientRequest request = client.createClientRequest(ctx);
    request.portAddress = port;
    request.packet.sender = request.packet.originator = new InfoSystem("test", "test");
    return request;
  }

  private X509Certificate genCertificate(KeyPair pair) throws NoSuchAlgorithmException, CertificateEncodingException, NoSuchProviderException, InvalidKeyException, SignatureException {
    Date startDate = new Date();
    Date expiryDate = new Date(startDate.getTime() + 10000);
    BigInteger serialNumber = new BigInteger("123456789");
    X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
    X500Principal dnName = new X500Principal("CN=Test CA Certificate");
    certGen.setSerialNumber(serialNumber);
    certGen.setIssuerDN(dnName);
    certGen.setNotBefore(startDate);
    certGen.setNotAfter(expiryDate);
    certGen.setSubjectDN(dnName);
    certGen.setPublicKey(pair.getPublic());
    certGen.setSignatureAlgorithm("GOST3411withECGOST3410");
    return certGen.generate(pair.getPrivate(), "BC");
  }

  private KeyPair genKeyPair() throws InvalidAlgorithmParameterException, NoSuchProviderException, NoSuchAlgorithmException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECGOST3410", "BC");
    keyGen.initialize(new ECGenParameterSpec("GostR3410-2001-CryptoPro-A"));
    keyGen.initialize(new ECGenParameterSpec("GostR3410-2001-CryptoPro-A"));
    return keyGen.generateKeyPair();
  }
}

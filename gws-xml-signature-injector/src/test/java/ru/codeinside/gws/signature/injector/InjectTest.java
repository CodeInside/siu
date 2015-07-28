/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.signature.injector;

import junit.framework.Assert;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.junit.Before;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.Signature;
import ru.codeinside.gws.api.WrappedAppData;
import ru.codeinside.gws.api.XmlSignatureInjector;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;

import javax.security.auth.x500.X500Principal;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

public class InjectTest extends Assert {
  @Before
  public void setSecurityProvider() {
    Security.addProvider(new BouncyCastleProvider());
  }

  @Test
  public void inject_sp_first() throws Exception {
    XmlSignatureInjector impl = new XmlSignatureInjectorImp();

    ClientRequest request = new ClientRequest();
    request.appData = "<rev:createRequestBean xmlns:rev=\"http://smev.gosuslugi.ru/rev110801\">" +
        "<Hello></Hello><rev:okato>11111111111</rev:okato><rev:requestType>558102100000</rev:requestType>" +
        "</rev:createRequestBean>";
    request.signRequired = true;
    byte[] signedInfoBytes = impl.prepareAppData(request, false, new XmlNormalizerImpl(), new CryptoProvider());
    assertTrue(signedInfoBytes.length > 0);

    X509Certificate certificate = genCertificate(genKeyPair());
    byte[] content = request.appData.getBytes("UTF-8");
    byte[] sign = {2, 3, 4, 5, 6, 7, 8};
    byte[] digest = {1, 2, 3, 4, 5, 6, 7};

    Signature signature = new Signature(certificate, content, sign, digest, true);
    String injected = impl.injectSpToAppData(new WrappedAppData(request.appData, signature));

    assertTrue(injected.startsWith("<AppData Id=\"AppData\">" +
        "<ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">"));
    assertFalse(injected.endsWith("</ds:Signature></ns:AppData>"));
    assertTrue("Is X509Certificate section present", injected.contains("X509Certificate"));
  }

  @Test
  public void inject_sp_last() throws Exception {
    XmlSignatureInjectorImp impl = new XmlSignatureInjectorImp();

    ClientRequest request = new ClientRequest();
    request.appData = "<rev:createRequestBean xmlns:rev=\"http://smev.gosuslugi.ru/rev110801\">" +
        "<Hello></Hello><rev:okato>11111111111</rev:okato><rev:requestType>558102100000</rev:requestType>" +
        "</rev:createRequestBean>";
    request.signRequired = true;

    X509Certificate certificate = genCertificate(genKeyPair());
    byte[] signedInfoBytes = impl.prepareAppData(request, true, new XmlNormalizerImpl(), new CryptoProvider());
    assertTrue(signedInfoBytes.length > 0);

    byte[] content = request.appData.getBytes("UTF-8");
    byte[] sign = {2, 3, 4, 5, 6, 7, 8};
    byte[] digest = {1, 2, 3, 4, 5, 6, 7};

    Signature signature = new Signature(certificate, content, sign, digest, true);
    String injected = impl.injectSpToAppData(new WrappedAppData(request.appData, signature));

    assertFalse(injected.startsWith("<AppData Id=\"AppData\">" +
        "<ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">"));
    assertTrue(injected.endsWith("</ds:Signature></AppData>"));
    assertTrue("Is X509Certificate section present", injected.contains("X509Certificate"));
  }

  @Test
  public void inject_ov_test() throws Exception {
    byte[] content = getPreparedResult().getBytes("UTF-8");
    ByteArrayInputStream stream = new ByteArrayInputStream(content);
    final SOAPMessage message = MessageFactory.newInstance().createMessage(null, stream);

    X509Certificate certificate = genCertificate(genKeyPair());

    byte[] sign = {2, 3, 4, 5, 6, 7, 8};
    byte[] digest = {1, 2, 3, 4, 5, 6, 7};
    Signature signature = new Signature(certificate, content, sign, digest, true);

    XmlSignatureInjectorImp impl = new XmlSignatureInjectorImp();
    impl.injectOvToSoapHeader(message, signature);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    message.writeTo(out);
    String result = out.toString("UTF-8");

    assertTrue(result.contains(getResult()[0]));
    assertTrue(result.contains(getResult()[1]));
  }


  @Test
  public void prepare_soapMessage_test() throws Exception {
    byte[] source = getSource().getBytes("UTF-8");
    ByteArrayInputStream stream = new ByteArrayInputStream(source);
    final SOAPMessage message = MessageFactory.newInstance().createMessage(null, stream);

    byte[] digest = {1, 2, 3, 4, 5, 6, 7};

    XmlSignatureInjectorImp impl = new XmlSignatureInjectorImp();
    impl.prepareSoapMessage(message, digest);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    message.writeTo(out);
    String result = out.toString("UTF-8");

    assertTrue(result.contains(getPreparedResult()));
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

  private String getSource() {
    return "<SOAP-ENV:Envelope " +
        "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
        "xmlns:smev=\"http://smev.gosuslugi.ru/rev120315\" " +
        "xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" +
        "<SOAP-ENV:Header/>" +
        "<SOAP-ENV:Body wsu:Id=\"body\">" +
        "<SOAP-WS:putData xmlns:SOAP-WS=\"http://smev.gosuslugi.ru/rev120315\">" +
        "<smev:Message>" +
        "<smev:Sender>" +
        "<smev:Code>PNZR01581</smev:Code><smev:Name>Комплексная система предоставления государственных и муниципальных услуг Пензенской области</smev:Name>" +
        "</smev:Sender>" +
        "<smev:Recipient>" +
        "<smev:Code>8201</smev:Code><smev:Name>Комплексная система предоставления государственных и муниципальных услуг Пензенской области</smev:Name>" +
        "</smev:Recipient>" +
        "<smev:Originator>" +
        "<smev:Code>PNZR01581</smev:Code><smev:Name>Комплексная система предоставления государственных и муниципальных услуг Пензенской области</smev:Name>" +
        "</smev:Originator>" +
        "<smev:ServiceName>PENZUniversalMVV</smev:ServiceName>" +
        "<smev:TypeCode>GSRV</smev:TypeCode>" +
        "<smev:Status>REQUEST</smev:Status>" +
        "<smev:Date>2015-06-26T00:38:59.798+03:00</smev:Date>" +
        "<smev:ExchangeType>1</smev:ExchangeType></smev:Message>" +
        "<smev:MessageData><smev:AppData>" +
        "<oep:result xmlns:oep=\"http://oep-penza.ru/com/oep\">" +
        "<oep:dataRow><oep:name>appData_addressRegister</oep:name><oep:value>г. Пенза ул. Попова 36</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>appData_phone</oep:name><oep:value>+79023456789</oep:value></oep:dataRow><oep:dataRow>" +
        "<oep:name>appData_birthDay</oep:name><oep:value>12/03/1986</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>appData_FIO</oep:name><oep:value>Иванов Иван Иванович</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>appData_toOrganizationName</oep:name><oep:value>Оператор электронного правительства Пензы</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>procedureCode</oep:name><oep:value>123</oep:value></oep:dataRow>" +
        "<oep:params><oep:app_id>0</oep:app_id><oep:status_date>2015-06-26T00:38:59.798+03:00</oep:status_date></oep:params>" +
        "</oep:result></smev:AppData></smev:MessageData></SOAP-WS:putData></SOAP-ENV:Body></SOAP-ENV:Envelope>";
  }

  //Добавляются блоки BinarySecurityToken и SignatureValue
  private String[] getResult() {
    String firstPart = "<SOAP-ENV:Envelope " +
        "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
        "xmlns:smev=\"http://smev.gosuslugi.ru/rev120315\" " +
        "xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" +
        "<SOAP-ENV:Header>" +
        "<wsse:Security " +
        "xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" " +
        "SOAP-ENV:actor=\"http://smev.gosuslugi.ru/actors/smev\">" +
        "<wsse:BinarySecurityToken " +
        "EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" " +
        "ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" " +
        "wsu:Id=\"CertId\">";

    String secondPart = "</wsse:BinarySecurityToken>" +
        "<ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">" +
        "<ds:SignedInfo><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>" +
        "<ds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411\"/>" +
        "<ds:Reference URI=\"#body\"><ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/></ds:Transforms>" +
        "<ds:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr3411\"/><ds:DigestValue>AQIDBAUGBw==</ds:DigestValue>" +
        "</ds:Reference></ds:SignedInfo>" +
        "<ds:SignatureValue>AgMEBQYHCA==</ds:SignatureValue>" +
        "<KeyInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
        "<SecurityTokenReference xmlns=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
        "<Reference URI=\"#CertId\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\"/>" +
        "</SecurityTokenReference></KeyInfo></ds:Signature></wsse:Security></SOAP-ENV:Header>" +
        "<SOAP-ENV:Body wsu:Id=\"body\"><SOAP-WS:putData xmlns:SOAP-WS=\"http://smev.gosuslugi.ru/rev120315\">" +
        "<smev:Message>" +
        "<smev:Sender>" +
        "<smev:Code>PNZR01581</smev:Code><smev:Name>Комплексная система предоставления государственных и муниципальных услуг Пензенской области</smev:Name>" +
        "</smev:Sender" +
        "><smev:Recipient>" +
        "<smev:Code>8201</smev:Code><smev:Name>Комплексная система предоставления государственных и муниципальных услуг Пензенской области</smev:Name>" +
        "</smev:Recipient>" +
        "<smev:Originator>" +
        "<smev:Code>PNZR01581</smev:Code><smev:Name>Комплексная система предоставления государственных и муниципальных услуг Пензенской области</smev:Name>" +
        "</smev:Originator>" +
        "<smev:ServiceName>PENZUniversalMVV</smev:ServiceName><smev:TypeCode>GSRV</smev:TypeCode><smev:Status>REQUEST</smev:Status>" +
        "<smev:Date>2015-06-26T00:38:59.798+03:00</smev:Date><smev:ExchangeType>1</smev:ExchangeType></smev:Message>" +
        "<smev:MessageData><smev:AppData><oep:result xmlns:oep=\"http://oep-penza.ru/com/oep\">" +
        "<oep:dataRow><oep:name>appData_addressRegister</oep:name><oep:value>г. Пенза ул. Попова 36</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>appData_phone</oep:name><oep:value>+79023456789</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>appData_birthDay</oep:name><oep:value>12/03/1986</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>appData_FIO</oep:name><oep:value>Иванов Иван Иванович</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>appData_toOrganizationName</oep:name><oep:value>Оператор электронного правительства Пензы</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>procedureCode</oep:name><oep:value>123</oep:value></oep:dataRow><oep:params><oep:app_id>0</oep:app_id>" +
        "<oep:status_date>2015-06-26T00:38:59.798+03:00</oep:status_date></oep:params></oep:result>" +
        "</smev:AppData></smev:MessageData></SOAP-WS:putData></SOAP-ENV:Body></SOAP-ENV:Envelope>";

    String[] result = {firstPart, secondPart};

    return result;
  }

  //добавляется блок Security
  private String getPreparedResult() {
    return "<SOAP-ENV:Envelope " +
        "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
        "xmlns:smev=\"http://smev.gosuslugi.ru/rev120315\" " +
        "xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" +
        "<SOAP-ENV:Header>" +
        "<wsse:Security " +
        "xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" " +
        "SOAP-ENV:actor=\"http://smev.gosuslugi.ru/actors/smev\">" +
        "<ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">" +
        "<ds:SignedInfo>" +
        "<ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>" +
        "<ds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411\"/>" +
        "<ds:Reference URI=\"#body\"><ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/></ds:Transforms>" +
        "<ds:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr3411\"/>" +
        "<ds:DigestValue>AQIDBAUGBw==</ds:DigestValue></ds:Reference></ds:SignedInfo>" +
        "<KeyInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
        "<SecurityTokenReference xmlns=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
        "<Reference URI=\"#CertId\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\"/>" +
        "</SecurityTokenReference></KeyInfo></ds:Signature></wsse:Security></SOAP-ENV:Header>" +
        "<SOAP-ENV:Body wsu:Id=\"body\">" +
        "<SOAP-WS:putData xmlns:SOAP-WS=\"http://smev.gosuslugi.ru/rev120315\">" +
        "<smev:Message>" +
        "<smev:Sender>" +
        "<smev:Code>PNZR01581</smev:Code><smev:Name>Комплексная система предоставления государственных и муниципальных услуг Пензенской области</smev:Name>" +
        "</smev:Sender>" +
        "<smev:Recipient>" +
        "<smev:Code>8201</smev:Code><smev:Name>Комплексная система предоставления государственных и муниципальных услуг Пензенской области</smev:Name>" +
        "</smev:Recipient>" +
        "<smev:Originator>" +
        "<smev:Code>PNZR01581</smev:Code><smev:Name>Комплексная система предоставления государственных и муниципальных услуг Пензенской области</smev:Name>" +
        "</smev:Originator>" +
        "<smev:ServiceName>PENZUniversalMVV</smev:ServiceName>" +
        "<smev:TypeCode>GSRV</smev:TypeCode><smev:Status>REQUEST</smev:Status><smev:Date>2015-06-26T00:38:59.798+03:00</smev:Date>" +
        "<smev:ExchangeType>1</smev:ExchangeType></smev:Message>" +
        "<smev:MessageData><smev:AppData><oep:result xmlns:oep=\"http://oep-penza.ru/com/oep\">" +
        "<oep:dataRow><oep:name>appData_addressRegister</oep:name><oep:value>г. Пенза ул. Попова 36</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>appData_phone</oep:name><oep:value>+79023456789</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>appData_birthDay</oep:name><oep:value>12/03/1986</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>appData_FIO</oep:name><oep:value>Иванов Иван Иванович</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>appData_toOrganizationName</oep:name><oep:value>Оператор электронного правительства Пензы</oep:value></oep:dataRow>" +
        "<oep:dataRow><oep:name>procedureCode</oep:name><oep:value>123</oep:value></oep:dataRow>" +
        "<oep:params><oep:app_id>0</oep:app_id><oep:status_date>2015-06-26T00:38:59.798+03:00</oep:status_date></oep:params></oep:result>" +
        "</smev:AppData></smev:MessageData></SOAP-WS:putData></SOAP-ENV:Body></SOAP-ENV:Envelope>";
  }
}

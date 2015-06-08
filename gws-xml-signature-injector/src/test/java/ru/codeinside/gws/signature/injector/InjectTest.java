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
import ru.codeinside.gws.api.Signature;
import ru.codeinside.gws.api.WrappedAppData;

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
    public void test_inject() throws Exception {
        XmlSignatureInjectorImp impl = new XmlSignatureInjectorImp();

        String sourceXML = "<rev:createRequestBean xmlns:rev=\"http://smev.gosuslugi.ru/rev110801\">" +
                "<Hello></Hello><rev:okato>11111111111</rev:okato><rev:requestType>558102100000</rev:requestType>" +
                "</rev:createRequestBean>";
        X509Certificate certificate = genCertificate(genKeyPair());

        byte[] content = sourceXML.getBytes("UTF-8");
        byte[] sign = {2, 3, 4, 5, 6, 7, 8};
        byte[] digest = {1, 2, 3, 4, 5, 6, 7};
        Signature signature = new Signature(certificate, content, sign, digest, true);
        String wrapped = "<ns:AppData Id=\"MegaID\" xmlns:ns=\"http://smev.gosuslugi.ru/rev110801\">" + sourceXML + "</ns:AppData>";
        String injected = impl.injectSpToAppData(new WrappedAppData(wrapped, signature));

        assertTrue(injected.startsWith("<ns:AppData xmlns:ns=\"http://smev.gosuslugi.ru/rev110801\" Id=\"MegaID\"><ds:Signature "));
    }

    @Test
    public void inject_ov_test() throws Exception {
        byte[] source = getSource().getBytes("UTF-8");
        ByteArrayInputStream stream = new ByteArrayInputStream(source);
        final SOAPMessage message = MessageFactory.newInstance().createMessage(null, stream);

        X509Certificate certificate = genCertificate(genKeyPair());

        byte[] content = source;
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
        assertTrue(result.contains(getResult()[2]));
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
        return  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:smev=\"http://smev.gosuslugi.ru/rev120315\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
            "\n" +
            "<soapenv:Header>\n" +
            "</soapenv:Header>\n" +
            "\n" +
            "<soapenv:Body wsu:Id=\"sampleRequest\">\n" +
            "\t<smevSampleMsg:sampleRequest xmlns:smevSampleMsg=\"http://smev.gosuslugi.ru/SampleMessage\">\t\t\n" +
            "\t\t<smev:Message>\n" +
            "\t\t\t<smev:Sender/>\n" +
            "\t\t\t<smev:Recipient/>\n" +
            "\t\t\t<smev:Originator/>\n" +
            "\t\t\t<smev:TypeCode/>\n" +
            "\t\t\t<smev:Status/>\n" +
            "\t\t\t<smev:Date/>\n" +
            "                        <smev:ServiceCode/>\n" +
            "                        <smev:CaseNumber/>\n" +
            "\t\t\t<smev:ExchangeType/>\n" +
            "\t\t\t<smev:RequestIdRef/>\n" +
            "\t\t\t<smev:OriginRequestIdRef/>\n" +
            "\t\t</smev:Message>\n" +
            "\t\t<smev:MessageData>\n" +
            "\t\t\t<smev:AppData/>\t\t\t\n" +
            "\t\t\t<smev:AppDocument/>\n" +
            "\t\t</smev:MessageData>\n" +
            "\t</smevSampleMsg:sampleRequest>\n" +
            "</soapenv:Body>\n" +
            "</soapenv:Envelope>";
    }

    private String[] getResult() {
        String firstPart = "<soapenv:Header>\n" +
            "<wsse:Security soapenv:actor=\"http://smev.gosuslugi.ru/actors/smev\">" +
            "<wsse:BinarySecurityToken " +
            "EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" " +
            "ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" " +
            "wsu:Id=\"CertId\">";

        String secondPart = "</wsse:BinarySecurityToken>" +
            "<ds:Signature>" +
            "<ds:SignedInfo>" +
            "<ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>" +
            "<ds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411\"/>" +
            "<ds:Reference URI=\"#sampleRequest\">" +
            "<ds:Transforms>" +
            "<ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>" +
            "</ds:Transforms>" +
            "<ds:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#gostr3411\"/>" +
            "<ds:DigestValue>AQIDBAUGBw==</ds:DigestValue>" +
            "</ds:Reference>" +
            "</ds:SignedInfo>" +
            "<ds:SignatureValue>AgMEBQYHCA==</ds:SignatureValue>";

        String thirdPart = "<KeyInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
            "<SecurityTokenReference xmlns=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
            "<Reference URI=\"#CertId\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\"/>" +
            "</SecurityTokenReference>" +
            "</KeyInfo>" +
            "</ds:Signature>" +
            "</wsse:Security>" +
            "</soapenv:Header>";

        String[] result = {firstPart, secondPart, thirdPart};

        return result;
    }
}

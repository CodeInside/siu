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
import org.junit.Test;
import ru.codeinside.gws.api.AppData;
import ru.codeinside.gws.api.Signature;
import ru.codeinside.gws.api.WrappedAppData;

import javax.security.auth.x500.X500Principal;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.util.Date;

public class InjectTest extends Assert {
    @Test
    public void test_inject() throws UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CertificateEncodingException, InvalidKeyException, SignatureException {
        Security.addProvider(new BouncyCastleProvider());
        XmlSignatureInjectorImp impl = new XmlSignatureInjectorImp();

        String sourceXML = "<rev:createRequestBean xmlns:rev=\"http://smev.gosuslugi.ru/rev110801\">" +
                "<Hello></Hello><rev:okato>11111111111</rev:okato><rev:requestType>558102100000</rev:requestType>" +
                "</rev:createRequestBean>";
        X509Certificate certificate = genCertificate(genKeyPair());

        AppData appData = new AppData(sourceXML.getBytes("UTF8"), new byte[]{1, 2, 3, 4, 5, 6, 7});
        Signature signature = new Signature(certificate, sourceXML.getBytes("UTF8"), new byte[]{2,3,4,5,6,7,8}, true);
        String wrapped = "<AppData Id=\"MegaID\">" + sourceXML + "</AppData>";
        String injected = impl.inject(new WrappedAppData(wrapped, appData, signature));

        assertTrue(injected.startsWith("<AppData Id=\"MegaID\"><Signature "));
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

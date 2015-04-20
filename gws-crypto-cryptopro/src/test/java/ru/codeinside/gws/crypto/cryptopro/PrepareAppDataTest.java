package ru.codeinside.gws.crypto.cryptopro;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.AppData;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class PrepareAppDataTest extends Assert {
    @Test
    public void test_prepareData() throws IOException, NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        String sourceXML = "<rev:createRequestBean xmlns:rev=\"http://smev.gosuslugi.ru/rev110801\"\n" +
                "      ><Hello/><rev:okato>11111111111</rev:okato\n" +
                "      ><rev:requestType>558102100000</rev:requestType\n" +
                "></rev:createRequestBean>";
        CryptoProvider cryptoProvider = new CryptoProvider();
        AppData appData = cryptoProvider.prepareAppDataToSign(sourceXML);
        String expected = "<rev:createRequestBean xmlns:rev=\"http://smev.gosuslugi.ru/rev110801\">" +
                "<Hello></Hello><rev:okato>11111111111</rev:okato><rev:requestType>558102100000</rev:requestType>" +
                "</rev:createRequestBean>";
        assertEquals(expected, new String(appData.content));

        MessageDigest md = MessageDigest.getInstance("GOST3411");
        md.update(expected.getBytes("UTF8"));
        assertArrayEquals(md.digest(), appData.digest);
    }
}

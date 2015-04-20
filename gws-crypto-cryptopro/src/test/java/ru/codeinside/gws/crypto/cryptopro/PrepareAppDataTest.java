package ru.codeinside.gws.crypto.cryptopro;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class PrepareAppDataTest extends Assert {
    @Test
    public void test_prepareData() throws IOException {
        String sourceXML = "<rev:createRequestBean xmlns:rev=\"http://smev.gosuslugi.ru/rev110801\"\n" +
                "      ><Hello/><rev:okato>11111111111</rev:okato\n" +
                "      ><rev:requestType>558102100000</rev:requestType\n" +
                "></rev:createRequestBean>";
        CryptoProvider cryptoProvider = new CryptoProvider();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        cryptoProvider.prepareAppDataToSign(sourceXML, os);
        String expected = "<rev:createRequestBean xmlns:rev=\"http://smev.gosuslugi.ru/rev110801\">" +
                "<Hello></Hello><rev:okato>11111111111</rev:okato><rev:requestType>558102100000</rev:requestType>" +
                "</rev:createRequestBean>";
        assertEquals(expected, new String(os.toByteArray()));
    }
}

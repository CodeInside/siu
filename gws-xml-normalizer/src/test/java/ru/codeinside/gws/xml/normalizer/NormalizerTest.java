package ru.codeinside.gws.xml.normalizer;

import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.XmlNormalizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class NormalizerTest extends Assert {
    @Test
    public void test_normalizer() throws IOException, NoSuchAlgorithmException {
        String sourceXML = "<rev:createRequestBean xmlns:rev=\"http://smev.gosuslugi.ru/rev110801\"\n" +
                "      ><Hello/><rev:okato>11111111111</rev:okato\n" +
                "      ><rev:requestType>558102100000</rev:requestType\n" +
                "></rev:createRequestBean>";
        XmlNormalizer normalizer = new XmlNormalizerImpl();
        ByteArrayInputStream is = new ByteArrayInputStream(sourceXML.getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        normalizer.normalize(is, os);
        String expected = "<rev:createRequestBean xmlns:rev=\"http://smev.gosuslugi.ru/rev110801\">" +
                "<Hello></Hello><rev:okato>11111111111</rev:okato><rev:requestType>558102100000</rev:requestType>" +
                "</rev:createRequestBean>";
        assertEquals(expected, os.toString());
    }
}

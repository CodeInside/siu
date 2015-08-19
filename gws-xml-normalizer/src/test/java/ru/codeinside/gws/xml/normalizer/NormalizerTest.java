package ru.codeinside.gws.xml.normalizer;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ru.codeinside.gws.api.XmlNormalizer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class NormalizerTest extends Assert {
    @Test
    public void test_normalizerIs() throws IOException, NoSuchAlgorithmException {
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

    @Test
    public void test_normalizerEl() throws IOException, NoSuchAlgorithmException, ParserConfigurationException, SAXException {
        String sourceXML = "<rev:createRequestBean xmlns:rev=\"http://smev.gosuslugi.ru/rev110801\"\n" +
                "      ><Hello/><rev:okato>11111111111</rev:okato\n" +
                "      ><rev:requestType>558102100000</rev:requestType\n" +
                "></rev:createRequestBean>";
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        ByteArrayInputStream is = new ByteArrayInputStream(sourceXML.getBytes());
        Document document = documentBuilderFactory.newDocumentBuilder().parse(is);
        XmlNormalizer normalizer = new XmlNormalizerImpl();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        normalizer.normalize(document.getDocumentElement(), os);
        String expected = "<rev:createRequestBean xmlns:rev=\"http://smev.gosuslugi.ru/rev110801\">" +
                "<Hello></Hello><rev:okato>11111111111</rev:okato><rev:requestType>558102100000</rev:requestType>" +
                "</rev:createRequestBean>";
        assertEquals(expected, os.toString());
    }
}

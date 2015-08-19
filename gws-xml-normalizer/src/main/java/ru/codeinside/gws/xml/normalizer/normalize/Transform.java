package ru.codeinside.gws.xml.normalizer.normalize;

import org.xml.sax.SAXException;
import ru.codeinside.gws.xml.normalizer.normalize.exceptions.CanonicalizationException;
import ru.codeinside.gws.xml.normalizer.normalize.exceptions.TransformationException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.OutputStream;

public class Transform {
    public static XMLSignatureInput performTransform(XMLSignatureInput xmlSignatureInput, OutputStream result)
            throws ParserConfigurationException, IOException, SAXException, CanonicalizationException, TransformationException {
        xmlSignatureInput = TransformC14Exclusive.performTransform(xmlSignatureInput, result);
        return xmlSignatureInput;
    }
}

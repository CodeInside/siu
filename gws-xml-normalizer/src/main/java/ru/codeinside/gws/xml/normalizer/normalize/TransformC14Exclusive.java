package ru.codeinside.gws.xml.normalizer.normalize;


import ru.codeinside.gws.xml.normalizer.normalize.exceptions.CanonicalizationException;
import ru.codeinside.gws.xml.normalizer.normalize.implementations.Canonicalizer20010315ExclOmitComments;

import java.io.OutputStream;

public class TransformC14Exclusive {
    public static XMLSignatureInput performTransform(XMLSignatureInput input, OutputStream os) throws CanonicalizationException {
        Canonicalizer20010315ExclOmitComments c14n =
                new Canonicalizer20010315ExclOmitComments();
        if (os != null) {
            c14n.setWriter(os);
        }
        byte[] result = c14n.engineCanonicalize(input, null);
        XMLSignatureInput output = new XMLSignatureInput(result);
        if (os != null) {
            output.setOutputStream(os);
        }
        return output;
    }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.signature.injector;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.codeinside.gws.api.Signature;
import ru.codeinside.gws.api.WrappedAppData;
import ru.codeinside.gws.api.XmlSignatureInjector;
import ru.codeinside.gws.api.XmlTypes;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Logger;

public final class XmlSignatureInjectorImp implements XmlSignatureInjector {
    private static final String APP_DATA = "AppData";

    final Logger log = Logger.getLogger(XmlSignatureInjector.class.getName());

    @Override
    public String inject(WrappedAppData wrappedAppData) {
        DocumentBuilder documentBuilder = getDocumentBuilder();
        Document document = parseData(wrappedAppData.getWrappedAppData(), documentBuilder);

        validateAppData(document);

        Element signatureElement = assembleSignature(wrappedAppData.getSignature(),
                wrappedAppData.getAppData().digest, getIdAttr(document).getValue());
        insertSignature(document, signatureElement);
        return contentToString(document);
    }

    private Attr getIdAttr(Document document) {
        Attr attrId = document.getDocumentElement().getAttributeNode("Id");
        if (attrId == null || attrId.getValue().isEmpty()) {
            throw new IllegalStateException("AppData must have 'Id' attribute");
        }
        return attrId;
    }

    private void validateAppData(Document document) {
        String tagName = document.getDocumentElement().getTagName();
        if (!APP_DATA.equals(tagName)) {
            throw new IllegalStateException("Expected 'AppData' tag, but was: " + tagName);
        }
    }

    private String contentToString(Document document) {
        StreamResult result = new StreamResult(new StringWriter());
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(document), result);
            return result.getWriter().toString();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertSignature(Document document, Element signatureElement) {
        Node imported = document.importNode(signatureElement, true);
        Element documentElement = document.getDocumentElement();
        documentElement.insertBefore(imported, documentElement.getFirstChild());
    }

    private Element assembleSignature(Signature signature, byte[] digest, String id) {
        XMLDSign xmldSign = new XMLDSign(signature, digest, id);
        xmldSign.setEnveloped(true);
        return XmlTypes.beanToElement(xmldSign);
    }

    private Document parseData(String source, DocumentBuilder documentBuilder) {
        InputSource is = new InputSource(new StringReader(source));
        try {
            return documentBuilder.parse(is);
        } catch (SAXException e) {
            log.severe("Unable to parse appData to Document: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.severe("IOException when parsing appData: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private DocumentBuilder getDocumentBuilder() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setCoalescing(true);
        try {
            return documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.severe("Unable to get DocumentBuilder: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

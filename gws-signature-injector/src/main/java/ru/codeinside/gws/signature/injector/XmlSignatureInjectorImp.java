/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.signature.injector;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.codeinside.gws.api.AppData;
import ru.codeinside.gws.api.Signature;
import ru.codeinside.gws.api.XmlSignatureInjector;
import ru.codeinside.gws.api.XmlTypes;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

public final class XmlSignatureInjectorImp implements XmlSignatureInjector {
    final Logger log = Logger.getLogger(XmlSignatureInjector.class.getName());

    /**
     * Встроить подпись
     *
     * @param appData содержимое тега AppData
     * @param signature подпись
     */
    public String inject(AppData appData, Signature signature) {
        DocumentBuilder documentBuilder = getDocumentBuilder();
        String wrapped = "<dummy>" + appData + "</dummy>";
        Document document = parseData(wrapped, documentBuilder);
        Element signatureElement = assembleSignature(signature, appData.digest);
        insertSignature(document, signatureElement);
        return contentToString(document);
    }

    private String contentToString(Document document) {
        NodeList list = document.getChildNodes();
        String result = "";
        for (int i = 0; i < list.getLength(); ++i) {
            Node node = list.item(i);
            result += XmlTypes.beanToXml(node);
        }
        return result;
    }

    private void insertSignature(Document document, Element signatureElement) {
        Node firstChild = document.getFirstChild();
        document.insertBefore(signatureElement, firstChild);
    }

    private Element assembleSignature(Signature signature, byte[] digest) {
        XMLDSign xmldSign = new XMLDSign(signature, digest, "AppData");
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

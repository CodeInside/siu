/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.test;

import org.jboss.shrinkwrap.api.asset.Asset;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

final class PersistanceAsset implements Asset {
    @Override
    public InputStream openStream() {
        byte[] content = null;
        try {
            final InputStream is = getClass().getClassLoader().getResourceAsStream("META-INF/persistence.xml");
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = f.newDocumentBuilder();
            Document doc = builder.parse(is);
            NodeList units = doc.getElementsByTagName("persistence-unit");
            for (int i = 0; i < units.getLength(); i++) {
                final Element unit = (Element) units.item(i);
                final NodeList propertiesList = unit.getElementsByTagName("properties");
                for (int j = 0; j < propertiesList.getLength(); j++) {
                    final Element properties = (Element) propertiesList.item(j);
                    Element property = doc.createElement("property");
                    property.setAttribute("name", "eclipselink.ddl-generation");
                    property.setAttribute("value", "drop-and-create-tables");
                    properties.appendChild(property);
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(baos);
            transformer.transform(source, result);
            content = baos.toByteArray();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream(content);
    }
}

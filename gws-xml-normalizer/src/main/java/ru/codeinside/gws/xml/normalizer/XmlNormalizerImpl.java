/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.xml.normalizer;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.xml.normalizer.normalize.Transform;
import ru.codeinside.gws.xml.normalizer.normalize.XMLSignatureInput;
import ru.codeinside.gws.xml.normalizer.normalize.exceptions.CanonicalizationException;
import ru.codeinside.gws.xml.normalizer.normalize.exceptions.TransformationException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class XmlNormalizerImpl implements XmlNormalizer {
    /**
     * @param source       данные для нормализации
     * @param outputStream исходящий поток результата
     */
    @Override
    public void normalize(InputStream source, OutputStream outputStream) {
        Element element = toElement(source);
        performTransform(element, outputStream);
    }

    /**
     * @param sourceElement элемент для нормализации
     * @param outputStream  исходящий поток результата
     */
    @Override
    public void normalize(Element sourceElement, OutputStream outputStream) {
        performTransform(sourceElement, outputStream);
    }

    private void performTransform(Element element, OutputStream outputStream) {
        XMLSignatureInput input = new XMLSignatureInput(element);
        try {
            Transform.performTransform(input, outputStream);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (CanonicalizationException e) {
            throw new RuntimeException(e);
        } catch (TransformationException e) {
            throw new RuntimeException(e);
        }
    }

    private Element toElement(InputStream source) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        try {
            return documentBuilderFactory.newDocumentBuilder().parse(source).getDocumentElement();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}

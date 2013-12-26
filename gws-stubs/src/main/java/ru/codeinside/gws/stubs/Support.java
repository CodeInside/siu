/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.stubs;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.core.Xml;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Support {

    final public static class PortDefinition {
        public QName service;
        public QName port;
        public ServiceDefinition.Port portDef;
    }

    public static PortDefinition getServicePort(String resource) {
        final URL wsdl = R.getRequiredURL(resource);
        final ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
        final ServiceDefinition def = definitionParser.parseServiceDefinition(wsdl);
        return Support.getPortDefinition(def, wsdl);
    }

    public static PortDefinition getPortDefinition(ServiceDefinition wsdl, URL wsdlUrl) {
        if (wsdl.services == null) {
            throw new IllegalArgumentException("Invalid wsdl " + wsdlUrl);
        }
        if (!wsdl.namespaces.contains(Xml.REV120315)) {
            throw new IllegalArgumentException("WSDL " + wsdlUrl + " not use " + Xml.REV120315);
        }

        final QName serviceName;
        final ServiceDefinition.Service serviceDef;
        final QName portName;
        final ServiceDefinition.Port port;

        {
            if (wsdl.services.size() != 1) {
                throw new IllegalArgumentException("Ambiguous service in " + wsdlUrl);
            }
            final Map.Entry<QName, ServiceDefinition.Service> entry = first(wsdl.services);
            serviceDef = entry.getValue();
            serviceName = entry.getKey();
        }

        {
            if (serviceDef.ports.size() != 1) {
                throw new IllegalArgumentException("Ambiguous port for service " + serviceName);
            }
            final Map.Entry<QName, ServiceDefinition.Port> entry = first(serviceDef.ports);
            portName = entry.getKey();
            port = entry.getValue();
        }

        final PortDefinition portDefinition = new PortDefinition();
        portDefinition.service = serviceName;
        portDefinition.port = portName;
        portDefinition.portDef = port;
        return portDefinition;
    }

    static <K, V> Map.Entry<K, V> first(final Map<K, V> map) {
        return map.entrySet().iterator().next();
    }

    private List<String> validateBySchema(final Document document, URL schemaUrl) {
        final long startMs = System.currentTimeMillis();
        final List<String> errors = new ArrayList<String>();
        try {
            final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            final Schema schema = schemaFactory.newSchema(schemaUrl);
            final Validator validator = schema.newValidator();
            validator.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    errors.add(exception.getMessage());
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    errors.add(exception.getMessage());
                }
            });
            validator.validate(new DOMSource(document));
        } catch (SAXException e) {
            errors.add(e.getMessage());
        } catch (IOException e) {
            errors.add(e.getMessage());
        } finally {
            System.out.println("VALIDATE: " + (System.currentTimeMillis() - startMs));
        }
        return errors;
    }

}

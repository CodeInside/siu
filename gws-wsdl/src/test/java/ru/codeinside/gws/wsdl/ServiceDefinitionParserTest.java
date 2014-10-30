/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.wsdl;

import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.ServiceDefinition;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.LogManager;

public class ServiceDefinitionParserTest extends Assert {

    static {
        final InputStream is = ServiceDefinitionParserTest.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            if (is != null) {
                LogManager.getLogManager().readConfiguration(is);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void parseFss() throws URISyntaxException {
        ServiceDefinitionParser parser = new ServiceDefinitionParser();
        ServiceDefinition definition = parser.parseServiceDefinition(r("gws3417/SvedRegisterNoPosob.wsdl"));
        assertEquals(11, definition.namespaces.size());
        assertEquals(1, definition.services.size());
        ServiceDefinition.Service service = definition.services.get(new QName("http://fss.ru/SvedRegisterNoPosob", "fss"));
        assertEquals(1, service.ports.size());
        ServiceDefinition.Port port = service.ports.get(new QName("http://fss.ru/SvedRegisterNoPosob", "SvedRegisterNoPosobPort"));
        assertEquals("http://smevtest.fss.ru/fss/SvedRegisterNoPosob", port.soapAddress);
        assertEquals(new QName("http://fss.ru/SvedRegisterNoPosob", "SvedRegisterNoPosobPortBinding"), port.binding);
        assertEquals(new QName("http://fss.ru/SvedRegisterNoPosob", "SvedRegisterNoPosob"), port.port);
        assertEquals(1, port.operations.size());
        ServiceDefinition.Operation request = port.operations.get(new QName("http://fss.ru/SvedRegisterNoPosob", "request"));
        assertEquals("", request.soapAction);
        ServiceDefinition.Arg in = request.in;
        assertNull(in.name);
        assertEquals(new QName("http://fss.ru/SvedRegisterNoPosob", "request"), in.message);
        assertEquals(1, in.parts.size());
        assertEquals(new QName("http://fss.ru/SvedRegisterNoPosob/request", "request"), in.parts.get("parameters"));

        ServiceDefinition.Arg out = request.out;
        assertEquals(new QName("http://fss.ru/SvedRegisterNoPosob", "requestResponse"), out.message);
        assertEquals(1, out.parts.size());
        assertEquals(new QName("http://fss.ru/SvedRegisterNoPosob/request", "requestResponse"), out.parts.get("parameters"));

        assertURI(
                definition.resources,
                "SvedRegisterNoPosob_1.xsd",
                "SvedRegisterNoPosob_2.xsd",
                "SvedRegisterNoPosob_3.xsd",
                "SvedRegisterNoPosob_4.xsd"
        );

    }

    @Test
    public void parseMvv() throws URISyntaxException {
        ServiceDefinitionParser parser = new ServiceDefinitionParser();
        ServiceDefinition definition = parser.parseServiceDefinition(r("gws3970/mvvact.wsdl"));
        assertEquals(7, definition.namespaces.size());
        assertEquals(1, definition.services.size());

        assertURI(
                definition.resources,
                "mvvact_schema1.xsd",
                "mvvact_schema2.xsd",
                "mvvact_schema3.xsd"
        );
    }

    @Test
    public void parseRosreestr() throws URISyntaxException {
        ServiceDefinitionParser parser = new ServiceDefinitionParser();
        ServiceDefinition definition = parser.parseServiceDefinition(r("rr/RR.wsdl"));
        assertEquals(7, definition.namespaces.size());
        assertEquals(1, definition.services.size());
        assertURI(
                definition.resources,
                "SmevTypes2.xsd", "xop-include.xsd", "Rosreestr2.xsd");

        ServiceDefinition.Service service = definition.services.get(new QName("http://portal.fccland.ru/rt/", "RosreestrService"));
        ServiceDefinition.Port port = service.ports.get(new QName("http://portal.fccland.ru/rt/", "RosreestrServicePort"));
        assertEquals(4, port.operations.size());
        ServiceDefinition.Operation createRequest = port.operations.get(new QName("http://portal.fccland.ru/rt/", "CreateRequest"));
        assertEquals("http://portal.rosreestr.ru/CreateRequest", createRequest.soapAction);
    }

    private URL r(String name) {
        final URL url = getClass().getClassLoader().getResource(name);
        assertNotNull(url);
        return url;
    }

    private void assertURI(final Set<URI> set, final String... urls) throws URISyntaxException {
        final Set<URI> expected = new LinkedHashSet<URI>();
        for (final String url : urls) {
            expected.add(new URI(url));
        }
        assertEquals(expected, set);
    }
}

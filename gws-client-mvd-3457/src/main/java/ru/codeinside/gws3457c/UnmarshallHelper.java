/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3457c;

import org.xml.sax.SAXException;
import ru.tower.mvd.response.addpayment.ResponseAdditionalPaymentRequest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

public class UnmarshallHelper {
    public static ResponseAdditionalPaymentRequest parseAdditionaPaymentResult(String xmlData) throws JAXBException, SAXException, UnsupportedEncodingException {
        InputStream regionCodeStream = UnmarshallHelper.class.getResourceAsStream("/gws3457/common/dictionaries.xsd");
        InputStream responseSchemaStream = UnmarshallHelper.class.getResourceAsStream("/gws3457/response/response_additional_payment.xsd");

        SchemaFactory schemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new StreamSource[]{ new StreamSource(regionCodeStream), new StreamSource(responseSchemaStream)});

        JAXBContext jc = JAXBContext.newInstance(ResponseAdditionalPaymentRequest.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        //unmarshaller.setSchema(schema);

        final ByteArrayInputStream xmlStream = new ByteArrayInputStream(xmlData.getBytes("cp1251"));
        return (ResponseAdditionalPaymentRequest) unmarshaller.unmarshal(xmlStream);
    }


}

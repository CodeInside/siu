
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.exportquittanceresponse;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.roskazna.xsd.exportquittanceresponse package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ExportQuittanceResponse_QNAME = new QName("http://roskazna.ru/xsd/ExportQuittanceResponse", "ExportQuittanceResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.roskazna.xsd.exportquittanceresponse
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExportQuittanceResponse }
     * 
     */
    public ExportQuittanceResponse createExportQuittanceResponse() {
        return new ExportQuittanceResponse();
    }

    /**
     * Create an instance of {@link ExportQuittanceResponse.Quittances }
     * 
     */
    public ExportQuittanceResponse.Quittances createExportQuittanceResponseQuittances() {
        return new ExportQuittanceResponse.Quittances();
    }

    /**
     * Create an instance of {@link ExportQuittanceResponse.Quittances.Quittance }
     * 
     */
    public ExportQuittanceResponse.Quittances.Quittance createExportQuittanceResponseQuittancesQuittance() {
        return new ExportQuittanceResponse.Quittances.Quittance();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExportQuittanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://roskazna.ru/xsd/ExportQuittanceResponse", name = "ExportQuittanceResponse")
    public JAXBElement<ExportQuittanceResponse> createExportQuittanceResponse(ExportQuittanceResponse value) {
        return new JAXBElement<ExportQuittanceResponse>(_ExportQuittanceResponse_QNAME, ExportQuittanceResponse.class, null, value);
    }

}

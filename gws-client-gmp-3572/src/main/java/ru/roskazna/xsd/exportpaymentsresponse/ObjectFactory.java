
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.exportpaymentsresponse;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.roskazna.xsd.exportpaymentsresponse package. 
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

    private final static QName _ExportPaymentsResponse_QNAME = new QName("http://roskazna.ru/xsd/ExportPaymentsResponse", "ExportPaymentsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.roskazna.xsd.exportpaymentsresponse
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExportPaymentsResponse }
     * 
     */
    public ExportPaymentsResponse createExportPaymentsResponse() {
        return new ExportPaymentsResponse();
    }

    /**
     * Create an instance of {@link ExportPaymentsResponse.Payments }
     * 
     */
    public ExportPaymentsResponse.Payments createExportPaymentsResponsePayments() {
        return new ExportPaymentsResponse.Payments();
    }

    /**
     * Create an instance of {@link ExportPaymentsResponse.Payments.PaymentInfo }
     * 
     */
    public ExportPaymentsResponse.Payments.PaymentInfo createExportPaymentsResponsePaymentsPaymentInfo() {
        return new ExportPaymentsResponse.Payments.PaymentInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExportPaymentsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://roskazna.ru/xsd/ExportPaymentsResponse", name = "ExportPaymentsResponse")
    public JAXBElement<ExportPaymentsResponse> createExportPaymentsResponse(ExportPaymentsResponse value) {
        return new JAXBElement<ExportPaymentsResponse>(_ExportPaymentsResponse_QNAME, ExportPaymentsResponse.class, null, value);
    }

}

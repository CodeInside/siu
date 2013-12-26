
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.paymentinfo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.roskazna.xsd.paymentinfo package. 
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

    private final static QName _FinalPayment_QNAME = new QName("http://roskazna.ru/xsd/PaymentInfo", "FinalPayment");
    private final static QName _Income_QNAME = new QName("http://roskazna.ru/xsd/PaymentInfo", "Income");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.roskazna.xsd.paymentinfo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PaymentType }
     * 
     */
    public PaymentType createPaymentType() {
        return new PaymentType();
    }

    /**
     * Create an instance of {@link IncomeInfoType }
     * 
     */
    public IncomeInfoType createIncomeInfoType() {
        return new IncomeInfoType();
    }

    /**
     * Create an instance of {@link PaymentInfoType }
     * 
     */
    public PaymentInfoType createPaymentInfoType() {
        return new PaymentInfoType();
    }

    /**
     * Create an instance of {@link PaymentIdentificationDataType }
     * 
     */
    public PaymentIdentificationDataType createPaymentIdentificationDataType() {
        return new PaymentIdentificationDataType();
    }

    /**
     * Create an instance of {@link PaymentType.AdditionalData }
     * 
     */
    public PaymentType.AdditionalData createPaymentTypeAdditionalData() {
        return new PaymentType.AdditionalData();
    }

    /**
     * Create an instance of {@link IncomeInfoType.IncomeRows }
     * 
     */
    public IncomeInfoType.IncomeRows createIncomeInfoTypeIncomeRows() {
        return new IncomeInfoType.IncomeRows();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaymentInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://roskazna.ru/xsd/PaymentInfo", name = "FinalPayment")
    public JAXBElement<PaymentInfoType> createFinalPayment(PaymentInfoType value) {
        return new JAXBElement<PaymentInfoType>(_FinalPayment_QNAME, PaymentInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IncomeInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://roskazna.ru/xsd/PaymentInfo", name = "Income")
    public JAXBElement<IncomeInfoType> createIncome(IncomeInfoType value) {
        return new JAXBElement<IncomeInfoType>(_Income_QNAME, IncomeInfoType.class, null, value);
    }

}

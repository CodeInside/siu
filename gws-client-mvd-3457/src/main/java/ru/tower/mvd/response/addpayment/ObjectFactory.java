/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */


package ru.tower.mvd.response.addpayment;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.tower.mvd.response.addpayment package. 
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

    private final static QName _ДатаРождения_QNAME = new QName("", "\u0414\u0430\u0442\u0430\u0420\u043e\u0436\u0434\u0435\u043d\u0438\u044f");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.tower.mvd.response.addpayment
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResponseAdditionalPaymentRequest.ВсеВыплаты }
     * 
     */
    public ResponseAdditionalPaymentRequest.ВсеВыплаты createОТВЕТНАЗАПРОССВЕДЕНИЙОПОЛУЧЕНИИПЕНСИИНАМЕСЯЦУСТАНОВЛЕНИЯДОПЛАТЫВсеВыплаты() {
        return new ResponseAdditionalPaymentRequest.ВсеВыплаты();
    }

    /**
     * Create an instance of {@link ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата }
     * 
     */
    public ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата createОТВЕТНАЗАПРОССВЕДЕНИЙОПОЛУЧЕНИИПЕНСИИНАМЕСЯЦУСТАНОВЛЕНИЯДОПЛАТЫВсеВыплатыВыплата() {
        return new ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата();
    }

    /**
     * Create an instance of {@link ResponseAdditionalPaymentRequest }
     * 
     */
    public ResponseAdditionalPaymentRequest createОТВЕТНАЗАПРОССВЕДЕНИЙОПОЛУЧЕНИИПЕНСИИНАМЕСЯЦУСТАНОВЛЕНИЯДОПЛАТЫ() {
        return new ResponseAdditionalPaymentRequest();
    }

    /**
     * Create an instance of {@link ResponseAdditionalPaymentRequest.ФИО }
     * 
     */
    public ResponseAdditionalPaymentRequest.ФИО createОТВЕТНАЗАПРОССВЕДЕНИЙОПОЛУЧЕНИИПЕНСИИНАМЕСЯЦУСТАНОВЛЕНИЯДОПЛАТЫФИО() {
        return new ResponseAdditionalPaymentRequest.ФИО();
    }

    /**
     * Create an instance of {@link ЛичныйДокумент }
     * 
     */
    public ЛичныйДокумент createЛичныйДокумент() {
        return new ЛичныйДокумент();
    }

    /**
     * Create an instance of {@link Подразделение }
     * 
     */
    public Подразделение createПодразделение() {
        return new Подразделение();
    }

    /**
     * Create an instance of {@link ПрекращениеВыплат }
     * 
     */
    public ПрекращениеВыплат createПрекращениеВыплат() {
        return new ПрекращениеВыплат();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "\u0414\u0430\u0442\u0430\u0420\u043e\u0436\u0434\u0435\u043d\u0438\u044f")
    public JAXBElement<String> createДатаРождения(String value) {
        return new JAXBElement<String>(_ДатаРождения_QNAME, String.class, null, value);
    }

}

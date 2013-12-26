
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.fccland.portal.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.fccland.portal.types package. 
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

    private final static QName _ErrorMessage_QNAME = new QName("http://portal.fccland.ru/types/", "errorMessage");
    private final static QName _EventID_QNAME = new QName("http://portal.fccland.ru/types/", "eventID");
    private final static QName _XML_QNAME = new QName("http://portal.fccland.ru/types/", "XML");
    private final static QName _RequestNumber_QNAME = new QName("http://portal.fccland.ru/types/", "requestNumber");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.fccland.portal.types
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetStatusRequestMessageType }
     * 
     */
    public GetStatusRequestMessageType createGetStatusRequestMessageType() {
        return new GetStatusRequestMessageType();
    }

    /**
     * Create an instance of {@link CreateRequestRequestMessageType }
     * 
     */
    public CreateRequestRequestMessageType createCreateRequestRequestMessageType() {
        return new CreateRequestRequestMessageType();
    }

    /**
     * Create an instance of {@link GetEventsRequestMessageType }
     * 
     */
    public GetEventsRequestMessageType createGetEventsRequestMessageType() {
        return new GetEventsRequestMessageType();
    }

    /**
     * Create an instance of {@link LoadEventDetailsResponseMessageType }
     * 
     */
    public LoadEventDetailsResponseMessageType createLoadEventDetailsResponseMessageType() {
        return new LoadEventDetailsResponseMessageType();
    }

    /**
     * Create an instance of {@link GetEventsResponseMessageType }
     * 
     */
    public GetEventsResponseMessageType createGetEventsResponseMessageType() {
        return new GetEventsResponseMessageType();
    }

    /**
     * Create an instance of {@link StatusResponseBean }
     * 
     */
    public StatusResponseBean createStatusResponseBean() {
        return new StatusResponseBean();
    }

    /**
     * Create an instance of {@link GetStatusResponseMessageType }
     * 
     */
    public GetStatusResponseMessageType createGetStatusResponseMessageType() {
        return new GetStatusResponseMessageType();
    }

    /**
     * Create an instance of {@link Events }
     * 
     */
    public Events createEvents() {
        return new Events();
    }

    /**
     * Create an instance of {@link Any }
     * 
     */
    public Any createAny() {
        return new Any();
    }

    /**
     * Create an instance of {@link LoadEventDetailsRequestMessageType }
     * 
     */
    public LoadEventDetailsRequestMessageType createLoadEventDetailsRequestMessageType() {
        return new LoadEventDetailsRequestMessageType();
    }

    /**
     * Create an instance of {@link CreateRequestBean }
     * 
     */
    public CreateRequestBean createCreateRequestBean() {
        return new CreateRequestBean();
    }

    /**
     * Create an instance of {@link CreateRequestResponseMessageType }
     * 
     */
    public CreateRequestResponseMessageType createCreateRequestResponseMessageType() {
        return new CreateRequestResponseMessageType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.fccland.ru/types/", name = "errorMessage")
    public JAXBElement<String> createErrorMessage(String value) {
        return new JAXBElement<String>(_ErrorMessage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.fccland.ru/types/", name = "eventID")
    public JAXBElement<String> createEventID(String value) {
        return new JAXBElement<String>(_EventID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Any }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.fccland.ru/types/", name = "XML")
    public JAXBElement<Any> createXML(Any value) {
        return new JAXBElement<Any>(_XML_QNAME, Any.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.fccland.ru/types/", name = "requestNumber")
    public JAXBElement<String> createRequestNumber(String value) {
        return new JAXBElement<String>(_RequestNumber_QNAME, String.class, null, value);
    }

}

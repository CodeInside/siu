
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.fccland.portal.rt;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import ru.gosuslugi.smev.rev111111.CreateRequestRequestMessage;
import ru.gosuslugi.smev.rev111111.CreateRequestResponseMessage;
import ru.gosuslugi.smev.rev111111.GetEventsRequestMessage;
import ru.gosuslugi.smev.rev111111.GetEventsResponseMessage;
import ru.gosuslugi.smev.rev111111.GetStatusRequestMessage;
import ru.gosuslugi.smev.rev111111.GetStatusResponseMessage;
import ru.gosuslugi.smev.rev111111.LoadEventDetailsRequestMessage;
import ru.gosuslugi.smev.rev111111.LoadEventDetailsResponseMessage;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.fccland.portal.rt package. 
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

    private final static QName _LoadEventDetailsResponse_QNAME = new QName("http://portal.fccland.ru/rt/", "loadEventDetailsResponse");
    private final static QName _CreateRequestResponse_QNAME = new QName("http://portal.fccland.ru/rt/", "createRequestResponse");
    private final static QName _GetStatusRequest_QNAME = new QName("http://portal.fccland.ru/rt/", "getStatusRequest");
    private final static QName _GetEventsResponse_QNAME = new QName("http://portal.fccland.ru/rt/", "getEventsResponse");
    private final static QName _GetStatusResponse_QNAME = new QName("http://portal.fccland.ru/rt/", "getStatusResponse");
    private final static QName _CreateRequestRequest_QNAME = new QName("http://portal.fccland.ru/rt/", "createRequestRequest");
    private final static QName _LoadEventDetailsRequest_QNAME = new QName("http://portal.fccland.ru/rt/", "loadEventDetailsRequest");
    private final static QName _GetEventsRequest_QNAME = new QName("http://portal.fccland.ru/rt/", "getEventsRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.fccland.portal.rt
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadEventDetailsResponseMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.fccland.ru/rt/", name = "loadEventDetailsResponse")
    public JAXBElement<LoadEventDetailsResponseMessage> createLoadEventDetailsResponse(LoadEventDetailsResponseMessage value) {
        return new JAXBElement<LoadEventDetailsResponseMessage>(_LoadEventDetailsResponse_QNAME, LoadEventDetailsResponseMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateRequestResponseMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.fccland.ru/rt/", name = "createRequestResponse")
    public JAXBElement<CreateRequestResponseMessage> createCreateRequestResponse(CreateRequestResponseMessage value) {
        return new JAXBElement<CreateRequestResponseMessage>(_CreateRequestResponse_QNAME, CreateRequestResponseMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStatusRequestMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.fccland.ru/rt/", name = "getStatusRequest")
    public JAXBElement<GetStatusRequestMessage> createGetStatusRequest(GetStatusRequestMessage value) {
        return new JAXBElement<GetStatusRequestMessage>(_GetStatusRequest_QNAME, GetStatusRequestMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEventsResponseMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.fccland.ru/rt/", name = "getEventsResponse")
    public JAXBElement<GetEventsResponseMessage> createGetEventsResponse(GetEventsResponseMessage value) {
        return new JAXBElement<GetEventsResponseMessage>(_GetEventsResponse_QNAME, GetEventsResponseMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStatusResponseMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.fccland.ru/rt/", name = "getStatusResponse")
    public JAXBElement<GetStatusResponseMessage> createGetStatusResponse(GetStatusResponseMessage value) {
        return new JAXBElement<GetStatusResponseMessage>(_GetStatusResponse_QNAME, GetStatusResponseMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateRequestRequestMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.fccland.ru/rt/", name = "createRequestRequest")
    public JAXBElement<CreateRequestRequestMessage> createCreateRequestRequest(CreateRequestRequestMessage value) {
        return new JAXBElement<CreateRequestRequestMessage>(_CreateRequestRequest_QNAME, CreateRequestRequestMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadEventDetailsRequestMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.fccland.ru/rt/", name = "loadEventDetailsRequest")
    public JAXBElement<LoadEventDetailsRequestMessage> createLoadEventDetailsRequest(LoadEventDetailsRequestMessage value) {
        return new JAXBElement<LoadEventDetailsRequestMessage>(_LoadEventDetailsRequest_QNAME, LoadEventDetailsRequestMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEventsRequestMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.fccland.ru/rt/", name = "getEventsRequest")
    public JAXBElement<GetEventsRequestMessage> createGetEventsRequest(GetEventsRequestMessage value) {
        return new JAXBElement<GetEventsRequestMessage>(_GetEventsRequest_QNAME, GetEventsRequestMessage.class, null, value);
    }

}


/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.gosuslugi.smev.rev111111;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.gosuslugi.smev.rev111111 package. 
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

    private final static QName _MessageClass_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "MessageClass");
    private final static QName _TestMsg_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "TestMsg");
    private final static QName _ExchangeType_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "ExchangeType");
    private final static QName _NodeId_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "NodeId");
    private final static QName _Sender_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "Sender");
    private final static QName _RequestIdRef_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "RequestIdRef");
    private final static QName _Header_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "Header");
    private final static QName _TypeCode_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "TypeCode");
    private final static QName _Date_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "Date");
    private final static QName _MessageId_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "MessageId");
    private final static QName _ServiceCode_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "ServiceCode");
    private final static QName _CaseNumber_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "CaseNumber");
    private final static QName _Recipient_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "Recipient");
    private final static QName _OriginRequestIdRef_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "OriginRequestIdRef");
    private final static QName _DigestValue_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "DigestValue");
    private final static QName _Originator_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "Originator");
    private final static QName _Message_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "Message");
    private final static QName _BinaryData_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "BinaryData");
    private final static QName _Status_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "Status");
    private final static QName _TimeStamp_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "TimeStamp");
    private final static QName _Reference_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "Reference");
    private final static QName _RequestCode_QNAME = new QName("http://smev.gosuslugi.ru/rev111111", "RequestCode");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.gosuslugi.smev.rev111111
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetEventsResponseMessage }
     * 
     */
    public GetEventsResponseMessage createGetEventsResponseMessage() {
        return new GetEventsResponseMessage();
    }

    /**
     * Create an instance of {@link CreateRequestResponseMessageData }
     * 
     */
    public CreateRequestResponseMessageData createCreateRequestResponseMessageData() {
        return new CreateRequestResponseMessageData();
    }

    /**
     * Create an instance of {@link GetStatusResponseMessage }
     * 
     */
    public GetStatusResponseMessage createGetStatusResponseMessage() {
        return new GetStatusResponseMessage();
    }

    /**
     * Create an instance of {@link CreateRequestResponseMessage }
     * 
     */
    public CreateRequestResponseMessage createCreateRequestResponseMessage() {
        return new CreateRequestResponseMessage();
    }

    /**
     * Create an instance of {@link LoadEventDetailsRequestMessage }
     * 
     */
    public LoadEventDetailsRequestMessage createLoadEventDetailsRequestMessage() {
        return new LoadEventDetailsRequestMessage();
    }

    /**
     * Create an instance of {@link GetEventsResponseMessageData }
     * 
     */
    public GetEventsResponseMessageData createGetEventsResponseMessageData() {
        return new GetEventsResponseMessageData();
    }

    /**
     * Create an instance of {@link HeaderType }
     * 
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link GetStatusResponseMessageData }
     * 
     */
    public GetStatusResponseMessageData createGetStatusResponseMessageData() {
        return new GetStatusResponseMessageData();
    }

    /**
     * Create an instance of {@link AppDocumentType }
     * 
     */
    public AppDocumentType createAppDocumentType() {
        return new AppDocumentType();
    }

    /**
     * Create an instance of {@link MessageType }
     * 
     */
    public MessageType createMessageType() {
        return new MessageType();
    }

    /**
     * Create an instance of {@link LoadEventDetailsRequestMessageData }
     * 
     */
    public LoadEventDetailsRequestMessageData createLoadEventDetailsRequestMessageData() {
        return new LoadEventDetailsRequestMessageData();
    }

    /**
     * Create an instance of {@link GetEventsRequestMessage }
     * 
     */
    public GetEventsRequestMessage createGetEventsRequestMessage() {
        return new GetEventsRequestMessage();
    }

    /**
     * Create an instance of {@link LoadEventDetailsResponseMessageData }
     * 
     */
    public LoadEventDetailsResponseMessageData createLoadEventDetailsResponseMessageData() {
        return new LoadEventDetailsResponseMessageData();
    }

    /**
     * Create an instance of {@link OrgExternalType }
     * 
     */
    public OrgExternalType createOrgExternalType() {
        return new OrgExternalType();
    }

    /**
     * Create an instance of {@link LoadEventDetailsResponseMessage }
     * 
     */
    public LoadEventDetailsResponseMessage createLoadEventDetailsResponseMessage() {
        return new LoadEventDetailsResponseMessage();
    }

    /**
     * Create an instance of {@link CreateRequestRequestMessageData }
     * 
     */
    public CreateRequestRequestMessageData createCreateRequestRequestMessageData() {
        return new CreateRequestRequestMessageData();
    }

    /**
     * Create an instance of {@link GetStatusRequestMessageData }
     * 
     */
    public GetStatusRequestMessageData createGetStatusRequestMessageData() {
        return new GetStatusRequestMessageData();
    }

    /**
     * Create an instance of {@link ReferenceType }
     * 
     */
    public ReferenceType createReferenceType() {
        return new ReferenceType();
    }

    /**
     * Create an instance of {@link GetEventsRequestMessageData }
     * 
     */
    public GetEventsRequestMessageData createGetEventsRequestMessageData() {
        return new GetEventsRequestMessageData();
    }

    /**
     * Create an instance of {@link CreateRequestRequestMessage }
     * 
     */
    public CreateRequestRequestMessage createCreateRequestRequestMessage() {
        return new CreateRequestRequestMessage();
    }

    /**
     * Create an instance of {@link GetStatusRequestMessage }
     * 
     */
    public GetStatusRequestMessage createGetStatusRequestMessage() {
        return new GetStatusRequestMessage();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageClassType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "MessageClass")
    public JAXBElement<MessageClassType> createMessageClass(MessageClassType value) {
        return new JAXBElement<MessageClassType>(_MessageClass_QNAME, MessageClassType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "TestMsg")
    public JAXBElement<String> createTestMsg(String value) {
        return new JAXBElement<String>(_TestMsg_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "ExchangeType")
    public JAXBElement<String> createExchangeType(String value) {
        return new JAXBElement<String>(_ExchangeType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "NodeId")
    public JAXBElement<String> createNodeId(String value) {
        return new JAXBElement<String>(_NodeId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrgExternalType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "Sender")
    public JAXBElement<OrgExternalType> createSender(OrgExternalType value) {
        return new JAXBElement<OrgExternalType>(_Sender_QNAME, OrgExternalType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "RequestIdRef")
    public JAXBElement<String> createRequestIdRef(String value) {
        return new JAXBElement<String>(_RequestIdRef_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HeaderType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "Header")
    public JAXBElement<HeaderType> createHeader(HeaderType value) {
        return new JAXBElement<HeaderType>(_Header_QNAME, HeaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TypeCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "TypeCode")
    public JAXBElement<TypeCodeType> createTypeCode(TypeCodeType value) {
        return new JAXBElement<TypeCodeType>(_TypeCode_QNAME, TypeCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "Date")
    public JAXBElement<XMLGregorianCalendar> createDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_Date_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "MessageId")
    public JAXBElement<String> createMessageId(String value) {
        return new JAXBElement<String>(_MessageId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "ServiceCode")
    public JAXBElement<String> createServiceCode(String value) {
        return new JAXBElement<String>(_ServiceCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "CaseNumber")
    public JAXBElement<String> createCaseNumber(String value) {
        return new JAXBElement<String>(_CaseNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrgExternalType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "Recipient")
    public JAXBElement<OrgExternalType> createRecipient(OrgExternalType value) {
        return new JAXBElement<OrgExternalType>(_Recipient_QNAME, OrgExternalType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "OriginRequestIdRef")
    public JAXBElement<String> createOriginRequestIdRef(String value) {
        return new JAXBElement<String>(_OriginRequestIdRef_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "DigestValue")
    public JAXBElement<byte[]> createDigestValue(byte[] value) {
        return new JAXBElement<byte[]>(_DigestValue_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrgExternalType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "Originator")
    public JAXBElement<OrgExternalType> createOriginator(OrgExternalType value) {
        return new JAXBElement<OrgExternalType>(_Originator_QNAME, OrgExternalType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "Message")
    public JAXBElement<MessageType> createMessage(MessageType value) {
        return new JAXBElement<MessageType>(_Message_QNAME, MessageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "BinaryData")
    public JAXBElement<byte[]> createBinaryData(byte[] value) {
        return new JAXBElement<byte[]>(_BinaryData_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatusType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "Status")
    public JAXBElement<StatusType> createStatus(StatusType value) {
        return new JAXBElement<StatusType>(_Status_QNAME, StatusType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "TimeStamp")
    public JAXBElement<XMLGregorianCalendar> createTimeStamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_TimeStamp_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "Reference")
    public JAXBElement<ReferenceType> createReference(ReferenceType value) {
        return new JAXBElement<ReferenceType>(_Reference_QNAME, ReferenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/rev111111", name = "RequestCode")
    public JAXBElement<String> createRequestCode(String value) {
        return new JAXBElement<String>(_RequestCode_QNAME, String.class, null, value);
    }

}


/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.grp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "eDocument",
    "request"
})
@XmlRootElement(name = "RequestGRP")
public class RequestGRP {

    @XmlElement(required = true)
    protected RequestGRP.EDocument eDocument;
    @XmlElement(name = "Request", required = true)
    protected RequestGRP.Request request;

    /**
     * Gets the value of the eDocument property.
     * 
     * @return
     *     possible object is
     *     {@link RequestGRP.EDocument }
     *     
     */
    public RequestGRP.EDocument getEDocument() {
        return eDocument;
    }

    /**
     * Sets the value of the eDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestGRP.EDocument }
     *     
     */
    public void setEDocument(RequestGRP.EDocument value) {
        this.eDocument = value;
    }

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link RequestGRP.Request }
     *     
     */
    public RequestGRP.Request getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestGRP.Request }
     *     
     */
    public void setRequest(RequestGRP.Request value) {
        this.request = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="Version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="1.16" />
     *       &lt;attribute name="GUID" use="required" type="{}sGUID" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class EDocument {

        @XmlAttribute(name = "Version", required = true)
        protected String version;
        @XmlAttribute(name = "GUID", required = true)
        protected String guid;

        /**
         * Gets the value of the version property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVersion() {
            if (version == null) {
                return "1.16";
            } else {
                return version;
            }
        }

        /**
         * Sets the value of the version property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVersion(String value) {
            this.version = value;
        }

        /**
         * Gets the value of the guid property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGUID() {
            return guid;
        }

        /**
         * Sets the value of the guid property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGUID(String value) {
            this.guid = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="RequiredData">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice>
     *                   &lt;element name="RequiredDataRealty">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;choice>
     *                             &lt;element name="ExtractRealty">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Objects" type="{}tRequiredObject" maxOccurs="unbounded"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="ExtractRealtyList">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Objects" type="{}tRequiredObject" maxOccurs="unbounded"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="ExtractRealtyInfo">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Objects" type="{}tRequiredObject"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/choice>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="RequiredDataSubject">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;choice>
     *                             &lt;element name="ExtractSubjectRegion">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Owner" type="{}tOwner" maxOccurs="unbounded"/>
     *                                       &lt;element name="Territory">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                               &lt;choice>
     *                                                 &lt;element name="Territory_Russia" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                                 &lt;element name="Regions">
     *                                                   &lt;complexType>
     *                                                     &lt;complexContent>
     *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                                         &lt;sequence>
     *                                                           &lt;element name="Region" type="{}dRegionsRF" maxOccurs="unbounded"/>
     *                                                         &lt;/sequence>
     *                                                       &lt;/restriction>
     *                                                     &lt;/complexContent>
     *                                                   &lt;/complexType>
     *                                                 &lt;/element>
     *                                               &lt;/choice>
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="ExtractSubject">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Owners" type="{}tOwner" maxOccurs="unbounded"/>
     *                                       &lt;element name="Data_Period">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                               &lt;choice>
     *                                                 &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *                                                 &lt;element name="Date_Start" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *                                                 &lt;element name="Date_End" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *                                                 &lt;element name="Interval">
     *                                                   &lt;complexType>
     *                                                     &lt;complexContent>
     *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                                         &lt;sequence>
     *                                                           &lt;element name="Date_Start" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *                                                           &lt;element name="Date_End" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *                                                         &lt;/sequence>
     *                                                       &lt;/restriction>
     *                                                     &lt;/complexContent>
     *                                                   &lt;/complexType>
     *                                                 &lt;/element>
     *                                               &lt;/choice>
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                       &lt;element name="RealtyType">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                               &lt;choice>
     *                                                 &lt;element name="RealtyType_All" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                                 &lt;element name="RealtyType_Text" type="{}tExtractSubjectObjectType" maxOccurs="unbounded"/>
     *                                               &lt;/choice>
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                       &lt;element name="Territory">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                               &lt;choice>
     *                                                 &lt;element name="Territory_Russia" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                                 &lt;element name="Regions">
     *                                                   &lt;complexType>
     *                                                     &lt;complexContent>
     *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                                         &lt;sequence>
     *                                                           &lt;element name="Region" type="{}dRegionsRF" maxOccurs="unbounded"/>
     *                                                         &lt;/sequence>
     *                                                       &lt;/restriction>
     *                                                     &lt;/complexContent>
     *                                                   &lt;/complexType>
     *                                                 &lt;/element>
     *                                               &lt;/choice>
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/choice>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="RequiredDataDocument">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;choice>
     *                             &lt;element name="CopyDocument">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;extension base="{}tRequiredDocument">
     *                                     &lt;sequence>
     *                                       &lt;element name="Objects" type="{}tRequiredObject"/>
     *                                     &lt;/sequence>
     *                                   &lt;/extension>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="ContentDocument">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;extension base="{}tRequiredDocument">
     *                                     &lt;sequence>
     *                                       &lt;element name="Objects">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                               &lt;sequence>
     *                                                 &lt;element name="Object" type="{}tRequiredObject" maxOccurs="unbounded"/>
     *                                               &lt;/sequence>
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                       &lt;element name="DescribeContract" type="{}s500" minOccurs="0"/>
     *                                     &lt;/sequence>
     *                                   &lt;/extension>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/choice>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="RequiredDataIncapacity">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="IncapacityOwner" maxOccurs="unbounded">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;extension base="{}tPersonOwner">
     *                                     &lt;attribute name="id_db_egrp">
     *                                       &lt;simpleType>
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                           &lt;maxLength value="50"/>
     *                                         &lt;/restriction>
     *                                       &lt;/simpleType>
     *                                     &lt;/attribute>
     *                                   &lt;/extension>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/choice>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Declarant" type="{}tDeclarant"/>
     *         &lt;element name="Payment">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice>
     *                   &lt;element name="Payment_Documents">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Payment_Document" type="{}tPayDocument" maxOccurs="unbounded"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="ReasonFree_Documents">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="ReasonFree_Document" type="{}tApplied_Document" maxOccurs="unbounded"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="Free" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/choice>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Delivery" type="{}tDelivery"/>
     *         &lt;element name="Applied_Documents">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Applied_Document" type="{}tApplied_Document" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="MunicipalService" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;choice>
     *                     &lt;element name="Service" minOccurs="0">
     *                       &lt;complexType>
     *                         &lt;complexContent>
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                             &lt;attribute name="Name_Service" use="required">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;maxLength value="1000"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/attribute>
     *                             &lt;attribute name="Code_Service" use="required">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="11"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/attribute>
     *                           &lt;/restriction>
     *                         &lt;/complexContent>
     *                       &lt;/complexType>
     *                     &lt;/element>
     *                     &lt;element name="BaseRequest" minOccurs="0">
     *                       &lt;simpleType>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                           &lt;maxLength value="4000"/>
     *                         &lt;/restriction>
     *                       &lt;/simpleType>
     *                     &lt;/element>
     *                   &lt;/choice>
     *                   &lt;element name="Item_Normative_Act" minOccurs="0">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;maxLength value="4000"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="Answer_Date" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *                   &lt;element name="Post" minOccurs="0">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;maxLength value="4000"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "requiredData",
        "declarant",
        "payment",
        "delivery",
        "appliedDocuments",
        "municipalService"
    })
    public static class Request {

        @XmlElement(name = "RequiredData", required = true)
        protected RequestGRP.Request.RequiredData requiredData;
        @XmlElement(name = "Declarant", required = true)
        protected TDeclarant declarant;
        @XmlElement(name = "Payment", required = true)
        protected RequestGRP.Request.Payment payment;
        @XmlElement(name = "Delivery", required = true)
        protected TDelivery delivery;
        @XmlElement(name = "Applied_Documents", required = true)
        protected RequestGRP.Request.AppliedDocuments appliedDocuments;
        @XmlElement(name = "MunicipalService")
        protected RequestGRP.Request.MunicipalService municipalService;

        /**
         * Gets the value of the requiredData property.
         * 
         * @return
         *     possible object is
         *     {@link RequestGRP.Request.RequiredData }
         *     
         */
        public RequestGRP.Request.RequiredData getRequiredData() {
            return requiredData;
        }

        /**
         * Sets the value of the requiredData property.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestGRP.Request.RequiredData }
         *     
         */
        public void setRequiredData(RequestGRP.Request.RequiredData value) {
            this.requiredData = value;
        }

        /**
         * Gets the value of the declarant property.
         * 
         * @return
         *     possible object is
         *     {@link TDeclarant }
         *     
         */
        public TDeclarant getDeclarant() {
            return declarant;
        }

        /**
         * Sets the value of the declarant property.
         * 
         * @param value
         *     allowed object is
         *     {@link TDeclarant }
         *     
         */
        public void setDeclarant(TDeclarant value) {
            this.declarant = value;
        }

        /**
         * Gets the value of the payment property.
         * 
         * @return
         *     possible object is
         *     {@link RequestGRP.Request.Payment }
         *     
         */
        public RequestGRP.Request.Payment getPayment() {
            return payment;
        }

        /**
         * Sets the value of the payment property.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestGRP.Request.Payment }
         *     
         */
        public void setPayment(RequestGRP.Request.Payment value) {
            this.payment = value;
        }

        /**
         * Gets the value of the delivery property.
         * 
         * @return
         *     possible object is
         *     {@link TDelivery }
         *     
         */
        public TDelivery getDelivery() {
            return delivery;
        }

        /**
         * Sets the value of the delivery property.
         * 
         * @param value
         *     allowed object is
         *     {@link TDelivery }
         *     
         */
        public void setDelivery(TDelivery value) {
            this.delivery = value;
        }

        /**
         * Gets the value of the appliedDocuments property.
         * 
         * @return
         *     possible object is
         *     {@link RequestGRP.Request.AppliedDocuments }
         *     
         */
        public RequestGRP.Request.AppliedDocuments getAppliedDocuments() {
            return appliedDocuments;
        }

        /**
         * Sets the value of the appliedDocuments property.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestGRP.Request.AppliedDocuments }
         *     
         */
        public void setAppliedDocuments(RequestGRP.Request.AppliedDocuments value) {
            this.appliedDocuments = value;
        }

        /**
         * Gets the value of the municipalService property.
         * 
         * @return
         *     possible object is
         *     {@link RequestGRP.Request.MunicipalService }
         *     
         */
        public RequestGRP.Request.MunicipalService getMunicipalService() {
            return municipalService;
        }

        /**
         * Sets the value of the municipalService property.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestGRP.Request.MunicipalService }
         *     
         */
        public void setMunicipalService(RequestGRP.Request.MunicipalService value) {
            this.municipalService = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="Applied_Document" type="{}tApplied_Document" maxOccurs="unbounded"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "appliedDocument"
        })
        public static class AppliedDocuments {

            @XmlElement(name = "Applied_Document", required = true)
            protected List<TAppliedDocument> appliedDocument;

            /**
             * Gets the value of the appliedDocument property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the appliedDocument property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAppliedDocument().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link TAppliedDocument }
             * 
             * 
             */
            public List<TAppliedDocument> getAppliedDocument() {
                if (appliedDocument == null) {
                    appliedDocument = new ArrayList<TAppliedDocument>();
                }
                return this.appliedDocument;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;choice>
         *           &lt;element name="Service" minOccurs="0">
         *             &lt;complexType>
         *               &lt;complexContent>
         *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                   &lt;attribute name="Name_Service" use="required">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;maxLength value="1000"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/attribute>
         *                   &lt;attribute name="Code_Service" use="required">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="11"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/attribute>
         *                 &lt;/restriction>
         *               &lt;/complexContent>
         *             &lt;/complexType>
         *           &lt;/element>
         *           &lt;element name="BaseRequest" minOccurs="0">
         *             &lt;simpleType>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                 &lt;maxLength value="4000"/>
         *               &lt;/restriction>
         *             &lt;/simpleType>
         *           &lt;/element>
         *         &lt;/choice>
         *         &lt;element name="Item_Normative_Act" minOccurs="0">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;maxLength value="4000"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="Answer_Date" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
         *         &lt;element name="Post" minOccurs="0">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;maxLength value="4000"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "service",
            "baseRequest",
            "itemNormativeAct",
            "answerDate",
            "post"
        })
        public static class MunicipalService {

            @XmlElement(name = "Service")
            protected RequestGRP.Request.MunicipalService.Service service;
            @XmlElement(name = "BaseRequest")
            protected String baseRequest;
            @XmlElement(name = "Item_Normative_Act")
            protected String itemNormativeAct;
            @XmlElement(name = "Answer_Date")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar answerDate;
            @XmlElement(name = "Post")
            protected String post;

            /**
             * Gets the value of the service property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGRP.Request.MunicipalService.Service }
             *     
             */
            public RequestGRP.Request.MunicipalService.Service getService() {
                return service;
            }

            /**
             * Sets the value of the service property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGRP.Request.MunicipalService.Service }
             *     
             */
            public void setService(RequestGRP.Request.MunicipalService.Service value) {
                this.service = value;
            }

            /**
             * Gets the value of the baseRequest property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBaseRequest() {
                return baseRequest;
            }

            /**
             * Sets the value of the baseRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBaseRequest(String value) {
                this.baseRequest = value;
            }

            /**
             * Gets the value of the itemNormativeAct property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getItemNormativeAct() {
                return itemNormativeAct;
            }

            /**
             * Sets the value of the itemNormativeAct property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setItemNormativeAct(String value) {
                this.itemNormativeAct = value;
            }

            /**
             * Gets the value of the answerDate property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getAnswerDate() {
                return answerDate;
            }

            /**
             * Sets the value of the answerDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setAnswerDate(XMLGregorianCalendar value) {
                this.answerDate = value;
            }

            /**
             * Gets the value of the post property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPost() {
                return post;
            }

            /**
             * Sets the value of the post property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPost(String value) {
                this.post = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="Name_Service" use="required">
             *         &lt;simpleType>
             *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *             &lt;maxLength value="1000"/>
             *           &lt;/restriction>
             *         &lt;/simpleType>
             *       &lt;/attribute>
             *       &lt;attribute name="Code_Service" use="required">
             *         &lt;simpleType>
             *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *             &lt;length value="11"/>
             *           &lt;/restriction>
             *         &lt;/simpleType>
             *       &lt;/attribute>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Service {

                @XmlAttribute(name = "Name_Service", required = true)
                protected String nameService;
                @XmlAttribute(name = "Code_Service", required = true)
                protected String codeService;

                /**
                 * Gets the value of the nameService property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getNameService() {
                    return nameService;
                }

                /**
                 * Sets the value of the nameService property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setNameService(String value) {
                    this.nameService = value;
                }

                /**
                 * Gets the value of the codeService property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCodeService() {
                    return codeService;
                }

                /**
                 * Sets the value of the codeService property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCodeService(String value) {
                    this.codeService = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;choice>
         *         &lt;element name="Payment_Documents">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Payment_Document" type="{}tPayDocument" maxOccurs="unbounded"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="ReasonFree_Documents">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="ReasonFree_Document" type="{}tApplied_Document" maxOccurs="unbounded"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="Free" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *       &lt;/choice>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "paymentDocuments",
            "reasonFreeDocuments",
            "free"
        })
        public static class Payment {

            @XmlElement(name = "Payment_Documents")
            protected RequestGRP.Request.Payment.PaymentDocuments paymentDocuments;
            @XmlElement(name = "ReasonFree_Documents")
            protected RequestGRP.Request.Payment.ReasonFreeDocuments reasonFreeDocuments;
            @XmlElement(name = "Free")
            protected Boolean free;

            /**
             * Gets the value of the paymentDocuments property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGRP.Request.Payment.PaymentDocuments }
             *     
             */
            public RequestGRP.Request.Payment.PaymentDocuments getPaymentDocuments() {
                return paymentDocuments;
            }

            /**
             * Sets the value of the paymentDocuments property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGRP.Request.Payment.PaymentDocuments }
             *     
             */
            public void setPaymentDocuments(RequestGRP.Request.Payment.PaymentDocuments value) {
                this.paymentDocuments = value;
            }

            /**
             * Gets the value of the reasonFreeDocuments property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGRP.Request.Payment.ReasonFreeDocuments }
             *     
             */
            public RequestGRP.Request.Payment.ReasonFreeDocuments getReasonFreeDocuments() {
                return reasonFreeDocuments;
            }

            /**
             * Sets the value of the reasonFreeDocuments property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGRP.Request.Payment.ReasonFreeDocuments }
             *     
             */
            public void setReasonFreeDocuments(RequestGRP.Request.Payment.ReasonFreeDocuments value) {
                this.reasonFreeDocuments = value;
            }

            /**
             * Gets the value of the free property.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public Boolean isFree() {
                return free;
            }

            /**
             * Sets the value of the free property.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setFree(Boolean value) {
                this.free = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="Payment_Document" type="{}tPayDocument" maxOccurs="unbounded"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "paymentDocument"
            })
            public static class PaymentDocuments {

                @XmlElement(name = "Payment_Document", required = true)
                protected List<TPayDocument> paymentDocument;

                /**
                 * Gets the value of the paymentDocument property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the paymentDocument property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getPaymentDocument().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link TPayDocument }
                 * 
                 * 
                 */
                public List<TPayDocument> getPaymentDocument() {
                    if (paymentDocument == null) {
                        paymentDocument = new ArrayList<TPayDocument>();
                    }
                    return this.paymentDocument;
                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="ReasonFree_Document" type="{}tApplied_Document" maxOccurs="unbounded"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "reasonFreeDocument"
            })
            public static class ReasonFreeDocuments {

                @XmlElement(name = "ReasonFree_Document", required = true)
                protected List<TAppliedDocument> reasonFreeDocument;

                /**
                 * Gets the value of the reasonFreeDocument property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the reasonFreeDocument property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getReasonFreeDocument().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link TAppliedDocument }
                 * 
                 * 
                 */
                public List<TAppliedDocument> getReasonFreeDocument() {
                    if (reasonFreeDocument == null) {
                        reasonFreeDocument = new ArrayList<TAppliedDocument>();
                    }
                    return this.reasonFreeDocument;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;choice>
         *         &lt;element name="RequiredDataRealty">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;choice>
         *                   &lt;element name="ExtractRealty">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Objects" type="{}tRequiredObject" maxOccurs="unbounded"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="ExtractRealtyList">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Objects" type="{}tRequiredObject" maxOccurs="unbounded"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="ExtractRealtyInfo">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Objects" type="{}tRequiredObject"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/choice>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="RequiredDataSubject">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;choice>
         *                   &lt;element name="ExtractSubjectRegion">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Owner" type="{}tOwner" maxOccurs="unbounded"/>
         *                             &lt;element name="Territory">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                     &lt;choice>
         *                                       &lt;element name="Territory_Russia" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                                       &lt;element name="Regions">
         *                                         &lt;complexType>
         *                                           &lt;complexContent>
         *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                               &lt;sequence>
         *                                                 &lt;element name="Region" type="{}dRegionsRF" maxOccurs="unbounded"/>
         *                                               &lt;/sequence>
         *                                             &lt;/restriction>
         *                                           &lt;/complexContent>
         *                                         &lt;/complexType>
         *                                       &lt;/element>
         *                                     &lt;/choice>
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="ExtractSubject">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Owners" type="{}tOwner" maxOccurs="unbounded"/>
         *                             &lt;element name="Data_Period">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                     &lt;choice>
         *                                       &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}date"/>
         *                                       &lt;element name="Date_Start" type="{http://www.w3.org/2001/XMLSchema}date"/>
         *                                       &lt;element name="Date_End" type="{http://www.w3.org/2001/XMLSchema}date"/>
         *                                       &lt;element name="Interval">
         *                                         &lt;complexType>
         *                                           &lt;complexContent>
         *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                               &lt;sequence>
         *                                                 &lt;element name="Date_Start" type="{http://www.w3.org/2001/XMLSchema}date"/>
         *                                                 &lt;element name="Date_End" type="{http://www.w3.org/2001/XMLSchema}date"/>
         *                                               &lt;/sequence>
         *                                             &lt;/restriction>
         *                                           &lt;/complexContent>
         *                                         &lt;/complexType>
         *                                       &lt;/element>
         *                                     &lt;/choice>
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                             &lt;element name="RealtyType">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                     &lt;choice>
         *                                       &lt;element name="RealtyType_All" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                                       &lt;element name="RealtyType_Text" type="{}tExtractSubjectObjectType" maxOccurs="unbounded"/>
         *                                     &lt;/choice>
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                             &lt;element name="Territory">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                     &lt;choice>
         *                                       &lt;element name="Territory_Russia" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                                       &lt;element name="Regions">
         *                                         &lt;complexType>
         *                                           &lt;complexContent>
         *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                               &lt;sequence>
         *                                                 &lt;element name="Region" type="{}dRegionsRF" maxOccurs="unbounded"/>
         *                                               &lt;/sequence>
         *                                             &lt;/restriction>
         *                                           &lt;/complexContent>
         *                                         &lt;/complexType>
         *                                       &lt;/element>
         *                                     &lt;/choice>
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/choice>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="RequiredDataDocument">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;choice>
         *                   &lt;element name="CopyDocument">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;extension base="{}tRequiredDocument">
         *                           &lt;sequence>
         *                             &lt;element name="Objects" type="{}tRequiredObject"/>
         *                           &lt;/sequence>
         *                         &lt;/extension>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="ContentDocument">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;extension base="{}tRequiredDocument">
         *                           &lt;sequence>
         *                             &lt;element name="Objects">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                     &lt;sequence>
         *                                       &lt;element name="Object" type="{}tRequiredObject" maxOccurs="unbounded"/>
         *                                     &lt;/sequence>
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                             &lt;element name="DescribeContract" type="{}s500" minOccurs="0"/>
         *                           &lt;/sequence>
         *                         &lt;/extension>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/choice>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="RequiredDataIncapacity">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="IncapacityOwner" maxOccurs="unbounded">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;extension base="{}tPersonOwner">
         *                           &lt;attribute name="id_db_egrp">
         *                             &lt;simpleType>
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                                 &lt;maxLength value="50"/>
         *                               &lt;/restriction>
         *                             &lt;/simpleType>
         *                           &lt;/attribute>
         *                         &lt;/extension>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/choice>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "requiredDataRealty",
            "requiredDataSubject",
            "requiredDataDocument",
            "requiredDataIncapacity"
        })
        public static class RequiredData {

            @XmlElement(name = "RequiredDataRealty")
            protected RequestGRP.Request.RequiredData.RequiredDataRealty requiredDataRealty;
            @XmlElement(name = "RequiredDataSubject")
            protected RequestGRP.Request.RequiredData.RequiredDataSubject requiredDataSubject;
            @XmlElement(name = "RequiredDataDocument")
            protected RequestGRP.Request.RequiredData.RequiredDataDocument requiredDataDocument;
            @XmlElement(name = "RequiredDataIncapacity")
            protected RequestGRP.Request.RequiredData.RequiredDataIncapacity requiredDataIncapacity;

            /**
             * Gets the value of the requiredDataRealty property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGRP.Request.RequiredData.RequiredDataRealty }
             *     
             */
            public RequestGRP.Request.RequiredData.RequiredDataRealty getRequiredDataRealty() {
                return requiredDataRealty;
            }

            /**
             * Sets the value of the requiredDataRealty property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGRP.Request.RequiredData.RequiredDataRealty }
             *     
             */
            public void setRequiredDataRealty(RequestGRP.Request.RequiredData.RequiredDataRealty value) {
                this.requiredDataRealty = value;
            }

            /**
             * Gets the value of the requiredDataSubject property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject }
             *     
             */
            public RequestGRP.Request.RequiredData.RequiredDataSubject getRequiredDataSubject() {
                return requiredDataSubject;
            }

            /**
             * Sets the value of the requiredDataSubject property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject }
             *     
             */
            public void setRequiredDataSubject(RequestGRP.Request.RequiredData.RequiredDataSubject value) {
                this.requiredDataSubject = value;
            }

            /**
             * Gets the value of the requiredDataDocument property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGRP.Request.RequiredData.RequiredDataDocument }
             *     
             */
            public RequestGRP.Request.RequiredData.RequiredDataDocument getRequiredDataDocument() {
                return requiredDataDocument;
            }

            /**
             * Sets the value of the requiredDataDocument property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGRP.Request.RequiredData.RequiredDataDocument }
             *     
             */
            public void setRequiredDataDocument(RequestGRP.Request.RequiredData.RequiredDataDocument value) {
                this.requiredDataDocument = value;
            }

            /**
             * Gets the value of the requiredDataIncapacity property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGRP.Request.RequiredData.RequiredDataIncapacity }
             *     
             */
            public RequestGRP.Request.RequiredData.RequiredDataIncapacity getRequiredDataIncapacity() {
                return requiredDataIncapacity;
            }

            /**
             * Sets the value of the requiredDataIncapacity property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGRP.Request.RequiredData.RequiredDataIncapacity }
             *     
             */
            public void setRequiredDataIncapacity(RequestGRP.Request.RequiredData.RequiredDataIncapacity value) {
                this.requiredDataIncapacity = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;choice>
             *         &lt;element name="CopyDocument">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;extension base="{}tRequiredDocument">
             *                 &lt;sequence>
             *                   &lt;element name="Objects" type="{}tRequiredObject"/>
             *                 &lt;/sequence>
             *               &lt;/extension>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="ContentDocument">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;extension base="{}tRequiredDocument">
             *                 &lt;sequence>
             *                   &lt;element name="Objects">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;sequence>
             *                             &lt;element name="Object" type="{}tRequiredObject" maxOccurs="unbounded"/>
             *                           &lt;/sequence>
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                   &lt;element name="DescribeContract" type="{}s500" minOccurs="0"/>
             *                 &lt;/sequence>
             *               &lt;/extension>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/choice>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "copyDocument",
                "contentDocument"
            })
            public static class RequiredDataDocument {

                @XmlElement(name = "CopyDocument")
                protected RequestGRP.Request.RequiredData.RequiredDataDocument.CopyDocument copyDocument;
                @XmlElement(name = "ContentDocument")
                protected RequestGRP.Request.RequiredData.RequiredDataDocument.ContentDocument contentDocument;

                /**
                 * Gets the value of the copyDocument property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataDocument.CopyDocument }
                 *     
                 */
                public RequestGRP.Request.RequiredData.RequiredDataDocument.CopyDocument getCopyDocument() {
                    return copyDocument;
                }

                /**
                 * Sets the value of the copyDocument property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataDocument.CopyDocument }
                 *     
                 */
                public void setCopyDocument(RequestGRP.Request.RequiredData.RequiredDataDocument.CopyDocument value) {
                    this.copyDocument = value;
                }

                /**
                 * Gets the value of the contentDocument property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataDocument.ContentDocument }
                 *     
                 */
                public RequestGRP.Request.RequiredData.RequiredDataDocument.ContentDocument getContentDocument() {
                    return contentDocument;
                }

                /**
                 * Sets the value of the contentDocument property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataDocument.ContentDocument }
                 *     
                 */
                public void setContentDocument(RequestGRP.Request.RequiredData.RequiredDataDocument.ContentDocument value) {
                    this.contentDocument = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;extension base="{}tRequiredDocument">
                 *       &lt;sequence>
                 *         &lt;element name="Objects">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;sequence>
                 *                   &lt;element name="Object" type="{}tRequiredObject" maxOccurs="unbounded"/>
                 *                 &lt;/sequence>
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *         &lt;element name="DescribeContract" type="{}s500" minOccurs="0"/>
                 *       &lt;/sequence>
                 *     &lt;/extension>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "objects",
                    "describeContract"
                })
                public static class ContentDocument
                    extends TRequiredDocument
                {

                    @XmlElement(name = "Objects", required = true)
                    protected RequestGRP.Request.RequiredData.RequiredDataDocument.ContentDocument.Objects objects;
                    @XmlElement(name = "DescribeContract")
                    protected String describeContract;

                    /**
                     * Gets the value of the objects property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RequestGRP.Request.RequiredData.RequiredDataDocument.ContentDocument.Objects }
                     *     
                     */
                    public RequestGRP.Request.RequiredData.RequiredDataDocument.ContentDocument.Objects getObjects() {
                        return objects;
                    }

                    /**
                     * Sets the value of the objects property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RequestGRP.Request.RequiredData.RequiredDataDocument.ContentDocument.Objects }
                     *     
                     */
                    public void setObjects(RequestGRP.Request.RequiredData.RequiredDataDocument.ContentDocument.Objects value) {
                        this.objects = value;
                    }

                    /**
                     * Gets the value of the describeContract property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDescribeContract() {
                        return describeContract;
                    }

                    /**
                     * Sets the value of the describeContract property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDescribeContract(String value) {
                        this.describeContract = value;
                    }


                    /**
                     * <p>Java class for anonymous complex type.
                     * 
                     * <p>The following schema fragment specifies the expected content contained within this class.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *       &lt;sequence>
                     *         &lt;element name="Object" type="{}tRequiredObject" maxOccurs="unbounded"/>
                     *       &lt;/sequence>
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                        "object"
                    })
                    public static class Objects {

                        @XmlElement(name = "Object", required = true)
                        protected List<TRequiredObject> object;

                        /**
                         * Gets the value of the object property.
                         * 
                         * <p>
                         * This accessor method returns a reference to the live list,
                         * not a snapshot. Therefore any modification you make to the
                         * returned list will be present inside the JAXB object.
                         * This is why there is not a <CODE>set</CODE> method for the object property.
                         * 
                         * <p>
                         * For example, to add a new item, do as follows:
                         * <pre>
                         *    getObject().add(newItem);
                         * </pre>
                         * 
                         * 
                         * <p>
                         * Objects of the following type(s) are allowed in the list
                         * {@link TRequiredObject }
                         * 
                         * 
                         */
                        public List<TRequiredObject> getObject() {
                            if (object == null) {
                                object = new ArrayList<TRequiredObject>();
                            }
                            return this.object;
                        }

                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;extension base="{}tRequiredDocument">
                 *       &lt;sequence>
                 *         &lt;element name="Objects" type="{}tRequiredObject"/>
                 *       &lt;/sequence>
                 *     &lt;/extension>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "objects"
                })
                public static class CopyDocument
                    extends TRequiredDocument
                {

                    @XmlElement(name = "Objects", required = true)
                    protected TRequiredObject objects;

                    /**
                     * Gets the value of the objects property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link TRequiredObject }
                     *     
                     */
                    public TRequiredObject getObjects() {
                        return objects;
                    }

                    /**
                     * Sets the value of the objects property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link TRequiredObject }
                     *     
                     */
                    public void setObjects(TRequiredObject value) {
                        this.objects = value;
                    }

                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="IncapacityOwner" maxOccurs="unbounded">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;extension base="{}tPersonOwner">
             *                 &lt;attribute name="id_db_egrp">
             *                   &lt;simpleType>
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *                       &lt;maxLength value="50"/>
             *                     &lt;/restriction>
             *                   &lt;/simpleType>
             *                 &lt;/attribute>
             *               &lt;/extension>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "incapacityOwner"
            })
            public static class RequiredDataIncapacity {

                @XmlElement(name = "IncapacityOwner", required = true)
                protected List<RequestGRP.Request.RequiredData.RequiredDataIncapacity.IncapacityOwner> incapacityOwner;

                /**
                 * Gets the value of the incapacityOwner property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the incapacityOwner property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getIncapacityOwner().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link RequestGRP.Request.RequiredData.RequiredDataIncapacity.IncapacityOwner }
                 * 
                 * 
                 */
                public List<RequestGRP.Request.RequiredData.RequiredDataIncapacity.IncapacityOwner> getIncapacityOwner() {
                    if (incapacityOwner == null) {
                        incapacityOwner = new ArrayList<RequestGRP.Request.RequiredData.RequiredDataIncapacity.IncapacityOwner>();
                    }
                    return this.incapacityOwner;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;extension base="{}tPersonOwner">
                 *       &lt;attribute name="id_db_egrp">
                 *         &lt;simpleType>
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
                 *             &lt;maxLength value="50"/>
                 *           &lt;/restriction>
                 *         &lt;/simpleType>
                 *       &lt;/attribute>
                 *     &lt;/extension>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class IncapacityOwner
                    extends TPersonOwner
                {

                    @XmlAttribute(name = "id_db_egrp")
                    protected String idDbEgrp;

                    /**
                     * Gets the value of the idDbEgrp property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getIdDbEgrp() {
                        return idDbEgrp;
                    }

                    /**
                     * Sets the value of the idDbEgrp property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setIdDbEgrp(String value) {
                        this.idDbEgrp = value;
                    }

                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;choice>
             *         &lt;element name="ExtractRealty">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Objects" type="{}tRequiredObject" maxOccurs="unbounded"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="ExtractRealtyList">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Objects" type="{}tRequiredObject" maxOccurs="unbounded"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="ExtractRealtyInfo">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Objects" type="{}tRequiredObject"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/choice>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "extractRealty",
                "extractRealtyList",
                "extractRealtyInfo"
            })
            public static class RequiredDataRealty {

                @XmlElement(name = "ExtractRealty")
                protected RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealty extractRealty;
                @XmlElement(name = "ExtractRealtyList")
                protected RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealtyList extractRealtyList;
                @XmlElement(name = "ExtractRealtyInfo")
                protected RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealtyInfo extractRealtyInfo;

                /**
                 * Gets the value of the extractRealty property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealty }
                 *     
                 */
                public RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealty getExtractRealty() {
                    return extractRealty;
                }

                /**
                 * Sets the value of the extractRealty property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealty }
                 *     
                 */
                public void setExtractRealty(RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealty value) {
                    this.extractRealty = value;
                }

                /**
                 * Gets the value of the extractRealtyList property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealtyList }
                 *     
                 */
                public RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealtyList getExtractRealtyList() {
                    return extractRealtyList;
                }

                /**
                 * Sets the value of the extractRealtyList property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealtyList }
                 *     
                 */
                public void setExtractRealtyList(RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealtyList value) {
                    this.extractRealtyList = value;
                }

                /**
                 * Gets the value of the extractRealtyInfo property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealtyInfo }
                 *     
                 */
                public RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealtyInfo getExtractRealtyInfo() {
                    return extractRealtyInfo;
                }

                /**
                 * Sets the value of the extractRealtyInfo property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealtyInfo }
                 *     
                 */
                public void setExtractRealtyInfo(RequestGRP.Request.RequiredData.RequiredDataRealty.ExtractRealtyInfo value) {
                    this.extractRealtyInfo = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Objects" type="{}tRequiredObject" maxOccurs="unbounded"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "objects"
                })
                public static class ExtractRealty {

                    @XmlElement(name = "Objects", required = true)
                    protected List<TRequiredObject> objects;

                    /**
                     * Gets the value of the objects property.
                     * 
                     * <p>
                     * This accessor method returns a reference to the live list,
                     * not a snapshot. Therefore any modification you make to the
                     * returned list will be present inside the JAXB object.
                     * This is why there is not a <CODE>set</CODE> method for the objects property.
                     * 
                     * <p>
                     * For example, to add a new item, do as follows:
                     * <pre>
                     *    getObjects().add(newItem);
                     * </pre>
                     * 
                     * 
                     * <p>
                     * Objects of the following type(s) are allowed in the list
                     * {@link TRequiredObject }
                     * 
                     * 
                     */
                    public List<TRequiredObject> getObjects() {
                        if (objects == null) {
                            objects = new ArrayList<TRequiredObject>();
                        }
                        return this.objects;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Objects" type="{}tRequiredObject"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "objects"
                })
                public static class ExtractRealtyInfo {

                    @XmlElement(name = "Objects", required = true)
                    protected TRequiredObject objects;

                    /**
                     * Gets the value of the objects property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link TRequiredObject }
                     *     
                     */
                    public TRequiredObject getObjects() {
                        return objects;
                    }

                    /**
                     * Sets the value of the objects property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link TRequiredObject }
                     *     
                     */
                    public void setObjects(TRequiredObject value) {
                        this.objects = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Objects" type="{}tRequiredObject" maxOccurs="unbounded"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "objects"
                })
                public static class ExtractRealtyList {

                    @XmlElement(name = "Objects", required = true)
                    protected List<TRequiredObject> objects;

                    /**
                     * Gets the value of the objects property.
                     * 
                     * <p>
                     * This accessor method returns a reference to the live list,
                     * not a snapshot. Therefore any modification you make to the
                     * returned list will be present inside the JAXB object.
                     * This is why there is not a <CODE>set</CODE> method for the objects property.
                     * 
                     * <p>
                     * For example, to add a new item, do as follows:
                     * <pre>
                     *    getObjects().add(newItem);
                     * </pre>
                     * 
                     * 
                     * <p>
                     * Objects of the following type(s) are allowed in the list
                     * {@link TRequiredObject }
                     * 
                     * 
                     */
                    public List<TRequiredObject> getObjects() {
                        if (objects == null) {
                            objects = new ArrayList<TRequiredObject>();
                        }
                        return this.objects;
                    }

                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;choice>
             *         &lt;element name="ExtractSubjectRegion">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Owner" type="{}tOwner" maxOccurs="unbounded"/>
             *                   &lt;element name="Territory">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;choice>
             *                             &lt;element name="Territory_Russia" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *                             &lt;element name="Regions">
             *                               &lt;complexType>
             *                                 &lt;complexContent>
             *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                                     &lt;sequence>
             *                                       &lt;element name="Region" type="{}dRegionsRF" maxOccurs="unbounded"/>
             *                                     &lt;/sequence>
             *                                   &lt;/restriction>
             *                                 &lt;/complexContent>
             *                               &lt;/complexType>
             *                             &lt;/element>
             *                           &lt;/choice>
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="ExtractSubject">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Owners" type="{}tOwner" maxOccurs="unbounded"/>
             *                   &lt;element name="Data_Period">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;choice>
             *                             &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}date"/>
             *                             &lt;element name="Date_Start" type="{http://www.w3.org/2001/XMLSchema}date"/>
             *                             &lt;element name="Date_End" type="{http://www.w3.org/2001/XMLSchema}date"/>
             *                             &lt;element name="Interval">
             *                               &lt;complexType>
             *                                 &lt;complexContent>
             *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                                     &lt;sequence>
             *                                       &lt;element name="Date_Start" type="{http://www.w3.org/2001/XMLSchema}date"/>
             *                                       &lt;element name="Date_End" type="{http://www.w3.org/2001/XMLSchema}date"/>
             *                                     &lt;/sequence>
             *                                   &lt;/restriction>
             *                                 &lt;/complexContent>
             *                               &lt;/complexType>
             *                             &lt;/element>
             *                           &lt;/choice>
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                   &lt;element name="RealtyType">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;choice>
             *                             &lt;element name="RealtyType_All" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *                             &lt;element name="RealtyType_Text" type="{}tExtractSubjectObjectType" maxOccurs="unbounded"/>
             *                           &lt;/choice>
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                   &lt;element name="Territory">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;choice>
             *                             &lt;element name="Territory_Russia" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *                             &lt;element name="Regions">
             *                               &lt;complexType>
             *                                 &lt;complexContent>
             *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                                     &lt;sequence>
             *                                       &lt;element name="Region" type="{}dRegionsRF" maxOccurs="unbounded"/>
             *                                     &lt;/sequence>
             *                                   &lt;/restriction>
             *                                 &lt;/complexContent>
             *                               &lt;/complexType>
             *                             &lt;/element>
             *                           &lt;/choice>
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/choice>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "extractSubjectRegion",
                "extractSubject"
            })
            public static class RequiredDataSubject {

                @XmlElement(name = "ExtractSubjectRegion")
                protected RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion extractSubjectRegion;
                @XmlElement(name = "ExtractSubject")
                protected RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject extractSubject;

                /**
                 * Gets the value of the extractSubjectRegion property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion }
                 *     
                 */
                public RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion getExtractSubjectRegion() {
                    return extractSubjectRegion;
                }

                /**
                 * Sets the value of the extractSubjectRegion property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion }
                 *     
                 */
                public void setExtractSubjectRegion(RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion value) {
                    this.extractSubjectRegion = value;
                }

                /**
                 * Gets the value of the extractSubject property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject }
                 *     
                 */
                public RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject getExtractSubject() {
                    return extractSubject;
                }

                /**
                 * Sets the value of the extractSubject property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject }
                 *     
                 */
                public void setExtractSubject(RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject value) {
                    this.extractSubject = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Owners" type="{}tOwner" maxOccurs="unbounded"/>
                 *         &lt;element name="Data_Period">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;choice>
                 *                   &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}date"/>
                 *                   &lt;element name="Date_Start" type="{http://www.w3.org/2001/XMLSchema}date"/>
                 *                   &lt;element name="Date_End" type="{http://www.w3.org/2001/XMLSchema}date"/>
                 *                   &lt;element name="Interval">
                 *                     &lt;complexType>
                 *                       &lt;complexContent>
                 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                           &lt;sequence>
                 *                             &lt;element name="Date_Start" type="{http://www.w3.org/2001/XMLSchema}date"/>
                 *                             &lt;element name="Date_End" type="{http://www.w3.org/2001/XMLSchema}date"/>
                 *                           &lt;/sequence>
                 *                         &lt;/restriction>
                 *                       &lt;/complexContent>
                 *                     &lt;/complexType>
                 *                   &lt;/element>
                 *                 &lt;/choice>
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *         &lt;element name="RealtyType">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;choice>
                 *                   &lt;element name="RealtyType_All" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                 *                   &lt;element name="RealtyType_Text" type="{}tExtractSubjectObjectType" maxOccurs="unbounded"/>
                 *                 &lt;/choice>
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *         &lt;element name="Territory">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;choice>
                 *                   &lt;element name="Territory_Russia" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                 *                   &lt;element name="Regions">
                 *                     &lt;complexType>
                 *                       &lt;complexContent>
                 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                           &lt;sequence>
                 *                             &lt;element name="Region" type="{}dRegionsRF" maxOccurs="unbounded"/>
                 *                           &lt;/sequence>
                 *                         &lt;/restriction>
                 *                       &lt;/complexContent>
                 *                     &lt;/complexType>
                 *                   &lt;/element>
                 *                 &lt;/choice>
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "owners",
                    "dataPeriod",
                    "realtyType",
                    "territory"
                })
                public static class ExtractSubject {

                    @XmlElement(name = "Owners", required = true)
                    protected List<TOwner> owners;
                    @XmlElement(name = "Data_Period", required = true)
                    protected RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.DataPeriod dataPeriod;
                    @XmlElement(name = "RealtyType", required = true)
                    protected RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.RealtyType realtyType;
                    @XmlElement(name = "Territory", required = true)
                    protected RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.Territory territory;

                    /**
                     * Gets the value of the owners property.
                     * 
                     * <p>
                     * This accessor method returns a reference to the live list,
                     * not a snapshot. Therefore any modification you make to the
                     * returned list will be present inside the JAXB object.
                     * This is why there is not a <CODE>set</CODE> method for the owners property.
                     * 
                     * <p>
                     * For example, to add a new item, do as follows:
                     * <pre>
                     *    getOwners().add(newItem);
                     * </pre>
                     * 
                     * 
                     * <p>
                     * Objects of the following type(s) are allowed in the list
                     * {@link TOwner }
                     * 
                     * 
                     */
                    public List<TOwner> getOwners() {
                        if (owners == null) {
                            owners = new ArrayList<TOwner>();
                        }
                        return this.owners;
                    }

                    /**
                     * Gets the value of the dataPeriod property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.DataPeriod }
                     *     
                     */
                    public RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.DataPeriod getDataPeriod() {
                        return dataPeriod;
                    }

                    /**
                     * Sets the value of the dataPeriod property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.DataPeriod }
                     *     
                     */
                    public void setDataPeriod(RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.DataPeriod value) {
                        this.dataPeriod = value;
                    }

                    /**
                     * Gets the value of the realtyType property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.RealtyType }
                     *     
                     */
                    public RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.RealtyType getRealtyType() {
                        return realtyType;
                    }

                    /**
                     * Sets the value of the realtyType property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.RealtyType }
                     *     
                     */
                    public void setRealtyType(RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.RealtyType value) {
                        this.realtyType = value;
                    }

                    /**
                     * Gets the value of the territory property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.Territory }
                     *     
                     */
                    public RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.Territory getTerritory() {
                        return territory;
                    }

                    /**
                     * Sets the value of the territory property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.Territory }
                     *     
                     */
                    public void setTerritory(RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.Territory value) {
                        this.territory = value;
                    }


                    /**
                     * <p>Java class for anonymous complex type.
                     * 
                     * <p>The following schema fragment specifies the expected content contained within this class.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *       &lt;choice>
                     *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}date"/>
                     *         &lt;element name="Date_Start" type="{http://www.w3.org/2001/XMLSchema}date"/>
                     *         &lt;element name="Date_End" type="{http://www.w3.org/2001/XMLSchema}date"/>
                     *         &lt;element name="Interval">
                     *           &lt;complexType>
                     *             &lt;complexContent>
                     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *                 &lt;sequence>
                     *                   &lt;element name="Date_Start" type="{http://www.w3.org/2001/XMLSchema}date"/>
                     *                   &lt;element name="Date_End" type="{http://www.w3.org/2001/XMLSchema}date"/>
                     *                 &lt;/sequence>
                     *               &lt;/restriction>
                     *             &lt;/complexContent>
                     *           &lt;/complexType>
                     *         &lt;/element>
                     *       &lt;/choice>
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                        "date",
                        "dateStart",
                        "dateEnd",
                        "interval"
                    })
                    public static class DataPeriod {

                        @XmlElement(name = "Date")
                        @XmlSchemaType(name = "date")
                        protected XMLGregorianCalendar date;
                        @XmlElement(name = "Date_Start")
                        @XmlSchemaType(name = "date")
                        protected XMLGregorianCalendar dateStart;
                        @XmlElement(name = "Date_End")
                        @XmlSchemaType(name = "date")
                        protected XMLGregorianCalendar dateEnd;
                        @XmlElement(name = "Interval")
                        protected RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.DataPeriod.Interval interval;

                        /**
                         * Gets the value of the date property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public XMLGregorianCalendar getDate() {
                            return date;
                        }

                        /**
                         * Sets the value of the date property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public void setDate(XMLGregorianCalendar value) {
                            this.date = value;
                        }

                        /**
                         * Gets the value of the dateStart property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public XMLGregorianCalendar getDateStart() {
                            return dateStart;
                        }

                        /**
                         * Sets the value of the dateStart property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public void setDateStart(XMLGregorianCalendar value) {
                            this.dateStart = value;
                        }

                        /**
                         * Gets the value of the dateEnd property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public XMLGregorianCalendar getDateEnd() {
                            return dateEnd;
                        }

                        /**
                         * Sets the value of the dateEnd property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public void setDateEnd(XMLGregorianCalendar value) {
                            this.dateEnd = value;
                        }

                        /**
                         * Gets the value of the interval property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.DataPeriod.Interval }
                         *     
                         */
                        public RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.DataPeriod.Interval getInterval() {
                            return interval;
                        }

                        /**
                         * Sets the value of the interval property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.DataPeriod.Interval }
                         *     
                         */
                        public void setInterval(RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.DataPeriod.Interval value) {
                            this.interval = value;
                        }


                        /**
                         * <p>Java class for anonymous complex type.
                         * 
                         * <p>The following schema fragment specifies the expected content contained within this class.
                         * 
                         * <pre>
                         * &lt;complexType>
                         *   &lt;complexContent>
                         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                         *       &lt;sequence>
                         *         &lt;element name="Date_Start" type="{http://www.w3.org/2001/XMLSchema}date"/>
                         *         &lt;element name="Date_End" type="{http://www.w3.org/2001/XMLSchema}date"/>
                         *       &lt;/sequence>
                         *     &lt;/restriction>
                         *   &lt;/complexContent>
                         * &lt;/complexType>
                         * </pre>
                         * 
                         * 
                         */
                        @XmlAccessorType(XmlAccessType.FIELD)
                        @XmlType(name = "", propOrder = {
                            "dateStart",
                            "dateEnd"
                        })
                        public static class Interval {

                            @XmlElement(name = "Date_Start", required = true)
                            @XmlSchemaType(name = "date")
                            protected XMLGregorianCalendar dateStart;
                            @XmlElement(name = "Date_End", required = true)
                            @XmlSchemaType(name = "date")
                            protected XMLGregorianCalendar dateEnd;

                            /**
                             * Gets the value of the dateStart property.
                             * 
                             * @return
                             *     possible object is
                             *     {@link XMLGregorianCalendar }
                             *     
                             */
                            public XMLGregorianCalendar getDateStart() {
                                return dateStart;
                            }

                            /**
                             * Sets the value of the dateStart property.
                             * 
                             * @param value
                             *     allowed object is
                             *     {@link XMLGregorianCalendar }
                             *     
                             */
                            public void setDateStart(XMLGregorianCalendar value) {
                                this.dateStart = value;
                            }

                            /**
                             * Gets the value of the dateEnd property.
                             * 
                             * @return
                             *     possible object is
                             *     {@link XMLGregorianCalendar }
                             *     
                             */
                            public XMLGregorianCalendar getDateEnd() {
                                return dateEnd;
                            }

                            /**
                             * Sets the value of the dateEnd property.
                             * 
                             * @param value
                             *     allowed object is
                             *     {@link XMLGregorianCalendar }
                             *     
                             */
                            public void setDateEnd(XMLGregorianCalendar value) {
                                this.dateEnd = value;
                            }

                        }

                    }


                    /**
                     * <p>Java class for anonymous complex type.
                     * 
                     * <p>The following schema fragment specifies the expected content contained within this class.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *       &lt;choice>
                     *         &lt;element name="RealtyType_All" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                     *         &lt;element name="RealtyType_Text" type="{}tExtractSubjectObjectType" maxOccurs="unbounded"/>
                     *       &lt;/choice>
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                        "realtyTypeAll",
                        "realtyTypeText"
                    })
                    public static class RealtyType {

                        @XmlElement(name = "RealtyType_All")
                        protected Boolean realtyTypeAll;
                        @XmlElement(name = "RealtyType_Text")
                        protected List<TExtractSubjectObjectType> realtyTypeText;

                        /**
                         * Gets the value of the realtyTypeAll property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link Boolean }
                         *     
                         */
                        public Boolean isRealtyTypeAll() {
                            return realtyTypeAll;
                        }

                        /**
                         * Sets the value of the realtyTypeAll property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link Boolean }
                         *     
                         */
                        public void setRealtyTypeAll(Boolean value) {
                            this.realtyTypeAll = value;
                        }

                        /**
                         * Gets the value of the realtyTypeText property.
                         * 
                         * <p>
                         * This accessor method returns a reference to the live list,
                         * not a snapshot. Therefore any modification you make to the
                         * returned list will be present inside the JAXB object.
                         * This is why there is not a <CODE>set</CODE> method for the realtyTypeText property.
                         * 
                         * <p>
                         * For example, to add a new item, do as follows:
                         * <pre>
                         *    getRealtyTypeText().add(newItem);
                         * </pre>
                         * 
                         * 
                         * <p>
                         * Objects of the following type(s) are allowed in the list
                         * {@link TExtractSubjectObjectType }
                         * 
                         * 
                         */
                        public List<TExtractSubjectObjectType> getRealtyTypeText() {
                            if (realtyTypeText == null) {
                                realtyTypeText = new ArrayList<TExtractSubjectObjectType>();
                            }
                            return this.realtyTypeText;
                        }

                    }


                    /**
                     * <p>Java class for anonymous complex type.
                     * 
                     * <p>The following schema fragment specifies the expected content contained within this class.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *       &lt;choice>
                     *         &lt;element name="Territory_Russia" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                     *         &lt;element name="Regions">
                     *           &lt;complexType>
                     *             &lt;complexContent>
                     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *                 &lt;sequence>
                     *                   &lt;element name="Region" type="{}dRegionsRF" maxOccurs="unbounded"/>
                     *                 &lt;/sequence>
                     *               &lt;/restriction>
                     *             &lt;/complexContent>
                     *           &lt;/complexType>
                     *         &lt;/element>
                     *       &lt;/choice>
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                        "territoryRussia",
                        "regions"
                    })
                    public static class Territory {

                        @XmlElement(name = "Territory_Russia")
                        protected Boolean territoryRussia;
                        @XmlElement(name = "Regions")
                        protected RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.Territory.Regions regions;

                        /**
                         * Gets the value of the territoryRussia property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link Boolean }
                         *     
                         */
                        public Boolean isTerritoryRussia() {
                            return territoryRussia;
                        }

                        /**
                         * Sets the value of the territoryRussia property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link Boolean }
                         *     
                         */
                        public void setTerritoryRussia(Boolean value) {
                            this.territoryRussia = value;
                        }

                        /**
                         * Gets the value of the regions property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.Territory.Regions }
                         *     
                         */
                        public RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.Territory.Regions getRegions() {
                            return regions;
                        }

                        /**
                         * Sets the value of the regions property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.Territory.Regions }
                         *     
                         */
                        public void setRegions(RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject.Territory.Regions value) {
                            this.regions = value;
                        }


                        /**
                         * <p>Java class for anonymous complex type.
                         * 
                         * <p>The following schema fragment specifies the expected content contained within this class.
                         * 
                         * <pre>
                         * &lt;complexType>
                         *   &lt;complexContent>
                         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                         *       &lt;sequence>
                         *         &lt;element name="Region" type="{}dRegionsRF" maxOccurs="unbounded"/>
                         *       &lt;/sequence>
                         *     &lt;/restriction>
                         *   &lt;/complexContent>
                         * &lt;/complexType>
                         * </pre>
                         * 
                         * 
                         */
                        @XmlAccessorType(XmlAccessType.FIELD)
                        @XmlType(name = "", propOrder = {
                            "region"
                        })
                        public static class Regions {

                            @XmlElement(name = "Region", required = true)
                            protected List<String> region;

                            /**
                             * Gets the value of the region property.
                             * 
                             * <p>
                             * This accessor method returns a reference to the live list,
                             * not a snapshot. Therefore any modification you make to the
                             * returned list will be present inside the JAXB object.
                             * This is why there is not a <CODE>set</CODE> method for the region property.
                             * 
                             * <p>
                             * For example, to add a new item, do as follows:
                             * <pre>
                             *    getRegion().add(newItem);
                             * </pre>
                             * 
                             * 
                             * <p>
                             * Objects of the following type(s) are allowed in the list
                             * {@link String }
                             * 
                             * 
                             */
                            public List<String> getRegion() {
                                if (region == null) {
                                    region = new ArrayList<String>();
                                }
                                return this.region;
                            }

                        }

                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Owner" type="{}tOwner" maxOccurs="unbounded"/>
                 *         &lt;element name="Territory">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;choice>
                 *                   &lt;element name="Territory_Russia" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                 *                   &lt;element name="Regions">
                 *                     &lt;complexType>
                 *                       &lt;complexContent>
                 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                           &lt;sequence>
                 *                             &lt;element name="Region" type="{}dRegionsRF" maxOccurs="unbounded"/>
                 *                           &lt;/sequence>
                 *                         &lt;/restriction>
                 *                       &lt;/complexContent>
                 *                     &lt;/complexType>
                 *                   &lt;/element>
                 *                 &lt;/choice>
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "owner",
                    "territory"
                })
                public static class ExtractSubjectRegion {

                    @XmlElement(name = "Owner", required = true)
                    protected List<TOwner> owner;
                    @XmlElement(name = "Territory", required = true)
                    protected RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion.Territory territory;

                    /**
                     * Gets the value of the owner property.
                     * 
                     * <p>
                     * This accessor method returns a reference to the live list,
                     * not a snapshot. Therefore any modification you make to the
                     * returned list will be present inside the JAXB object.
                     * This is why there is not a <CODE>set</CODE> method for the owner property.
                     * 
                     * <p>
                     * For example, to add a new item, do as follows:
                     * <pre>
                     *    getOwner().add(newItem);
                     * </pre>
                     * 
                     * 
                     * <p>
                     * Objects of the following type(s) are allowed in the list
                     * {@link TOwner }
                     * 
                     * 
                     */
                    public List<TOwner> getOwner() {
                        if (owner == null) {
                            owner = new ArrayList<TOwner>();
                        }
                        return this.owner;
                    }

                    /**
                     * Gets the value of the territory property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion.Territory }
                     *     
                     */
                    public RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion.Territory getTerritory() {
                        return territory;
                    }

                    /**
                     * Sets the value of the territory property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion.Territory }
                     *     
                     */
                    public void setTerritory(RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion.Territory value) {
                        this.territory = value;
                    }


                    /**
                     * <p>Java class for anonymous complex type.
                     * 
                     * <p>The following schema fragment specifies the expected content contained within this class.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *       &lt;choice>
                     *         &lt;element name="Territory_Russia" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                     *         &lt;element name="Regions">
                     *           &lt;complexType>
                     *             &lt;complexContent>
                     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *                 &lt;sequence>
                     *                   &lt;element name="Region" type="{}dRegionsRF" maxOccurs="unbounded"/>
                     *                 &lt;/sequence>
                     *               &lt;/restriction>
                     *             &lt;/complexContent>
                     *           &lt;/complexType>
                     *         &lt;/element>
                     *       &lt;/choice>
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                        "territoryRussia",
                        "regions"
                    })
                    public static class Territory {

                        @XmlElement(name = "Territory_Russia")
                        protected Boolean territoryRussia;
                        @XmlElement(name = "Regions")
                        protected RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion.Territory.Regions regions;

                        /**
                         * Gets the value of the territoryRussia property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link Boolean }
                         *     
                         */
                        public Boolean isTerritoryRussia() {
                            return territoryRussia;
                        }

                        /**
                         * Sets the value of the territoryRussia property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link Boolean }
                         *     
                         */
                        public void setTerritoryRussia(Boolean value) {
                            this.territoryRussia = value;
                        }

                        /**
                         * Gets the value of the regions property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion.Territory.Regions }
                         *     
                         */
                        public RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion.Territory.Regions getRegions() {
                            return regions;
                        }

                        /**
                         * Sets the value of the regions property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion.Territory.Regions }
                         *     
                         */
                        public void setRegions(RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion.Territory.Regions value) {
                            this.regions = value;
                        }


                        /**
                         * <p>Java class for anonymous complex type.
                         * 
                         * <p>The following schema fragment specifies the expected content contained within this class.
                         * 
                         * <pre>
                         * &lt;complexType>
                         *   &lt;complexContent>
                         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                         *       &lt;sequence>
                         *         &lt;element name="Region" type="{}dRegionsRF" maxOccurs="unbounded"/>
                         *       &lt;/sequence>
                         *     &lt;/restriction>
                         *   &lt;/complexContent>
                         * &lt;/complexType>
                         * </pre>
                         * 
                         * 
                         */
                        @XmlAccessorType(XmlAccessType.FIELD)
                        @XmlType(name = "", propOrder = {
                            "region"
                        })
                        public static class Regions {

                            @XmlElement(name = "Region", required = true)
                            protected List<String> region;

                            /**
                             * Gets the value of the region property.
                             * 
                             * <p>
                             * This accessor method returns a reference to the live list,
                             * not a snapshot. Therefore any modification you make to the
                             * returned list will be present inside the JAXB object.
                             * This is why there is not a <CODE>set</CODE> method for the region property.
                             * 
                             * <p>
                             * For example, to add a new item, do as follows:
                             * <pre>
                             *    getRegion().add(newItem);
                             * </pre>
                             * 
                             * 
                             * <p>
                             * Objects of the following type(s) are allowed in the list
                             * {@link String }
                             * 
                             * 
                             */
                            public List<String> getRegion() {
                                if (region == null) {
                                    region = new ArrayList<String>();
                                }
                                return this.region;
                            }

                        }

                    }

                }

            }

        }

    }

}

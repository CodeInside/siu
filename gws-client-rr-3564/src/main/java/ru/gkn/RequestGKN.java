/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */


package ru.gkn;

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
@XmlRootElement(name = "RequestGKN")
public class RequestGKN {

    @XmlElement(required = true)
    protected RequestGKN.EDocument eDocument;
    @XmlElement(name = "Request", required = true)
    protected RequestGKN.Request request;

    /**
     * Gets the value of the eDocument property.
     * 
     * @return
     *     possible object is
     *     {@link RequestGKN.EDocument }
     *     
     */
    public RequestGKN.EDocument getEDocument() {
        return eDocument;
    }

    /**
     * Sets the value of the eDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestGKN.EDocument }
     *     
     */
    public void setEDocument(RequestGKN.EDocument value) {
        this.eDocument = value;
    }

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link RequestGKN.Request }
     *     
     */
    public RequestGKN.Request getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestGKN.Request }
     *     
     */
    public void setRequest(RequestGKN.Request value) {
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
     *       &lt;attribute name="Version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="1.03" />
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
                return "1.03";
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
     *         &lt;element name="Declarant" type="{}tOwner"/>
     *         &lt;element name="RequiredData">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice>
     *                   &lt;element name="CadastralPassport">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Object" type="{}tObject"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="KV">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="ObjectLot" type="{}tObjectLot"/>
     *                             &lt;element name="KV1" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                             &lt;element name="KV2" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *                             &lt;element name="KV3" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *                             &lt;element name="KV4" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *                             &lt;element name="KV5" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *                             &lt;element name="KV6" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="KS_ZU_KS">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="ObjectLot" type="{}tObjectLot"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="CopyDocument">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Object" type="{}tObject"/>
     *                             &lt;element name="DocCopy" type="{}tCopyDocument"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="CancellationCertificate">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="ObjectOKS" type="{}tObjectOKS"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="KPT">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="CadastralNumber">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;maxLength value="40"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="Orient" minOccurs="0">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;maxLength value="4000"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
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
     *         &lt;element name="Payment_Documents" minOccurs="0">
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
     *         &lt;element name="MunicipalService" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Service" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="Name_Service" use="required">
     *                             &lt;simpleType>
     *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                 &lt;maxLength value="1000"/>
     *                               &lt;/restriction>
     *                             &lt;/simpleType>
     *                           &lt;/attribute>
     *                           &lt;attribute name="Code_Service" use="required">
     *                             &lt;simpleType>
     *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                 &lt;length value="11"/>
     *                               &lt;/restriction>
     *                             &lt;/simpleType>
     *                           &lt;/attribute>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
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
        "declarant",
        "requiredData",
        "delivery",
        "appliedDocuments",
        "paymentDocuments",
        "municipalService"
    })
    public static class Request {

        @XmlElement(name = "Declarant", required = true)
        protected TOwner declarant;
        @XmlElement(name = "RequiredData", required = true)
        protected RequestGKN.Request.RequiredData requiredData;
        @XmlElement(name = "Delivery", required = true)
        protected TDelivery delivery;
        @XmlElement(name = "Applied_Documents", required = true)
        protected RequestGKN.Request.AppliedDocuments appliedDocuments;
        @XmlElement(name = "Payment_Documents")
        protected RequestGKN.Request.PaymentDocuments paymentDocuments;
        @XmlElement(name = "MunicipalService")
        protected RequestGKN.Request.MunicipalService municipalService;

        /**
         * Gets the value of the declarant property.
         * 
         * @return
         *     possible object is
         *     {@link TOwner }
         *     
         */
        public TOwner getDeclarant() {
            return declarant;
        }

        /**
         * Sets the value of the declarant property.
         * 
         * @param value
         *     allowed object is
         *     {@link TOwner }
         *     
         */
        public void setDeclarant(TOwner value) {
            this.declarant = value;
        }

        /**
         * Gets the value of the requiredData property.
         * 
         * @return
         *     possible object is
         *     {@link RequestGKN.Request.RequiredData }
         *     
         */
        public RequestGKN.Request.RequiredData getRequiredData() {
            return requiredData;
        }

        /**
         * Sets the value of the requiredData property.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestGKN.Request.RequiredData }
         *     
         */
        public void setRequiredData(RequestGKN.Request.RequiredData value) {
            this.requiredData = value;
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
         *     {@link RequestGKN.Request.AppliedDocuments }
         *     
         */
        public RequestGKN.Request.AppliedDocuments getAppliedDocuments() {
            return appliedDocuments;
        }

        /**
         * Sets the value of the appliedDocuments property.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestGKN.Request.AppliedDocuments }
         *     
         */
        public void setAppliedDocuments(RequestGKN.Request.AppliedDocuments value) {
            this.appliedDocuments = value;
        }

        /**
         * Gets the value of the paymentDocuments property.
         * 
         * @return
         *     possible object is
         *     {@link RequestGKN.Request.PaymentDocuments }
         *     
         */
        public RequestGKN.Request.PaymentDocuments getPaymentDocuments() {
            return paymentDocuments;
        }

        /**
         * Sets the value of the paymentDocuments property.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestGKN.Request.PaymentDocuments }
         *     
         */
        public void setPaymentDocuments(RequestGKN.Request.PaymentDocuments value) {
            this.paymentDocuments = value;
        }

        /**
         * Gets the value of the municipalService property.
         * 
         * @return
         *     possible object is
         *     {@link RequestGKN.Request.MunicipalService }
         *     
         */
        public RequestGKN.Request.MunicipalService getMunicipalService() {
            return municipalService;
        }

        /**
         * Sets the value of the municipalService property.
         * 
         * @param value
         *     allowed object is
         *     {@link RequestGKN.Request.MunicipalService }
         *     
         */
        public void setMunicipalService(RequestGKN.Request.MunicipalService value) {
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
         *         &lt;element name="Service" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="Name_Service" use="required">
         *                   &lt;simpleType>
         *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                       &lt;maxLength value="1000"/>
         *                     &lt;/restriction>
         *                   &lt;/simpleType>
         *                 &lt;/attribute>
         *                 &lt;attribute name="Code_Service" use="required">
         *                   &lt;simpleType>
         *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                       &lt;length value="11"/>
         *                     &lt;/restriction>
         *                   &lt;/simpleType>
         *                 &lt;/attribute>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
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
            "itemNormativeAct",
            "answerDate",
            "post"
        })
        public static class MunicipalService {

            @XmlElement(name = "Service")
            protected RequestGKN.Request.MunicipalService.Service service;
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
             *     {@link RequestGKN.Request.MunicipalService.Service }
             *     
             */
            public RequestGKN.Request.MunicipalService.Service getService() {
                return service;
            }

            /**
             * Sets the value of the service property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGKN.Request.MunicipalService.Service }
             *     
             */
            public void setService(RequestGKN.Request.MunicipalService.Service value) {
                this.service = value;
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
         *       &lt;choice>
         *         &lt;element name="CadastralPassport">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Object" type="{}tObject"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="KV">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="ObjectLot" type="{}tObjectLot"/>
         *                   &lt;element name="KV1" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                   &lt;element name="KV2" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
         *                   &lt;element name="KV3" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
         *                   &lt;element name="KV4" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
         *                   &lt;element name="KV5" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
         *                   &lt;element name="KV6" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="KS_ZU_KS">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="ObjectLot" type="{}tObjectLot"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="CopyDocument">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Object" type="{}tObject"/>
         *                   &lt;element name="DocCopy" type="{}tCopyDocument"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="CancellationCertificate">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="ObjectOKS" type="{}tObjectOKS"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="KPT">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="CadastralNumber">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;maxLength value="40"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="Orient" minOccurs="0">
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
            "cadastralPassport",
            "kv",
            "kszuks",
            "copyDocument",
            "cancellationCertificate",
            "kpt"
        })
        public static class RequiredData {

            @XmlElement(name = "CadastralPassport")
            protected RequestGKN.Request.RequiredData.CadastralPassport cadastralPassport;
            @XmlElement(name = "KV")
            protected RequestGKN.Request.RequiredData.KV kv;
            @XmlElement(name = "KS_ZU_KS")
            protected RequestGKN.Request.RequiredData.KSZUKS kszuks;
            @XmlElement(name = "CopyDocument")
            protected RequestGKN.Request.RequiredData.CopyDocument copyDocument;
            @XmlElement(name = "CancellationCertificate")
            protected RequestGKN.Request.RequiredData.CancellationCertificate cancellationCertificate;
            @XmlElement(name = "KPT")
            protected RequestGKN.Request.RequiredData.KPT kpt;

            /**
             * Gets the value of the cadastralPassport property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGKN.Request.RequiredData.CadastralPassport }
             *     
             */
            public RequestGKN.Request.RequiredData.CadastralPassport getCadastralPassport() {
                return cadastralPassport;
            }

            /**
             * Sets the value of the cadastralPassport property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGKN.Request.RequiredData.CadastralPassport }
             *     
             */
            public void setCadastralPassport(RequestGKN.Request.RequiredData.CadastralPassport value) {
                this.cadastralPassport = value;
            }

            /**
             * Gets the value of the kv property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGKN.Request.RequiredData.KV }
             *     
             */
            public RequestGKN.Request.RequiredData.KV getKV() {
                return kv;
            }

            /**
             * Sets the value of the kv property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGKN.Request.RequiredData.KV }
             *     
             */
            public void setKV(RequestGKN.Request.RequiredData.KV value) {
                this.kv = value;
            }

            /**
             * Gets the value of the kszuks property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGKN.Request.RequiredData.KSZUKS }
             *     
             */
            public RequestGKN.Request.RequiredData.KSZUKS getKSZUKS() {
                return kszuks;
            }

            /**
             * Sets the value of the kszuks property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGKN.Request.RequiredData.KSZUKS }
             *     
             */
            public void setKSZUKS(RequestGKN.Request.RequiredData.KSZUKS value) {
                this.kszuks = value;
            }

            /**
             * Gets the value of the copyDocument property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGKN.Request.RequiredData.CopyDocument }
             *     
             */
            public RequestGKN.Request.RequiredData.CopyDocument getCopyDocument() {
                return copyDocument;
            }

            /**
             * Sets the value of the copyDocument property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGKN.Request.RequiredData.CopyDocument }
             *     
             */
            public void setCopyDocument(RequestGKN.Request.RequiredData.CopyDocument value) {
                this.copyDocument = value;
            }

            /**
             * Gets the value of the cancellationCertificate property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGKN.Request.RequiredData.CancellationCertificate }
             *     
             */
            public RequestGKN.Request.RequiredData.CancellationCertificate getCancellationCertificate() {
                return cancellationCertificate;
            }

            /**
             * Sets the value of the cancellationCertificate property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGKN.Request.RequiredData.CancellationCertificate }
             *     
             */
            public void setCancellationCertificate(RequestGKN.Request.RequiredData.CancellationCertificate value) {
                this.cancellationCertificate = value;
            }

            /**
             * Gets the value of the kpt property.
             * 
             * @return
             *     possible object is
             *     {@link RequestGKN.Request.RequiredData.KPT }
             *     
             */
            public RequestGKN.Request.RequiredData.KPT getKPT() {
                return kpt;
            }

            /**
             * Sets the value of the kpt property.
             * 
             * @param value
             *     allowed object is
             *     {@link RequestGKN.Request.RequiredData.KPT }
             *     
             */
            public void setKPT(RequestGKN.Request.RequiredData.KPT value) {
                this.kpt = value;
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
             *         &lt;element name="Object" type="{}tObject"/>
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
            public static class CadastralPassport {

                @XmlElement(name = "Object", required = true)
                protected TObject object;

                /**
                 * Gets the value of the object property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TObject }
                 *     
                 */
                public TObject getObject() {
                    return object;
                }

                /**
                 * Sets the value of the object property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TObject }
                 *     
                 */
                public void setObject(TObject value) {
                    this.object = value;
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
             *         &lt;element name="ObjectOKS" type="{}tObjectOKS"/>
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
                "objectOKS"
            })
            public static class CancellationCertificate {

                @XmlElement(name = "ObjectOKS", required = true)
                protected TObjectOKS objectOKS;

                /**
                 * Gets the value of the objectOKS property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TObjectOKS }
                 *     
                 */
                public TObjectOKS getObjectOKS() {
                    return objectOKS;
                }

                /**
                 * Sets the value of the objectOKS property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TObjectOKS }
                 *     
                 */
                public void setObjectOKS(TObjectOKS value) {
                    this.objectOKS = value;
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
             *         &lt;element name="Object" type="{}tObject"/>
             *         &lt;element name="DocCopy" type="{}tCopyDocument"/>
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
                "object",
                "docCopy"
            })
            public static class CopyDocument {

                @XmlElement(name = "Object", required = true)
                protected TObject object;
                @XmlElement(name = "DocCopy", required = true)
                protected TCopyDocument docCopy;

                /**
                 * Gets the value of the object property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TObject }
                 *     
                 */
                public TObject getObject() {
                    return object;
                }

                /**
                 * Sets the value of the object property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TObject }
                 *     
                 */
                public void setObject(TObject value) {
                    this.object = value;
                }

                /**
                 * Gets the value of the docCopy property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TCopyDocument }
                 *     
                 */
                public TCopyDocument getDocCopy() {
                    return docCopy;
                }

                /**
                 * Sets the value of the docCopy property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TCopyDocument }
                 *     
                 */
                public void setDocCopy(TCopyDocument value) {
                    this.docCopy = value;
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
             *         &lt;element name="CadastralNumber">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;maxLength value="40"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="Orient" minOccurs="0">
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
                "cadastralNumber",
                "orient"
            })
            public static class KPT {

                @XmlElement(name = "CadastralNumber", required = true)
                protected String cadastralNumber;
                @XmlElement(name = "Orient")
                protected String orient;

                /**
                 * Gets the value of the cadastralNumber property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCadastralNumber() {
                    return cadastralNumber;
                }

                /**
                 * Sets the value of the cadastralNumber property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCadastralNumber(String value) {
                    this.cadastralNumber = value;
                }

                /**
                 * Gets the value of the orient property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getOrient() {
                    return orient;
                }

                /**
                 * Sets the value of the orient property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setOrient(String value) {
                    this.orient = value;
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
             *         &lt;element name="ObjectLot" type="{}tObjectLot"/>
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
                "objectLot"
            })
            public static class KSZUKS {

                @XmlElement(name = "ObjectLot", required = true)
                protected TObjectLot objectLot;

                /**
                 * Gets the value of the objectLot property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TObjectLot }
                 *     
                 */
                public TObjectLot getObjectLot() {
                    return objectLot;
                }

                /**
                 * Sets the value of the objectLot property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TObjectLot }
                 *     
                 */
                public void setObjectLot(TObjectLot value) {
                    this.objectLot = value;
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
             *         &lt;element name="ObjectLot" type="{}tObjectLot"/>
             *         &lt;element name="KV1" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *         &lt;element name="KV2" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
             *         &lt;element name="KV3" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
             *         &lt;element name="KV4" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
             *         &lt;element name="KV5" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
             *         &lt;element name="KV6" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
                "objectLot",
                "kv1",
                "kv2",
                "kv3",
                "kv4",
                "kv5",
                "kv6"
            })
            public static class KV {

                @XmlElement(name = "ObjectLot", required = true)
                protected TObjectLot objectLot;
                @XmlElement(name = "KV1")
                protected boolean kv1;
                @XmlElement(name = "KV2")
                protected Boolean kv2;
                @XmlElement(name = "KV3")
                protected Boolean kv3;
                @XmlElement(name = "KV4")
                protected Boolean kv4;
                @XmlElement(name = "KV5")
                protected Boolean kv5;
                @XmlElement(name = "KV6")
                protected Boolean kv6;

                /**
                 * Gets the value of the objectLot property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TObjectLot }
                 *     
                 */
                public TObjectLot getObjectLot() {
                    return objectLot;
                }

                /**
                 * Sets the value of the objectLot property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TObjectLot }
                 *     
                 */
                public void setObjectLot(TObjectLot value) {
                    this.objectLot = value;
                }

                /**
                 * Gets the value of the kv1 property.
                 * 
                 */
                public boolean isKV1() {
                    return kv1;
                }

                /**
                 * Sets the value of the kv1 property.
                 * 
                 */
                public void setKV1(boolean value) {
                    this.kv1 = value;
                }

                /**
                 * Gets the value of the kv2 property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public Boolean isKV2() {
                    return kv2;
                }

                /**
                 * Sets the value of the kv2 property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setKV2(Boolean value) {
                    this.kv2 = value;
                }

                /**
                 * Gets the value of the kv3 property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public Boolean isKV3() {
                    return kv3;
                }

                /**
                 * Sets the value of the kv3 property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setKV3(Boolean value) {
                    this.kv3 = value;
                }

                /**
                 * Gets the value of the kv4 property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public Boolean isKV4() {
                    return kv4;
                }

                /**
                 * Sets the value of the kv4 property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setKV4(Boolean value) {
                    this.kv4 = value;
                }

                /**
                 * Gets the value of the kv5 property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public Boolean isKV5() {
                    return kv5;
                }

                /**
                 * Sets the value of the kv5 property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setKV5(Boolean value) {
                    this.kv5 = value;
                }

                /**
                 * Gets the value of the kv6 property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public Boolean isKV6() {
                    return kv6;
                }

                /**
                 * Sets the value of the kv6 property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setKV6(Boolean value) {
                    this.kv6 = value;
                }

            }

        }

    }

}

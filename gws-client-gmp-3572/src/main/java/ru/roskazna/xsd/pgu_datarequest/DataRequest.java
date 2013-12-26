
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.pgu_datarequest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig.SignatureType;
import ru.roskazna.xsd.requesttemplate.RequestTemplate;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataRequest", propOrder = {
    "supplierBillIDs",
    "payers",
    "applicationIDs",
    "signature"
})
public class DataRequest
    extends RequestTemplate
{

    @XmlElement(name = "SupplierBillIDs")
    protected DataRequest.SupplierBillIDs supplierBillIDs;
    @XmlElement(name = "Payers")
    protected DataRequest.Payers payers;
    @XmlElement(name = "ApplicationIDs")
    protected DataRequest.ApplicationIDs applicationIDs;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;

    /**
     * Gets the value of the supplierBillIDs property.
     * 
     * @return
     *     possible object is
     *     {@link DataRequest.SupplierBillIDs }
     *     
     */
    public DataRequest.SupplierBillIDs getSupplierBillIDs() {
        return supplierBillIDs;
    }

    /**
     * Sets the value of the supplierBillIDs property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataRequest.SupplierBillIDs }
     *     
     */
    public void setSupplierBillIDs(DataRequest.SupplierBillIDs value) {
        this.supplierBillIDs = value;
    }

    /**
     * Gets the value of the payers property.
     * 
     * @return
     *     possible object is
     *     {@link DataRequest.Payers }
     *     
     */
    public DataRequest.Payers getPayers() {
        return payers;
    }

    /**
     * Sets the value of the payers property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataRequest.Payers }
     *     
     */
    public void setPayers(DataRequest.Payers value) {
        this.payers = value;
    }

    /**
     * Gets the value of the applicationIDs property.
     * 
     * @return
     *     possible object is
     *     {@link DataRequest.ApplicationIDs }
     *     
     */
    public DataRequest.ApplicationIDs getApplicationIDs() {
        return applicationIDs;
    }

    /**
     * Sets the value of the applicationIDs property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataRequest.ApplicationIDs }
     *     
     */
    public void setApplicationIDs(DataRequest.ApplicationIDs value) {
        this.applicationIDs = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
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
     *         &lt;element name="ApplicationID" maxOccurs="unbounded">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
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
        "applicationID"
    })
    public static class ApplicationIDs {

        @XmlElement(name = "ApplicationID", required = true)
        protected List<String> applicationID;

        /**
         * Gets the value of the applicationID property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the applicationID property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getApplicationID().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getApplicationID() {
            if (applicationID == null) {
                applicationID = new ArrayList<String>();
            }
            return this.applicationID;
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
     *         &lt;element name="PayerIdentifier" maxOccurs="unbounded">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
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
        "payerIdentifier"
    })
    public static class Payers {

        @XmlElement(name = "PayerIdentifier", required = true)
        protected List<String> payerIdentifier;

        /**
         * Gets the value of the payerIdentifier property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the payerIdentifier property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPayerIdentifier().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getPayerIdentifier() {
            if (payerIdentifier == null) {
                payerIdentifier = new ArrayList<String>();
            }
            return this.payerIdentifier;
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
     *         &lt;element name="SupplierBillID" maxOccurs="unbounded">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
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
        "supplierBillID"
    })
    public static class SupplierBillIDs {

        @XmlElement(name = "SupplierBillID", required = true)
        protected List<String> supplierBillID;

        /**
         * Gets the value of the supplierBillID property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the supplierBillID property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSupplierBillID().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getSupplierBillID() {
            if (supplierBillID == null) {
                supplierBillID = new ArrayList<String>();
            }
            return this.supplierBillID;
        }

    }

}


/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.exportpaymentsresponse;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig.SignatureType;
import ru.roskazna.xsd.responsetemplate.ResponseTemplate;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExportPaymentsResponse", propOrder = {
    "payments",
    "signature"
})
public class ExportPaymentsResponse
    extends ResponseTemplate
{

    @XmlElement(name = "Payments")
    protected ExportPaymentsResponse.Payments payments;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;

    /**
     * Gets the value of the payments property.
     * 
     * @return
     *     possible object is
     *     {@link ExportPaymentsResponse.Payments }
     *     
     */
    public ExportPaymentsResponse.Payments getPayments() {
        return payments;
    }

    /**
     * Sets the value of the payments property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExportPaymentsResponse.Payments }
     *     
     */
    public void setPayments(ExportPaymentsResponse.Payments value) {
        this.payments = value;
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
     *         &lt;element name="PaymentInfo" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="PaymentData" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
     *                   &lt;element name="PaymentSignature" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
        "paymentInfo"
    })
    public static class Payments {

        @XmlElement(name = "PaymentInfo", required = true)
        protected List<ExportPaymentsResponse.Payments.PaymentInfo> paymentInfo;

        /**
         * Gets the value of the paymentInfo property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the paymentInfo property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPaymentInfo().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ExportPaymentsResponse.Payments.PaymentInfo }
         * 
         * 
         */
        public List<ExportPaymentsResponse.Payments.PaymentInfo> getPaymentInfo() {
            if (paymentInfo == null) {
                paymentInfo = new ArrayList<ExportPaymentsResponse.Payments.PaymentInfo>();
            }
            return this.paymentInfo;
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
         *         &lt;element name="PaymentData" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
         *         &lt;element name="PaymentSignature" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
            "paymentData",
            "paymentSignature"
        })
        public static class PaymentInfo {

            @XmlElement(name = "PaymentData", required = true)
            protected byte[] paymentData;
            @XmlElement(name = "PaymentSignature")
            protected byte[] paymentSignature;

            /**
             * Gets the value of the paymentData property.
             * 
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getPaymentData() {
                return paymentData;
            }

            /**
             * Sets the value of the paymentData property.
             * 
             * @param value
             *     allowed object is
             *     byte[]
             */
            public void setPaymentData(byte[] value) {
                this.paymentData = value;
            }

            /**
             * Gets the value of the paymentSignature property.
             * 
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getPaymentSignature() {
                return paymentSignature;
            }

            /**
             * Sets the value of the paymentSignature property.
             * 
             * @param value
             *     allowed object is
             *     byte[]
             */
            public void setPaymentSignature(byte[] value) {
                this.paymentSignature = value;
            }

        }

    }

}

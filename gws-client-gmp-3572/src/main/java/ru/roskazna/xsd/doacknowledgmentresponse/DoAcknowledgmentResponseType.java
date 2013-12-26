
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.doacknowledgmentresponse;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import org.w3._2000._09.xmldsig.SignatureType;
import ru.roskazna.xsd.quittance.QuittanceType;
import ru.roskazna.xsd.responsetemplate.ResponseTemplate;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DoAcknowledgmentResponseType", propOrder = {
    "quittances",
    "signature"
})
@XmlRootElement(name = "DoAcknowledgmentResponse")
public class DoAcknowledgmentResponseType
    extends ResponseTemplate
{

    @XmlElement(name = "Quittances")
    protected DoAcknowledgmentResponseType.Quittances quittances;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;

    /**
     * Gets the value of the quittances property.
     * 
     * @return
     *     possible object is
     *     {@link DoAcknowledgmentResponseType.Quittances }
     *     
     */
    public DoAcknowledgmentResponseType.Quittances getQuittances() {
        return quittances;
    }

    /**
     * Sets the value of the quittances property.
     * 
     * @param value
     *     allowed object is
     *     {@link DoAcknowledgmentResponseType.Quittances }
     *     
     */
    public void setQuittances(DoAcknowledgmentResponseType.Quittances value) {
        this.quittances = value;
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
     *         &lt;element name="Quittance" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;extension base="{http://roskazna.ru/xsd/Quittance}QuittanceType">
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
        "quittance"
    })
    public static class Quittances {

        @XmlElement(name = "Quittance", required = true)
        protected List<DoAcknowledgmentResponseType.Quittances.Quittance> quittance;

        /**
         * Gets the value of the quittance property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the quittance property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getQuittance().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DoAcknowledgmentResponseType.Quittances.Quittance }
         * 
         * 
         */
        public List<DoAcknowledgmentResponseType.Quittances.Quittance> getQuittance() {
            if (quittance == null) {
                quittance = new ArrayList<DoAcknowledgmentResponseType.Quittances.Quittance>();
            }
            return this.quittance;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;extension base="{http://roskazna.ru/xsd/Quittance}QuittanceType">
         *     &lt;/extension>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Quittance
            extends QuittanceType
        {


        }

    }

}

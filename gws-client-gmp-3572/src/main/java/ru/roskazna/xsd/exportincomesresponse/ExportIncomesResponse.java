
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.exportincomesresponse;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig.SignatureType;
import ru.roskazna.xsd.responsetemplate.ResponseTemplate;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExportIncomesResponse", propOrder = {
    "incomes",
    "signature"
})
public class ExportIncomesResponse
    extends ResponseTemplate
{

    @XmlElement(name = "Incomes")
    protected ExportIncomesResponse.Incomes incomes;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;

    /**
     * Gets the value of the incomes property.
     * 
     * @return
     *     possible object is
     *     {@link ExportIncomesResponse.Incomes }
     *     
     */
    public ExportIncomesResponse.Incomes getIncomes() {
        return incomes;
    }

    /**
     * Sets the value of the incomes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExportIncomesResponse.Incomes }
     *     
     */
    public void setIncomes(ExportIncomesResponse.Incomes value) {
        this.incomes = value;
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
     *         &lt;element name="IncometInfo" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="IncomeData" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
     *                   &lt;element name="IncomeSignature" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
        "incometInfo"
    })
    public static class Incomes {

        @XmlElement(name = "IncometInfo", required = true)
        protected List<ExportIncomesResponse.Incomes.IncometInfo> incometInfo;

        /**
         * Gets the value of the incometInfo property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the incometInfo property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIncometInfo().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ExportIncomesResponse.Incomes.IncometInfo }
         * 
         * 
         */
        public List<ExportIncomesResponse.Incomes.IncometInfo> getIncometInfo() {
            if (incometInfo == null) {
                incometInfo = new ArrayList<ExportIncomesResponse.Incomes.IncometInfo>();
            }
            return this.incometInfo;
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
         *         &lt;element name="IncomeData" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
         *         &lt;element name="IncomeSignature" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
            "incomeData",
            "incomeSignature"
        })
        public static class IncometInfo {

            @XmlElement(name = "IncomeData", required = true)
            protected byte[] incomeData;
            @XmlElement(name = "IncomeSignature")
            protected byte[] incomeSignature;

            /**
             * Gets the value of the incomeData property.
             * 
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getIncomeData() {
                return incomeData;
            }

            /**
             * Sets the value of the incomeData property.
             * 
             * @param value
             *     allowed object is
             *     byte[]
             */
            public void setIncomeData(byte[] value) {
                this.incomeData = value;
            }

            /**
             * Gets the value of the incomeSignature property.
             * 
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getIncomeSignature() {
                return incomeSignature;
            }

            /**
             * Sets the value of the incomeSignature property.
             * 
             * @param value
             *     allowed object is
             *     byte[]
             */
            public void setIncomeSignature(byte[] value) {
                this.incomeSignature = value;
            }

        }

    }

}

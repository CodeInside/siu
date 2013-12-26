
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.paymentinfo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3._2000._09.xmldsig.SignatureType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IncomeInfoType", propOrder = {
    "changeStatus",
    "consDocNumber",
    "consDocDate",
    "payeeINN",
    "payeeKPP",
    "kbk",
    "okato",
    "incomeRows",
    "tofk",
    "signature"
})
public class IncomeInfoType {

    @XmlElement(name = "ChangeStatus", required = true)
    protected String changeStatus;
    @XmlElement(name = "ConsDocNumber", required = true)
    protected String consDocNumber;
    @XmlElement(name = "ConsDocDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar consDocDate;
    @XmlElement(required = true)
    protected String payeeINN;
    @XmlElement(required = true)
    protected String payeeKPP;
    @XmlElement(name = "KBK", required = true)
    protected String kbk;
    @XmlElement(name = "OKATO", required = true)
    protected String okato;
    @XmlElement(name = "IncomeRows", required = true)
    protected IncomeInfoType.IncomeRows incomeRows;
    @XmlElement(name = "TOFK")
    protected String tofk;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;
    @XmlAttribute(name = "Version")
    protected String version;

    /**
     * Gets the value of the changeStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChangeStatus() {
        return changeStatus;
    }

    /**
     * Sets the value of the changeStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChangeStatus(String value) {
        this.changeStatus = value;
    }

    /**
     * Gets the value of the consDocNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsDocNumber() {
        return consDocNumber;
    }

    /**
     * Sets the value of the consDocNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsDocNumber(String value) {
        this.consDocNumber = value;
    }

    /**
     * Gets the value of the consDocDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getConsDocDate() {
        return consDocDate;
    }

    /**
     * Sets the value of the consDocDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setConsDocDate(XMLGregorianCalendar value) {
        this.consDocDate = value;
    }

    /**
     * Gets the value of the payeeINN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayeeINN() {
        return payeeINN;
    }

    /**
     * Sets the value of the payeeINN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayeeINN(String value) {
        this.payeeINN = value;
    }

    /**
     * Gets the value of the payeeKPP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayeeKPP() {
        return payeeKPP;
    }

    /**
     * Sets the value of the payeeKPP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayeeKPP(String value) {
        this.payeeKPP = value;
    }

    /**
     * Gets the value of the kbk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKBK() {
        return kbk;
    }

    /**
     * Sets the value of the kbk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKBK(String value) {
        this.kbk = value;
    }

    /**
     * Gets the value of the okato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOKATO() {
        return okato;
    }

    /**
     * Sets the value of the okato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOKATO(String value) {
        this.okato = value;
    }

    /**
     * Gets the value of the incomeRows property.
     * 
     * @return
     *     possible object is
     *     {@link IncomeInfoType.IncomeRows }
     *     
     */
    public IncomeInfoType.IncomeRows getIncomeRows() {
        return incomeRows;
    }

    /**
     * Sets the value of the incomeRows property.
     * 
     * @param value
     *     allowed object is
     *     {@link IncomeInfoType.IncomeRows }
     *     
     */
    public void setIncomeRows(IncomeInfoType.IncomeRows value) {
        this.incomeRows = value;
    }

    /**
     * Gets the value of the tofk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTOFK() {
        return tofk;
    }

    /**
     * Sets the value of the tofk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTOFK(String value) {
        this.tofk = value;
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
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="IncomeRow" type="{http://roskazna.ru/xsd/PaymentInfo}PaymentType" maxOccurs="unbounded"/>
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
        "incomeRow"
    })
    public static class IncomeRows {

        @XmlElement(name = "IncomeRow", required = true)
        protected List<PaymentType> incomeRow;

        /**
         * Gets the value of the incomeRow property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the incomeRow property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIncomeRow().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PaymentType }
         * 
         * 
         */
        public List<PaymentType> getIncomeRow() {
            if (incomeRow == null) {
                incomeRow = new ArrayList<PaymentType>();
            }
            return this.incomeRow;
        }

    }

}

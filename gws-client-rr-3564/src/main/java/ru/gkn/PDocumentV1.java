/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */


package ru.gkn;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pDocument_v1", propOrder = {
    "codeDocument",
    "name",
    "series",
    "number",
    "date",
    "issueOrgan",
    "numberReg",
    "dateReg",
    "duration",
    "register",
    "images",
    "desc"
})
@XmlSeeAlso({
    TCopyDocument.class,
    TDocument.class
})
public class PDocumentV1 {

    @XmlElement(name = "Code_Document", required = true)
    protected String codeDocument;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "Series")
    protected String series;
    @XmlElement(name = "Number")
    protected String number;
    @XmlElement(name = "Date")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar date;
    @XmlElement(name = "IssueOrgan")
    protected String issueOrgan;
    @XmlElement(name = "NumberReg")
    protected String numberReg;
    @XmlElement(name = "DateReg")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateReg;
    @XmlElement(name = "Duration")
    protected TDuration duration;
    @XmlElement(name = "Register")
    protected String register;
    @XmlElement(name = "Images")
    protected TImages images;
    @XmlElement(name = "Desc")
    protected String desc;

    /**
     * Gets the value of the codeDocument property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeDocument() {
        return codeDocument;
    }

    /**
     * Sets the value of the codeDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeDocument(String value) {
        this.codeDocument = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the series property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeries() {
        return series;
    }

    /**
     * Sets the value of the series property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeries(String value) {
        this.series = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumber(String value) {
        this.number = value;
    }

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
     * Gets the value of the issueOrgan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueOrgan() {
        return issueOrgan;
    }

    /**
     * Sets the value of the issueOrgan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueOrgan(String value) {
        this.issueOrgan = value;
    }

    /**
     * Gets the value of the numberReg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberReg() {
        return numberReg;
    }

    /**
     * Sets the value of the numberReg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberReg(String value) {
        this.numberReg = value;
    }

    /**
     * Gets the value of the dateReg property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateReg() {
        return dateReg;
    }

    /**
     * Sets the value of the dateReg property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateReg(XMLGregorianCalendar value) {
        this.dateReg = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     * @return
     *     possible object is
     *     {@link TDuration }
     *     
     */
    public TDuration getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDuration }
     *     
     */
    public void setDuration(TDuration value) {
        this.duration = value;
    }

    /**
     * Gets the value of the register property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegister() {
        return register;
    }

    /**
     * Sets the value of the register property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegister(String value) {
        this.register = value;
    }

    /**
     * Gets the value of the images property.
     * 
     * @return
     *     possible object is
     *     {@link TImages }
     *     
     */
    public TImages getImages() {
        return images;
    }

    /**
     * Sets the value of the images property.
     * 
     * @param value
     *     allowed object is
     *     {@link TImages }
     *     
     */
    public void setImages(TImages value) {
        this.images = value;
    }

    /**
     * Gets the value of the desc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets the value of the desc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }

}

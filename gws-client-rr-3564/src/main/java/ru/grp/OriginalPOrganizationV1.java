
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.grp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "codeOPF",
    "inn",
    "document",
    "location",
    "eMail",
    "codeCPP",
    "codeOGRN",
    "phone",
    "country",
    "contactInfo",
    "regDate",
    "registrationAgency"
})
@XmlSeeAlso({
    POrganizationV1 .class
})
public class OriginalPOrganizationV1 {

    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "Code_OPF")
    protected String codeOPF;
    @XmlElement(name = "INN")
    protected String inn;
    @XmlElement(name = "Document")
    protected PIdentityOrganizationDocumentV1 document;
    @XmlElement(name = "Location")
    protected PAddressV1 location;
    @XmlElement(name = "E-mail")
    protected String eMail;
    @XmlElement(name = "Code_CPP")
    protected String codeCPP;
    @XmlElement(name = "Code_OGRN")
    protected String codeOGRN;
    @XmlElement(name = "Phone")
    protected String phone;
    @XmlElement(name = "Country")
    protected String country;
    @XmlElement(name = "Contact_Info")
    protected String contactInfo;
    @XmlElement(name = "RegDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar regDate;
    @XmlElement(name = "Registration_Agency")
    protected String registrationAgency;

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
     * Gets the value of the codeOPF property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeOPF() {
        return codeOPF;
    }

    /**
     * Sets the value of the codeOPF property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeOPF(String value) {
        this.codeOPF = value;
    }

    /**
     * Gets the value of the inn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINN() {
        return inn;
    }

    /**
     * Sets the value of the inn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINN(String value) {
        this.inn = value;
    }

    /**
     * Gets the value of the document property.
     * 
     * @return
     *     possible object is
     *     {@link PIdentityOrganizationDocumentV1 }
     *     
     */
    public PIdentityOrganizationDocumentV1 getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     * 
     * @param value
     *     allowed object is
     *     {@link PIdentityOrganizationDocumentV1 }
     *     
     */
    public void setDocument(PIdentityOrganizationDocumentV1 value) {
        this.document = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link PAddressV1 }
     *     
     */
    public PAddressV1 getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link PAddressV1 }
     *     
     */
    public void setLocation(PAddressV1 value) {
        this.location = value;
    }

    /**
     * Gets the value of the eMail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEMail() {
        return eMail;
    }

    /**
     * Sets the value of the eMail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEMail(String value) {
        this.eMail = value;
    }

    /**
     * Gets the value of the codeCPP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeCPP() {
        return codeCPP;
    }

    /**
     * Sets the value of the codeCPP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeCPP(String value) {
        this.codeCPP = value;
    }

    /**
     * Gets the value of the codeOGRN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeOGRN() {
        return codeOGRN;
    }

    /**
     * Sets the value of the codeOGRN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeOGRN(String value) {
        this.codeOGRN = value;
    }

    /**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the contactInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the value of the contactInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactInfo(String value) {
        this.contactInfo = value;
    }

    /**
     * Gets the value of the regDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegDate() {
        return regDate;
    }

    /**
     * Sets the value of the regDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegDate(XMLGregorianCalendar value) {
        this.regDate = value;
    }

    /**
     * Gets the value of the registrationAgency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistrationAgency() {
        return registrationAgency;
    }

    /**
     * Sets the value of the registrationAgency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistrationAgency(String value) {
        this.registrationAgency = value;
    }

}

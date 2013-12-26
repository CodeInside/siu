
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
    "fio",
    "dateBirth",
    "placeBirth",
    "document",
    "location",
    "locationPermanent",
    "locationTemporary",
    "eMail",
    "phone",
    "familyStatus",
    "citizenship",
    "isUnderAge",
    "contactInfo",
    "sex",
    "snils"
})
@XmlSeeAlso({
    PPersonV1 .class
})
public class OriginalPPersonV1 {

    @XmlElement(name = "FIO", required = true)
    protected TFIO fio;
    @XmlElement(name = "DateBirth")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateBirth;
    @XmlElement(name = "Place_Birth")
    protected String placeBirth;
    @XmlElement(name = "Document")
    protected PIdentityPersonDocumentV1 document;
    @XmlElement(name = "Location")
    protected PAddressV1 location;
    @XmlElement(name = "LocationPermanent")
    protected PAddressV1 locationPermanent;
    @XmlElement(name = "LocationTemporary")
    protected PAddressV1 locationTemporary;
    @XmlElement(name = "E-mail")
    protected String eMail;
    @XmlElement(name = "Phone")
    protected String phone;
    @XmlElement(name = "FamilyStatus")
    protected String familyStatus;
    @XmlElement(name = "Citizenship")
    protected String citizenship;
    @XmlElement(name = "IsUnderAge")
    protected Boolean isUnderAge;
    @XmlElement(name = "Contact_Info")
    protected String contactInfo;
    @XmlElement(name = "Sex")
    protected SSex sex;
    @XmlElement(name = "SNILS")
    protected String snils;

    /**
     * Gets the value of the fio property.
     * 
     * @return
     *     possible object is
     *     {@link TFIO }
     *     
     */
    public TFIO getFIO() {
        return fio;
    }

    /**
     * Sets the value of the fio property.
     * 
     * @param value
     *     allowed object is
     *     {@link TFIO }
     *     
     */
    public void setFIO(TFIO value) {
        this.fio = value;
    }

    /**
     * Gets the value of the dateBirth property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateBirth() {
        return dateBirth;
    }

    /**
     * Sets the value of the dateBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateBirth(XMLGregorianCalendar value) {
        this.dateBirth = value;
    }

    /**
     * Gets the value of the placeBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaceBirth() {
        return placeBirth;
    }

    /**
     * Sets the value of the placeBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaceBirth(String value) {
        this.placeBirth = value;
    }

    /**
     * Gets the value of the document property.
     * 
     * @return
     *     possible object is
     *     {@link PIdentityPersonDocumentV1 }
     *     
     */
    public PIdentityPersonDocumentV1 getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     * 
     * @param value
     *     allowed object is
     *     {@link PIdentityPersonDocumentV1 }
     *     
     */
    public void setDocument(PIdentityPersonDocumentV1 value) {
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
     * Gets the value of the locationPermanent property.
     * 
     * @return
     *     possible object is
     *     {@link PAddressV1 }
     *     
     */
    public PAddressV1 getLocationPermanent() {
        return locationPermanent;
    }

    /**
     * Sets the value of the locationPermanent property.
     * 
     * @param value
     *     allowed object is
     *     {@link PAddressV1 }
     *     
     */
    public void setLocationPermanent(PAddressV1 value) {
        this.locationPermanent = value;
    }

    /**
     * Gets the value of the locationTemporary property.
     * 
     * @return
     *     possible object is
     *     {@link PAddressV1 }
     *     
     */
    public PAddressV1 getLocationTemporary() {
        return locationTemporary;
    }

    /**
     * Sets the value of the locationTemporary property.
     * 
     * @param value
     *     allowed object is
     *     {@link PAddressV1 }
     *     
     */
    public void setLocationTemporary(PAddressV1 value) {
        this.locationTemporary = value;
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
     * Gets the value of the familyStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFamilyStatus() {
        return familyStatus;
    }

    /**
     * Sets the value of the familyStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFamilyStatus(String value) {
        this.familyStatus = value;
    }

    /**
     * Gets the value of the citizenship property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCitizenship() {
        return citizenship;
    }

    /**
     * Sets the value of the citizenship property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCitizenship(String value) {
        this.citizenship = value;
    }

    /**
     * Gets the value of the isUnderAge property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsUnderAge() {
        return isUnderAge;
    }

    /**
     * Sets the value of the isUnderAge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsUnderAge(Boolean value) {
        this.isUnderAge = value;
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
     * Gets the value of the sex property.
     * 
     * @return
     *     possible object is
     *     {@link SSex }
     *     
     */
    public SSex getSex() {
        return sex;
    }

    /**
     * Sets the value of the sex property.
     * 
     * @param value
     *     allowed object is
     *     {@link SSex }
     *     
     */
    public void setSex(SSex value) {
        this.sex = value;
    }

    /**
     * Gets the value of the snils property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSNILS() {
        return snils;
    }

    /**
     * Sets the value of the snils property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSNILS(String value) {
        this.snils = value;
    }

}

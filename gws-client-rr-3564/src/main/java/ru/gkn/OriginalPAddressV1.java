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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "codeOKATO",
    "codeKLADR",
    "postalCode",
    "region",
    "district",
    "city",
    "urbanDistrict",
    "sovietVillage",
    "locality",
    "street",
    "level1",
    "level2",
    "level3",
    "apartment",
    "other",
    "note"
})
@XmlSeeAlso({
    PAddressV1 .class
})
public class OriginalPAddressV1 {

    @XmlElement(name = "Code_OKATO")
    protected String codeOKATO;
    @XmlElement(name = "Code_KLADR")
    protected String codeKLADR;
    @XmlElement(name = "Postal_Code")
    protected String postalCode;
    @XmlElement(name = "Region", required = true)
    protected String region;
    @XmlElement(name = "District")
    protected TDistrict district;
    @XmlElement(name = "City")
    protected TCity city;
    @XmlElement(name = "Urban_District")
    protected TUrbanDistrict urbanDistrict;
    @XmlElement(name = "Soviet_Village")
    protected TSovietVillage sovietVillage;
    @XmlElement(name = "Locality")
    protected TLocality locality;
    @XmlElement(name = "Street")
    protected TStreet street;
    @XmlElement(name = "Level1")
    protected TLevel1 level1;
    @XmlElement(name = "Level2")
    protected TLevel2 level2;
    @XmlElement(name = "Level3")
    protected TLevel3 level3;
    @XmlElement(name = "Apartment")
    protected TApartment apartment;
    @XmlElement(name = "Other")
    protected String other;
    @XmlElement(name = "Note")
    protected String note;

    /**
     * Gets the value of the codeOKATO property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeOKATO() {
        return codeOKATO;
    }

    /**
     * Sets the value of the codeOKATO property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeOKATO(String value) {
        this.codeOKATO = value;
    }

    /**
     * Gets the value of the codeKLADR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeKLADR() {
        return codeKLADR;
    }

    /**
     * Sets the value of the codeKLADR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeKLADR(String value) {
        this.codeKLADR = value;
    }

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalCode(String value) {
        this.postalCode = value;
    }

    /**
     * Gets the value of the region property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the value of the region property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegion(String value) {
        this.region = value;
    }

    /**
     * Gets the value of the district property.
     * 
     * @return
     *     possible object is
     *     {@link TDistrict }
     *     
     */
    public TDistrict getDistrict() {
        return district;
    }

    /**
     * Sets the value of the district property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDistrict }
     *     
     */
    public void setDistrict(TDistrict value) {
        this.district = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link TCity }
     *     
     */
    public TCity getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCity }
     *     
     */
    public void setCity(TCity value) {
        this.city = value;
    }

    /**
     * Gets the value of the urbanDistrict property.
     * 
     * @return
     *     possible object is
     *     {@link TUrbanDistrict }
     *     
     */
    public TUrbanDistrict getUrbanDistrict() {
        return urbanDistrict;
    }

    /**
     * Sets the value of the urbanDistrict property.
     * 
     * @param value
     *     allowed object is
     *     {@link TUrbanDistrict }
     *     
     */
    public void setUrbanDistrict(TUrbanDistrict value) {
        this.urbanDistrict = value;
    }

    /**
     * Gets the value of the sovietVillage property.
     * 
     * @return
     *     possible object is
     *     {@link TSovietVillage }
     *     
     */
    public TSovietVillage getSovietVillage() {
        return sovietVillage;
    }

    /**
     * Sets the value of the sovietVillage property.
     * 
     * @param value
     *     allowed object is
     *     {@link TSovietVillage }
     *     
     */
    public void setSovietVillage(TSovietVillage value) {
        this.sovietVillage = value;
    }

    /**
     * Gets the value of the locality property.
     * 
     * @return
     *     possible object is
     *     {@link TLocality }
     *     
     */
    public TLocality getLocality() {
        return locality;
    }

    /**
     * Sets the value of the locality property.
     * 
     * @param value
     *     allowed object is
     *     {@link TLocality }
     *     
     */
    public void setLocality(TLocality value) {
        this.locality = value;
    }

    /**
     * Gets the value of the street property.
     * 
     * @return
     *     possible object is
     *     {@link TStreet }
     *     
     */
    public TStreet getStreet() {
        return street;
    }

    /**
     * Sets the value of the street property.
     * 
     * @param value
     *     allowed object is
     *     {@link TStreet }
     *     
     */
    public void setStreet(TStreet value) {
        this.street = value;
    }

    /**
     * Gets the value of the level1 property.
     * 
     * @return
     *     possible object is
     *     {@link TLevel1 }
     *     
     */
    public TLevel1 getLevel1() {
        return level1;
    }

    /**
     * Sets the value of the level1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TLevel1 }
     *     
     */
    public void setLevel1(TLevel1 value) {
        this.level1 = value;
    }

    /**
     * Gets the value of the level2 property.
     * 
     * @return
     *     possible object is
     *     {@link TLevel2 }
     *     
     */
    public TLevel2 getLevel2() {
        return level2;
    }

    /**
     * Sets the value of the level2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TLevel2 }
     *     
     */
    public void setLevel2(TLevel2 value) {
        this.level2 = value;
    }

    /**
     * Gets the value of the level3 property.
     * 
     * @return
     *     possible object is
     *     {@link TLevel3 }
     *     
     */
    public TLevel3 getLevel3() {
        return level3;
    }

    /**
     * Sets the value of the level3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TLevel3 }
     *     
     */
    public void setLevel3(TLevel3 value) {
        this.level3 = value;
    }

    /**
     * Gets the value of the apartment property.
     * 
     * @return
     *     possible object is
     *     {@link TApartment }
     *     
     */
    public TApartment getApartment() {
        return apartment;
    }

    /**
     * Sets the value of the apartment property.
     * 
     * @param value
     *     allowed object is
     *     {@link TApartment }
     *     
     */
    public void setApartment(TApartment value) {
        this.apartment = value;
    }

    /**
     * Gets the value of the other property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOther() {
        return other;
    }

    /**
     * Sets the value of the other property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOther(String value) {
        this.other = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

}

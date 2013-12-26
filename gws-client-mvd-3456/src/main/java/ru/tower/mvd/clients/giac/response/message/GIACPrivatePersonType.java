
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.tower.mvd.clients.giac.response.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GIACPrivatePersonType", propOrder = {
    "placeOfBirth",
    "snils",
    "address"
})
public class GIACPrivatePersonType
    extends PrivatePersonType
{

    @XmlElement(name = "PlaceOfBirth", required = true)
    protected PlaceOfBirthType placeOfBirth;
    @XmlElement(name = "SNILS")
    protected String snils;
    @XmlElement(name = "Address", required = true)
    protected GIACPrivatePersonType.Address address;

    /**
     * Gets the value of the placeOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link PlaceOfBirthType }
     *     
     */
    public PlaceOfBirthType getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     * Sets the value of the placeOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlaceOfBirthType }
     *     
     */
    public void setPlaceOfBirth(PlaceOfBirthType value) {
        this.placeOfBirth = value;
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

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link GIACPrivatePersonType.Address }
     *     
     */
    public GIACPrivatePersonType.Address getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link GIACPrivatePersonType.Address }
     *     
     */
    public void setAddress(GIACPrivatePersonType.Address value) {
        this.address = value;
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
     *         &lt;element name="Region" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="RegistrationPlace" type="{http://tower.ru/mvd/clients/giac/response}String250Type"/>
     *         &lt;element name="TypeRegistration" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "region",
        "registrationPlace",
        "typeRegistration"
    })
    public static class Address {

        @XmlElement(name = "Region")
        protected String region;
        @XmlElement(name = "RegistrationPlace", required = true)
        protected String registrationPlace;
        @XmlElement(name = "TypeRegistration", required = true)
        protected String typeRegistration;

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
         * Gets the value of the registrationPlace property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRegistrationPlace() {
            return registrationPlace;
        }

        /**
         * Sets the value of the registrationPlace property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRegistrationPlace(String value) {
            this.registrationPlace = value;
        }

        /**
         * Gets the value of the typeRegistration property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTypeRegistration() {
            return typeRegistration;
        }

        /**
         * Sets the value of the typeRegistration property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTypeRegistration(String value) {
            this.typeRegistration = value;
        }

    }

}

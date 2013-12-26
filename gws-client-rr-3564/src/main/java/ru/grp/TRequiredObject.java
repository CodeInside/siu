
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.grp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tRequiredObject", propOrder = {
    "parcel",
    "object"
})
public class TRequiredObject {

    @XmlElement(name = "Parcel")
    protected TRequiredObject.Parcel parcel;
    @XmlElement(name = "Object")
    protected TRequiredObject.Object object;
    @XmlAttribute(name = "id_db_egrp")
    protected String idDbEgrp;

    /**
     * Gets the value of the parcel property.
     * 
     * @return
     *     possible object is
     *     {@link TRequiredObject.Parcel }
     *     
     */
    public TRequiredObject.Parcel getParcel() {
        return parcel;
    }

    /**
     * Sets the value of the parcel property.
     * 
     * @param value
     *     allowed object is
     *     {@link TRequiredObject.Parcel }
     *     
     */
    public void setParcel(TRequiredObject.Parcel value) {
        this.parcel = value;
    }

    /**
     * Gets the value of the object property.
     * 
     * @return
     *     possible object is
     *     {@link TRequiredObject.Object }
     *     
     */
    public TRequiredObject.Object getObject() {
        return object;
    }

    /**
     * Sets the value of the object property.
     * 
     * @param value
     *     allowed object is
     *     {@link TRequiredObject.Object }
     *     
     */
    public void setObject(TRequiredObject.Object value) {
        this.object = value;
    }

    /**
     * Gets the value of the idDbEgrp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDbEgrp() {
        return idDbEgrp;
    }

    /**
     * Sets the value of the idDbEgrp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDbEgrp(String value) {
        this.idDbEgrp = value;
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
     *         &lt;element name="ObjKind" type="{}tExtractObjectType"/>
     *         &lt;element name="CadastralNumbers" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice>
     *                   &lt;element name="CadastralNumber">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;maxLength value="40"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ConditionalCadastralNumber">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;maxLength value="100"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                 &lt;/choice>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Area" type="{}tArea" minOccurs="0"/>
     *         &lt;element name="Location" type="{}pAddress_v1"/>
     *         &lt;element name="DopInfo" type="{}tDopInfo" minOccurs="0"/>
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
        "objKind",
        "cadastralNumbers",
        "area",
        "location",
        "dopInfo"
    })
    public static class Object {

        @XmlElement(name = "ObjKind", required = true)
        protected TExtractObjectType objKind;
        @XmlElement(name = "CadastralNumbers")
        protected TRequiredObject.Object.CadastralNumbers cadastralNumbers;
        @XmlElement(name = "Area")
        protected TArea area;
        @XmlElement(name = "Location", required = true)
        protected PAddressV1 location;
        @XmlElement(name = "DopInfo")
        protected TDopInfo dopInfo;

        /**
         * Gets the value of the objKind property.
         * 
         * @return
         *     possible object is
         *     {@link TExtractObjectType }
         *     
         */
        public TExtractObjectType getObjKind() {
            return objKind;
        }

        /**
         * Sets the value of the objKind property.
         * 
         * @param value
         *     allowed object is
         *     {@link TExtractObjectType }
         *     
         */
        public void setObjKind(TExtractObjectType value) {
            this.objKind = value;
        }

        /**
         * Gets the value of the cadastralNumbers property.
         * 
         * @return
         *     possible object is
         *     {@link TRequiredObject.Object.CadastralNumbers }
         *     
         */
        public TRequiredObject.Object.CadastralNumbers getCadastralNumbers() {
            return cadastralNumbers;
        }

        /**
         * Sets the value of the cadastralNumbers property.
         * 
         * @param value
         *     allowed object is
         *     {@link TRequiredObject.Object.CadastralNumbers }
         *     
         */
        public void setCadastralNumbers(TRequiredObject.Object.CadastralNumbers value) {
            this.cadastralNumbers = value;
        }

        /**
         * Gets the value of the area property.
         * 
         * @return
         *     possible object is
         *     {@link TArea }
         *     
         */
        public TArea getArea() {
            return area;
        }

        /**
         * Sets the value of the area property.
         * 
         * @param value
         *     allowed object is
         *     {@link TArea }
         *     
         */
        public void setArea(TArea value) {
            this.area = value;
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
         * Gets the value of the dopInfo property.
         * 
         * @return
         *     possible object is
         *     {@link TDopInfo }
         *     
         */
        public TDopInfo getDopInfo() {
            return dopInfo;
        }

        /**
         * Sets the value of the dopInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link TDopInfo }
         *     
         */
        public void setDopInfo(TDopInfo value) {
            this.dopInfo = value;
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
         *       &lt;choice>
         *         &lt;element name="CadastralNumber">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;maxLength value="40"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ConditionalCadastralNumber">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;maxLength value="100"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *       &lt;/choice>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "cadastralNumber",
            "conditionalCadastralNumber"
        })
        public static class CadastralNumbers {

            @XmlElement(name = "CadastralNumber")
            protected String cadastralNumber;
            @XmlElement(name = "ConditionalCadastralNumber")
            protected String conditionalCadastralNumber;

            /**
             * Gets the value of the cadastralNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCadastralNumber() {
                return cadastralNumber;
            }

            /**
             * Sets the value of the cadastralNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCadastralNumber(String value) {
                this.cadastralNumber = value;
            }

            /**
             * Gets the value of the conditionalCadastralNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getConditionalCadastralNumber() {
                return conditionalCadastralNumber;
            }

            /**
             * Sets the value of the conditionalCadastralNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setConditionalCadastralNumber(String value) {
                this.conditionalCadastralNumber = value;
            }

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
     *         &lt;element name="Description">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice>
     *                   &lt;element name="CadastralNumbers">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="CadastralNumber">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;maxLength value="40"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="Areas" type="{}tArea" minOccurs="0"/>
     *                             &lt;element name="Location" type="{}pAddress_v1" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="Address">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="CadastralNumber" minOccurs="0">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;maxLength value="40"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="Areas" type="{}tArea"/>
     *                             &lt;element name="Location" type="{}pAddress_v1"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/choice>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="DopInfo" type="{}tDopInfoParcel" minOccurs="0"/>
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
        "description",
        "dopInfo"
    })
    public static class Parcel {

        @XmlElement(name = "Description", required = true)
        protected TRequiredObject.Parcel.Description description;
        @XmlElement(name = "DopInfo")
        protected TDopInfoParcel dopInfo;

        /**
         * Gets the value of the description property.
         * 
         * @return
         *     possible object is
         *     {@link TRequiredObject.Parcel.Description }
         *     
         */
        public TRequiredObject.Parcel.Description getDescription() {
            return description;
        }

        /**
         * Sets the value of the description property.
         * 
         * @param value
         *     allowed object is
         *     {@link TRequiredObject.Parcel.Description }
         *     
         */
        public void setDescription(TRequiredObject.Parcel.Description value) {
            this.description = value;
        }

        /**
         * Gets the value of the dopInfo property.
         * 
         * @return
         *     possible object is
         *     {@link TDopInfoParcel }
         *     
         */
        public TDopInfoParcel getDopInfo() {
            return dopInfo;
        }

        /**
         * Sets the value of the dopInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link TDopInfoParcel }
         *     
         */
        public void setDopInfo(TDopInfoParcel value) {
            this.dopInfo = value;
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
         *       &lt;choice>
         *         &lt;element name="CadastralNumbers">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="CadastralNumber">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;maxLength value="40"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="Areas" type="{}tArea" minOccurs="0"/>
         *                   &lt;element name="Location" type="{}pAddress_v1" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="Address">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="CadastralNumber" minOccurs="0">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;maxLength value="40"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="Areas" type="{}tArea"/>
         *                   &lt;element name="Location" type="{}pAddress_v1"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/choice>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "cadastralNumbers",
            "address"
        })
        public static class Description {

            @XmlElement(name = "CadastralNumbers")
            protected TRequiredObject.Parcel.Description.CadastralNumbers cadastralNumbers;
            @XmlElement(name = "Address")
            protected TRequiredObject.Parcel.Description.Address address;

            /**
             * Gets the value of the cadastralNumbers property.
             * 
             * @return
             *     possible object is
             *     {@link TRequiredObject.Parcel.Description.CadastralNumbers }
             *     
             */
            public TRequiredObject.Parcel.Description.CadastralNumbers getCadastralNumbers() {
                return cadastralNumbers;
            }

            /**
             * Sets the value of the cadastralNumbers property.
             * 
             * @param value
             *     allowed object is
             *     {@link TRequiredObject.Parcel.Description.CadastralNumbers }
             *     
             */
            public void setCadastralNumbers(TRequiredObject.Parcel.Description.CadastralNumbers value) {
                this.cadastralNumbers = value;
            }

            /**
             * Gets the value of the address property.
             * 
             * @return
             *     possible object is
             *     {@link TRequiredObject.Parcel.Description.Address }
             *     
             */
            public TRequiredObject.Parcel.Description.Address getAddress() {
                return address;
            }

            /**
             * Sets the value of the address property.
             * 
             * @param value
             *     allowed object is
             *     {@link TRequiredObject.Parcel.Description.Address }
             *     
             */
            public void setAddress(TRequiredObject.Parcel.Description.Address value) {
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
             *         &lt;element name="CadastralNumber" minOccurs="0">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;maxLength value="40"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="Areas" type="{}tArea"/>
             *         &lt;element name="Location" type="{}pAddress_v1"/>
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
                "cadastralNumber",
                "areas",
                "location"
            })
            public static class Address {

                @XmlElement(name = "CadastralNumber")
                protected String cadastralNumber;
                @XmlElement(name = "Areas", required = true)
                protected TArea areas;
                @XmlElement(name = "Location", required = true)
                protected PAddressV1 location;

                /**
                 * Gets the value of the cadastralNumber property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCadastralNumber() {
                    return cadastralNumber;
                }

                /**
                 * Sets the value of the cadastralNumber property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCadastralNumber(String value) {
                    this.cadastralNumber = value;
                }

                /**
                 * Gets the value of the areas property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TArea }
                 *     
                 */
                public TArea getAreas() {
                    return areas;
                }

                /**
                 * Sets the value of the areas property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TArea }
                 *     
                 */
                public void setAreas(TArea value) {
                    this.areas = value;
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
             *         &lt;element name="CadastralNumber">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;maxLength value="40"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="Areas" type="{}tArea" minOccurs="0"/>
             *         &lt;element name="Location" type="{}pAddress_v1" minOccurs="0"/>
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
                "cadastralNumber",
                "areas",
                "location"
            })
            public static class CadastralNumbers {

                @XmlElement(name = "CadastralNumber", required = true)
                protected String cadastralNumber;
                @XmlElement(name = "Areas")
                protected TArea areas;
                @XmlElement(name = "Location")
                protected PAddressV1 location;

                /**
                 * Gets the value of the cadastralNumber property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCadastralNumber() {
                    return cadastralNumber;
                }

                /**
                 * Sets the value of the cadastralNumber property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCadastralNumber(String value) {
                    this.cadastralNumber = value;
                }

                /**
                 * Gets the value of the areas property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TArea }
                 *     
                 */
                public TArea getAreas() {
                    return areas;
                }

                /**
                 * Sets the value of the areas property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TArea }
                 *     
                 */
                public void setAreas(TArea value) {
                    this.areas = value;
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

            }

        }

    }

}

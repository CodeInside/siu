
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.tower.mvd.clients.giac.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseInfoType", propOrder = {

})
public class ResponseInfoType {

    @XmlElement(name = "Subdivision", required = true)
    protected ResponseInfoType.Subdivision subdivision;

    /**
     * Gets the value of the subdivision property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseInfoType.Subdivision }
     *     
     */
    public ResponseInfoType.Subdivision getSubdivision() {
        return subdivision;
    }

    /**
     * Sets the value of the subdivision property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseInfoType.Subdivision }
     *     
     */
    public void setSubdivision(ResponseInfoType.Subdivision value) {
        this.subdivision = value;
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
     *       &lt;all>
     *         &lt;element name="Result" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
     *       &lt;/all>
     *       &lt;attribute name="code" use="required" type="{http://tower.ru/mvd/clients/giac/request}RegionCode" />
     *       &lt;attribute name="date" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="found" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {

    })
    public static class Subdivision {

        @XmlElement(name = "Result", required = true)
        protected byte[] result;
        @XmlAttribute(required = true)
        protected String code;
        @XmlAttribute
        protected String date;
        @XmlAttribute
        protected String found;
        @XmlAttribute
        protected String name;

        /**
         * Gets the value of the result property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getResult() {
            return result;
        }

        /**
         * Sets the value of the result property.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setResult(byte[] value) {
            this.result = ((byte[]) value);
        }

        /**
         * Gets the value of the code property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCode() {
            return code;
        }

        /**
         * Sets the value of the code property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCode(String value) {
            this.code = value;
        }

        /**
         * Gets the value of the date property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDate() {
            return date;
        }

        /**
         * Sets the value of the date property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDate(String value) {
            this.date = value;
        }

        /**
         * Gets the value of the found property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFound() {
            return found;
        }

        /**
         * Sets the value of the found property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFound(String value) {
            this.found = value;
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

    }

}

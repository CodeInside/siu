
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.tower.mvd.clients.giac.response.message;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseHeaderType", propOrder = {
    "initialRegNumber",
    "service",
    "reason",
    "originator"
})
@XmlSeeAlso({
    HeaderType.class
})
public class BaseHeaderType {

    @XmlElement(name = "InitialRegNumber", required = true)
    protected BaseHeaderType.InitialRegNumber initialRegNumber;
    @XmlElement(name = "Service", required = true)
    protected BaseHeaderType.Service service;
    @XmlElement(name = "Reason", required = true)
    protected String reason;
    @XmlElement(name = "Originator", required = true)
    protected BaseHeaderType.Originator originator;
    @XmlAttribute(name = "msg_type", required = true)
    protected String msgType;
    @XmlAttribute(name = "msg_vid", required = true)
    protected String msgVid;
    @XmlAttribute(name = "from_foiv_id", required = true)
    protected String fromFoivId;
    @XmlAttribute(name = "from_foiv_name", required = true)
    protected String fromFoivName;
    @XmlAttribute(name = "from_system", required = true)
    protected String fromSystem;
    @XmlAttribute(name = "from_system_id", required = true)
    protected String fromSystemId;
    @XmlAttribute(name = "to_foiv_id", required = true)
    protected String toFoivId;
    @XmlAttribute(name = "to_foiv_name", required = true)
    protected String toFoivName;
    @XmlAttribute(name = "to_system", required = true)
    protected String toSystem;
    @XmlAttribute(name = "to_system_id", required = true)
    protected String toSystemId;
    @XmlAttribute
    protected String version;

    /**
     * Gets the value of the initialRegNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BaseHeaderType.InitialRegNumber }
     *     
     */
    public BaseHeaderType.InitialRegNumber getInitialRegNumber() {
        return initialRegNumber;
    }

    /**
     * Sets the value of the initialRegNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseHeaderType.InitialRegNumber }
     *     
     */
    public void setInitialRegNumber(BaseHeaderType.InitialRegNumber value) {
        this.initialRegNumber = value;
    }

    /**
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link BaseHeaderType.Service }
     *     
     */
    public BaseHeaderType.Service getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseHeaderType.Service }
     *     
     */
    public void setService(BaseHeaderType.Service value) {
        this.service = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
    }

    /**
     * Gets the value of the originator property.
     * 
     * @return
     *     possible object is
     *     {@link BaseHeaderType.Originator }
     *     
     */
    public BaseHeaderType.Originator getOriginator() {
        return originator;
    }

    /**
     * Sets the value of the originator property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseHeaderType.Originator }
     *     
     */
    public void setOriginator(BaseHeaderType.Originator value) {
        this.originator = value;
    }

    /**
     * Gets the value of the msgType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * Sets the value of the msgType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgType(String value) {
        this.msgType = value;
    }

    /**
     * Gets the value of the msgVid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgVid() {
        return msgVid;
    }

    /**
     * Sets the value of the msgVid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgVid(String value) {
        this.msgVid = value;
    }

    /**
     * Gets the value of the fromFoivId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromFoivId() {
        return fromFoivId;
    }

    /**
     * Sets the value of the fromFoivId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromFoivId(String value) {
        this.fromFoivId = value;
    }

    /**
     * Gets the value of the fromFoivName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromFoivName() {
        return fromFoivName;
    }

    /**
     * Sets the value of the fromFoivName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromFoivName(String value) {
        this.fromFoivName = value;
    }

    /**
     * Gets the value of the fromSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromSystem() {
        return fromSystem;
    }

    /**
     * Sets the value of the fromSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromSystem(String value) {
        this.fromSystem = value;
    }

    /**
     * Gets the value of the fromSystemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromSystemId() {
        return fromSystemId;
    }

    /**
     * Sets the value of the fromSystemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromSystemId(String value) {
        this.fromSystemId = value;
    }

    /**
     * Gets the value of the toFoivId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToFoivId() {
        return toFoivId;
    }

    /**
     * Sets the value of the toFoivId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToFoivId(String value) {
        this.toFoivId = value;
    }

    /**
     * Gets the value of the toFoivName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToFoivName() {
        return toFoivName;
    }

    /**
     * Sets the value of the toFoivName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToFoivName(String value) {
        this.toFoivName = value;
    }

    /**
     * Gets the value of the toSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToSystem() {
        return toSystem;
    }

    /**
     * Sets the value of the toSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToSystem(String value) {
        this.toSystem = value;
    }

    /**
     * Gets the value of the toSystemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToSystemId() {
        return toSystemId;
    }

    /**
     * Sets the value of the toSystemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToSystemId(String value) {
        this.toSystemId = value;
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
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>unsignedLong">
     *       &lt;attribute name="regtime" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class InitialRegNumber {

        @XmlValue
        @XmlSchemaType(name = "unsignedLong")
        protected BigInteger value;
        @XmlAttribute(required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar regtime;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setValue(BigInteger value) {
            this.value = value;
        }

        /**
         * Gets the value of the regtime property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getRegtime() {
            return regtime;
        }

        /**
         * Sets the value of the regtime property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setRegtime(XMLGregorianCalendar value) {
            this.regtime = value;
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
     *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="region" use="required" type="{http://tower.ru/mvd/clients/giac/response}RegionCode" />
     *       &lt;attribute name="fio" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Originator {

        @XmlAttribute(required = true)
        protected String name;
        @XmlAttribute(required = true)
        protected String code;
        @XmlAttribute(required = true)
        protected String region;
        @XmlAttribute(required = true)
        protected String fio;

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
         * Gets the value of the fio property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFio() {
            return fio;
        }

        /**
         * Sets the value of the fio property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFio(String value) {
            this.fio = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
     *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Service {

        @XmlValue
        protected String value;
        @XmlAttribute(required = true)
        @XmlSchemaType(name = "unsignedShort")
        protected int code;
        @XmlAttribute(required = true)
        protected String name;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the code property.
         * 
         */
        public int getCode() {
            return code;
        }

        /**
         * Sets the value of the code property.
         * 
         */
        public void setCode(int value) {
            this.code = value;
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

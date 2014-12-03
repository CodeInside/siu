//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.17 at 02:11:53 PM MSK 
//


package com.rstyle.skmv.snils_by_data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.rstyle.skmv.pfr.FIO;
import com.rstyle.skmv.pfr.GENDER;


/**
 * <p>Java class for SnilsByDataIn complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SnilsByDataIn">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fio" type="{http://pfr.skmv.rstyle.com}FIO"/>
 *         &lt;element name="gender" type="{http://pfr.skmv.rstyle.com}GENDER"/>
 *         &lt;element name="birthDate" type="{http://pfr.skmv.rstyle.com}PFR_DATE"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "request", namespace = "http://smev.gosuslugi.ru/rev120315")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SnilsByDataIn",  propOrder = {
    "fio",
    "gender",
    "birthDate"
})
public class SnilsByDataIn {

    @XmlElement(required = true)
    protected FIO fio;
    @XmlElement(required = true)
    protected GENDER gender;
    @XmlElement(required = true)
    protected String birthDate;

    /**
     * Gets the value of the fio property.
     * 
     * @return
     *     possible object is
     *     {@link FIO }
     *     
     */
    public FIO getFio() {
        return fio;
    }

    /**
     * Sets the value of the fio property.
     * 
     * @param value
     *     allowed object is
     *     {@link FIO }
     *     
     */
    public void setFio(FIO value) {
        this.fio = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link GENDER }
     *     
     */
    public GENDER getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link GENDER }
     *     
     */
    public void setGender(GENDER value) {
        this.gender = value;
    }

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthDate(String value) {
        this.birthDate = value;
    }

}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.14 at 12:23:19 PM MSK 
//


package ru.kdmid;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for smevreqAppData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="smevreqAppData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LAT_SURNAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LAT_FIRSTNAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COUNTRY_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "smevreqAppData", propOrder = {
    "latsurname",
    "latfirstname",
    "countrycode"
})
public class SmevreqAppData {

    @XmlElement(name = "LAT_SURNAME")
    protected String latsurname;
    @XmlElement(name = "LAT_FIRSTNAME")
    protected String latfirstname;
    @XmlElement(name = "COUNTRY_CODE")
    protected String countrycode;

    /**
     * Gets the value of the latsurname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLATSURNAME() {
        return latsurname;
    }

    /**
     * Sets the value of the latsurname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLATSURNAME(String value) {
        this.latsurname = value;
    }

    /**
     * Gets the value of the latfirstname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLATFIRSTNAME() {
        return latfirstname;
    }

    /**
     * Sets the value of the latfirstname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLATFIRSTNAME(String value) {
        this.latfirstname = value;
    }

    /**
     * Gets the value of the countrycode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOUNTRYCODE() {
        return countrycode;
    }

    /**
     * Sets the value of the countrycode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOUNTRYCODE(String value) {
        this.countrycode = value;
    }

}
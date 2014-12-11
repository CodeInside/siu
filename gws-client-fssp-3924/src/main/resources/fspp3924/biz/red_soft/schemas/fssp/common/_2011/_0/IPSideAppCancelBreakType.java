//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.schemas.fssp.common._2011._0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Документ IPSideAppCancelBreakType «Регистрация заявления об отмене постановления о прекращении
 *         ИП от взыскателя» (Z_010_V)
 * 
 * <p>Java class for IPSideAppCancelBreakType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPSideAppCancelBreakType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideAppType">
 *       &lt;sequence>
 *         &lt;element name="IPEndDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="IPEndGroundCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPEndGroundType"/>
 *         &lt;element name="IDLocationCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IDLocationCodeType"/>
 *         &lt;element name="directionDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="otherLocation" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_150" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPSideAppCancelBreakType", propOrder = {
    "ipEndDate",
    "ipEndGroundCode",
    "idLocationCode",
    "directionDate",
    "otherLocation"
})
public class IPSideAppCancelBreakType
    extends IPSideAppType
{

    @XmlElement(name = "IPEndDate", required = true)
    protected XMLGregorianCalendar ipEndDate;
    @XmlElement(name = "IPEndGroundCode", required = true)
    protected String ipEndGroundCode;
    @XmlElement(name = "IDLocationCode", required = true)
    protected String idLocationCode;
    protected XMLGregorianCalendar directionDate;
    protected String otherLocation;

    /**
     * Gets the value of the ipEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIPEndDate() {
        return ipEndDate;
    }

    /**
     * Sets the value of the ipEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIPEndDate(XMLGregorianCalendar value) {
        this.ipEndDate = value;
    }

    /**
     * Gets the value of the ipEndGroundCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPEndGroundCode() {
        return ipEndGroundCode;
    }

    /**
     * Sets the value of the ipEndGroundCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPEndGroundCode(String value) {
        this.ipEndGroundCode = value;
    }

    /**
     * Gets the value of the idLocationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDLocationCode() {
        return idLocationCode;
    }

    /**
     * Sets the value of the idLocationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDLocationCode(String value) {
        this.idLocationCode = value;
    }

    /**
     * Gets the value of the directionDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDirectionDate() {
        return directionDate;
    }

    /**
     * Sets the value of the directionDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDirectionDate(XMLGregorianCalendar value) {
        this.directionDate = value;
    }

    /**
     * Gets the value of the otherLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtherLocation() {
        return otherLocation;
    }

    /**
     * Sets the value of the otherLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtherLocation(String value) {
        this.otherLocation = value;
    }

}
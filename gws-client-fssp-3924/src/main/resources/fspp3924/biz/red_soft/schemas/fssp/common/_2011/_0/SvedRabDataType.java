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


/**
 * Тип сведений SvedRabDataType "Место получения дохода"
 * 
 * <p>Java class for SvedRabDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SvedRabDataType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InformationType">
 *       &lt;sequence>
 *         &lt;element name="NaimOrg" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000"/>
 *         &lt;element name="AdresJ" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300" minOccurs="0"/>
 *         &lt;element name="AdresF" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SvedRabDataType", propOrder = {
    "naimOrg",
    "adresJ",
    "adresF"
})
public class SvedRabDataType
    extends InformationType
{

    @XmlElement(name = "NaimOrg", required = true)
    protected String naimOrg;
    @XmlElement(name = "AdresJ")
    protected String adresJ;
    @XmlElement(name = "AdresF")
    protected String adresF;

    /**
     * Gets the value of the naimOrg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNaimOrg() {
        return naimOrg;
    }

    /**
     * Sets the value of the naimOrg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNaimOrg(String value) {
        this.naimOrg = value;
    }

    /**
     * Gets the value of the adresJ property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresJ() {
        return adresJ;
    }

    /**
     * Sets the value of the adresJ property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresJ(String value) {
        this.adresJ = value;
    }

    /**
     * Gets the value of the adresF property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresF() {
        return adresF;
    }

    /**
     * Sets the value of the adresF property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresF(String value) {
        this.adresF = value;
    }

}

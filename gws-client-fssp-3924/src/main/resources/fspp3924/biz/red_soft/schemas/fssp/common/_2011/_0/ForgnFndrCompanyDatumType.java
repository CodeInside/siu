//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.schemas.fssp.common._2011._0;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Сведения об иностранном учредителе — юридическом лице
 * 
 * <p>Java class for ForgnFndrCompanyDatumType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ForgnFndrCompanyDatumType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}CompanyDatumType">
 *       &lt;sequence>
 *         &lt;element name="capitalAmount" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money" minOccurs="0"/>
 *         &lt;element name="countryId" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FnsIdType" minOccurs="0"/>
 *         &lt;element name="countryCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}CountryCodeType" minOccurs="0"/>
 *         &lt;element name="countryName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ForgnFndrCompanyDatumType", propOrder = {
    "capitalAmount",
    "countryId",
    "countryCode",
    "countryName"
})
public class ForgnFndrCompanyDatumType
    extends CompanyDatumType
{

    protected BigDecimal capitalAmount;
    protected BigInteger countryId;
    protected String countryCode;
    protected String countryName;

    /**
     * Gets the value of the capitalAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCapitalAmount() {
        return capitalAmount;
    }

    /**
     * Sets the value of the capitalAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCapitalAmount(BigDecimal value) {
        this.capitalAmount = value;
    }

    /**
     * Gets the value of the countryId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCountryId() {
        return countryId;
    }

    /**
     * Sets the value of the countryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCountryId(BigInteger value) {
        this.countryId = value;
    }

    /**
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * Gets the value of the countryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the value of the countryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryName(String value) {
        this.countryName = value;
    }

}

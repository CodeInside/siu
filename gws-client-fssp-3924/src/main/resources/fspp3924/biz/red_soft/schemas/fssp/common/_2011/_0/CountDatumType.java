//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.schemas.fssp.common._2011._0;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Сведения об общем и переданном количестве сведений
 * 
 * <p>Java class for CountDatumType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CountDatumType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InformationType">
 *       &lt;sequence>
 *         &lt;element name="itemKindData" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}KindDataType"/>
 *         &lt;element name="total" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Integer"/>
 *         &lt;element name="include" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Integer" minOccurs="0"/>
 *         &lt;element name="max" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Integer" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CountDatumType", propOrder = {
    "itemKindData",
    "total",
    "include",
    "max"
})
public class CountDatumType
    extends InformationType
{

    @XmlElement(required = true)
    protected String itemKindData;
    @XmlElement(required = true)
    protected BigInteger total;
    protected BigInteger include;
    protected BigInteger max;

    /**
     * Gets the value of the itemKindData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemKindData() {
        return itemKindData;
    }

    /**
     * Sets the value of the itemKindData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemKindData(String value) {
        this.itemKindData = value;
    }

    /**
     * Gets the value of the total property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotal(BigInteger value) {
        this.total = value;
    }

    /**
     * Gets the value of the include property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getInclude() {
        return include;
    }

    /**
     * Sets the value of the include property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setInclude(BigInteger value) {
        this.include = value;
    }

    /**
     * Gets the value of the max property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMax() {
        return max;
    }

    /**
     * Sets the value of the max property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMax(BigInteger value) {
        this.max = value;
    }

}
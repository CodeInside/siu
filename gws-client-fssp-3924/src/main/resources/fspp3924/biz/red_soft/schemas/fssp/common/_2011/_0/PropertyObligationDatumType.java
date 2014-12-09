//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.schemas.fssp.common._2011._0;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Сведения об имущественных обязательствах
 * 
 * <p>Java class for PropertyObligationDatumType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PropertyObligationDatumType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InformationType">
 *       &lt;sequence>
 *         &lt;element name="ContractNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="ContractDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="ContractEndDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="Amount" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *         &lt;element name="PropertyKind" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ObligationPropertyKindType" minOccurs="0"/>
 *         &lt;element name="PropertyType" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ObligationPropertyType" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PropertyObligationDatumType", propOrder = {
    "contractNumber",
    "contractDate",
    "contractEndDate",
    "amount",
    "propertyKind",
    "propertyType",
    "description"
})
public class PropertyObligationDatumType
    extends InformationType
{

    @XmlElement(name = "ContractNumber", required = true)
    protected String contractNumber;
    @XmlElement(name = "ContractDate", required = true)
    protected XMLGregorianCalendar contractDate;
    @XmlElement(name = "ContractEndDate")
    protected XMLGregorianCalendar contractEndDate;
    @XmlElement(name = "Amount", required = true)
    protected BigDecimal amount;
    @XmlElement(name = "PropertyKind")
    protected String propertyKind;
    @XmlElement(name = "PropertyType")
    protected String propertyType;
    @XmlElement(name = "Description")
    protected String description;

    /**
     * Gets the value of the contractNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContractNumber() {
        return contractNumber;
    }

    /**
     * Sets the value of the contractNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContractNumber(String value) {
        this.contractNumber = value;
    }

    /**
     * Gets the value of the contractDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getContractDate() {
        return contractDate;
    }

    /**
     * Sets the value of the contractDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setContractDate(XMLGregorianCalendar value) {
        this.contractDate = value;
    }

    /**
     * Gets the value of the contractEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getContractEndDate() {
        return contractEndDate;
    }

    /**
     * Sets the value of the contractEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setContractEndDate(XMLGregorianCalendar value) {
        this.contractEndDate = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the propertyKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyKind() {
        return propertyKind;
    }

    /**
     * Sets the value of the propertyKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyKind(String value) {
        this.propertyKind = value;
    }

    /**
     * Gets the value of the propertyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * Sets the value of the propertyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyType(String value) {
        this.propertyType = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}

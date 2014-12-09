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
 * Сведения о периодических выплатах, зачислениях
 * 
 * <p>Java class for CashLetterDatumType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CashLetterDatumType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InformationType">
 *       &lt;sequence>
 *         &lt;element name="CashLetter" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}CashLetterType"/>
 *         &lt;element name="TextCashLetter" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000" minOccurs="0"/>
 *         &lt;element name="CashStartDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="CashFinDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="PaymentPeriod" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}PaymentPeriodType" minOccurs="0"/>
 *         &lt;element name="NamePaymentPeriod" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000" minOccurs="0"/>
 *         &lt;element name="Outpayments" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *         &lt;element name="AccessDrawingFacilities" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}AccessDrawingFacilitiesType"/>
 *         &lt;element name="NameAccessDrawingFacilities" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000" minOccurs="0"/>
 *         &lt;element name="Grounds" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CashLetterDatumType", propOrder = {
    "cashLetter",
    "textCashLetter",
    "cashStartDate",
    "cashFinDate",
    "paymentPeriod",
    "namePaymentPeriod",
    "outpayments",
    "accessDrawingFacilities",
    "nameAccessDrawingFacilities",
    "grounds"
})
public class CashLetterDatumType
    extends InformationType
{

    @XmlElement(name = "CashLetter", required = true)
    protected String cashLetter;
    @XmlElement(name = "TextCashLetter")
    protected String textCashLetter;
    @XmlElement(name = "CashStartDate", required = true)
    protected XMLGregorianCalendar cashStartDate;
    @XmlElement(name = "CashFinDate")
    protected XMLGregorianCalendar cashFinDate;
    @XmlElement(name = "PaymentPeriod")
    protected String paymentPeriod;
    @XmlElement(name = "NamePaymentPeriod")
    protected String namePaymentPeriod;
    @XmlElement(name = "Outpayments", required = true)
    protected BigDecimal outpayments;
    @XmlElement(name = "AccessDrawingFacilities", required = true)
    protected String accessDrawingFacilities;
    @XmlElement(name = "NameAccessDrawingFacilities")
    protected String nameAccessDrawingFacilities;
    @XmlElement(name = "Grounds")
    protected String grounds;

    /**
     * Gets the value of the cashLetter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCashLetter() {
        return cashLetter;
    }

    /**
     * Sets the value of the cashLetter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCashLetter(String value) {
        this.cashLetter = value;
    }

    /**
     * Gets the value of the textCashLetter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextCashLetter() {
        return textCashLetter;
    }

    /**
     * Sets the value of the textCashLetter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextCashLetter(String value) {
        this.textCashLetter = value;
    }

    /**
     * Gets the value of the cashStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCashStartDate() {
        return cashStartDate;
    }

    /**
     * Sets the value of the cashStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCashStartDate(XMLGregorianCalendar value) {
        this.cashStartDate = value;
    }

    /**
     * Gets the value of the cashFinDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCashFinDate() {
        return cashFinDate;
    }

    /**
     * Sets the value of the cashFinDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCashFinDate(XMLGregorianCalendar value) {
        this.cashFinDate = value;
    }

    /**
     * Gets the value of the paymentPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentPeriod() {
        return paymentPeriod;
    }

    /**
     * Sets the value of the paymentPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentPeriod(String value) {
        this.paymentPeriod = value;
    }

    /**
     * Gets the value of the namePaymentPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNamePaymentPeriod() {
        return namePaymentPeriod;
    }

    /**
     * Sets the value of the namePaymentPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNamePaymentPeriod(String value) {
        this.namePaymentPeriod = value;
    }

    /**
     * Gets the value of the outpayments property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOutpayments() {
        return outpayments;
    }

    /**
     * Sets the value of the outpayments property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOutpayments(BigDecimal value) {
        this.outpayments = value;
    }

    /**
     * Gets the value of the accessDrawingFacilities property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessDrawingFacilities() {
        return accessDrawingFacilities;
    }

    /**
     * Sets the value of the accessDrawingFacilities property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessDrawingFacilities(String value) {
        this.accessDrawingFacilities = value;
    }

    /**
     * Gets the value of the nameAccessDrawingFacilities property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameAccessDrawingFacilities() {
        return nameAccessDrawingFacilities;
    }

    /**
     * Sets the value of the nameAccessDrawingFacilities property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameAccessDrawingFacilities(String value) {
        this.nameAccessDrawingFacilities = value;
    }

    /**
     * Gets the value of the grounds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrounds() {
        return grounds;
    }

    /**
     * Sets the value of the grounds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrounds(String value) {
        this.grounds = value;
    }

}

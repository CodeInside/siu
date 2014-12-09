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


/**
 * Реквизиты для перечисления взысканных сумм взыскателю (д??я налоговых платежей может быть
 *         указано более одного набора реквизитов)
 * 
 * <p>Java class for PaymentProperties complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentProperties">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RecpName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300"/>
 *         &lt;element name="RecpBank" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300"/>
 *         &lt;element name="RecpBIK" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}BikType"/>
 *         &lt;element name="RecpCnt" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_20" minOccurs="0"/>
 *         &lt;element name="RecpINN" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}CompanyInnType" minOccurs="0"/>
 *         &lt;element name="RecpKPP" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}KppType" minOccurs="0"/>
 *         &lt;element name="Okato" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}OkatoType" minOccurs="0"/>
 *         &lt;element name="Oktmo" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}OktmoType" minOccurs="0"/>
 *         &lt;element name="Kbk" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}KbkType" minOccurs="0"/>
 *         &lt;element name="PersonalAccount" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}PersonalAccountType" minOccurs="0"/>
 *         &lt;element name="UNIFOCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}UNIFOChargeType" minOccurs="0"/>
 *         &lt;element name="PokPl" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}PaymentIndexType" minOccurs="0"/>
 *         &lt;element name="RekvSum" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentProperties", propOrder = {
    "recpName",
    "recpBank",
    "recpBIK",
    "recpCnt",
    "recpINN",
    "recpKPP",
    "okato",
    "oktmo",
    "kbk",
    "personalAccount",
    "unifoCode",
    "pokPl",
    "rekvSum"
})
public class PaymentProperties {

    @XmlElement(name = "RecpName", required = true)
    protected String recpName;
    @XmlElement(name = "RecpBank", required = true)
    protected String recpBank;
    @XmlElement(name = "RecpBIK", required = true)
    protected String recpBIK;
    @XmlElement(name = "RecpCnt")
    protected String recpCnt;
    @XmlElement(name = "RecpINN")
    protected String recpINN;
    @XmlElement(name = "RecpKPP")
    protected String recpKPP;
    @XmlElement(name = "Okato")
    protected String okato;
    @XmlElement(name = "Oktmo")
    protected String oktmo;
    @XmlElement(name = "Kbk")
    protected String kbk;
    @XmlElement(name = "PersonalAccount")
    protected String personalAccount;
    @XmlElement(name = "UNIFOCode")
    protected String unifoCode;
    @XmlElement(name = "PokPl")
    protected String pokPl;
    @XmlElement(name = "RekvSum", required = true)
    protected BigDecimal rekvSum;

    /**
     * Gets the value of the recpName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecpName() {
        return recpName;
    }

    /**
     * Sets the value of the recpName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecpName(String value) {
        this.recpName = value;
    }

    /**
     * Gets the value of the recpBank property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecpBank() {
        return recpBank;
    }

    /**
     * Sets the value of the recpBank property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecpBank(String value) {
        this.recpBank = value;
    }

    /**
     * Gets the value of the recpBIK property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecpBIK() {
        return recpBIK;
    }

    /**
     * Sets the value of the recpBIK property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecpBIK(String value) {
        this.recpBIK = value;
    }

    /**
     * Gets the value of the recpCnt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecpCnt() {
        return recpCnt;
    }

    /**
     * Sets the value of the recpCnt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecpCnt(String value) {
        this.recpCnt = value;
    }

    /**
     * Gets the value of the recpINN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecpINN() {
        return recpINN;
    }

    /**
     * Sets the value of the recpINN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecpINN(String value) {
        this.recpINN = value;
    }

    /**
     * Gets the value of the recpKPP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecpKPP() {
        return recpKPP;
    }

    /**
     * Sets the value of the recpKPP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecpKPP(String value) {
        this.recpKPP = value;
    }

    /**
     * Gets the value of the okato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOkato() {
        return okato;
    }

    /**
     * Sets the value of the okato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOkato(String value) {
        this.okato = value;
    }

    /**
     * Gets the value of the oktmo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOktmo() {
        return oktmo;
    }

    /**
     * Sets the value of the oktmo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOktmo(String value) {
        this.oktmo = value;
    }

    /**
     * Gets the value of the kbk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKbk() {
        return kbk;
    }

    /**
     * Sets the value of the kbk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKbk(String value) {
        this.kbk = value;
    }

    /**
     * Gets the value of the personalAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalAccount() {
        return personalAccount;
    }

    /**
     * Sets the value of the personalAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalAccount(String value) {
        this.personalAccount = value;
    }

    /**
     * Gets the value of the unifoCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNIFOCode() {
        return unifoCode;
    }

    /**
     * Sets the value of the unifoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNIFOCode(String value) {
        this.unifoCode = value;
    }

    /**
     * Gets the value of the pokPl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPokPl() {
        return pokPl;
    }

    /**
     * Sets the value of the pokPl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPokPl(String value) {
        this.pokPl = value;
    }

    /**
     * Gets the value of the rekvSum property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRekvSum() {
        return rekvSum;
    }

    /**
     * Sets the value of the rekvSum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRekvSum(BigDecimal value) {
        this.rekvSum = value;
    }

}

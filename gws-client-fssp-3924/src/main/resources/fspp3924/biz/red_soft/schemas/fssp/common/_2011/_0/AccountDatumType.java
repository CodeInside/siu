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
 * Сведения о банковском счете
 * 
 * <p>Java class for AccountDatumType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountDatumType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InformationType">
 *       &lt;sequence>
 *         &lt;element name="Acc" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_20"/>
 *         &lt;element name="PersonalAcc" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_60" minOccurs="0"/>
 *         &lt;element name="BicBank" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}BikType" minOccurs="0"/>
 *         &lt;element name="bankORGN" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}OgrnType" minOccurs="0"/>
 *         &lt;element name="bankINN" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}CompanyInnType" minOccurs="0"/>
 *         &lt;element name="bankKPP" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}KppType" minOccurs="0"/>
 *         &lt;element name="contractNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_20" minOccurs="0"/>
 *         &lt;element name="contractStartDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="contractFinDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="accountKindCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}AccountKindCodeType" minOccurs="0"/>
 *         &lt;element name="accountKindName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_500" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountDatumType", propOrder = {
    "acc",
    "personalAcc",
    "bicBank",
    "bankORGN",
    "bankINN",
    "bankKPP",
    "contractNumber",
    "contractStartDate",
    "contractFinDate",
    "accountKindCode",
    "accountKindName"
})
public class AccountDatumType
    extends InformationType
{

    @XmlElement(name = "Acc", required = true)
    protected String acc;
    @XmlElement(name = "PersonalAcc")
    protected String personalAcc;
    @XmlElement(name = "BicBank")
    protected String bicBank;
    protected String bankORGN;
    protected String bankINN;
    protected String bankKPP;
    protected String contractNumber;
    protected XMLGregorianCalendar contractStartDate;
    protected XMLGregorianCalendar contractFinDate;
    protected String accountKindCode;
    protected String accountKindName;

    /**
     * Gets the value of the acc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcc() {
        return acc;
    }

    /**
     * Sets the value of the acc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcc(String value) {
        this.acc = value;
    }

    /**
     * Gets the value of the personalAcc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalAcc() {
        return personalAcc;
    }

    /**
     * Sets the value of the personalAcc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalAcc(String value) {
        this.personalAcc = value;
    }

    /**
     * Gets the value of the bicBank property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBicBank() {
        return bicBank;
    }

    /**
     * Sets the value of the bicBank property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBicBank(String value) {
        this.bicBank = value;
    }

    /**
     * Gets the value of the bankORGN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankORGN() {
        return bankORGN;
    }

    /**
     * Sets the value of the bankORGN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankORGN(String value) {
        this.bankORGN = value;
    }

    /**
     * Gets the value of the bankINN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankINN() {
        return bankINN;
    }

    /**
     * Sets the value of the bankINN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankINN(String value) {
        this.bankINN = value;
    }

    /**
     * Gets the value of the bankKPP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankKPP() {
        return bankKPP;
    }

    /**
     * Sets the value of the bankKPP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankKPP(String value) {
        this.bankKPP = value;
    }

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
     * Gets the value of the contractStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getContractStartDate() {
        return contractStartDate;
    }

    /**
     * Sets the value of the contractStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setContractStartDate(XMLGregorianCalendar value) {
        this.contractStartDate = value;
    }

    /**
     * Gets the value of the contractFinDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getContractFinDate() {
        return contractFinDate;
    }

    /**
     * Sets the value of the contractFinDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setContractFinDate(XMLGregorianCalendar value) {
        this.contractFinDate = value;
    }

    /**
     * Gets the value of the accountKindCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountKindCode() {
        return accountKindCode;
    }

    /**
     * Sets the value of the accountKindCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountKindCode(String value) {
        this.accountKindCode = value;
    }

    /**
     * Gets the value of the accountKindName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountKindName() {
        return accountKindName;
    }

    /**
     * Sets the value of the accountKindName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountKindName(String value) {
        this.accountKindName = value;
    }

}
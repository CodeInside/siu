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
 * Тип данных BriefIPInformationType «Сокращенные сведения об ИП»
 * 
 * <p>Java class for BriefIPInformationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BriefIPInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IPKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType"/>
 *         &lt;element name="IDType" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IdType"/>
 *         &lt;element name="IDName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_100" minOccurs="0"/>
 *         &lt;element name="IDOrgCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}OrganizationCodeType" minOccurs="0"/>
 *         &lt;element name="IDOrgName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300" minOccurs="0"/>
 *         &lt;element name="IDNum" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="IDDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="number" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_25"/>
 *         &lt;element name="riseDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="debtText" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_200" minOccurs="0"/>
 *         &lt;element name="debtorBirthDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="debtorBirthplace" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300" minOccurs="0"/>
 *         &lt;element name="debtorAddress" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_150"/>
 *         &lt;element name="debtSum" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *         &lt;element name="debtRestTotal" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *         &lt;element name="debtRestIP" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *         &lt;element name="debtRestFine" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *         &lt;element name="debtRestDuty" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *         &lt;element name="debtRestOther" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *         &lt;element name="ospCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}VkspType"/>
 *         &lt;element name="SPIShortName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_20"/>
 *         &lt;element name="SPITelephone" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}TelephoneNumberType" minOccurs="0"/>
 *         &lt;element name="actualityDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DateTime"/>
 *         &lt;element name="unifoChargeFssp" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}UNIFOChargeType" minOccurs="0"/>
 *         &lt;element name="unifoChargeExt" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}UNIFOChargeType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BriefIPInformationType", propOrder = {
    "ipKey",
    "idType",
    "idName",
    "idOrgCode",
    "idOrgName",
    "idNum",
    "idDate",
    "number",
    "riseDate",
    "debtText",
    "debtorBirthDate",
    "debtorBirthplace",
    "debtorAddress",
    "debtSum",
    "debtRestTotal",
    "debtRestIP",
    "debtRestFine",
    "debtRestDuty",
    "debtRestOther",
    "ospCode",
    "spiShortName",
    "spiTelephone",
    "actualityDate",
    "unifoChargeFssp",
    "unifoChargeExt"
})
public class BriefIPInformationType {

    @XmlElement(name = "IPKey")
    protected long ipKey;
    @XmlElement(name = "IDType", required = true)
    protected String idType;
    @XmlElement(name = "IDName")
    protected String idName;
    @XmlElement(name = "IDOrgCode")
    protected String idOrgCode;
    @XmlElement(name = "IDOrgName")
    protected String idOrgName;
    @XmlElement(name = "IDNum", required = true)
    protected String idNum;
    @XmlElement(name = "IDDate", required = true)
    protected XMLGregorianCalendar idDate;
    @XmlElement(required = true)
    protected String number;
    @XmlElement(required = true)
    protected XMLGregorianCalendar riseDate;
    protected String debtText;
    protected XMLGregorianCalendar debtorBirthDate;
    protected String debtorBirthplace;
    @XmlElement(required = true)
    protected String debtorAddress;
    @XmlElement(required = true)
    protected BigDecimal debtSum;
    @XmlElement(required = true)
    protected BigDecimal debtRestTotal;
    @XmlElement(required = true)
    protected BigDecimal debtRestIP;
    @XmlElement(required = true)
    protected BigDecimal debtRestFine;
    @XmlElement(required = true)
    protected BigDecimal debtRestDuty;
    @XmlElement(required = true)
    protected BigDecimal debtRestOther;
    @XmlElement(required = true)
    protected String ospCode;
    @XmlElement(name = "SPIShortName", required = true)
    protected String spiShortName;
    @XmlElement(name = "SPITelephone")
    protected String spiTelephone;
    @XmlElement(required = true)
    protected XMLGregorianCalendar actualityDate;
    protected String unifoChargeFssp;
    protected String unifoChargeExt;

    /**
     * Gets the value of the ipKey property.
     * 
     */
    public long getIPKey() {
        return ipKey;
    }

    /**
     * Sets the value of the ipKey property.
     * 
     */
    public void setIPKey(long value) {
        this.ipKey = value;
    }

    /**
     * Gets the value of the idType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDType() {
        return idType;
    }

    /**
     * Sets the value of the idType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDType(String value) {
        this.idType = value;
    }

    /**
     * Gets the value of the idName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDName() {
        return idName;
    }

    /**
     * Sets the value of the idName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDName(String value) {
        this.idName = value;
    }

    /**
     * Gets the value of the idOrgCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDOrgCode() {
        return idOrgCode;
    }

    /**
     * Sets the value of the idOrgCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDOrgCode(String value) {
        this.idOrgCode = value;
    }

    /**
     * Gets the value of the idOrgName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDOrgName() {
        return idOrgName;
    }

    /**
     * Sets the value of the idOrgName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDOrgName(String value) {
        this.idOrgName = value;
    }

    /**
     * Gets the value of the idNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDNum() {
        return idNum;
    }

    /**
     * Sets the value of the idNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDNum(String value) {
        this.idNum = value;
    }

    /**
     * Gets the value of the idDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIDDate() {
        return idDate;
    }

    /**
     * Sets the value of the idDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIDDate(XMLGregorianCalendar value) {
        this.idDate = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * Gets the value of the riseDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRiseDate() {
        return riseDate;
    }

    /**
     * Sets the value of the riseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRiseDate(XMLGregorianCalendar value) {
        this.riseDate = value;
    }

    /**
     * Gets the value of the debtText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtText() {
        return debtText;
    }

    /**
     * Sets the value of the debtText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtText(String value) {
        this.debtText = value;
    }

    /**
     * Gets the value of the debtorBirthDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDebtorBirthDate() {
        return debtorBirthDate;
    }

    /**
     * Sets the value of the debtorBirthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDebtorBirthDate(XMLGregorianCalendar value) {
        this.debtorBirthDate = value;
    }

    /**
     * Gets the value of the debtorBirthplace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtorBirthplace() {
        return debtorBirthplace;
    }

    /**
     * Sets the value of the debtorBirthplace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtorBirthplace(String value) {
        this.debtorBirthplace = value;
    }

    /**
     * Gets the value of the debtorAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtorAddress() {
        return debtorAddress;
    }

    /**
     * Sets the value of the debtorAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtorAddress(String value) {
        this.debtorAddress = value;
    }

    /**
     * Gets the value of the debtSum property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDebtSum() {
        return debtSum;
    }

    /**
     * Sets the value of the debtSum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDebtSum(BigDecimal value) {
        this.debtSum = value;
    }

    /**
     * Gets the value of the debtRestTotal property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDebtRestTotal() {
        return debtRestTotal;
    }

    /**
     * Sets the value of the debtRestTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDebtRestTotal(BigDecimal value) {
        this.debtRestTotal = value;
    }

    /**
     * Gets the value of the debtRestIP property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDebtRestIP() {
        return debtRestIP;
    }

    /**
     * Sets the value of the debtRestIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDebtRestIP(BigDecimal value) {
        this.debtRestIP = value;
    }

    /**
     * Gets the value of the debtRestFine property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDebtRestFine() {
        return debtRestFine;
    }

    /**
     * Sets the value of the debtRestFine property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDebtRestFine(BigDecimal value) {
        this.debtRestFine = value;
    }

    /**
     * Gets the value of the debtRestDuty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDebtRestDuty() {
        return debtRestDuty;
    }

    /**
     * Sets the value of the debtRestDuty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDebtRestDuty(BigDecimal value) {
        this.debtRestDuty = value;
    }

    /**
     * Gets the value of the debtRestOther property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDebtRestOther() {
        return debtRestOther;
    }

    /**
     * Sets the value of the debtRestOther property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDebtRestOther(BigDecimal value) {
        this.debtRestOther = value;
    }

    /**
     * Gets the value of the ospCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOspCode() {
        return ospCode;
    }

    /**
     * Sets the value of the ospCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOspCode(String value) {
        this.ospCode = value;
    }

    /**
     * Gets the value of the spiShortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPIShortName() {
        return spiShortName;
    }

    /**
     * Sets the value of the spiShortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPIShortName(String value) {
        this.spiShortName = value;
    }

    /**
     * Gets the value of the spiTelephone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPITelephone() {
        return spiTelephone;
    }

    /**
     * Sets the value of the spiTelephone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPITelephone(String value) {
        this.spiTelephone = value;
    }

    /**
     * Gets the value of the actualityDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getActualityDate() {
        return actualityDate;
    }

    /**
     * Sets the value of the actualityDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setActualityDate(XMLGregorianCalendar value) {
        this.actualityDate = value;
    }

    /**
     * Gets the value of the unifoChargeFssp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnifoChargeFssp() {
        return unifoChargeFssp;
    }

    /**
     * Sets the value of the unifoChargeFssp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnifoChargeFssp(String value) {
        this.unifoChargeFssp = value;
    }

    /**
     * Gets the value of the unifoChargeExt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnifoChargeExt() {
        return unifoChargeExt;
    }

    /**
     * Sets the value of the unifoChargeExt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnifoChargeExt(String value) {
        this.unifoChargeExt = value;
    }

}

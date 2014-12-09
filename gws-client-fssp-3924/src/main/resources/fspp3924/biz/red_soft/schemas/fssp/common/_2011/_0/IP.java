//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.schemas.fssp.common._2011._0;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Тип данных содержит исчерпывающие сведения об исполнительном производстве.
 * 
 * <p>Java class for IP complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType"/>
 *         &lt;element name="IdExternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ExternalKeyType" minOccurs="0"/>
 *         &lt;element name="IPNum" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="IPDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="IPName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_100"/>
 *         &lt;element name="IDType" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IdType"/>
 *         &lt;element name="IDName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_100" minOccurs="0"/>
 *         &lt;element name="IDOrgCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}OrganizationCodeType" minOccurs="0"/>
 *         &lt;element name="IDOrgName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300" minOccurs="0"/>
 *         &lt;element name="IDNum" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="IDDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="AktDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="IDSubj" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IdSubjType" minOccurs="0"/>
 *         &lt;element name="IDSubjName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300"/>
 *         &lt;element name="IDSum" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money" minOccurs="0"/>
 *         &lt;element name="IPDebt" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money" minOccurs="0"/>
 *         &lt;element name="DebtorType" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ContragentType"/>
 *         &lt;element name="DebtorName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000"/>
 *         &lt;element name="DebtorFio" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FioType" minOccurs="0"/>
 *         &lt;element name="DebtorAdr" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300"/>
 *         &lt;element name="DebtorINN" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InnType" minOccurs="0"/>
 *         &lt;element name="DebtorKPP" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}KppType" minOccurs="0"/>
 *         &lt;element name="DebtorOGRN" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}OgrnOgrnipType" minOccurs="0"/>
 *         &lt;element name="DebtorBirthDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="DebtorBirthYear" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Year" minOccurs="0"/>
 *         &lt;element name="DebtorGender" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}GenderCodeType" minOccurs="0"/>
 *         &lt;element name="DebtorBirthPlace" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300" minOccurs="0"/>
 *         &lt;element name="DebtorSnils" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}SnilsType" minOccurs="0"/>
 *         &lt;element name="OSPCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}VkspType" minOccurs="0"/>
 *         &lt;element name="OSPName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_100" minOccurs="0"/>
 *         &lt;element name="SPI" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType"/>
 *         &lt;element name="SPIFio" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FullFioType"/>
 *         &lt;element name="OldIPNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ChargeOffQueue" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ChargeOffQueueType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IP", propOrder = {
    "internalKey",
    "idExternalKey",
    "ipNum",
    "ipDate",
    "ipName",
    "idType",
    "idName",
    "idOrgCode",
    "idOrgName",
    "idNum",
    "idDate",
    "aktDate",
    "idSubj",
    "idSubjName",
    "idSum",
    "ipDebt",
    "debtorType",
    "debtorName",
    "debtorFio",
    "debtorAdr",
    "debtorINN",
    "debtorKPP",
    "debtorOGRN",
    "debtorBirthDate",
    "debtorBirthYear",
    "debtorGender",
    "debtorBirthPlace",
    "debtorSnils",
    "ospCode",
    "ospName",
    "spi",
    "spiFio",
    "oldIPNumber",
    "chargeOffQueue"
})
public class IP {

    @XmlElement(name = "InternalKey")
    protected long internalKey;
    @XmlElement(name = "IdExternalKey")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String idExternalKey;
    @XmlElement(name = "IPNum", required = true)
    protected String ipNum;
    @XmlElement(name = "IPDate")
    protected XMLGregorianCalendar ipDate;
    @XmlElement(name = "IPName", required = true)
    protected String ipName;
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
    @XmlElement(name = "AktDate")
    protected XMLGregorianCalendar aktDate;
    @XmlElement(name = "IDSubj")
    protected String idSubj;
    @XmlElement(name = "IDSubjName", required = true)
    protected String idSubjName;
    @XmlElement(name = "IDSum")
    protected BigDecimal idSum;
    @XmlElement(name = "IPDebt")
    protected BigDecimal ipDebt;
    @XmlElement(name = "DebtorType", required = true)
    protected String debtorType;
    @XmlElement(name = "DebtorName", required = true)
    protected String debtorName;
    @XmlElement(name = "DebtorFio")
    protected FioType debtorFio;
    @XmlElement(name = "DebtorAdr", required = true)
    protected String debtorAdr;
    @XmlElement(name = "DebtorINN")
    protected String debtorINN;
    @XmlElement(name = "DebtorKPP")
    protected String debtorKPP;
    @XmlElement(name = "DebtorOGRN")
    protected String debtorOGRN;
    @XmlElement(name = "DebtorBirthDate")
    protected XMLGregorianCalendar debtorBirthDate;
    @XmlElement(name = "DebtorBirthYear")
    protected XMLGregorianCalendar debtorBirthYear;
    @XmlElement(name = "DebtorGender")
    protected String debtorGender;
    @XmlElement(name = "DebtorBirthPlace")
    protected String debtorBirthPlace;
    @XmlElement(name = "DebtorSnils")
    protected String debtorSnils;
    @XmlElement(name = "OSPCode")
    protected String ospCode;
    @XmlElement(name = "OSPName")
    protected String ospName;
    @XmlElement(name = "SPI")
    protected long spi;
    @XmlElement(name = "SPIFio", required = true)
    protected String spiFio;
    @XmlElement(name = "OldIPNumber")
    protected List<String> oldIPNumber;
    @XmlElement(name = "ChargeOffQueue")
    protected String chargeOffQueue;

    /**
     * Gets the value of the internalKey property.
     * 
     */
    public long getInternalKey() {
        return internalKey;
    }

    /**
     * Sets the value of the internalKey property.
     * 
     */
    public void setInternalKey(long value) {
        this.internalKey = value;
    }

    /**
     * Gets the value of the idExternalKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdExternalKey() {
        return idExternalKey;
    }

    /**
     * Sets the value of the idExternalKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdExternalKey(String value) {
        this.idExternalKey = value;
    }

    /**
     * Gets the value of the ipNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPNum() {
        return ipNum;
    }

    /**
     * Sets the value of the ipNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPNum(String value) {
        this.ipNum = value;
    }

    /**
     * Gets the value of the ipDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIPDate() {
        return ipDate;
    }

    /**
     * Sets the value of the ipDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIPDate(XMLGregorianCalendar value) {
        this.ipDate = value;
    }

    /**
     * Gets the value of the ipName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPName() {
        return ipName;
    }

    /**
     * Sets the value of the ipName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPName(String value) {
        this.ipName = value;
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
     * Gets the value of the aktDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAktDate() {
        return aktDate;
    }

    /**
     * Sets the value of the aktDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAktDate(XMLGregorianCalendar value) {
        this.aktDate = value;
    }

    /**
     * Gets the value of the idSubj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDSubj() {
        return idSubj;
    }

    /**
     * Sets the value of the idSubj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDSubj(String value) {
        this.idSubj = value;
    }

    /**
     * Gets the value of the idSubjName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDSubjName() {
        return idSubjName;
    }

    /**
     * Sets the value of the idSubjName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDSubjName(String value) {
        this.idSubjName = value;
    }

    /**
     * Gets the value of the idSum property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIDSum() {
        return idSum;
    }

    /**
     * Sets the value of the idSum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIDSum(BigDecimal value) {
        this.idSum = value;
    }

    /**
     * Gets the value of the ipDebt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIPDebt() {
        return ipDebt;
    }

    /**
     * Sets the value of the ipDebt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIPDebt(BigDecimal value) {
        this.ipDebt = value;
    }

    /**
     * Gets the value of the debtorType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtorType() {
        return debtorType;
    }

    /**
     * Sets the value of the debtorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtorType(String value) {
        this.debtorType = value;
    }

    /**
     * Gets the value of the debtorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtorName() {
        return debtorName;
    }

    /**
     * Sets the value of the debtorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtorName(String value) {
        this.debtorName = value;
    }

    /**
     * Gets the value of the debtorFio property.
     * 
     * @return
     *     possible object is
     *     {@link FioType }
     *     
     */
    public FioType getDebtorFio() {
        return debtorFio;
    }

    /**
     * Sets the value of the debtorFio property.
     * 
     * @param value
     *     allowed object is
     *     {@link FioType }
     *     
     */
    public void setDebtorFio(FioType value) {
        this.debtorFio = value;
    }

    /**
     * Gets the value of the debtorAdr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtorAdr() {
        return debtorAdr;
    }

    /**
     * Sets the value of the debtorAdr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtorAdr(String value) {
        this.debtorAdr = value;
    }

    /**
     * Gets the value of the debtorINN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtorINN() {
        return debtorINN;
    }

    /**
     * Sets the value of the debtorINN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtorINN(String value) {
        this.debtorINN = value;
    }

    /**
     * Gets the value of the debtorKPP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtorKPP() {
        return debtorKPP;
    }

    /**
     * Sets the value of the debtorKPP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtorKPP(String value) {
        this.debtorKPP = value;
    }

    /**
     * Gets the value of the debtorOGRN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtorOGRN() {
        return debtorOGRN;
    }

    /**
     * Sets the value of the debtorOGRN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtorOGRN(String value) {
        this.debtorOGRN = value;
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
     * Gets the value of the debtorBirthYear property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDebtorBirthYear() {
        return debtorBirthYear;
    }

    /**
     * Sets the value of the debtorBirthYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDebtorBirthYear(XMLGregorianCalendar value) {
        this.debtorBirthYear = value;
    }

    /**
     * Gets the value of the debtorGender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtorGender() {
        return debtorGender;
    }

    /**
     * Sets the value of the debtorGender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtorGender(String value) {
        this.debtorGender = value;
    }

    /**
     * Gets the value of the debtorBirthPlace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtorBirthPlace() {
        return debtorBirthPlace;
    }

    /**
     * Sets the value of the debtorBirthPlace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtorBirthPlace(String value) {
        this.debtorBirthPlace = value;
    }

    /**
     * Gets the value of the debtorSnils property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtorSnils() {
        return debtorSnils;
    }

    /**
     * Sets the value of the debtorSnils property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtorSnils(String value) {
        this.debtorSnils = value;
    }

    /**
     * Gets the value of the ospCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOSPCode() {
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
    public void setOSPCode(String value) {
        this.ospCode = value;
    }

    /**
     * Gets the value of the ospName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOSPName() {
        return ospName;
    }

    /**
     * Sets the value of the ospName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOSPName(String value) {
        this.ospName = value;
    }

    /**
     * Gets the value of the spi property.
     * 
     */
    public long getSPI() {
        return spi;
    }

    /**
     * Sets the value of the spi property.
     * 
     */
    public void setSPI(long value) {
        this.spi = value;
    }

    /**
     * Gets the value of the spiFio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPIFio() {
        return spiFio;
    }

    /**
     * Sets the value of the spiFio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPIFio(String value) {
        this.spiFio = value;
    }

    /**
     * Gets the value of the oldIPNumber property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the oldIPNumber property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOldIPNumber().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOldIPNumber() {
        if (oldIPNumber == null) {
            oldIPNumber = new ArrayList<String>();
        }
        return this.oldIPNumber;
    }

    /**
     * Gets the value of the chargeOffQueue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChargeOffQueue() {
        return chargeOffQueue;
    }

    /**
     * Sets the value of the chargeOffQueue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChargeOffQueue(String value) {
        this.chargeOffQueue = value;
    }

}

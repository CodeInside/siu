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
 * Строка выписки из реестра арестованного имущество отправленного на реализацию
 * 
 * <p>Java class for ArrestRegistryDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrestRegistryDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IPNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType" minOccurs="0"/>
 *         &lt;element name="IPStatus" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255"/>
 *         &lt;element name="OSPName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_100" minOccurs="0"/>
 *         &lt;element name="SPI" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType"/>
 *         &lt;element name="SPIFio" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FullFioType"/>
 *         &lt;element name="SPIFioParts" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FioType" minOccurs="0"/>
 *         &lt;element name="SPITel" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}TelephoneNumberType" minOccurs="0"/>
 *         &lt;element name="SPIPost" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}PostType" minOccurs="0"/>
 *         &lt;element name="Debtor" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ContragentDatumType" minOccurs="0"/>
 *         &lt;element name="IDSubjName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300"/>
 *         &lt;element name="IPDebt" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *         &lt;element name="DateArrest" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="TypeProperty" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300"/>
 *         &lt;element name="TypePropertyText" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000"/>
 *         &lt;element name="CostPropertyAct" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *         &lt;element name="NumberRealEstate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="DateRealEstate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="DateDecreeAppraiser" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="NameOrgAppraiser" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000"/>
 *         &lt;element name="DateReportAppraiser" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="DateDecreeApprovalAppraiser" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="DateAppraiser" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="CostPropertyAppraiser" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *         &lt;element name="DateDecreeSelling" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="NumberApplicationSelling" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="DateApplicationSelling" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="DateDliverySelling" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="OrganizationSelling" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000"/>
 *         &lt;element name="AddressOrganizationSelling" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}AddressType"/>
 *         &lt;element name="PhoneOrganizationSelling" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}TelephoneNumberType"/>
 *         &lt;element name="DateSelling" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="AddressSelling" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}AddressType"/>
 *         &lt;element name="NameMedia" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000"/>
 *         &lt;element name="DatePublication" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="StartingPrice" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrestRegistryDetail", propOrder = {
    "ipNumber",
    "ipStatus",
    "ospName",
    "spi",
    "spiFio",
    "spiFioParts",
    "spiTel",
    "spiPost",
    "debtor",
    "idSubjName",
    "ipDebt",
    "dateArrest",
    "typeProperty",
    "typePropertyText",
    "costPropertyAct",
    "numberRealEstate",
    "dateRealEstate",
    "dateDecreeAppraiser",
    "nameOrgAppraiser",
    "dateReportAppraiser",
    "dateDecreeApprovalAppraiser",
    "dateAppraiser",
    "costPropertyAppraiser",
    "dateDecreeSelling",
    "numberApplicationSelling",
    "dateApplicationSelling",
    "dateDliverySelling",
    "organizationSelling",
    "addressOrganizationSelling",
    "phoneOrganizationSelling",
    "dateSelling",
    "addressSelling",
    "nameMedia",
    "datePublication",
    "startingPrice"
})
public class ArrestRegistryDetail {

    @XmlElement(name = "IPNumber")
    protected String ipNumber;
    @XmlElement(name = "IPStatus", required = true)
    protected String ipStatus;
    @XmlElement(name = "OSPName")
    protected String ospName;
    @XmlElement(name = "SPI")
    protected long spi;
    @XmlElement(name = "SPIFio", required = true)
    protected String spiFio;
    @XmlElement(name = "SPIFioParts")
    protected FioType spiFioParts;
    @XmlElement(name = "SPITel")
    protected String spiTel;
    @XmlElement(name = "SPIPost")
    protected String spiPost;
    @XmlElement(name = "Debtor")
    protected ContragentDatumType debtor;
    @XmlElement(name = "IDSubjName", required = true)
    protected String idSubjName;
    @XmlElement(name = "IPDebt", required = true)
    protected BigDecimal ipDebt;
    @XmlElement(name = "DateArrest", required = true)
    protected XMLGregorianCalendar dateArrest;
    @XmlElement(name = "TypeProperty", required = true)
    protected String typeProperty;
    @XmlElement(name = "TypePropertyText", required = true)
    protected String typePropertyText;
    @XmlElement(name = "CostPropertyAct", required = true)
    protected BigDecimal costPropertyAct;
    @XmlElement(name = "NumberRealEstate", required = true)
    protected String numberRealEstate;
    @XmlElement(name = "DateRealEstate", required = true)
    protected XMLGregorianCalendar dateRealEstate;
    @XmlElement(name = "DateDecreeAppraiser", required = true)
    protected XMLGregorianCalendar dateDecreeAppraiser;
    @XmlElement(name = "NameOrgAppraiser", required = true)
    protected String nameOrgAppraiser;
    @XmlElement(name = "DateReportAppraiser", required = true)
    protected XMLGregorianCalendar dateReportAppraiser;
    @XmlElement(name = "DateDecreeApprovalAppraiser", required = true)
    protected XMLGregorianCalendar dateDecreeApprovalAppraiser;
    @XmlElement(name = "DateAppraiser", required = true)
    protected XMLGregorianCalendar dateAppraiser;
    @XmlElement(name = "CostPropertyAppraiser", required = true)
    protected BigDecimal costPropertyAppraiser;
    @XmlElement(name = "DateDecreeSelling", required = true)
    protected XMLGregorianCalendar dateDecreeSelling;
    @XmlElement(name = "NumberApplicationSelling", required = true)
    protected String numberApplicationSelling;
    @XmlElement(name = "DateApplicationSelling", required = true)
    protected XMLGregorianCalendar dateApplicationSelling;
    @XmlElement(name = "DateDliverySelling", required = true)
    protected XMLGregorianCalendar dateDliverySelling;
    @XmlElement(name = "OrganizationSelling", required = true)
    protected String organizationSelling;
    @XmlElement(name = "AddressOrganizationSelling", required = true)
    protected AddressType addressOrganizationSelling;
    @XmlElement(name = "PhoneOrganizationSelling", required = true)
    protected String phoneOrganizationSelling;
    @XmlElement(name = "DateSelling", required = true)
    protected XMLGregorianCalendar dateSelling;
    @XmlElement(name = "AddressSelling", required = true)
    protected AddressType addressSelling;
    @XmlElement(name = "NameMedia", required = true)
    protected String nameMedia;
    @XmlElement(name = "DatePublication", required = true)
    protected XMLGregorianCalendar datePublication;
    @XmlElement(name = "StartingPrice", required = true)
    protected BigDecimal startingPrice;

    /**
     * Gets the value of the ipNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPNumber() {
        return ipNumber;
    }

    /**
     * Sets the value of the ipNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPNumber(String value) {
        this.ipNumber = value;
    }

    /**
     * Gets the value of the ipStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPStatus() {
        return ipStatus;
    }

    /**
     * Sets the value of the ipStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPStatus(String value) {
        this.ipStatus = value;
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
     * Gets the value of the spiFioParts property.
     * 
     * @return
     *     possible object is
     *     {@link FioType }
     *     
     */
    public FioType getSPIFioParts() {
        return spiFioParts;
    }

    /**
     * Sets the value of the spiFioParts property.
     * 
     * @param value
     *     allowed object is
     *     {@link FioType }
     *     
     */
    public void setSPIFioParts(FioType value) {
        this.spiFioParts = value;
    }

    /**
     * Gets the value of the spiTel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPITel() {
        return spiTel;
    }

    /**
     * Sets the value of the spiTel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPITel(String value) {
        this.spiTel = value;
    }

    /**
     * Gets the value of the spiPost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPIPost() {
        return spiPost;
    }

    /**
     * Sets the value of the spiPost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPIPost(String value) {
        this.spiPost = value;
    }

    /**
     * Gets the value of the debtor property.
     * 
     * @return
     *     possible object is
     *     {@link ContragentDatumType }
     *     
     */
    public ContragentDatumType getDebtor() {
        return debtor;
    }

    /**
     * Sets the value of the debtor property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContragentDatumType }
     *     
     */
    public void setDebtor(ContragentDatumType value) {
        this.debtor = value;
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
     * Gets the value of the dateArrest property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateArrest() {
        return dateArrest;
    }

    /**
     * Sets the value of the dateArrest property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateArrest(XMLGregorianCalendar value) {
        this.dateArrest = value;
    }

    /**
     * Gets the value of the typeProperty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeProperty() {
        return typeProperty;
    }

    /**
     * Sets the value of the typeProperty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeProperty(String value) {
        this.typeProperty = value;
    }

    /**
     * Gets the value of the typePropertyText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypePropertyText() {
        return typePropertyText;
    }

    /**
     * Sets the value of the typePropertyText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypePropertyText(String value) {
        this.typePropertyText = value;
    }

    /**
     * Gets the value of the costPropertyAct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCostPropertyAct() {
        return costPropertyAct;
    }

    /**
     * Sets the value of the costPropertyAct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCostPropertyAct(BigDecimal value) {
        this.costPropertyAct = value;
    }

    /**
     * Gets the value of the numberRealEstate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberRealEstate() {
        return numberRealEstate;
    }

    /**
     * Sets the value of the numberRealEstate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberRealEstate(String value) {
        this.numberRealEstate = value;
    }

    /**
     * Gets the value of the dateRealEstate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateRealEstate() {
        return dateRealEstate;
    }

    /**
     * Sets the value of the dateRealEstate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateRealEstate(XMLGregorianCalendar value) {
        this.dateRealEstate = value;
    }

    /**
     * Gets the value of the dateDecreeAppraiser property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDecreeAppraiser() {
        return dateDecreeAppraiser;
    }

    /**
     * Sets the value of the dateDecreeAppraiser property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDecreeAppraiser(XMLGregorianCalendar value) {
        this.dateDecreeAppraiser = value;
    }

    /**
     * Gets the value of the nameOrgAppraiser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOrgAppraiser() {
        return nameOrgAppraiser;
    }

    /**
     * Sets the value of the nameOrgAppraiser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOrgAppraiser(String value) {
        this.nameOrgAppraiser = value;
    }

    /**
     * Gets the value of the dateReportAppraiser property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateReportAppraiser() {
        return dateReportAppraiser;
    }

    /**
     * Sets the value of the dateReportAppraiser property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateReportAppraiser(XMLGregorianCalendar value) {
        this.dateReportAppraiser = value;
    }

    /**
     * Gets the value of the dateDecreeApprovalAppraiser property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDecreeApprovalAppraiser() {
        return dateDecreeApprovalAppraiser;
    }

    /**
     * Sets the value of the dateDecreeApprovalAppraiser property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDecreeApprovalAppraiser(XMLGregorianCalendar value) {
        this.dateDecreeApprovalAppraiser = value;
    }

    /**
     * Gets the value of the dateAppraiser property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateAppraiser() {
        return dateAppraiser;
    }

    /**
     * Sets the value of the dateAppraiser property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateAppraiser(XMLGregorianCalendar value) {
        this.dateAppraiser = value;
    }

    /**
     * Gets the value of the costPropertyAppraiser property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCostPropertyAppraiser() {
        return costPropertyAppraiser;
    }

    /**
     * Sets the value of the costPropertyAppraiser property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCostPropertyAppraiser(BigDecimal value) {
        this.costPropertyAppraiser = value;
    }

    /**
     * Gets the value of the dateDecreeSelling property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDecreeSelling() {
        return dateDecreeSelling;
    }

    /**
     * Sets the value of the dateDecreeSelling property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDecreeSelling(XMLGregorianCalendar value) {
        this.dateDecreeSelling = value;
    }

    /**
     * Gets the value of the numberApplicationSelling property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberApplicationSelling() {
        return numberApplicationSelling;
    }

    /**
     * Sets the value of the numberApplicationSelling property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberApplicationSelling(String value) {
        this.numberApplicationSelling = value;
    }

    /**
     * Gets the value of the dateApplicationSelling property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateApplicationSelling() {
        return dateApplicationSelling;
    }

    /**
     * Sets the value of the dateApplicationSelling property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateApplicationSelling(XMLGregorianCalendar value) {
        this.dateApplicationSelling = value;
    }

    /**
     * Gets the value of the dateDliverySelling property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDliverySelling() {
        return dateDliverySelling;
    }

    /**
     * Sets the value of the dateDliverySelling property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDliverySelling(XMLGregorianCalendar value) {
        this.dateDliverySelling = value;
    }

    /**
     * Gets the value of the organizationSelling property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizationSelling() {
        return organizationSelling;
    }

    /**
     * Sets the value of the organizationSelling property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizationSelling(String value) {
        this.organizationSelling = value;
    }

    /**
     * Gets the value of the addressOrganizationSelling property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getAddressOrganizationSelling() {
        return addressOrganizationSelling;
    }

    /**
     * Sets the value of the addressOrganizationSelling property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setAddressOrganizationSelling(AddressType value) {
        this.addressOrganizationSelling = value;
    }

    /**
     * Gets the value of the phoneOrganizationSelling property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhoneOrganizationSelling() {
        return phoneOrganizationSelling;
    }

    /**
     * Sets the value of the phoneOrganizationSelling property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhoneOrganizationSelling(String value) {
        this.phoneOrganizationSelling = value;
    }

    /**
     * Gets the value of the dateSelling property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateSelling() {
        return dateSelling;
    }

    /**
     * Sets the value of the dateSelling property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateSelling(XMLGregorianCalendar value) {
        this.dateSelling = value;
    }

    /**
     * Gets the value of the addressSelling property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getAddressSelling() {
        return addressSelling;
    }

    /**
     * Sets the value of the addressSelling property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setAddressSelling(AddressType value) {
        this.addressSelling = value;
    }

    /**
     * Gets the value of the nameMedia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameMedia() {
        return nameMedia;
    }

    /**
     * Sets the value of the nameMedia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameMedia(String value) {
        this.nameMedia = value;
    }

    /**
     * Gets the value of the datePublication property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDatePublication() {
        return datePublication;
    }

    /**
     * Sets the value of the datePublication property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDatePublication(XMLGregorianCalendar value) {
        this.datePublication = value;
    }

    /**
     * Gets the value of the startingPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    /**
     * Sets the value of the startingPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setStartingPrice(BigDecimal value) {
        this.startingPrice = value;
    }

}
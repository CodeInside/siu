//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.schemas.fssp.common._2011._0;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Расширенные сведения о юридическом лице
 * 
 * <p>Java class for CompanyExtDatumType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CompanyExtDatumType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}CompanyDatumType">
 *       &lt;sequence>
 *         &lt;element name="documentId" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FnsDocIdType"/>
 *         &lt;element name="OKVED" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}OkvedDetailType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="statusId" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FnsIdType"/>
 *         &lt;element name="nameActDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="shortName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255" minOccurs="0"/>
 *         &lt;element name="brandName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255" minOccurs="0"/>
 *         &lt;element name="nationalName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255" minOccurs="0"/>
 *         &lt;element name="nationalLangId" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FnsIdType" minOccurs="0"/>
 *         &lt;element name="nationalLangCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}LangCodeType" minOccurs="0"/>
 *         &lt;element name="nationalLangName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255" minOccurs="0"/>
 *         &lt;element name="foreignName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255" minOccurs="0"/>
 *         &lt;element name="foreignLangId" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FnsIdType" minOccurs="0"/>
 *         &lt;element name="foreignLangCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}LangCodeType" minOccurs="0"/>
 *         &lt;element name="foreignLandName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255" minOccurs="0"/>
 *         &lt;element name="capitalActDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="capitalAmount" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money" minOccurs="0"/>
 *         &lt;element name="capitalKindId" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FnsIdType"/>
 *         &lt;element name="capitalKindName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255"/>
 *         &lt;element name="foundRegNum" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255" minOccurs="0"/>
 *         &lt;element name="foundRegKindId" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FnsIdType"/>
 *         &lt;element name="foundRegKindName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255"/>
 *         &lt;element name="closureRegNum" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_13" minOccurs="0"/>
 *         &lt;element name="closureDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="closureRegKindId" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FnsIdType" minOccurs="0"/>
 *         &lt;element name="closureRegKindName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255" minOccurs="0"/>
 *         &lt;element name="founderCompanyCount" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Integer"/>
 *         &lt;element name="founderForeignCompanyCount" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Integer"/>
 *         &lt;element name="founderPersonCount" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Integer"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompanyExtDatumType", propOrder = {
    "documentId",
    "okved",
    "statusId",
    "nameActDate",
    "shortName",
    "brandName",
    "nationalName",
    "nationalLangId",
    "nationalLangCode",
    "nationalLangName",
    "foreignName",
    "foreignLangId",
    "foreignLangCode",
    "foreignLandName",
    "capitalActDate",
    "capitalAmount",
    "capitalKindId",
    "capitalKindName",
    "foundRegNum",
    "foundRegKindId",
    "foundRegKindName",
    "closureRegNum",
    "closureDate",
    "closureRegKindId",
    "closureRegKindName",
    "founderCompanyCount",
    "founderForeignCompanyCount",
    "founderPersonCount"
})
public class CompanyExtDatumType
    extends CompanyDatumType
{

    @XmlElement(required = true)
    protected BigInteger documentId;
    @XmlElement(name = "OKVED")
    protected List<OkvedDetailType> okved;
    @XmlElement(required = true)
    protected BigInteger statusId;
    @XmlElement(required = true)
    protected XMLGregorianCalendar nameActDate;
    protected String shortName;
    protected String brandName;
    protected String nationalName;
    protected BigInteger nationalLangId;
    protected String nationalLangCode;
    protected String nationalLangName;
    protected String foreignName;
    protected BigInteger foreignLangId;
    protected String foreignLangCode;
    protected String foreignLandName;
    @XmlElement(required = true)
    protected XMLGregorianCalendar capitalActDate;
    protected BigDecimal capitalAmount;
    @XmlElement(required = true)
    protected BigInteger capitalKindId;
    @XmlElement(required = true)
    protected String capitalKindName;
    protected String foundRegNum;
    @XmlElement(required = true)
    protected BigInteger foundRegKindId;
    @XmlElement(required = true)
    protected String foundRegKindName;
    protected String closureRegNum;
    protected XMLGregorianCalendar closureDate;
    protected BigInteger closureRegKindId;
    protected String closureRegKindName;
    @XmlElement(required = true)
    protected BigInteger founderCompanyCount;
    @XmlElement(required = true)
    protected BigInteger founderForeignCompanyCount;
    @XmlElement(required = true)
    protected BigInteger founderPersonCount;

    /**
     * Gets the value of the documentId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDocumentId() {
        return documentId;
    }

    /**
     * Sets the value of the documentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDocumentId(BigInteger value) {
        this.documentId = value;
    }

    /**
     * Gets the value of the okved property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the okved property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOKVED().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OkvedDetailType }
     * 
     * 
     */
    public List<OkvedDetailType> getOKVED() {
        if (okved == null) {
            okved = new ArrayList<OkvedDetailType>();
        }
        return this.okved;
    }

    /**
     * Gets the value of the statusId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStatusId() {
        return statusId;
    }

    /**
     * Sets the value of the statusId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStatusId(BigInteger value) {
        this.statusId = value;
    }

    /**
     * Gets the value of the nameActDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getNameActDate() {
        return nameActDate;
    }

    /**
     * Sets the value of the nameActDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setNameActDate(XMLGregorianCalendar value) {
        this.nameActDate = value;
    }

    /**
     * Gets the value of the shortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Sets the value of the shortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortName(String value) {
        this.shortName = value;
    }

    /**
     * Gets the value of the brandName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * Sets the value of the brandName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBrandName(String value) {
        this.brandName = value;
    }

    /**
     * Gets the value of the nationalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationalName() {
        return nationalName;
    }

    /**
     * Sets the value of the nationalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationalName(String value) {
        this.nationalName = value;
    }

    /**
     * Gets the value of the nationalLangId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNationalLangId() {
        return nationalLangId;
    }

    /**
     * Sets the value of the nationalLangId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNationalLangId(BigInteger value) {
        this.nationalLangId = value;
    }

    /**
     * Gets the value of the nationalLangCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationalLangCode() {
        return nationalLangCode;
    }

    /**
     * Sets the value of the nationalLangCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationalLangCode(String value) {
        this.nationalLangCode = value;
    }

    /**
     * Gets the value of the nationalLangName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationalLangName() {
        return nationalLangName;
    }

    /**
     * Sets the value of the nationalLangName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationalLangName(String value) {
        this.nationalLangName = value;
    }

    /**
     * Gets the value of the foreignName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignName() {
        return foreignName;
    }

    /**
     * Sets the value of the foreignName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignName(String value) {
        this.foreignName = value;
    }

    /**
     * Gets the value of the foreignLangId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getForeignLangId() {
        return foreignLangId;
    }

    /**
     * Sets the value of the foreignLangId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setForeignLangId(BigInteger value) {
        this.foreignLangId = value;
    }

    /**
     * Gets the value of the foreignLangCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignLangCode() {
        return foreignLangCode;
    }

    /**
     * Sets the value of the foreignLangCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignLangCode(String value) {
        this.foreignLangCode = value;
    }

    /**
     * Gets the value of the foreignLandName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignLandName() {
        return foreignLandName;
    }

    /**
     * Sets the value of the foreignLandName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignLandName(String value) {
        this.foreignLandName = value;
    }

    /**
     * Gets the value of the capitalActDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCapitalActDate() {
        return capitalActDate;
    }

    /**
     * Sets the value of the capitalActDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCapitalActDate(XMLGregorianCalendar value) {
        this.capitalActDate = value;
    }

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
     * Gets the value of the capitalKindId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCapitalKindId() {
        return capitalKindId;
    }

    /**
     * Sets the value of the capitalKindId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCapitalKindId(BigInteger value) {
        this.capitalKindId = value;
    }

    /**
     * Gets the value of the capitalKindName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapitalKindName() {
        return capitalKindName;
    }

    /**
     * Sets the value of the capitalKindName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapitalKindName(String value) {
        this.capitalKindName = value;
    }

    /**
     * Gets the value of the foundRegNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFoundRegNum() {
        return foundRegNum;
    }

    /**
     * Sets the value of the foundRegNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFoundRegNum(String value) {
        this.foundRegNum = value;
    }

    /**
     * Gets the value of the foundRegKindId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFoundRegKindId() {
        return foundRegKindId;
    }

    /**
     * Sets the value of the foundRegKindId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFoundRegKindId(BigInteger value) {
        this.foundRegKindId = value;
    }

    /**
     * Gets the value of the foundRegKindName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFoundRegKindName() {
        return foundRegKindName;
    }

    /**
     * Sets the value of the foundRegKindName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFoundRegKindName(String value) {
        this.foundRegKindName = value;
    }

    /**
     * Gets the value of the closureRegNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClosureRegNum() {
        return closureRegNum;
    }

    /**
     * Sets the value of the closureRegNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClosureRegNum(String value) {
        this.closureRegNum = value;
    }

    /**
     * Gets the value of the closureDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getClosureDate() {
        return closureDate;
    }

    /**
     * Sets the value of the closureDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setClosureDate(XMLGregorianCalendar value) {
        this.closureDate = value;
    }

    /**
     * Gets the value of the closureRegKindId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getClosureRegKindId() {
        return closureRegKindId;
    }

    /**
     * Sets the value of the closureRegKindId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setClosureRegKindId(BigInteger value) {
        this.closureRegKindId = value;
    }

    /**
     * Gets the value of the closureRegKindName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClosureRegKindName() {
        return closureRegKindName;
    }

    /**
     * Sets the value of the closureRegKindName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClosureRegKindName(String value) {
        this.closureRegKindName = value;
    }

    /**
     * Gets the value of the founderCompanyCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFounderCompanyCount() {
        return founderCompanyCount;
    }

    /**
     * Sets the value of the founderCompanyCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFounderCompanyCount(BigInteger value) {
        this.founderCompanyCount = value;
    }

    /**
     * Gets the value of the founderForeignCompanyCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFounderForeignCompanyCount() {
        return founderForeignCompanyCount;
    }

    /**
     * Sets the value of the founderForeignCompanyCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFounderForeignCompanyCount(BigInteger value) {
        this.founderForeignCompanyCount = value;
    }

    /**
     * Gets the value of the founderPersonCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFounderPersonCount() {
        return founderPersonCount;
    }

    /**
     * Sets the value of the founderPersonCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFounderPersonCount(BigInteger value) {
        this.founderPersonCount = value;
    }

}

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
import org.w3._2000._09.xmldsig.SignatureType;


/**
 * Содержит уведомление об исполнении
 * 
 * <p>Java class for Report complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Report">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ExternalKeyType"/>
 *         &lt;element name="RestrictnInternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType"/>
 *         &lt;element name="IpInternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType"/>
 *         &lt;element name="DocDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="DocNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType" minOccurs="0"/>
 *         &lt;element name="RestrDocDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="RestrDocNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="RestrictionAnswerType" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}RestrictionAnswerType"/>
 *         &lt;element name="LegalImpossibility" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_4000" minOccurs="0"/>
 *         &lt;element name="RestrictedAmount" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money" minOccurs="0"/>
 *         &lt;element name="RestrictedAmountRub" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money" minOccurs="0"/>
 *         &lt;element name="Data" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Data" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RestrictedData" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Data" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="NonRestrictedData" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Data" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SignDateTime" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DateTime" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Report", propOrder = {
    "externalKey",
    "restrictnInternalKey",
    "ipInternalKey",
    "docDate",
    "docNumber",
    "restrDocDate",
    "restrDocNumber",
    "restrictionAnswerType",
    "legalImpossibility",
    "restrictedAmount",
    "restrictedAmountRub",
    "data",
    "restrictedData",
    "nonRestrictedData",
    "signDateTime",
    "signature"
})
public class Report {

    @XmlElement(name = "ExternalKey", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String externalKey;
    @XmlElement(name = "RestrictnInternalKey")
    protected long restrictnInternalKey;
    @XmlElement(name = "IpInternalKey")
    protected long ipInternalKey;
    @XmlElement(name = "DocDate", required = true)
    protected XMLGregorianCalendar docDate;
    @XmlElement(name = "DocNumber")
    protected String docNumber;
    @XmlElement(name = "RestrDocDate", required = true)
    protected XMLGregorianCalendar restrDocDate;
    @XmlElement(name = "RestrDocNumber", required = true)
    protected String restrDocNumber;
    @XmlElement(name = "RestrictionAnswerType", required = true)
    protected String restrictionAnswerType;
    @XmlElement(name = "LegalImpossibility")
    protected String legalImpossibility;
    @XmlElement(name = "RestrictedAmount")
    protected BigDecimal restrictedAmount;
    @XmlElement(name = "RestrictedAmountRub")
    protected BigDecimal restrictedAmountRub;
    @XmlElement(name = "Data")
    protected List<Data> data;
    @XmlElement(name = "RestrictedData")
    protected List<Data> restrictedData;
    @XmlElement(name = "NonRestrictedData")
    protected List<Data> nonRestrictedData;
    @XmlElement(name = "SignDateTime")
    protected XMLGregorianCalendar signDateTime;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected List<SignatureType> signature;

    /**
     * Gets the value of the externalKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalKey() {
        return externalKey;
    }

    /**
     * Sets the value of the externalKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalKey(String value) {
        this.externalKey = value;
    }

    /**
     * Gets the value of the restrictnInternalKey property.
     * 
     */
    public long getRestrictnInternalKey() {
        return restrictnInternalKey;
    }

    /**
     * Sets the value of the restrictnInternalKey property.
     * 
     */
    public void setRestrictnInternalKey(long value) {
        this.restrictnInternalKey = value;
    }

    /**
     * Gets the value of the ipInternalKey property.
     * 
     */
    public long getIpInternalKey() {
        return ipInternalKey;
    }

    /**
     * Sets the value of the ipInternalKey property.
     * 
     */
    public void setIpInternalKey(long value) {
        this.ipInternalKey = value;
    }

    /**
     * Gets the value of the docDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDocDate() {
        return docDate;
    }

    /**
     * Sets the value of the docDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDocDate(XMLGregorianCalendar value) {
        this.docDate = value;
    }

    /**
     * Gets the value of the docNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocNumber() {
        return docNumber;
    }

    /**
     * Sets the value of the docNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocNumber(String value) {
        this.docNumber = value;
    }

    /**
     * Gets the value of the restrDocDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRestrDocDate() {
        return restrDocDate;
    }

    /**
     * Sets the value of the restrDocDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRestrDocDate(XMLGregorianCalendar value) {
        this.restrDocDate = value;
    }

    /**
     * Gets the value of the restrDocNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestrDocNumber() {
        return restrDocNumber;
    }

    /**
     * Sets the value of the restrDocNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestrDocNumber(String value) {
        this.restrDocNumber = value;
    }

    /**
     * Gets the value of the restrictionAnswerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestrictionAnswerType() {
        return restrictionAnswerType;
    }

    /**
     * Sets the value of the restrictionAnswerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestrictionAnswerType(String value) {
        this.restrictionAnswerType = value;
    }

    /**
     * Gets the value of the legalImpossibility property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalImpossibility() {
        return legalImpossibility;
    }

    /**
     * Sets the value of the legalImpossibility property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalImpossibility(String value) {
        this.legalImpossibility = value;
    }

    /**
     * Gets the value of the restrictedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRestrictedAmount() {
        return restrictedAmount;
    }

    /**
     * Sets the value of the restrictedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRestrictedAmount(BigDecimal value) {
        this.restrictedAmount = value;
    }

    /**
     * Gets the value of the restrictedAmountRub property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRestrictedAmountRub() {
        return restrictedAmountRub;
    }

    /**
     * Sets the value of the restrictedAmountRub property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRestrictedAmountRub(BigDecimal value) {
        this.restrictedAmountRub = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the data property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Data }
     * 
     * 
     */
    public List<Data> getData() {
        if (data == null) {
            data = new ArrayList<Data>();
        }
        return this.data;
    }

    /**
     * Gets the value of the restrictedData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the restrictedData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRestrictedData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Data }
     * 
     * 
     */
    public List<Data> getRestrictedData() {
        if (restrictedData == null) {
            restrictedData = new ArrayList<Data>();
        }
        return this.restrictedData;
    }

    /**
     * Gets the value of the nonRestrictedData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nonRestrictedData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNonRestrictedData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Data }
     * 
     * 
     */
    public List<Data> getNonRestrictedData() {
        if (nonRestrictedData == null) {
            nonRestrictedData = new ArrayList<Data>();
        }
        return this.nonRestrictedData;
    }

    /**
     * Gets the value of the signDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSignDateTime() {
        return signDateTime;
    }

    /**
     * Sets the value of the signDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSignDateTime(XMLGregorianCalendar value) {
        this.signDateTime = value;
    }

    /**
     * ЭП Gets the value of the signature property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signature property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignature().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SignatureType }
     * 
     * 
     */
    public List<SignatureType> getSignature() {
        if (signature == null) {
            signature = new ArrayList<SignatureType>();
        }
        return this.signature;
    }

}

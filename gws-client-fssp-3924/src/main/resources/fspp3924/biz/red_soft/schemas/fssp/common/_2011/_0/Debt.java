//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.schemas.fssp.common._2011._0;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3._2000._09.xmldsig.SignatureType;


/**
 * Содержит сведения об остатках долга по находящимся на исполнении исполнительным производствам
 *         для использования в системах платежей
 * 
 * <p>Java class for Debt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Debt">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType"/>
 *         &lt;element name="RequestExternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ExternalKeyType"/>
 *         &lt;element name="ReeIDD" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="IsUpdate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Boolean"/>
 *         &lt;element name="UnloadDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DateTime"/>
 *         &lt;element name="PrevDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DateTime" minOccurs="0"/>
 *         &lt;element name="HasMoreDebt" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Boolean"/>
 *         &lt;element name="DebtRecordCount" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Integer"/>
 *         &lt;element name="ToFrom" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}VkspType" minOccurs="0"/>
 *         &lt;element name="DebtDetail" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
@XmlType(name = "Debt", propOrder = {
    "internalKey",
    "requestExternalKey",
    "reeIDD",
    "isUpdate",
    "unloadDate",
    "prevDate",
    "hasMoreDebt",
    "debtRecordCount",
    "toFrom",
    "debtDetail",
    "signDateTime",
    "signature"
})
public class Debt {

    @XmlElement(name = "InternalKey")
    protected long internalKey;
    @XmlElement(name = "RequestExternalKey", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String requestExternalKey;
    @XmlElement(name = "ReeIDD", required = true)
    protected String reeIDD;
    @XmlElement(name = "IsUpdate")
    protected boolean isUpdate;
    @XmlElement(name = "UnloadDate", required = true)
    protected XMLGregorianCalendar unloadDate;
    @XmlElement(name = "PrevDate")
    protected XMLGregorianCalendar prevDate;
    @XmlElement(name = "HasMoreDebt")
    protected boolean hasMoreDebt;
    @XmlElement(name = "DebtRecordCount", required = true)
    protected BigInteger debtRecordCount;
    @XmlElement(name = "ToFrom")
    protected String toFrom;
    @XmlElement(name = "DebtDetail")
    @XmlMimeType("application/zip")
    protected DataHandler debtDetail;
    @XmlElement(name = "SignDateTime")
    protected XMLGregorianCalendar signDateTime;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected List<SignatureType> signature;

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
     * Gets the value of the requestExternalKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestExternalKey() {
        return requestExternalKey;
    }

    /**
     * Sets the value of the requestExternalKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestExternalKey(String value) {
        this.requestExternalKey = value;
    }

    /**
     * Gets the value of the reeIDD property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReeIDD() {
        return reeIDD;
    }

    /**
     * Sets the value of the reeIDD property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReeIDD(String value) {
        this.reeIDD = value;
    }

    /**
     * Gets the value of the isUpdate property.
     * 
     */
    public boolean isIsUpdate() {
        return isUpdate;
    }

    /**
     * Sets the value of the isUpdate property.
     * 
     */
    public void setIsUpdate(boolean value) {
        this.isUpdate = value;
    }

    /**
     * Gets the value of the unloadDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUnloadDate() {
        return unloadDate;
    }

    /**
     * Sets the value of the unloadDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUnloadDate(XMLGregorianCalendar value) {
        this.unloadDate = value;
    }

    /**
     * Gets the value of the prevDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPrevDate() {
        return prevDate;
    }

    /**
     * Sets the value of the prevDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPrevDate(XMLGregorianCalendar value) {
        this.prevDate = value;
    }

    /**
     * Gets the value of the hasMoreDebt property.
     * 
     */
    public boolean isHasMoreDebt() {
        return hasMoreDebt;
    }

    /**
     * Sets the value of the hasMoreDebt property.
     * 
     */
    public void setHasMoreDebt(boolean value) {
        this.hasMoreDebt = value;
    }

    /**
     * Gets the value of the debtRecordCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDebtRecordCount() {
        return debtRecordCount;
    }

    /**
     * Sets the value of the debtRecordCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDebtRecordCount(BigInteger value) {
        this.debtRecordCount = value;
    }

    /**
     * Gets the value of the toFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToFrom() {
        return toFrom;
    }

    /**
     * Sets the value of the toFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToFrom(String value) {
        this.toFrom = value;
    }

    /**
     * Gets the value of the debtDetail property.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getDebtDetail() {
        return debtDetail;
    }

    /**
     * Sets the value of the debtDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setDebtDetail(DataHandler value) {
        this.debtDetail = value;
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

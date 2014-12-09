//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.schemas.fssp.common._2011._0;

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
 * Справка о начисленном денежном довольствии и произведенных удержаниях
 * 
 * <p>Java class for SalaryWithheldAnswer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SalaryWithheldAnswer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ExternalKeyType"/>
 *         &lt;element name="QueryInternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType" minOccurs="0"/>
 *         &lt;element name="IpInternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType"/>
 *         &lt;element name="AnswerDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="AnswerType" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}QueryAnswerType"/>
 *         &lt;element name="AuthorName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FullFioType" minOccurs="0"/>
 *         &lt;element name="AuthorPost" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_50" minOccurs="0"/>
 *         &lt;element name="AuthorEmail" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}EMailType" minOccurs="0"/>
 *         &lt;element name="AuthorTelephone" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}TelephoneNumberType" minOccurs="0"/>
 *         &lt;element name="Data" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Data" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Attachments" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}AttachmentType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "SalaryWithheldAnswer", propOrder = {
    "externalKey",
    "queryInternalKey",
    "ipInternalKey",
    "answerDate",
    "answerType",
    "authorName",
    "authorPost",
    "authorEmail",
    "authorTelephone",
    "data",
    "attachments",
    "signDateTime",
    "signature"
})
public class SalaryWithheldAnswer {

    @XmlElement(name = "ExternalKey", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String externalKey;
    @XmlElement(name = "QueryInternalKey")
    protected Long queryInternalKey;
    @XmlElement(name = "IpInternalKey")
    protected long ipInternalKey;
    @XmlElement(name = "AnswerDate")
    protected XMLGregorianCalendar answerDate;
    @XmlElement(name = "AnswerType", required = true)
    protected String answerType;
    @XmlElement(name = "AuthorName")
    protected String authorName;
    @XmlElement(name = "AuthorPost")
    protected String authorPost;
    @XmlElement(name = "AuthorEmail")
    protected String authorEmail;
    @XmlElement(name = "AuthorTelephone")
    protected String authorTelephone;
    @XmlElement(name = "Data")
    protected List<Data> data;
    @XmlElement(name = "Attachments")
    protected List<AttachmentType> attachments;
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
     * Gets the value of the queryInternalKey property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getQueryInternalKey() {
        return queryInternalKey;
    }

    /**
     * Sets the value of the queryInternalKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setQueryInternalKey(Long value) {
        this.queryInternalKey = value;
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
     * Gets the value of the answerDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAnswerDate() {
        return answerDate;
    }

    /**
     * Sets the value of the answerDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAnswerDate(XMLGregorianCalendar value) {
        this.answerDate = value;
    }

    /**
     * Gets the value of the answerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnswerType() {
        return answerType;
    }

    /**
     * Sets the value of the answerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnswerType(String value) {
        this.answerType = value;
    }

    /**
     * Gets the value of the authorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Sets the value of the authorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorName(String value) {
        this.authorName = value;
    }

    /**
     * Gets the value of the authorPost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorPost() {
        return authorPost;
    }

    /**
     * Sets the value of the authorPost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorPost(String value) {
        this.authorPost = value;
    }

    /**
     * Gets the value of the authorEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorEmail() {
        return authorEmail;
    }

    /**
     * Sets the value of the authorEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorEmail(String value) {
        this.authorEmail = value;
    }

    /**
     * Gets the value of the authorTelephone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorTelephone() {
        return authorTelephone;
    }

    /**
     * Sets the value of the authorTelephone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorTelephone(String value) {
        this.authorTelephone = value;
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
     * Gets the value of the attachments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attachments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttachments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttachmentType }
     * 
     * 
     */
    public List<AttachmentType> getAttachments() {
        if (attachments == null) {
            attachments = new ArrayList<AttachmentType>();
        }
        return this.attachments;
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

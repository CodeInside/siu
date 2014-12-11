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
 * Сведения об открытии должниками по ИП новых счетов в кредитных организациях
 * 
 * <p>Java class for DebtorNewAccountNotice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DebtorNewAccountNotice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ExternalKeyType"/>
 *         &lt;element name="RestrictnInternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType" minOccurs="0"/>
 *         &lt;element name="IpInternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType"/>
 *         &lt;element name="IpNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="RestrictnDocNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="RestrictnDocDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="DocNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType" minOccurs="0"/>
 *         &lt;element name="DocDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="AuthorName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FullFioType"/>
 *         &lt;element name="AuthorPost" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_50" minOccurs="0"/>
 *         &lt;element name="AuthorEmail" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}EMailType" minOccurs="0"/>
 *         &lt;element name="AuthorTelephone" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}TelephoneNumberType" minOccurs="0"/>
 *         &lt;element name="Data" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Data" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "DebtorNewAccountNotice", propOrder = {
    "externalKey",
    "restrictnInternalKey",
    "ipInternalKey",
    "ipNumber",
    "restrictnDocNumber",
    "restrictnDocDate",
    "docNumber",
    "docDate",
    "authorName",
    "authorPost",
    "authorEmail",
    "authorTelephone",
    "data",
    "signature"
})
public class DebtorNewAccountNotice {

    @XmlElement(name = "ExternalKey", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String externalKey;
    @XmlElement(name = "RestrictnInternalKey")
    protected Long restrictnInternalKey;
    @XmlElement(name = "IpInternalKey")
    protected long ipInternalKey;
    @XmlElement(name = "IpNumber", required = true)
    protected String ipNumber;
    @XmlElement(name = "RestrictnDocNumber", required = true)
    protected String restrictnDocNumber;
    @XmlElement(name = "RestrictnDocDate", required = true)
    protected XMLGregorianCalendar restrictnDocDate;
    @XmlElement(name = "DocNumber")
    protected String docNumber;
    @XmlElement(name = "DocDate", required = true)
    protected XMLGregorianCalendar docDate;
    @XmlElement(name = "AuthorName", required = true)
    protected String authorName;
    @XmlElement(name = "AuthorPost")
    protected String authorPost;
    @XmlElement(name = "AuthorEmail")
    protected String authorEmail;
    @XmlElement(name = "AuthorTelephone")
    protected String authorTelephone;
    @XmlElement(name = "Data")
    protected List<Data> data;
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
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRestrictnInternalKey() {
        return restrictnInternalKey;
    }

    /**
     * Sets the value of the restrictnInternalKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRestrictnInternalKey(Long value) {
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
     * Gets the value of the ipNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpNumber() {
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
    public void setIpNumber(String value) {
        this.ipNumber = value;
    }

    /**
     * Gets the value of the restrictnDocNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestrictnDocNumber() {
        return restrictnDocNumber;
    }

    /**
     * Sets the value of the restrictnDocNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestrictnDocNumber(String value) {
        this.restrictnDocNumber = value;
    }

    /**
     * Gets the value of the restrictnDocDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRestrictnDocDate() {
        return restrictnDocDate;
    }

    /**
     * Sets the value of the restrictnDocDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRestrictnDocDate(XMLGregorianCalendar value) {
        this.restrictnDocDate = value;
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
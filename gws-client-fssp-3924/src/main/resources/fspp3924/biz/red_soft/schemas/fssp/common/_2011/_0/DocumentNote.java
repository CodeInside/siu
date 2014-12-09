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
 * Документ DocumentNote «Технологическое сообщение»
 * 
 * <p>Java class for DocumentNote complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocumentNote">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ExternalKeyType" minOccurs="0"/>
 *         &lt;element name="InternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType" minOccurs="0"/>
 *         &lt;element name="TypeDoc" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocumentType" minOccurs="0"/>
 *         &lt;element name="DocExternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ExternalKeyType" minOccurs="0"/>
 *         &lt;element name="DocInternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType" minOccurs="0"/>
 *         &lt;element name="SpecStr" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000" minOccurs="0"/>
 *         &lt;element name="DateMess" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DateTime"/>
 *         &lt;element name="MessCod" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}MessageCodeType"/>
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
@XmlType(name = "DocumentNote", propOrder = {
    "externalKey",
    "internalKey",
    "typeDoc",
    "docExternalKey",
    "docInternalKey",
    "specStr",
    "dateMess",
    "messCod",
    "signDateTime",
    "signature"
})
public class DocumentNote {

    @XmlElement(name = "ExternalKey")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String externalKey;
    @XmlElement(name = "InternalKey")
    protected Long internalKey;
    @XmlElement(name = "TypeDoc")
    protected String typeDoc;
    @XmlElement(name = "DocExternalKey")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String docExternalKey;
    @XmlElement(name = "DocInternalKey")
    protected Long docInternalKey;
    @XmlElement(name = "SpecStr")
    protected String specStr;
    @XmlElement(name = "DateMess", required = true)
    protected XMLGregorianCalendar dateMess;
    @XmlElement(name = "MessCod", required = true)
    protected String messCod;
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
     * Gets the value of the internalKey property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getInternalKey() {
        return internalKey;
    }

    /**
     * Sets the value of the internalKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setInternalKey(Long value) {
        this.internalKey = value;
    }

    /**
     * Gets the value of the typeDoc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeDoc() {
        return typeDoc;
    }

    /**
     * Sets the value of the typeDoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeDoc(String value) {
        this.typeDoc = value;
    }

    /**
     * Gets the value of the docExternalKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocExternalKey() {
        return docExternalKey;
    }

    /**
     * Sets the value of the docExternalKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocExternalKey(String value) {
        this.docExternalKey = value;
    }

    /**
     * Gets the value of the docInternalKey property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDocInternalKey() {
        return docInternalKey;
    }

    /**
     * Sets the value of the docInternalKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDocInternalKey(Long value) {
        this.docInternalKey = value;
    }

    /**
     * Gets the value of the specStr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecStr() {
        return specStr;
    }

    /**
     * Sets the value of the specStr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecStr(String value) {
        this.specStr = value;
    }

    /**
     * Gets the value of the dateMess property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateMess() {
        return dateMess;
    }

    /**
     * Sets the value of the dateMess property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateMess(XMLGregorianCalendar value) {
        this.dateMess = value;
    }

    /**
     * Gets the value of the messCod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessCod() {
        return messCod;
    }

    /**
     * Sets the value of the messCod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessCod(String value) {
        this.messCod = value;
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

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
import org.w3._2000._09.xmldsig.SignatureType;


/**
 * Заявление взыскателя о подписке на сведения о ходе исполнительного производства
 * 
 * <p>Java class for IPSideAppSubscription complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPSideAppSubscription">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideRequestType">
 *       &lt;sequence>
 *         &lt;element name="Subscription" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_4000"/>
 *         &lt;element name="AuthorBackAddrType" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DebtorNotifyType"/>
 *         &lt;element name="AuthorBackAddr" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_150"/>
 *         &lt;element name="Attachments" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}AttachmentType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPSideAppSubscription", propOrder = {
    "subscription",
    "authorBackAddrType",
    "authorBackAddr",
    "attachments",
    "signature"
})
public class IPSideAppSubscription
    extends IPSideRequestType
{

    @XmlElement(name = "Subscription", required = true)
    protected String subscription;
    @XmlElement(name = "AuthorBackAddrType", required = true)
    protected String authorBackAddrType;
    @XmlElement(name = "AuthorBackAddr", required = true)
    protected String authorBackAddr;
    @XmlElement(name = "Attachments")
    protected List<AttachmentType> attachments;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected List<SignatureType> signature;

    /**
     * Gets the value of the subscription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubscription() {
        return subscription;
    }

    /**
     * Sets the value of the subscription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubscription(String value) {
        this.subscription = value;
    }

    /**
     * Gets the value of the authorBackAddrType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorBackAddrType() {
        return authorBackAddrType;
    }

    /**
     * Sets the value of the authorBackAddrType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorBackAddrType(String value) {
        this.authorBackAddrType = value;
    }

    /**
     * Gets the value of the authorBackAddr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorBackAddr() {
        return authorBackAddr;
    }

    /**
     * Sets the value of the authorBackAddr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorBackAddr(String value) {
        this.authorBackAddr = value;
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
     * ЭП документа Gets the value of the signature property.
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
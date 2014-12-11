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
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3._2000._09.xmldsig.SignatureType;


/**
 * Жалоба в порядке подчинённости
 * 
 * <p>Java class for ComplaintApplication complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ComplaintApplication">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideRequestType">
 *       &lt;sequence>
 *         &lt;element name="WarrantType" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}WarrantTypeType" minOccurs="0"/>
 *         &lt;element name="WarrantDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="WarrantNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType" minOccurs="0"/>
 *         &lt;element name="AnswerForm" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}AnswerFormType"/>
 *         &lt;element name="ComplaintType" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ComplaintTypeType"/>
 *         &lt;element name="IPNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="SPIDocDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="SPIDocNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType" minOccurs="0"/>
 *         &lt;element name="SPIManagementType" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}SPIManagementTypeType"/>
 *         &lt;element name="SPIFIO" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FioType"/>
 *         &lt;element name="Ground" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_4000"/>
 *         &lt;element name="Requirements" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_4000"/>
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
@XmlType(name = "ComplaintApplication", propOrder = {
    "warrantType",
    "warrantDate",
    "warrantNumber",
    "answerForm",
    "complaintType",
    "ipNumber",
    "spiDocDate",
    "spiDocNumber",
    "spiManagementType",
    "spifio",
    "ground",
    "requirements",
    "attachments",
    "signature"
})
public class ComplaintApplication
    extends IPSideRequestType
{

    @XmlElement(name = "WarrantType")
    protected String warrantType;
    @XmlElement(name = "WarrantDate")
    protected XMLGregorianCalendar warrantDate;
    @XmlElement(name = "WarrantNumber")
    protected String warrantNumber;
    @XmlElement(name = "AnswerForm", required = true)
    protected String answerForm;
    @XmlElement(name = "ComplaintType", required = true)
    protected String complaintType;
    @XmlElement(name = "IPNumber", required = true)
    protected String ipNumber;
    @XmlElement(name = "SPIDocDate")
    protected XMLGregorianCalendar spiDocDate;
    @XmlElement(name = "SPIDocNumber")
    protected String spiDocNumber;
    @XmlElement(name = "SPIManagementType", required = true)
    protected SPIManagementTypeType spiManagementType;
    @XmlElement(name = "SPIFIO", required = true)
    protected FioType spifio;
    @XmlElement(name = "Ground", required = true)
    protected String ground;
    @XmlElement(name = "Requirements", required = true)
    protected String requirements;
    @XmlElement(name = "Attachments")
    protected List<AttachmentType> attachments;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected List<SignatureType> signature;

    /**
     * Gets the value of the warrantType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarrantType() {
        return warrantType;
    }

    /**
     * Sets the value of the warrantType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarrantType(String value) {
        this.warrantType = value;
    }

    /**
     * Gets the value of the warrantDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getWarrantDate() {
        return warrantDate;
    }

    /**
     * Sets the value of the warrantDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setWarrantDate(XMLGregorianCalendar value) {
        this.warrantDate = value;
    }

    /**
     * Gets the value of the warrantNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarrantNumber() {
        return warrantNumber;
    }

    /**
     * Sets the value of the warrantNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarrantNumber(String value) {
        this.warrantNumber = value;
    }

    /**
     * Gets the value of the answerForm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnswerForm() {
        return answerForm;
    }

    /**
     * Sets the value of the answerForm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnswerForm(String value) {
        this.answerForm = value;
    }

    /**
     * Gets the value of the complaintType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComplaintType() {
        return complaintType;
    }

    /**
     * Sets the value of the complaintType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComplaintType(String value) {
        this.complaintType = value;
    }

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
     * Gets the value of the spiDocDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSPIDocDate() {
        return spiDocDate;
    }

    /**
     * Sets the value of the spiDocDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSPIDocDate(XMLGregorianCalendar value) {
        this.spiDocDate = value;
    }

    /**
     * Gets the value of the spiDocNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPIDocNumber() {
        return spiDocNumber;
    }

    /**
     * Sets the value of the spiDocNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPIDocNumber(String value) {
        this.spiDocNumber = value;
    }

    /**
     * Gets the value of the spiManagementType property.
     * 
     * @return
     *     possible object is
     *     {@link SPIManagementTypeType }
     *     
     */
    public SPIManagementTypeType getSPIManagementType() {
        return spiManagementType;
    }

    /**
     * Sets the value of the spiManagementType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SPIManagementTypeType }
     *     
     */
    public void setSPIManagementType(SPIManagementTypeType value) {
        this.spiManagementType = value;
    }

    /**
     * Gets the value of the spifio property.
     * 
     * @return
     *     possible object is
     *     {@link FioType }
     *     
     */
    public FioType getSPIFIO() {
        return spifio;
    }

    /**
     * Sets the value of the spifio property.
     * 
     * @param value
     *     allowed object is
     *     {@link FioType }
     *     
     */
    public void setSPIFIO(FioType value) {
        this.spifio = value;
    }

    /**
     * Gets the value of the ground property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGround() {
        return ground;
    }

    /**
     * Sets the value of the ground property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGround(String value) {
        this.ground = value;
    }

    /**
     * Gets the value of the requirements property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequirements() {
        return requirements;
    }

    /**
     * Sets the value of the requirements property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequirements(String value) {
        this.requirements = value;
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
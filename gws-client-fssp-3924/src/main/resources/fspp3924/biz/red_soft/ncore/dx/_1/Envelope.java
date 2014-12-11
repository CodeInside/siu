//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.ncore.dx._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Конверт
 * 
 * <p>Java class for Envelope complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Envelope">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Direction" type="{http://www.red-soft.biz/ncore/dx/1.1}Direction" minOccurs="0"/>
 *         &lt;element name="DocNumber" type="{http://www.red-soft.biz/ncore/dx/1.1}DocumentNumber" minOccurs="0"/>
 *         &lt;element name="DocDate" type="{http://www.red-soft.biz/ncore/dx/1.1}Date" minOccurs="0"/>
 *         &lt;element name="DocBarCode" type="{http://www.red-soft.biz/ncore/dx/1.1}BarCode" minOccurs="0"/>
 *         &lt;element ref="{http://www.red-soft.biz/ncore/dx/1.1}DocumentAttrs" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="Document" type="{http://www.red-soft.biz/ncore/dx/1.1}DocumentContainer" minOccurs="0"/>
 *             &lt;choice minOccurs="0">
 *               &lt;element name="AttachmentsFileInfo" type="{http://www.red-soft.biz/ncore/dx/1.1}EnvelopeFileInfo"/>
 *             &lt;/choice>
 *           &lt;/sequence>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.red-soft.biz/ncore/dx/1.1}FileInfoAttr"/>
 *       &lt;attribute name="attachmentTypeCode" type="{http://www.red-soft.biz/ncore/dx/1.1}AttachmentTypeCodeType" />
 *       &lt;attribute name="doc_id" type="{http://www.red-soft.biz/ncore/dx/1.1}Id" />
 *       &lt;attribute name="doc_type" type="{http://www.red-soft.biz/ncore/dx/1.1}DocumentType" />
 *       &lt;attribute name="id" use="required" type="{http://www.red-soft.biz/ncore/dx/1.1}Id" />
 *       &lt;attribute name="event" type="{http://www.red-soft.biz/ncore/dx/1.1}Event" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Envelope", propOrder = {
    "direction",
    "docNumber",
    "docDate",
    "docBarCode",
    "documentAttrs",
    "document",
    "attachmentsFileInfo"
})
public class Envelope {

    @XmlElement(name = "Direction")
    protected Direction direction;
    @XmlElement(name = "DocNumber")
    protected String docNumber;
    @XmlElement(name = "DocDate")
    protected XMLGregorianCalendar docDate;
    @XmlElement(name = "DocBarCode")
    protected String docBarCode;
    @XmlElement(name = "DocumentAttrs")
    protected DocumentAttrs documentAttrs;
    @XmlElement(name = "Document")
    protected DocumentContainer document;
    @XmlElement(name = "AttachmentsFileInfo")
    protected EnvelopeFileInfo attachmentsFileInfo;
    @XmlAttribute(name = "attachmentTypeCode")
    protected String attachmentTypeCode;
    @XmlAttribute(name = "doc_id")
    protected String docId;
    @XmlAttribute(name = "doc_type")
    protected String docType;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "event")
    protected String event;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "content_type")
    protected String contentType;
    @XmlAttribute(name = "attachment_id")
    protected String attachmentId;

    /**
     * Gets the value of the direction property.
     * 
     * @return
     *     possible object is
     *     {@link Direction }
     *     
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Direction }
     *     
     */
    public void setDirection(Direction value) {
        this.direction = value;
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
     * Gets the value of the docBarCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocBarCode() {
        return docBarCode;
    }

    /**
     * Sets the value of the docBarCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocBarCode(String value) {
        this.docBarCode = value;
    }

    /**
     * Дополнительные атрибуты документов
     * 
     * @return
     *     possible object is
     *     {@link DocumentAttrs }
     *     
     */
    public DocumentAttrs getDocumentAttrs() {
        return documentAttrs;
    }

    /**
     * Sets the value of the documentAttrs property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentAttrs }
     *     
     */
    public void setDocumentAttrs(DocumentAttrs value) {
        this.documentAttrs = value;
    }

    /**
     * Gets the value of the document property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentContainer }
     *     
     */
    public DocumentContainer getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentContainer }
     *     
     */
    public void setDocument(DocumentContainer value) {
        this.document = value;
    }

    /**
     * Gets the value of the attachmentsFileInfo property.
     * 
     * @return
     *     possible object is
     *     {@link EnvelopeFileInfo }
     *     
     */
    public EnvelopeFileInfo getAttachmentsFileInfo() {
        return attachmentsFileInfo;
    }

    /**
     * Sets the value of the attachmentsFileInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnvelopeFileInfo }
     *     
     */
    public void setAttachmentsFileInfo(EnvelopeFileInfo value) {
        this.attachmentsFileInfo = value;
    }

    /**
     * Gets the value of the attachmentTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachmentTypeCode() {
        return attachmentTypeCode;
    }

    /**
     * Sets the value of the attachmentTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachmentTypeCode(String value) {
        this.attachmentTypeCode = value;
    }

    /**
     * Gets the value of the docId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocId() {
        return docId;
    }

    /**
     * Sets the value of the docId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocId(String value) {
        this.docId = value;
    }

    /**
     * Gets the value of the docType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocType() {
        return docType;
    }

    /**
     * Sets the value of the docType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocType(String value) {
        this.docType = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the event property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvent() {
        return event;
    }

    /**
     * Sets the value of the event property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvent(String value) {
        this.event = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the contentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the value of the contentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentType(String value) {
        this.contentType = value;
    }

    /**
     * Gets the value of the attachmentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachmentId() {
        return attachmentId;
    }

    /**
     * Sets the value of the attachmentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachmentId(String value) {
        this.attachmentId = value;
    }

}
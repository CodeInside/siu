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
 * Квитанция на документ пакета
 * 
 * <p>Java class for DocNote complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocNote">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Message" type="{http://www.red-soft.biz/ncore/dx/1.1}MessageEx"/>
 *         &lt;element name="DocRef" type="{http://www.red-soft.biz/ncore/dx/1.1}DocumentRef" minOccurs="0"/>
 *         &lt;element name="Status" type="{http://www.red-soft.biz/ncore/dx/1.1}ProcessResult" minOccurs="0"/>
 *         &lt;element name="FileNote" type="{http://www.red-soft.biz/ncore/dx/1.1}AttachmentFileNote" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="timestamp" type="{http://www.red-soft.biz/ncore/dx/1.1}DateTime" />
 *       &lt;attribute name="id" type="{http://www.red-soft.biz/ncore/dx/1.1}Id" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocNote", propOrder = {
    "message",
    "docRef",
    "status",
    "fileNote"
})
public class DocNote {

    @XmlElement(name = "Message", required = true)
    protected MessageEx message;
    @XmlElement(name = "DocRef")
    protected DocumentRef docRef;
    @XmlElement(name = "Status")
    protected ProcessResult status;
    @XmlElement(name = "FileNote")
    protected AttachmentFileNote fileNote;
    @XmlAttribute(name = "timestamp")
    protected XMLGregorianCalendar timestamp;
    @XmlAttribute(name = "id")
    protected String id;

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link MessageEx }
     *     
     */
    public MessageEx getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageEx }
     *     
     */
    public void setMessage(MessageEx value) {
        this.message = value;
    }

    /**
     * Gets the value of the docRef property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentRef }
     *     
     */
    public DocumentRef getDocRef() {
        return docRef;
    }

    /**
     * Sets the value of the docRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentRef }
     *     
     */
    public void setDocRef(DocumentRef value) {
        this.docRef = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link ProcessResult }
     *     
     */
    public ProcessResult getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessResult }
     *     
     */
    public void setStatus(ProcessResult value) {
        this.status = value;
    }

    /**
     * Gets the value of the fileNote property.
     * 
     * @return
     *     possible object is
     *     {@link AttachmentFileNote }
     *     
     */
    public AttachmentFileNote getFileNote() {
        return fileNote;
    }

    /**
     * Sets the value of the fileNote property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttachmentFileNote }
     *     
     */
    public void setFileNote(AttachmentFileNote value) {
        this.fileNote = value;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimestamp(XMLGregorianCalendar value) {
        this.timestamp = value;
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

}

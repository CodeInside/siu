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
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Информация о фрагменте файла
 * 
 * <p>Java class for FileBlockInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FileBlockInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attGroup ref="{http://www.red-soft.biz/ncore/dx/1.1}FileBlockInfoAttr"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileBlockInfo")
public class FileBlockInfo {

    @XmlAttribute(name = "offset")
    protected Long offset;
    @XmlAttribute(name = "length")
    protected Long length;
    @XmlAttribute(name = "receiver_pack_id")
    protected String receiverPackId;
    @XmlAttribute(name = "receiver_env_id")
    protected String receiverEnvId;
    @XmlAttribute(name = "receiver_doc_id")
    protected String receiverDocId;
    @XmlAttribute(name = "encoding")
    protected String encoding;
    @XmlAttribute(name = "size", required = true)
    protected long size;
    @XmlAttribute(name = "type")
    protected FileType type;
    @XmlAttribute(name = "digest")
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    protected byte[] digest;
    @XmlAttribute(name = "pack_id")
    protected String packId;
    @XmlAttribute(name = "env_id")
    protected String envId;
    @XmlAttribute(name = "doc_id")
    protected String docId;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "content_type")
    protected String contentType;
    @XmlAttribute(name = "attachment_id")
    protected String attachmentId;

    /**
     * Gets the value of the offset property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOffset() {
        return offset;
    }

    /**
     * Sets the value of the offset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOffset(Long value) {
        this.offset = value;
    }

    /**
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLength(Long value) {
        this.length = value;
    }

    /**
     * Gets the value of the receiverPackId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiverPackId() {
        return receiverPackId;
    }

    /**
     * Sets the value of the receiverPackId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiverPackId(String value) {
        this.receiverPackId = value;
    }

    /**
     * Gets the value of the receiverEnvId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiverEnvId() {
        return receiverEnvId;
    }

    /**
     * Sets the value of the receiverEnvId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiverEnvId(String value) {
        this.receiverEnvId = value;
    }

    /**
     * Gets the value of the receiverDocId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiverDocId() {
        return receiverDocId;
    }

    /**
     * Sets the value of the receiverDocId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiverDocId(String value) {
        this.receiverDocId = value;
    }

    /**
     * Gets the value of the encoding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the value of the encoding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncoding(String value) {
        this.encoding = value;
    }

    /**
     * Gets the value of the size property.
     * 
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     */
    public void setSize(long value) {
        this.size = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link FileType }
     *     
     */
    public FileType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileType }
     *     
     */
    public void setType(FileType value) {
        this.type = value;
    }

    /**
     * Gets the value of the digest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public byte[] getDigest() {
        return digest;
    }

    /**
     * Sets the value of the digest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDigest(byte[] value) {
        this.digest = value;
    }

    /**
     * Gets the value of the packId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackId() {
        return packId;
    }

    /**
     * Sets the value of the packId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackId(String value) {
        this.packId = value;
    }

    /**
     * Gets the value of the envId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvId() {
        return envId;
    }

    /**
     * Sets the value of the envId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvId(String value) {
        this.envId = value;
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

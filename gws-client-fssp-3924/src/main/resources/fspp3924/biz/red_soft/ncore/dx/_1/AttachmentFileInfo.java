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


/**
 * Информация о вложении документа
 * 
 * <p>Java class for AttachmentFileInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AttachmentFileInfo">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/ncore/dx/1.1}FileInfo">
 *       &lt;attribute name="attachmentTypeCode" type="{http://www.red-soft.biz/ncore/dx/1.1}AttachmentTypeCodeType" />
 *       &lt;attribute name="systemCode1" type="{http://www.red-soft.biz/ncore/dx/1.1}SystemCode1Type" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttachmentFileInfo")
public class AttachmentFileInfo
    extends FileInfo
{

    @XmlAttribute(name = "attachmentTypeCode")
    protected String attachmentTypeCode;
    @XmlAttribute(name = "systemCode1")
    protected String systemCode1;

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
     * Gets the value of the systemCode1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemCode1() {
        return systemCode1;
    }

    /**
     * Sets the value of the systemCode1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemCode1(String value) {
        this.systemCode1 = value;
    }

}

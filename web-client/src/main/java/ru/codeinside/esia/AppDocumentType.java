
package ru.codeinside.esia;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AppDocumentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AppDocumentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RequestCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BinaryData" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="Reference" type="{http://smev.gosuslugi.ru/rev120315}ReferenceType" minOccurs="0"/>
 *         &lt;element name="DigestValue" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppDocumentType", propOrder = {
    "requestCode",
    "binaryData",
    "reference",
    "digestValue"
})
public class AppDocumentType {

    @XmlElement(name = "RequestCode", required = true)
    protected String requestCode;
    @XmlElement(name = "BinaryData")
    protected byte[] binaryData;
    @XmlElement(name = "Reference")
    protected ReferenceType reference;
    @XmlElement(name = "DigestValue")
    protected byte[] digestValue;

    /**
     * Gets the value of the requestCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestCode() {
        return requestCode;
    }

    /**
     * Sets the value of the requestCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestCode(String value) {
        this.requestCode = value;
    }

    /**
     * Gets the value of the binaryData property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBinaryData() {
        return binaryData;
    }

    /**
     * Sets the value of the binaryData property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBinaryData(byte[] value) {
        this.binaryData = value;
    }

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link ReferenceType }
     *     
     */
    public ReferenceType getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferenceType }
     *     
     */
    public void setReference(ReferenceType value) {
        this.reference = value;
    }

    /**
     * Gets the value of the digestValue property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDigestValue() {
        return digestValue;
    }

    /**
     * Sets the value of the digestValue property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDigestValue(byte[] value) {
        this.digestValue = value;
    }

}

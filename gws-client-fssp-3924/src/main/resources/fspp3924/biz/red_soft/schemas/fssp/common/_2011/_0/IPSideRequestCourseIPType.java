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
 * Документ IPSideRequestCourseIPType «Регистрация запроса о ходе ИП по номеру ИП»
 * 
 * <p>Java class for IPSideRequestCourseIPType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPSideRequestCourseIPType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideRequestType">
 *       &lt;sequence>
 *         &lt;element name="IPNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_25"/>
 *         &lt;element name="IsInteractive" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Boolean"/>
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
@XmlType(name = "IPSideRequestCourseIPType", propOrder = {
    "ipNumber",
    "isInteractive",
    "signature"
})
public class IPSideRequestCourseIPType
    extends IPSideRequestType
{

    @XmlElement(name = "IPNumber", required = true)
    protected String ipNumber;
    @XmlElement(name = "IsInteractive")
    protected boolean isInteractive;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected List<SignatureType> signature;

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
     * Gets the value of the isInteractive property.
     * 
     */
    public boolean isIsInteractive() {
        return isInteractive;
    }

    /**
     * Sets the value of the isInteractive property.
     * 
     */
    public void setIsInteractive(boolean value) {
        this.isInteractive = value;
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
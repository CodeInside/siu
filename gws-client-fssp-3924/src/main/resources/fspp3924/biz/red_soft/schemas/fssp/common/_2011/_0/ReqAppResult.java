//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.schemas.fssp.common._2011._0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Запрос получения статуса рассмотрения обращений
 * 
 * <p>Java class for ReqAppResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReqAppResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ExternalKeyType"/>
 *         &lt;element name="DocDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="DocNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="Applicant" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}EMailType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReqAppResult", propOrder = {
    "externalKey",
    "docDate",
    "docNumber",
    "applicant"
})
public class ReqAppResult {

    @XmlElement(name = "ExternalKey", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String externalKey;
    @XmlElement(name = "DocDate", required = true)
    protected XMLGregorianCalendar docDate;
    @XmlElement(name = "DocNumber", required = true)
    protected String docNumber;
    @XmlElement(name = "Applicant")
    protected String applicant;

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
     * Gets the value of the applicant property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicant() {
        return applicant;
    }

    /**
     * Sets the value of the applicant property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicant(String value) {
        this.applicant = value;
    }

}
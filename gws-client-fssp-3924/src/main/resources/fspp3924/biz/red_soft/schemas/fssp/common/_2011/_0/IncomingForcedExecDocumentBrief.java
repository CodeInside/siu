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
 * Тип данных документа «Входящий документ принудительного исполнения (краткая форма)» используется
 *         для предоставления информации внешнему контрагенту о документе в составе списка документов.
 * 
 * <p>Java class for IncomingForcedExecDocumentBrief complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IncomingForcedExecDocumentBrief">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ExternalKeyType"/>
 *         &lt;group ref="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocumentPropertiesGroup"/>
 *         &lt;element name="SideProperties" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ContragentDatumType"/>
 *         &lt;element name="Status" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ApplicationStatus"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IncomingForcedExecDocumentBrief", propOrder = {
    "externalKey",
    "docType",
    "docNumber",
    "docDate",
    "sideProperties",
    "status"
})
public class IncomingForcedExecDocumentBrief {

    @XmlElement(name = "ExternalKey", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String externalKey;
    @XmlElement(name = "DocType", required = true)
    protected String docType;
    @XmlElement(name = "DocNumber")
    protected String docNumber;
    @XmlElement(name = "DocDate")
    protected XMLGregorianCalendar docDate;
    @XmlElement(name = "SideProperties", required = true)
    protected ContragentDatumType sideProperties;
    @XmlElement(name = "Status", required = true)
    protected ApplicationStatus status;

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
     * Gets the value of the sideProperties property.
     * 
     * @return
     *     possible object is
     *     {@link ContragentDatumType }
     *     
     */
    public ContragentDatumType getSideProperties() {
        return sideProperties;
    }

    /**
     * Sets the value of the sideProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContragentDatumType }
     *     
     */
    public void setSideProperties(ContragentDatumType value) {
        this.sideProperties = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link ApplicationStatus }
     *     
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplicationStatus }
     *     
     */
    public void setStatus(ApplicationStatus value) {
        this.status = value;
    }

}
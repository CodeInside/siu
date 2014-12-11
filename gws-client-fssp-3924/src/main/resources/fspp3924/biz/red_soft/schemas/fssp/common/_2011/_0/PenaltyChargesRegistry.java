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


/**
 * Реестр штрафов по АД
 * 
 * <p>Java class for PenaltyChargesRegistry complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PenaltyChargesRegistry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Version" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_8"/>
 *         &lt;element name="InternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType"/>
 *         &lt;element name="OspCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}VkspType"/>
 *         &lt;element name="DocDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="DocNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="UnloadDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DateTime"/>
 *         &lt;element name="PenaltyData" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}PenaltyChargesData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PenaltyChargesRegistry", propOrder = {
    "version",
    "internalKey",
    "ospCode",
    "docDate",
    "docNumber",
    "unloadDate",
    "penaltyData"
})
public class PenaltyChargesRegistry {

    @XmlElement(name = "Version", required = true)
    protected String version;
    @XmlElement(name = "InternalKey")
    protected long internalKey;
    @XmlElement(name = "OspCode", required = true)
    protected String ospCode;
    @XmlElement(name = "DocDate", required = true)
    protected XMLGregorianCalendar docDate;
    @XmlElement(name = "DocNumber", required = true)
    protected String docNumber;
    @XmlElement(name = "UnloadDate", required = true)
    protected XMLGregorianCalendar unloadDate;
    @XmlElement(name = "PenaltyData")
    protected List<PenaltyChargesData> penaltyData;

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the internalKey property.
     * 
     */
    public long getInternalKey() {
        return internalKey;
    }

    /**
     * Sets the value of the internalKey property.
     * 
     */
    public void setInternalKey(long value) {
        this.internalKey = value;
    }

    /**
     * Gets the value of the ospCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOspCode() {
        return ospCode;
    }

    /**
     * Sets the value of the ospCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOspCode(String value) {
        this.ospCode = value;
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
     * Gets the value of the unloadDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUnloadDate() {
        return unloadDate;
    }

    /**
     * Sets the value of the unloadDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUnloadDate(XMLGregorianCalendar value) {
        this.unloadDate = value;
    }

    /**
     * Gets the value of the penaltyData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the penaltyData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPenaltyData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PenaltyChargesData }
     * 
     * 
     */
    public List<PenaltyChargesData> getPenaltyData() {
        if (penaltyData == null) {
            penaltyData = new ArrayList<PenaltyChargesData>();
        }
        return this.penaltyData;
    }

}
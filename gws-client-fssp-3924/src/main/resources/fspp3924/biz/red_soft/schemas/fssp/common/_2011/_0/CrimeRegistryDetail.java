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
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Выписка из реестра розыска подозреваемых в преступлениях
 * 
 * <p>Java class for CrimeRegistryDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CrimeRegistryDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OSPName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_100"/>
 *         &lt;element name="CrimeName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000"/>
 *         &lt;element name="BirthDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="BirthPlace" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300" minOccurs="0"/>
 *         &lt;element name="Address" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}AddressType" minOccurs="0"/>
 *         &lt;element name="DistinguishingCharacteristics" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_4000" minOccurs="0"/>
 *         &lt;element name="InitiatorName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_100" minOccurs="0"/>
 *         &lt;element name="ContactInformation" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000" minOccurs="0"/>
 *         &lt;element name="DateDecreeInvestigation" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="InvestigationDepartment" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000" minOccurs="0"/>
 *         &lt;element name="CriminalCase" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType" minOccurs="0"/>
 *         &lt;element name="DateCriminalCase" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="ArrestLocally" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_100" minOccurs="0"/>
 *         &lt;element name="Pre-trialRestrictions" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CrimeRegistryDetail", propOrder = {
    "ospName",
    "crimeName",
    "birthDate",
    "birthPlace",
    "address",
    "distinguishingCharacteristics",
    "initiatorName",
    "contactInformation",
    "dateDecreeInvestigation",
    "investigationDepartment",
    "criminalCase",
    "dateCriminalCase",
    "arrestLocally",
    "preTrialRestrictions"
})
public class CrimeRegistryDetail {

    @XmlElement(name = "OSPName", required = true)
    protected String ospName;
    @XmlElement(name = "CrimeName", required = true)
    protected String crimeName;
    @XmlElement(name = "BirthDate", required = true)
    protected XMLGregorianCalendar birthDate;
    @XmlElement(name = "BirthPlace")
    protected String birthPlace;
    @XmlElement(name = "Address")
    protected AddressType address;
    @XmlElement(name = "DistinguishingCharacteristics")
    protected String distinguishingCharacteristics;
    @XmlElement(name = "InitiatorName")
    protected String initiatorName;
    @XmlElement(name = "ContactInformation")
    protected String contactInformation;
    @XmlElement(name = "DateDecreeInvestigation")
    protected XMLGregorianCalendar dateDecreeInvestigation;
    @XmlElement(name = "InvestigationDepartment")
    protected String investigationDepartment;
    @XmlElement(name = "CriminalCase")
    protected String criminalCase;
    @XmlElement(name = "DateCriminalCase")
    protected XMLGregorianCalendar dateCriminalCase;
    @XmlElement(name = "ArrestLocally")
    protected String arrestLocally;
    @XmlElement(name = "Pre-trialRestrictions")
    protected String preTrialRestrictions;

    /**
     * Gets the value of the ospName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOSPName() {
        return ospName;
    }

    /**
     * Sets the value of the ospName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOSPName(String value) {
        this.ospName = value;
    }

    /**
     * Gets the value of the crimeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCrimeName() {
        return crimeName;
    }

    /**
     * Sets the value of the crimeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCrimeName(String value) {
        this.crimeName = value;
    }

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBirthDate(XMLGregorianCalendar value) {
        this.birthDate = value;
    }

    /**
     * Gets the value of the birthPlace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthPlace() {
        return birthPlace;
    }

    /**
     * Sets the value of the birthPlace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthPlace(String value) {
        this.birthPlace = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setAddress(AddressType value) {
        this.address = value;
    }

    /**
     * Gets the value of the distinguishingCharacteristics property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistinguishingCharacteristics() {
        return distinguishingCharacteristics;
    }

    /**
     * Sets the value of the distinguishingCharacteristics property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistinguishingCharacteristics(String value) {
        this.distinguishingCharacteristics = value;
    }

    /**
     * Gets the value of the initiatorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitiatorName() {
        return initiatorName;
    }

    /**
     * Sets the value of the initiatorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitiatorName(String value) {
        this.initiatorName = value;
    }

    /**
     * Gets the value of the contactInformation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactInformation() {
        return contactInformation;
    }

    /**
     * Sets the value of the contactInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactInformation(String value) {
        this.contactInformation = value;
    }

    /**
     * Gets the value of the dateDecreeInvestigation property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDecreeInvestigation() {
        return dateDecreeInvestigation;
    }

    /**
     * Sets the value of the dateDecreeInvestigation property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDecreeInvestigation(XMLGregorianCalendar value) {
        this.dateDecreeInvestigation = value;
    }

    /**
     * Gets the value of the investigationDepartment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvestigationDepartment() {
        return investigationDepartment;
    }

    /**
     * Sets the value of the investigationDepartment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvestigationDepartment(String value) {
        this.investigationDepartment = value;
    }

    /**
     * Gets the value of the criminalCase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriminalCase() {
        return criminalCase;
    }

    /**
     * Sets the value of the criminalCase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriminalCase(String value) {
        this.criminalCase = value;
    }

    /**
     * Gets the value of the dateCriminalCase property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateCriminalCase() {
        return dateCriminalCase;
    }

    /**
     * Sets the value of the dateCriminalCase property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateCriminalCase(XMLGregorianCalendar value) {
        this.dateCriminalCase = value;
    }

    /**
     * Gets the value of the arrestLocally property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArrestLocally() {
        return arrestLocally;
    }

    /**
     * Sets the value of the arrestLocally property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArrestLocally(String value) {
        this.arrestLocally = value;
    }

    /**
     * Gets the value of the preTrialRestrictions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreTrialRestrictions() {
        return preTrialRestrictions;
    }

    /**
     * Sets the value of the preTrialRestrictions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreTrialRestrictions(String value) {
        this.preTrialRestrictions = value;
    }

}

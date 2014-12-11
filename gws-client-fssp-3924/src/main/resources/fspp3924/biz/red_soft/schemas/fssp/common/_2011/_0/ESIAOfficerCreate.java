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


/**
 * Запрос на создание учетной записи сотрудника организации в ЕСИА
 * 
 * <p>Java class for ESIAOfficerCreate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ESIAOfficerCreate">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ESIADocument">
 *       &lt;sequence>
 *         &lt;element name="FIO" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FioType"/>
 *         &lt;element name="INN" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}PersonInnType" minOccurs="0"/>
 *         &lt;element name="IdentificationDocument" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IdentificationDataType"/>
 *         &lt;element name="Department" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000" minOccurs="0"/>
 *         &lt;element name="Post" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_1000" minOccurs="0"/>
 *         &lt;element name="OfficialEmail" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}EMailType"/>
 *         &lt;element name="Description" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_4000" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ESIAOfficerCreate", propOrder = {
    "fio",
    "inn",
    "identificationDocument",
    "department",
    "post",
    "officialEmail",
    "description"
})
public class ESIAOfficerCreate
    extends ESIADocument
{

    @XmlElement(name = "FIO", required = true)
    protected FioType fio;
    @XmlElement(name = "INN")
    protected String inn;
    @XmlElement(name = "IdentificationDocument", required = true)
    protected IdentificationDataType identificationDocument;
    @XmlElement(name = "Department")
    protected String department;
    @XmlElement(name = "Post")
    protected String post;
    @XmlElement(name = "OfficialEmail", required = true)
    protected String officialEmail;
    @XmlElement(name = "Description")
    protected String description;

    /**
     * Gets the value of the fio property.
     * 
     * @return
     *     possible object is
     *     {@link FioType }
     *     
     */
    public FioType getFIO() {
        return fio;
    }

    /**
     * Sets the value of the fio property.
     * 
     * @param value
     *     allowed object is
     *     {@link FioType }
     *     
     */
    public void setFIO(FioType value) {
        this.fio = value;
    }

    /**
     * Gets the value of the inn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINN() {
        return inn;
    }

    /**
     * Sets the value of the inn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINN(String value) {
        this.inn = value;
    }

    /**
     * Gets the value of the identificationDocument property.
     * 
     * @return
     *     possible object is
     *     {@link IdentificationDataType }
     *     
     */
    public IdentificationDataType getIdentificationDocument() {
        return identificationDocument;
    }

    /**
     * Sets the value of the identificationDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificationDataType }
     *     
     */
    public void setIdentificationDocument(IdentificationDataType value) {
        this.identificationDocument = value;
    }

    /**
     * Gets the value of the department property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the value of the department property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartment(String value) {
        this.department = value;
    }

    /**
     * Gets the value of the post property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPost() {
        return post;
    }

    /**
     * Sets the value of the post property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPost(String value) {
        this.post = value;
    }

    /**
     * Gets the value of the officialEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficialEmail() {
        return officialEmail;
    }

    /**
     * Sets the value of the officialEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficialEmail(String value) {
        this.officialEmail = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}
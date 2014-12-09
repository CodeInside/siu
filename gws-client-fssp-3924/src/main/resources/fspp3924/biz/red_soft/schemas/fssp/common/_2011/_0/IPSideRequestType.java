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
 * Тип данных IPSideRequestType «Регистрация запроса стороны ИП в ОСП (абстрактный)»
 * 
 * <p>Java class for IPSideRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPSideRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="reqeustKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ExternalKeyType"/>
 *         &lt;element name="sideType" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideType" minOccurs="0"/>
 *         &lt;element name="side" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ContragentDatumType"/>
 *         &lt;element name="representative" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ContragentDatumType" minOccurs="0"/>
 *         &lt;element name="debtor" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ContragentDatumType"/>
 *         &lt;element name="ospCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}VkspType"/>
 *         &lt;element name="requestDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="EApplication" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}EmbeddedDocumentType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPSideRequestType", propOrder = {
    "reqeustKey",
    "sideType",
    "side",
    "representative",
    "debtor",
    "ospCode",
    "requestDate",
    "eApplication"
})
public abstract class IPSideRequestType {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String reqeustKey;
    protected String sideType;
    @XmlElement(required = true)
    protected ContragentDatumType side;
    protected ContragentDatumType representative;
    @XmlElement(required = true)
    protected ContragentDatumType debtor;
    @XmlElement(required = true)
    protected String ospCode;
    @XmlElement(required = true)
    protected XMLGregorianCalendar requestDate;
    @XmlElement(name = "EApplication")
    protected EmbeddedDocumentType eApplication;

    /**
     * Gets the value of the reqeustKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReqeustKey() {
        return reqeustKey;
    }

    /**
     * Sets the value of the reqeustKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReqeustKey(String value) {
        this.reqeustKey = value;
    }

    /**
     * Gets the value of the sideType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSideType() {
        return sideType;
    }

    /**
     * Sets the value of the sideType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSideType(String value) {
        this.sideType = value;
    }

    /**
     * Gets the value of the side property.
     * 
     * @return
     *     possible object is
     *     {@link ContragentDatumType }
     *     
     */
    public ContragentDatumType getSide() {
        return side;
    }

    /**
     * Sets the value of the side property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContragentDatumType }
     *     
     */
    public void setSide(ContragentDatumType value) {
        this.side = value;
    }

    /**
     * Gets the value of the representative property.
     * 
     * @return
     *     possible object is
     *     {@link ContragentDatumType }
     *     
     */
    public ContragentDatumType getRepresentative() {
        return representative;
    }

    /**
     * Sets the value of the representative property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContragentDatumType }
     *     
     */
    public void setRepresentative(ContragentDatumType value) {
        this.representative = value;
    }

    /**
     * Gets the value of the debtor property.
     * 
     * @return
     *     possible object is
     *     {@link ContragentDatumType }
     *     
     */
    public ContragentDatumType getDebtor() {
        return debtor;
    }

    /**
     * Sets the value of the debtor property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContragentDatumType }
     *     
     */
    public void setDebtor(ContragentDatumType value) {
        this.debtor = value;
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
     * Gets the value of the requestDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the value of the requestDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRequestDate(XMLGregorianCalendar value) {
        this.requestDate = value;
    }

    /**
     * Gets the value of the eApplication property.
     * 
     * @return
     *     possible object is
     *     {@link EmbeddedDocumentType }
     *     
     */
    public EmbeddedDocumentType getEApplication() {
        return eApplication;
    }

    /**
     * Sets the value of the eApplication property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmbeddedDocumentType }
     *     
     */
    public void setEApplication(EmbeddedDocumentType value) {
        this.eApplication = value;
    }

}

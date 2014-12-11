//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.schemas.fssp.common._2011._0;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Тип данных документа «Постановление об аресте и ограничении» используется для передачи внешнему контрагенту сведений об арестах и ограничениях, наложенных судебными приставами-исполнителями на имущество должников
 * 
 * <p>Java class for ActInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ActInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InternalKey" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType"/>
 *         &lt;element name="DocDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date"/>
 *         &lt;element name="DocNum" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocNumberType"/>
 *         &lt;element name="Barcode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Code39Type" minOccurs="0"/>
 *         &lt;element name="SPI" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InternalKeyType"/>
 *         &lt;element name="SPIFio" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}FullFioType"/>
 *         &lt;element name="SPITel" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}TelephoneNumberType" minOccurs="0"/>
 *         &lt;element name="SPIPost" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}PostType"/>
 *         &lt;element name="Amount" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Money" minOccurs="0"/>
 *         &lt;element name="StartDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="FinDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="AppOrder" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300"/>
 *         &lt;element name="DocCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DocumentType"/>
 *         &lt;element name="Reason" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300"/>
 *         &lt;element name="Article" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_10"/>
 *         &lt;element name="Point" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_10" minOccurs="0"/>
 *         &lt;element name="Part" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_10"/>
 *         &lt;element name="ResolutionBase" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="SignatureResolutionBase" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="IP" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IP" minOccurs="0"/>
 *         &lt;element name="Data" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Data" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActInfoType", propOrder = {
    "internalKey",
    "docDate",
    "docNum",
    "barcode",
    "spi",
    "spiFio",
    "spiTel",
    "spiPost",
    "amount",
    "startDate",
    "finDate",
    "appOrder",
    "docCode",
    "reason",
    "article",
    "point",
    "part",
    "resolutionBase",
    "signatureResolutionBase",
    "ip",
    "data"
})
public class ActInfoType {

    @XmlElement(name = "InternalKey")
    protected long internalKey;
    @XmlElement(name = "DocDate", required = true)
    protected XMLGregorianCalendar docDate;
    @XmlElement(name = "DocNum", required = true)
    protected String docNum;
    @XmlElement(name = "Barcode")
    protected String barcode;
    @XmlElement(name = "SPI")
    protected long spi;
    @XmlElement(name = "SPIFio", required = true)
    protected String spiFio;
    @XmlElement(name = "SPITel")
    protected String spiTel;
    @XmlElement(name = "SPIPost", required = true)
    protected String spiPost;
    @XmlElement(name = "Amount")
    protected BigDecimal amount;
    @XmlElement(name = "StartDate")
    protected XMLGregorianCalendar startDate;
    @XmlElement(name = "FinDate")
    protected XMLGregorianCalendar finDate;
    @XmlElement(name = "AppOrder", required = true)
    protected String appOrder;
    @XmlElement(name = "DocCode", required = true)
    protected String docCode;
    @XmlElement(name = "Reason", required = true)
    protected String reason;
    @XmlElement(name = "Article", required = true)
    protected String article;
    @XmlElement(name = "Point")
    protected String point;
    @XmlElement(name = "Part", required = true)
    protected String part;
    @XmlElement(name = "ResolutionBase")
    protected byte[] resolutionBase;
    @XmlElement(name = "SignatureResolutionBase")
    protected byte[] signatureResolutionBase;
    @XmlElement(name = "IP")
    protected IP ip;
    @XmlElement(name = "Data")
    protected List<Data> data;

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
     * Gets the value of the docNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocNum() {
        return docNum;
    }

    /**
     * Sets the value of the docNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocNum(String value) {
        this.docNum = value;
    }

    /**
     * Gets the value of the barcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Sets the value of the barcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBarcode(String value) {
        this.barcode = value;
    }

    /**
     * Gets the value of the spi property.
     * 
     */
    public long getSPI() {
        return spi;
    }

    /**
     * Sets the value of the spi property.
     * 
     */
    public void setSPI(long value) {
        this.spi = value;
    }

    /**
     * Gets the value of the spiFio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPIFio() {
        return spiFio;
    }

    /**
     * Sets the value of the spiFio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPIFio(String value) {
        this.spiFio = value;
    }

    /**
     * Gets the value of the spiTel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPITel() {
        return spiTel;
    }

    /**
     * Sets the value of the spiTel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPITel(String value) {
        this.spiTel = value;
    }

    /**
     * Gets the value of the spiPost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPIPost() {
        return spiPost;
    }

    /**
     * Sets the value of the spiPost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPIPost(String value) {
        this.spiPost = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the finDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFinDate() {
        return finDate;
    }

    /**
     * Sets the value of the finDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFinDate(XMLGregorianCalendar value) {
        this.finDate = value;
    }

    /**
     * Gets the value of the appOrder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppOrder() {
        return appOrder;
    }

    /**
     * Sets the value of the appOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppOrder(String value) {
        this.appOrder = value;
    }

    /**
     * Gets the value of the docCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocCode() {
        return docCode;
    }

    /**
     * Sets the value of the docCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocCode(String value) {
        this.docCode = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
    }

    /**
     * Gets the value of the article property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArticle() {
        return article;
    }

    /**
     * Sets the value of the article property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArticle(String value) {
        this.article = value;
    }

    /**
     * Gets the value of the point property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoint() {
        return point;
    }

    /**
     * Sets the value of the point property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoint(String value) {
        this.point = value;
    }

    /**
     * Gets the value of the part property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPart() {
        return part;
    }

    /**
     * Sets the value of the part property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPart(String value) {
        this.part = value;
    }

    /**
     * Gets the value of the resolutionBase property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getResolutionBase() {
        return resolutionBase;
    }

    /**
     * Sets the value of the resolutionBase property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setResolutionBase(byte[] value) {
        this.resolutionBase = value;
    }

    /**
     * Gets the value of the signatureResolutionBase property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSignatureResolutionBase() {
        return signatureResolutionBase;
    }

    /**
     * Sets the value of the signatureResolutionBase property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSignatureResolutionBase(byte[] value) {
        this.signatureResolutionBase = value;
    }

    /**
     * Gets the value of the ip property.
     * 
     * @return
     *     possible object is
     *     {@link IP }
     *     
     */
    public IP getIP() {
        return ip;
    }

    /**
     * Sets the value of the ip property.
     * 
     * @param value
     *     allowed object is
     *     {@link IP }
     *     
     */
    public void setIP(IP value) {
        this.ip = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the data property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Data }
     * 
     * 
     */
    public List<Data> getData() {
        if (data == null) {
            data = new ArrayList<Data>();
        }
        return this.data;
    }

}
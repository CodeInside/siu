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
 * Запрос интерактивный (синхронный)
 * 
 * <p>Java class for InquiryMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InquiryMessage">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}SyncRq">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="debtorIPListRequest" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DebtorIPListRequestType"/>
 *           &lt;element name="IPSideRequestExistence" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideRequestExistenceType"/>
 *           &lt;element name="IPSideRequestCourseID" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideRequestCourseIDType"/>
 *           &lt;element name="IPSideRequestCourseIP" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideRequestCourseIPType"/>
 *           &lt;element name="IPGeneralApplication" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPGeneralApplication"/>
 *           &lt;element name="ComplaintApplication" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ComplaintApplication"/>
 *           &lt;element name="IPSideApp" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideAppType"/>
 *           &lt;element name="IPSideAppSPIAct" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideAppSPIActType"/>
 *           &lt;element name="IPSideAppProlong" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideAppProlongType"/>
 *           &lt;element name="IPSideAppDelayByCrdr" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideAppDelayByCrdrType"/>
 *           &lt;element name="IPSideAppSuspendIP" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideAppSuspendIPType"/>
 *           &lt;element name="IPSideAppCancelBreak" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideAppCancelBreakType"/>
 *           &lt;element name="IPSideAppAccountingChk" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPSideAppAccountingChkType"/>
 *           &lt;element name="IPReqAppResultRequest" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPReqAppResultRequestType"/>
 *           &lt;element name="IPBriefInfoRequest" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}IPBriefInfoRequestType"/>
 *         &lt;/choice>
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
@XmlType(name = "InquiryMessage", propOrder = {
    "debtorIPListRequest",
    "ipSideRequestExistence",
    "ipSideRequestCourseID",
    "ipSideRequestCourseIP",
    "ipGeneralApplication",
    "complaintApplication",
    "ipSideApp",
    "ipSideAppSPIAct",
    "ipSideAppProlong",
    "ipSideAppDelayByCrdr",
    "ipSideAppSuspendIP",
    "ipSideAppCancelBreak",
    "ipSideAppAccountingChk",
    "ipReqAppResultRequest",
    "ipBriefInfoRequest",
    "signature"
})
public class InquiryMessage
    extends SyncRq
{

    protected DebtorIPListRequestType debtorIPListRequest;
    @XmlElement(name = "IPSideRequestExistence")
    protected IPSideRequestExistenceType ipSideRequestExistence;
    @XmlElement(name = "IPSideRequestCourseID")
    protected IPSideRequestCourseIDType ipSideRequestCourseID;
    @XmlElement(name = "IPSideRequestCourseIP")
    protected IPSideRequestCourseIPType ipSideRequestCourseIP;
    @XmlElement(name = "IPGeneralApplication")
    protected IPGeneralApplication ipGeneralApplication;
    @XmlElement(name = "ComplaintApplication")
    protected ComplaintApplication complaintApplication;
    @XmlElement(name = "IPSideApp")
    protected IPSideAppType ipSideApp;
    @XmlElement(name = "IPSideAppSPIAct")
    protected IPSideAppSPIActType ipSideAppSPIAct;
    @XmlElement(name = "IPSideAppProlong")
    protected IPSideAppProlongType ipSideAppProlong;
    @XmlElement(name = "IPSideAppDelayByCrdr")
    protected IPSideAppDelayByCrdrType ipSideAppDelayByCrdr;
    @XmlElement(name = "IPSideAppSuspendIP")
    protected IPSideAppSuspendIPType ipSideAppSuspendIP;
    @XmlElement(name = "IPSideAppCancelBreak")
    protected IPSideAppCancelBreakType ipSideAppCancelBreak;
    @XmlElement(name = "IPSideAppAccountingChk")
    protected IPSideAppAccountingChkType ipSideAppAccountingChk;
    @XmlElement(name = "IPReqAppResultRequest")
    protected IPReqAppResultRequestType ipReqAppResultRequest;
    @XmlElement(name = "IPBriefInfoRequest")
    protected IPBriefInfoRequestType ipBriefInfoRequest;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected List<SignatureType> signature;

    /**
     * Gets the value of the debtorIPListRequest property.
     * 
     * @return
     *     possible object is
     *     {@link DebtorIPListRequestType }
     *     
     */
    public DebtorIPListRequestType getDebtorIPListRequest() {
        return debtorIPListRequest;
    }

    /**
     * Sets the value of the debtorIPListRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link DebtorIPListRequestType }
     *     
     */
    public void setDebtorIPListRequest(DebtorIPListRequestType value) {
        this.debtorIPListRequest = value;
    }

    /**
     * Gets the value of the ipSideRequestExistence property.
     * 
     * @return
     *     possible object is
     *     {@link IPSideRequestExistenceType }
     *     
     */
    public IPSideRequestExistenceType getIPSideRequestExistence() {
        return ipSideRequestExistence;
    }

    /**
     * Sets the value of the ipSideRequestExistence property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSideRequestExistenceType }
     *     
     */
    public void setIPSideRequestExistence(IPSideRequestExistenceType value) {
        this.ipSideRequestExistence = value;
    }

    /**
     * Gets the value of the ipSideRequestCourseID property.
     * 
     * @return
     *     possible object is
     *     {@link IPSideRequestCourseIDType }
     *     
     */
    public IPSideRequestCourseIDType getIPSideRequestCourseID() {
        return ipSideRequestCourseID;
    }

    /**
     * Sets the value of the ipSideRequestCourseID property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSideRequestCourseIDType }
     *     
     */
    public void setIPSideRequestCourseID(IPSideRequestCourseIDType value) {
        this.ipSideRequestCourseID = value;
    }

    /**
     * Gets the value of the ipSideRequestCourseIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPSideRequestCourseIPType }
     *     
     */
    public IPSideRequestCourseIPType getIPSideRequestCourseIP() {
        return ipSideRequestCourseIP;
    }

    /**
     * Sets the value of the ipSideRequestCourseIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSideRequestCourseIPType }
     *     
     */
    public void setIPSideRequestCourseIP(IPSideRequestCourseIPType value) {
        this.ipSideRequestCourseIP = value;
    }

    /**
     * Gets the value of the ipGeneralApplication property.
     * 
     * @return
     *     possible object is
     *     {@link IPGeneralApplication }
     *     
     */
    public IPGeneralApplication getIPGeneralApplication() {
        return ipGeneralApplication;
    }

    /**
     * Sets the value of the ipGeneralApplication property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPGeneralApplication }
     *     
     */
    public void setIPGeneralApplication(IPGeneralApplication value) {
        this.ipGeneralApplication = value;
    }

    /**
     * Gets the value of the complaintApplication property.
     * 
     * @return
     *     possible object is
     *     {@link ComplaintApplication }
     *     
     */
    public ComplaintApplication getComplaintApplication() {
        return complaintApplication;
    }

    /**
     * Sets the value of the complaintApplication property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComplaintApplication }
     *     
     */
    public void setComplaintApplication(ComplaintApplication value) {
        this.complaintApplication = value;
    }

    /**
     * Gets the value of the ipSideApp property.
     * 
     * @return
     *     possible object is
     *     {@link IPSideAppType }
     *     
     */
    public IPSideAppType getIPSideApp() {
        return ipSideApp;
    }

    /**
     * Sets the value of the ipSideApp property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSideAppType }
     *     
     */
    public void setIPSideApp(IPSideAppType value) {
        this.ipSideApp = value;
    }

    /**
     * Gets the value of the ipSideAppSPIAct property.
     * 
     * @return
     *     possible object is
     *     {@link IPSideAppSPIActType }
     *     
     */
    public IPSideAppSPIActType getIPSideAppSPIAct() {
        return ipSideAppSPIAct;
    }

    /**
     * Sets the value of the ipSideAppSPIAct property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSideAppSPIActType }
     *     
     */
    public void setIPSideAppSPIAct(IPSideAppSPIActType value) {
        this.ipSideAppSPIAct = value;
    }

    /**
     * Gets the value of the ipSideAppProlong property.
     * 
     * @return
     *     possible object is
     *     {@link IPSideAppProlongType }
     *     
     */
    public IPSideAppProlongType getIPSideAppProlong() {
        return ipSideAppProlong;
    }

    /**
     * Sets the value of the ipSideAppProlong property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSideAppProlongType }
     *     
     */
    public void setIPSideAppProlong(IPSideAppProlongType value) {
        this.ipSideAppProlong = value;
    }

    /**
     * Gets the value of the ipSideAppDelayByCrdr property.
     * 
     * @return
     *     possible object is
     *     {@link IPSideAppDelayByCrdrType }
     *     
     */
    public IPSideAppDelayByCrdrType getIPSideAppDelayByCrdr() {
        return ipSideAppDelayByCrdr;
    }

    /**
     * Sets the value of the ipSideAppDelayByCrdr property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSideAppDelayByCrdrType }
     *     
     */
    public void setIPSideAppDelayByCrdr(IPSideAppDelayByCrdrType value) {
        this.ipSideAppDelayByCrdr = value;
    }

    /**
     * Gets the value of the ipSideAppSuspendIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPSideAppSuspendIPType }
     *     
     */
    public IPSideAppSuspendIPType getIPSideAppSuspendIP() {
        return ipSideAppSuspendIP;
    }

    /**
     * Sets the value of the ipSideAppSuspendIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSideAppSuspendIPType }
     *     
     */
    public void setIPSideAppSuspendIP(IPSideAppSuspendIPType value) {
        this.ipSideAppSuspendIP = value;
    }

    /**
     * Gets the value of the ipSideAppCancelBreak property.
     * 
     * @return
     *     possible object is
     *     {@link IPSideAppCancelBreakType }
     *     
     */
    public IPSideAppCancelBreakType getIPSideAppCancelBreak() {
        return ipSideAppCancelBreak;
    }

    /**
     * Sets the value of the ipSideAppCancelBreak property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSideAppCancelBreakType }
     *     
     */
    public void setIPSideAppCancelBreak(IPSideAppCancelBreakType value) {
        this.ipSideAppCancelBreak = value;
    }

    /**
     * Gets the value of the ipSideAppAccountingChk property.
     * 
     * @return
     *     possible object is
     *     {@link IPSideAppAccountingChkType }
     *     
     */
    public IPSideAppAccountingChkType getIPSideAppAccountingChk() {
        return ipSideAppAccountingChk;
    }

    /**
     * Sets the value of the ipSideAppAccountingChk property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSideAppAccountingChkType }
     *     
     */
    public void setIPSideAppAccountingChk(IPSideAppAccountingChkType value) {
        this.ipSideAppAccountingChk = value;
    }

    /**
     * Gets the value of the ipReqAppResultRequest property.
     * 
     * @return
     *     possible object is
     *     {@link IPReqAppResultRequestType }
     *     
     */
    public IPReqAppResultRequestType getIPReqAppResultRequest() {
        return ipReqAppResultRequest;
    }

    /**
     * Sets the value of the ipReqAppResultRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPReqAppResultRequestType }
     *     
     */
    public void setIPReqAppResultRequest(IPReqAppResultRequestType value) {
        this.ipReqAppResultRequest = value;
    }

    /**
     * Gets the value of the ipBriefInfoRequest property.
     * 
     * @return
     *     possible object is
     *     {@link IPBriefInfoRequestType }
     *     
     */
    public IPBriefInfoRequestType getIPBriefInfoRequest() {
        return ipBriefInfoRequest;
    }

    /**
     * Sets the value of the ipBriefInfoRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPBriefInfoRequestType }
     *     
     */
    public void setIPBriefInfoRequest(IPBriefInfoRequestType value) {
        this.ipBriefInfoRequest = value;
    }

    /**
     * Технологическая ЭЦП (накладывается сервером, формирующим сообщение) Gets the value of the signature property.
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

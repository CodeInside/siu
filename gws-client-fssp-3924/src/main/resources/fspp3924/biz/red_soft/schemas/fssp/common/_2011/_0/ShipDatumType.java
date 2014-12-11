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
 * Судно
 * 
 * <p>Java class for ShipDatumType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShipDatumType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InformationType">
 *       &lt;sequence>
 *         &lt;element name="CategoryCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ShipCategoryType"/>
 *         &lt;element name="Name" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300" minOccurs="0"/>
 *         &lt;element name="RegNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_20"/>
 *         &lt;element name="HIN" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}HinType" minOccurs="0"/>
 *         &lt;element name="BrandName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_50" minOccurs="0"/>
 *         &lt;element name="ModelName" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_50"/>
 *         &lt;element name="Color" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_20" minOccurs="0"/>
 *         &lt;element name="MadeYear" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Year" minOccurs="0"/>
 *         &lt;element name="Shipyard" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_50" minOccurs="0"/>
 *         &lt;element name="HullMaterial" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_20" minOccurs="0"/>
 *         &lt;element name="MaxOccupancy" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}PositiveShort" minOccurs="0"/>
 *         &lt;element name="Moorings" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300" minOccurs="0"/>
 *         &lt;element name="EngineCount" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}PositiveShort" minOccurs="0"/>
 *         &lt;element name="EnginePowerKWh" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Quantity" minOccurs="0"/>
 *         &lt;element name="EnginePowerHp" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Quantity" minOccurs="0"/>
 *         &lt;element name="Engines" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ShipEngineType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ClassFormula" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_20" minOccurs="0"/>
 *         &lt;element name="Length" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Quantity" minOccurs="0"/>
 *         &lt;element name="Width" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Quantity" minOccurs="0"/>
 *         &lt;element name="SideHeight" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Quantity" minOccurs="0"/>
 *         &lt;element name="SailSquare" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Quantity" minOccurs="0"/>
 *         &lt;element name="OwnershipDocument" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_300" minOccurs="0"/>
 *         &lt;element name="OwnershipDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="NavigationPermitNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_20" minOccurs="0"/>
 *         &lt;element name="NavigationPermitDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="MortgageCertNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_20" minOccurs="0"/>
 *         &lt;element name="MortgageCertDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="Hijacking" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}HijackingType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ShareValue" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}ShareType" minOccurs="0"/>
 *         &lt;element name="ShareText" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_255" minOccurs="0"/>
 *         &lt;element name="DeptCode" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_10" minOccurs="0"/>
 *         &lt;element name="RegisterDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *         &lt;element name="UnregisterDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}Date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShipDatumType", propOrder = {
    "categoryCode",
    "name",
    "regNumber",
    "hin",
    "brandName",
    "modelName",
    "color",
    "madeYear",
    "shipyard",
    "hullMaterial",
    "maxOccupancy",
    "moorings",
    "engineCount",
    "enginePowerKWh",
    "enginePowerHp",
    "engines",
    "classFormula",
    "length",
    "width",
    "sideHeight",
    "sailSquare",
    "ownershipDocument",
    "ownershipDate",
    "navigationPermitNumber",
    "navigationPermitDate",
    "mortgageCertNumber",
    "mortgageCertDate",
    "hijacking",
    "shareValue",
    "shareText",
    "deptCode",
    "registerDate",
    "unregisterDate"
})
public class ShipDatumType
    extends InformationType
{

    @XmlElement(name = "CategoryCode", required = true)
    protected String categoryCode;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "RegNumber", required = true)
    protected String regNumber;
    @XmlElement(name = "HIN")
    protected String hin;
    @XmlElement(name = "BrandName")
    protected String brandName;
    @XmlElement(name = "ModelName", required = true)
    protected String modelName;
    @XmlElement(name = "Color")
    protected String color;
    @XmlElement(name = "MadeYear")
    protected XMLGregorianCalendar madeYear;
    @XmlElement(name = "Shipyard")
    protected String shipyard;
    @XmlElement(name = "HullMaterial")
    protected String hullMaterial;
    @XmlElement(name = "MaxOccupancy")
    protected Integer maxOccupancy;
    @XmlElement(name = "Moorings")
    protected String moorings;
    @XmlElement(name = "EngineCount")
    protected Integer engineCount;
    @XmlElement(name = "EnginePowerKWh")
    protected BigDecimal enginePowerKWh;
    @XmlElement(name = "EnginePowerHp")
    protected BigDecimal enginePowerHp;
    @XmlElement(name = "Engines")
    protected List<ShipEngineType> engines;
    @XmlElement(name = "ClassFormula")
    protected String classFormula;
    @XmlElement(name = "Length")
    protected BigDecimal length;
    @XmlElement(name = "Width")
    protected BigDecimal width;
    @XmlElement(name = "SideHeight")
    protected BigDecimal sideHeight;
    @XmlElement(name = "SailSquare")
    protected BigDecimal sailSquare;
    @XmlElement(name = "OwnershipDocument")
    protected String ownershipDocument;
    @XmlElement(name = "OwnershipDate")
    protected XMLGregorianCalendar ownershipDate;
    @XmlElement(name = "NavigationPermitNumber")
    protected String navigationPermitNumber;
    @XmlElement(name = "NavigationPermitDate")
    protected XMLGregorianCalendar navigationPermitDate;
    @XmlElement(name = "MortgageCertNumber")
    protected String mortgageCertNumber;
    @XmlElement(name = "MortgageCertDate")
    protected XMLGregorianCalendar mortgageCertDate;
    @XmlElement(name = "Hijacking")
    protected List<HijackingType> hijacking;
    @XmlElement(name = "ShareValue")
    protected ShareType shareValue;
    @XmlElement(name = "ShareText")
    protected String shareText;
    @XmlElement(name = "DeptCode")
    protected String deptCode;
    @XmlElement(name = "RegisterDate")
    protected XMLGregorianCalendar registerDate;
    @XmlElement(name = "UnregisterDate")
    protected XMLGregorianCalendar unregisterDate;

    /**
     * Gets the value of the categoryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategoryCode() {
        return categoryCode;
    }

    /**
     * Sets the value of the categoryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoryCode(String value) {
        this.categoryCode = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the regNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegNumber() {
        return regNumber;
    }

    /**
     * Sets the value of the regNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegNumber(String value) {
        this.regNumber = value;
    }

    /**
     * Gets the value of the hin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHIN() {
        return hin;
    }

    /**
     * Sets the value of the hin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHIN(String value) {
        this.hin = value;
    }

    /**
     * Gets the value of the brandName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * Sets the value of the brandName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBrandName(String value) {
        this.brandName = value;
    }

    /**
     * Gets the value of the modelName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Sets the value of the modelName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelName(String value) {
        this.modelName = value;
    }

    /**
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColor(String value) {
        this.color = value;
    }

    /**
     * Gets the value of the madeYear property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMadeYear() {
        return madeYear;
    }

    /**
     * Sets the value of the madeYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMadeYear(XMLGregorianCalendar value) {
        this.madeYear = value;
    }

    /**
     * Gets the value of the shipyard property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipyard() {
        return shipyard;
    }

    /**
     * Sets the value of the shipyard property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipyard(String value) {
        this.shipyard = value;
    }

    /**
     * Gets the value of the hullMaterial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHullMaterial() {
        return hullMaterial;
    }

    /**
     * Sets the value of the hullMaterial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHullMaterial(String value) {
        this.hullMaterial = value;
    }

    /**
     * Gets the value of the maxOccupancy property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxOccupancy() {
        return maxOccupancy;
    }

    /**
     * Sets the value of the maxOccupancy property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxOccupancy(Integer value) {
        this.maxOccupancy = value;
    }

    /**
     * Gets the value of the moorings property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoorings() {
        return moorings;
    }

    /**
     * Sets the value of the moorings property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoorings(String value) {
        this.moorings = value;
    }

    /**
     * Gets the value of the engineCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEngineCount() {
        return engineCount;
    }

    /**
     * Sets the value of the engineCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEngineCount(Integer value) {
        this.engineCount = value;
    }

    /**
     * Gets the value of the enginePowerKWh property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEnginePowerKWh() {
        return enginePowerKWh;
    }

    /**
     * Sets the value of the enginePowerKWh property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEnginePowerKWh(BigDecimal value) {
        this.enginePowerKWh = value;
    }

    /**
     * Gets the value of the enginePowerHp property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEnginePowerHp() {
        return enginePowerHp;
    }

    /**
     * Sets the value of the enginePowerHp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEnginePowerHp(BigDecimal value) {
        this.enginePowerHp = value;
    }

    /**
     * Gets the value of the engines property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the engines property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEngines().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ShipEngineType }
     * 
     * 
     */
    public List<ShipEngineType> getEngines() {
        if (engines == null) {
            engines = new ArrayList<ShipEngineType>();
        }
        return this.engines;
    }

    /**
     * Gets the value of the classFormula property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassFormula() {
        return classFormula;
    }

    /**
     * Sets the value of the classFormula property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassFormula(String value) {
        this.classFormula = value;
    }

    /**
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLength(BigDecimal value) {
        this.length = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWidth(BigDecimal value) {
        this.width = value;
    }

    /**
     * Gets the value of the sideHeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSideHeight() {
        return sideHeight;
    }

    /**
     * Sets the value of the sideHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSideHeight(BigDecimal value) {
        this.sideHeight = value;
    }

    /**
     * Gets the value of the sailSquare property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSailSquare() {
        return sailSquare;
    }

    /**
     * Sets the value of the sailSquare property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSailSquare(BigDecimal value) {
        this.sailSquare = value;
    }

    /**
     * Gets the value of the ownershipDocument property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnershipDocument() {
        return ownershipDocument;
    }

    /**
     * Sets the value of the ownershipDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnershipDocument(String value) {
        this.ownershipDocument = value;
    }

    /**
     * Gets the value of the ownershipDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOwnershipDate() {
        return ownershipDate;
    }

    /**
     * Sets the value of the ownershipDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOwnershipDate(XMLGregorianCalendar value) {
        this.ownershipDate = value;
    }

    /**
     * Gets the value of the navigationPermitNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNavigationPermitNumber() {
        return navigationPermitNumber;
    }

    /**
     * Sets the value of the navigationPermitNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNavigationPermitNumber(String value) {
        this.navigationPermitNumber = value;
    }

    /**
     * Gets the value of the navigationPermitDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getNavigationPermitDate() {
        return navigationPermitDate;
    }

    /**
     * Sets the value of the navigationPermitDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setNavigationPermitDate(XMLGregorianCalendar value) {
        this.navigationPermitDate = value;
    }

    /**
     * Gets the value of the mortgageCertNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMortgageCertNumber() {
        return mortgageCertNumber;
    }

    /**
     * Sets the value of the mortgageCertNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMortgageCertNumber(String value) {
        this.mortgageCertNumber = value;
    }

    /**
     * Gets the value of the mortgageCertDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMortgageCertDate() {
        return mortgageCertDate;
    }

    /**
     * Sets the value of the mortgageCertDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMortgageCertDate(XMLGregorianCalendar value) {
        this.mortgageCertDate = value;
    }

    /**
     * Gets the value of the hijacking property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hijacking property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHijacking().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HijackingType }
     * 
     * 
     */
    public List<HijackingType> getHijacking() {
        if (hijacking == null) {
            hijacking = new ArrayList<HijackingType>();
        }
        return this.hijacking;
    }

    /**
     * Gets the value of the shareValue property.
     * 
     * @return
     *     possible object is
     *     {@link ShareType }
     *     
     */
    public ShareType getShareValue() {
        return shareValue;
    }

    /**
     * Sets the value of the shareValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShareType }
     *     
     */
    public void setShareValue(ShareType value) {
        this.shareValue = value;
    }

    /**
     * Gets the value of the shareText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShareText() {
        return shareText;
    }

    /**
     * Sets the value of the shareText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShareText(String value) {
        this.shareText = value;
    }

    /**
     * Gets the value of the deptCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeptCode() {
        return deptCode;
    }

    /**
     * Sets the value of the deptCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeptCode(String value) {
        this.deptCode = value;
    }

    /**
     * Gets the value of the registerDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegisterDate() {
        return registerDate;
    }

    /**
     * Sets the value of the registerDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegisterDate(XMLGregorianCalendar value) {
        this.registerDate = value;
    }

    /**
     * Gets the value of the unregisterDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUnregisterDate() {
        return unregisterDate;
    }

    /**
     * Sets the value of the unregisterDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUnregisterDate(XMLGregorianCalendar value) {
        this.unregisterDate = value;
    }

}
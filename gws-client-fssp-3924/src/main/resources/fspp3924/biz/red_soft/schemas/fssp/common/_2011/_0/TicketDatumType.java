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
 * Тип сведений TicketDatumType "Сведения о передвижении"
 * 
 * <p>Java class for TicketDatumType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TicketDatumType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}InformationType">
 *       &lt;sequence>
 *         &lt;element name="TicketDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DateTime"/>
 *         &lt;element name="DepartureDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DateTime"/>
 *         &lt;element name="ArrivalDate" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}DateTime"/>
 *         &lt;element name="DepartureStation" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_100"/>
 *         &lt;element name="DestinationStation" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}String_1_100"/>
 *         &lt;element name="TelephoneNumber" type="{http://www.red-soft.biz/schemas/fssp/common/2011/0.5}TelephoneNumberType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TicketDatumType", propOrder = {
    "ticketDate",
    "departureDate",
    "arrivalDate",
    "departureStation",
    "destinationStation",
    "telephoneNumber"
})
public class TicketDatumType
    extends InformationType
{

    @XmlElement(name = "TicketDate", required = true)
    protected XMLGregorianCalendar ticketDate;
    @XmlElement(name = "DepartureDate", required = true)
    protected XMLGregorianCalendar departureDate;
    @XmlElement(name = "ArrivalDate", required = true)
    protected XMLGregorianCalendar arrivalDate;
    @XmlElement(name = "DepartureStation", required = true)
    protected String departureStation;
    @XmlElement(name = "DestinationStation", required = true)
    protected String destinationStation;
    @XmlElement(name = "TelephoneNumber", required = true)
    protected String telephoneNumber;

    /**
     * Gets the value of the ticketDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTicketDate() {
        return ticketDate;
    }

    /**
     * Sets the value of the ticketDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTicketDate(XMLGregorianCalendar value) {
        this.ticketDate = value;
    }

    /**
     * Gets the value of the departureDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDepartureDate() {
        return departureDate;
    }

    /**
     * Sets the value of the departureDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDepartureDate(XMLGregorianCalendar value) {
        this.departureDate = value;
    }

    /**
     * Gets the value of the arrivalDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getArrivalDate() {
        return arrivalDate;
    }

    /**
     * Sets the value of the arrivalDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setArrivalDate(XMLGregorianCalendar value) {
        this.arrivalDate = value;
    }

    /**
     * Gets the value of the departureStation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartureStation() {
        return departureStation;
    }

    /**
     * Sets the value of the departureStation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartureStation(String value) {
        this.departureStation = value;
    }

    /**
     * Gets the value of the destinationStation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationStation() {
        return destinationStation;
    }

    /**
     * Sets the value of the destinationStation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationStation(String value) {
        this.destinationStation = value;
    }

    /**
     * Gets the value of the telephoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Sets the value of the telephoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber(String value) {
        this.telephoneNumber = value;
    }

}
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.10 at 10:59:29 AM MSK 
//


package unisoft.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import unisoft.ws.egrnxx.response.Документ;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ws.unisoft/EGRNXX/Response}Документ"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "\u0434\u043e\u043a\u0443\u043c\u0435\u043d\u0442"
})
@XmlRootElement(name = "SendShortFLRequestFault")
public class SendShortFLRequestFault {

    @XmlElement(name = "\u0414\u043e\u043a\u0443\u043c\u0435\u043d\u0442", namespace = "http://ws.unisoft/EGRNXX/Response", required = true)
    protected Документ документ;

    /**
     * Gets the value of the документ property.
     * 
     * @return
     *     possible object is
     *     {@link Документ }
     *     
     */
    public Документ getДокумент() {
        return документ;
    }

    /**
     * Sets the value of the документ property.
     * 
     * @param value
     *     allowed object is
     *     {@link Документ }
     *     
     */
    public void setДокумент(Документ value) {
        this.документ = value;
    }

}

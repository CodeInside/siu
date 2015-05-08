
package ru.codeinside.esia;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getConnectESIA complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getConnectESIA">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Message" type="{http://smev.gosuslugi.ru/rev120315}MessageType" minOccurs="0"/>
 *         &lt;element name="MessageData" type="{http://smev.gosuslugi.ru/rev120315}MessageConnectESIAData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getConnectESIA", propOrder = {
    "message",
    "messageData"
})
public class GetConnectESIA {

    @XmlElement(name = "Message")
    protected MessageType message;
    @XmlElement(name = "MessageData")
    protected MessageConnectESIAData messageData;

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link MessageType }
     *     
     */
    public MessageType getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageType }
     *     
     */
    public void setMessage(MessageType value) {
        this.message = value;
    }

    /**
     * Gets the value of the messageData property.
     * 
     * @return
     *     possible object is
     *     {@link MessageConnectESIAData }
     *     
     */
    public MessageConnectESIAData getMessageData() {
        return messageData;
    }

    /**
     * Sets the value of the messageData property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageConnectESIAData }
     *     
     */
    public void setMessageData(MessageConnectESIAData value) {
        this.messageData = value;
    }

}

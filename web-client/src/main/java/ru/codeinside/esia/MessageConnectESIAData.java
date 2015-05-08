
package ru.codeinside.esia;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageConnectESIAData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageConnectESIAData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://smev.gosuslugi.ru/rev120315}messageDataType">
 *       &lt;sequence>
 *         &lt;element name="AppData" type="{http://smev.gosuslugi.ru/rev120315}appData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageConnectESIAData", propOrder = {
    "appData"
})
public class MessageConnectESIAData
    extends MessageDataType
{

    @XmlElement(name = "AppData")
    protected AppData appData;

    /**
     * Gets the value of the appData property.
     * 
     * @return
     *     possible object is
     *     {@link AppData }
     *     
     */
    public AppData getAppData() {
        return appData;
    }

    /**
     * Sets the value of the appData property.
     * 
     * @param value
     *     allowed object is
     *     {@link AppData }
     *     
     */
    public void setAppData(AppData value) {
        this.appData = value;
    }

}

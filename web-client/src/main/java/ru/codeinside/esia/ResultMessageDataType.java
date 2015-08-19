
package ru.codeinside.esia;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResultMessageDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResultMessageDataType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://smev.gosuslugi.ru/rev120315}messageDataType">
 *       &lt;sequence>
 *         &lt;element name="AppData" type="{http://smev.gosuslugi.ru/rev120315}ResultAppData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultMessageDataType", propOrder = {
    "appData"
})
public class ResultMessageDataType
    extends MessageDataType
{

    @XmlElement(name = "AppData")
    protected ResultAppData appData;

    /**
     * Gets the value of the appData property.
     * 
     * @return
     *     possible object is
     *     {@link ResultAppData }
     *     
     */
    public ResultAppData getAppData() {
        return appData;
    }

    /**
     * Sets the value of the appData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultAppData }
     *     
     */
    public void setAppData(ResultAppData value) {
        this.appData = value;
    }

}

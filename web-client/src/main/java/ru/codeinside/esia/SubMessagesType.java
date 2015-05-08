
package ru.codeinside.esia;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for SubMessagesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubMessagesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SubMessage" type="{http://smev.gosuslugi.ru/rev120315}SubMessageType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubMessagesType", propOrder = {
    "subMessage"
})
public class SubMessagesType {

    @XmlElement(name = "SubMessage", required = true)
    protected List<SubMessageType> subMessage;

    /**
     * Gets the value of the subMessage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subMessage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubMessage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubMessageType }
     * 
     * 
     */
    public List<SubMessageType> getSubMessage() {
        if (subMessage == null) {
            subMessage = new ArrayList<SubMessageType>();
        }
        return this.subMessage;
    }

}

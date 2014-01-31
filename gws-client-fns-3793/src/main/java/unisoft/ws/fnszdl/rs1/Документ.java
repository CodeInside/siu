
package unisoft.ws.fnszdl.rs1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="ИдЗапросФ">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="36"/>
 *               &lt;pattern value="[0-9]+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="КодОбр">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="2"/>
 *               &lt;enumeration value="55"/>
 *               &lt;enumeration value="56"/>
 *               &lt;enumeration value="81"/>
 *               &lt;enumeration value="82"/>
 *               &lt;enumeration value="84"/>
 *               &lt;enumeration value="85"/>
 *               &lt;enumeration value="86"/>
 *               &lt;enumeration value="99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/choice>
 *       &lt;attribute name="ВерсФорм" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="5"/>
 *             &lt;enumeration value="4.02"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "\u0438\u0434\u0417\u0430\u043f\u0440\u043e\u0441\u0424",
    "\u043a\u043e\u0434\u041e\u0431\u0440"
})
@XmlRootElement(name = "\u0414\u043e\u043a\u0443\u043c\u0435\u043d\u0442")
public class Документ {

    @XmlElement(name = "\u0418\u0434\u0417\u0430\u043f\u0440\u043e\u0441\u0424")
    protected String идЗапросФ;
    @XmlElement(name = "\u041a\u043e\u0434\u041e\u0431\u0440")
    protected String кодОбр;
    @XmlAttribute(name = "\u0412\u0435\u0440\u0441\u0424\u043e\u0440\u043c", required = true)
    protected String версФорм;

    /**
     * Gets the value of the идЗапросФ property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getИдЗапросФ() {
        return идЗапросФ;
    }

    /**
     * Sets the value of the идЗапросФ property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setИдЗапросФ(String value) {
        this.идЗапросФ = value;
    }

    /**
     * Gets the value of the кодОбр property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getКодОбр() {
        return кодОбр;
    }

    /**
     * Sets the value of the кодОбр property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setКодОбр(String value) {
        this.кодОбр = value;
    }

    /**
     * Gets the value of the версФорм property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getВерсФорм() {
        return версФорм;
    }

    /**
     * Sets the value of the версФорм property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setВерсФорм(String value) {
        this.версФорм = value;
    }

}

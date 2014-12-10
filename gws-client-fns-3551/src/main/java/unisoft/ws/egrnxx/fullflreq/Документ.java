//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.10 at 10:59:29 AM MSK 
//


package unisoft.ws.egrnxx.fullflreq;

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
 *       &lt;sequence>
 *         &lt;element name="ЗапросИП">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="ОГРНИП" type="{http://ws.unisoft/EGRNXX/FullFLReq}ОГРНИПТип"/>
 *                   &lt;element name="ИНН" type="{http://ws.unisoft/EGRNXX/FullFLReq}ИННФЛТип"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="ВерсФорм" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="5"/>
 *             &lt;enumeration value="4.02"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="ИдДок" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;length value="36"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="НомерДела">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="2"/>
 *             &lt;maxLength value="50"/>
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
    "\u0437\u0430\u043f\u0440\u043e\u0441\u0418\u041f"
})
@XmlRootElement(name = "\u0414\u043e\u043a\u0443\u043c\u0435\u043d\u0442")
public class Документ {

    @XmlElement(name = "\u0417\u0430\u043f\u0440\u043e\u0441\u0418\u041f", required = true)
    protected Документ.ЗапросИП запросИП;
    @XmlAttribute(name = "\u0412\u0435\u0440\u0441\u0424\u043e\u0440\u043c", required = true)
    protected String версФорм;
    @XmlAttribute(name = "\u0418\u0434\u0414\u043e\u043a", required = true)
    protected String идДок;
    @XmlAttribute(name = "\u041d\u043e\u043c\u0435\u0440\u0414\u0435\u043b\u0430")
    protected String номерДела;

    /**
     * Gets the value of the запросИП property.
     * 
     * @return
     *     possible object is
     *     {@link Документ.ЗапросИП }
     *     
     */
    public Документ.ЗапросИП getЗапросИП() {
        return запросИП;
    }

    /**
     * Sets the value of the запросИП property.
     * 
     * @param value
     *     allowed object is
     *     {@link Документ.ЗапросИП }
     *     
     */
    public void setЗапросИП(Документ.ЗапросИП value) {
        this.запросИП = value;
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

    /**
     * Gets the value of the идДок property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getИдДок() {
        return идДок;
    }

    /**
     * Sets the value of the идДок property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setИдДок(String value) {
        this.идДок = value;
    }

    /**
     * Gets the value of the номерДела property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getНомерДела() {
        return номерДела;
    }

    /**
     * Sets the value of the номерДела property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setНомерДела(String value) {
        this.номерДела = value;
    }


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
     *         &lt;element name="ОГРНИП" type="{http://ws.unisoft/EGRNXX/FullFLReq}ОГРНИПТип"/>
     *         &lt;element name="ИНН" type="{http://ws.unisoft/EGRNXX/FullFLReq}ИННФЛТип"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "\u043e\u0433\u0440\u043d\u0438\u043f",
        "\u0438\u043d\u043d"
    })
    public static class ЗапросИП {

        @XmlElement(name = "\u041e\u0413\u0420\u041d\u0418\u041f")
        protected String огрнип;
        @XmlElement(name = "\u0418\u041d\u041d")
        protected String инн;

        /**
         * Gets the value of the огрнип property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getОГРНИП() {
            return огрнип;
        }

        /**
         * Sets the value of the огрнип property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setОГРНИП(String value) {
            this.огрнип = value;
        }

        /**
         * Gets the value of the инн property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getИНН() {
            return инн;
        }

        /**
         * Sets the value of the инн property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setИНН(String value) {
            this.инн = value;
        }

    }

}

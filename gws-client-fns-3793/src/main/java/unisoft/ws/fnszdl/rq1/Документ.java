
package unisoft.ws.fnszdl.rq1;

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
 *         &lt;element name="СвЮЛ">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="НаимЮЛ" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;maxLength value="1000"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="ИННЮЛ" use="required" type="{http://ws.unisoft/FNSZDL/Rq1}ИННЮЛТип" />
 *                 &lt;attribute name="ОГРН" use="required" type="{http://ws.unisoft/FNSZDL/Rq1}ОГРНТип" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ЗапросНП">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="ИННЮЛ" type="{http://ws.unisoft/FNSZDL/Rq1}ИННЮЛТип"/>
 *                   &lt;element name="ИННФЛ" type="{http://ws.unisoft/FNSZDL/Rq1}ИННФЛТип"/>
 *                 &lt;/choice>
 *                 &lt;attribute name="ДатаНа" use="required" type="{http://ws.unisoft/FNSZDL/Rq1}Дата1900ТипР" />
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
 *       &lt;attribute name="ИдЗапросП">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;length value="36"/>
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
    "\u0441\u0432\u042e\u041b",
    "\u0437\u0430\u043f\u0440\u043e\u0441\u041d\u041f"
})
@XmlRootElement(name = "\u0414\u043e\u043a\u0443\u043c\u0435\u043d\u0442")
public class Документ {

    @XmlElement(name = "\u0421\u0432\u042e\u041b", required = true)
    protected Документ.СвЮЛ свЮЛ;
    @XmlElement(name = "\u0417\u0430\u043f\u0440\u043e\u0441\u041d\u041f", required = true)
    protected Документ.ЗапросНП запросНП;
    @XmlAttribute(name = "\u0412\u0435\u0440\u0441\u0424\u043e\u0440\u043c", required = true)
    protected String версФорм;
    @XmlAttribute(name = "\u0418\u0434\u0417\u0430\u043f\u0440\u043e\u0441\u041f")
    protected String идЗапросП;

    /**
     * Gets the value of the свЮЛ property.
     * 
     * @return
     *     possible object is
     *     {@link Документ.СвЮЛ }
     *     
     */
    public Документ.СвЮЛ getСвЮЛ() {
        return свЮЛ;
    }

    /**
     * Sets the value of the свЮЛ property.
     * 
     * @param value
     *     allowed object is
     *     {@link Документ.СвЮЛ }
     *     
     */
    public void setСвЮЛ(Документ.СвЮЛ value) {
        this.свЮЛ = value;
    }

    /**
     * Gets the value of the запросНП property.
     * 
     * @return
     *     possible object is
     *     {@link Документ.ЗапросНП }
     *     
     */
    public Документ.ЗапросНП getЗапросНП() {
        return запросНП;
    }

    /**
     * Sets the value of the запросНП property.
     * 
     * @param value
     *     allowed object is
     *     {@link Документ.ЗапросНП }
     *     
     */
    public void setЗапросНП(Документ.ЗапросНП value) {
        this.запросНП = value;
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
     * Gets the value of the идЗапросП property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getИдЗапросП() {
        return идЗапросП;
    }

    /**
     * Sets the value of the идЗапросП property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setИдЗапросП(String value) {
        this.идЗапросП = value;
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
     *         &lt;element name="ИННЮЛ" type="{http://ws.unisoft/FNSZDL/Rq1}ИННЮЛТип"/>
     *         &lt;element name="ИННФЛ" type="{http://ws.unisoft/FNSZDL/Rq1}ИННФЛТип"/>
     *       &lt;/choice>
     *       &lt;attribute name="ДатаНа" use="required" type="{http://ws.unisoft/FNSZDL/Rq1}Дата1900ТипР" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "\u0438\u043d\u043d\u044e\u043b",
        "\u0438\u043d\u043d\u0444\u043b"
    })
    public static class ЗапросНП {

        @XmlElement(name = "\u0418\u041d\u041d\u042e\u041b")
        protected String иннюл;
        @XmlElement(name = "\u0418\u041d\u041d\u0424\u041b")
        protected String иннфл;
        @XmlAttribute(name = "\u0414\u0430\u0442\u0430\u041d\u0430", required = true)
        protected String датаНа;

        /**
         * Gets the value of the иннюл property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getИННЮЛ() {
            return иннюл;
        }

        /**
         * Sets the value of the иннюл property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setИННЮЛ(String value) {
            this.иннюл = value;
        }

        /**
         * Gets the value of the иннфл property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getИННФЛ() {
            return иннфл;
        }

        /**
         * Sets the value of the иннфл property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setИННФЛ(String value) {
            this.иннфл = value;
        }

        /**
         * Gets the value of the датаНа property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getДатаНа() {
            return датаНа;
        }

        /**
         * Sets the value of the датаНа property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setДатаНа(String value) {
            this.датаНа = value;
        }

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
     *       &lt;attribute name="НаимЮЛ" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="1"/>
     *             &lt;maxLength value="1000"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="ИННЮЛ" use="required" type="{http://ws.unisoft/FNSZDL/Rq1}ИННЮЛТип" />
     *       &lt;attribute name="ОГРН" use="required" type="{http://ws.unisoft/FNSZDL/Rq1}ОГРНТип" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class СвЮЛ {

        @XmlAttribute(name = "\u041d\u0430\u0438\u043c\u042e\u041b", required = true)
        protected String наимЮЛ;
        @XmlAttribute(name = "\u0418\u041d\u041d\u042e\u041b", required = true)
        protected String иннюл;
        @XmlAttribute(name = "\u041e\u0413\u0420\u041d", required = true)
        protected String огрн;

        /**
         * Gets the value of the наимЮЛ property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getНаимЮЛ() {
            return наимЮЛ;
        }

        /**
         * Sets the value of the наимЮЛ property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setНаимЮЛ(String value) {
            this.наимЮЛ = value;
        }

        /**
         * Gets the value of the иннюл property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getИННЮЛ() {
            return иннюл;
        }

        /**
         * Sets the value of the иннюл property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setИННЮЛ(String value) {
            this.иннюл = value;
        }

        /**
         * Gets the value of the огрн property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getОГРН() {
            return огрн;
        }

        /**
         * Sets the value of the огрн property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setОГРН(String value) {
            this.огрн = value;
        }

    }

}

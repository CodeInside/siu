
package unisoft.ws.fnszdl.rs2;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="СвЗад">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;choice>
 *                     &lt;element name="ИННЮЛ" type="{http://ws.unisoft/FNSZDL/Rs2}ИННЮЛТип"/>
 *                     &lt;element name="ИННФЛ" type="{http://ws.unisoft/FNSZDL/Rs2}ИННФЛТип"/>
 *                   &lt;/choice>
 *                   &lt;element name="ПерНО" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="КодИФНС" type="{http://ws.unisoft/FNSZDL/Rs2}СОНОТип" maxOccurs="unbounded"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="ДатаНа" use="required" type="{http://ws.unisoft/FNSZDL/Rs2}Дата1900ТипР" />
 *                 &lt;attribute name="ПрЗад" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;length value="1"/>
 *                       &lt;enumeration value="0"/>
 *                       &lt;enumeration value="1"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="КодОбр">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="2"/>
 *               &lt;enumeration value="02"/>
 *               &lt;enumeration value="11"/>
 *               &lt;enumeration value="52"/>
 *               &lt;enumeration value="55"/>
 *               &lt;enumeration value="81"/>
 *               &lt;enumeration value="82"/>
 *               &lt;enumeration value="83"/>
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
 *             &lt;enumeration value="4.03"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="ИдЗапросФ" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="36"/>
 *             &lt;pattern value="[0-9]+"/>
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
    "\u0441\u0432\u0417\u0430\u0434",
    "\u043a\u043e\u0434\u041e\u0431\u0440"
})
@XmlRootElement(name = "\u0414\u043e\u043a\u0443\u043c\u0435\u043d\u0442")
public class Документ {

    @XmlElement(name = "\u0421\u0432\u0417\u0430\u0434")
    protected Документ.СвЗад свЗад;
    @XmlElement(name = "\u041a\u043e\u0434\u041e\u0431\u0440")
    protected String кодОбр;
    @XmlAttribute(name = "\u0412\u0435\u0440\u0441\u0424\u043e\u0440\u043c", required = true)
    protected String версФорм;
    @XmlAttribute(name = "\u0418\u0434\u0417\u0430\u043f\u0440\u043e\u0441\u0424", required = true)
    protected String идЗапросФ;

    /**
     * Gets the value of the свЗад property.
     * 
     * @return
     *     possible object is
     *     {@link Документ.СвЗад }
     *     
     */
    public Документ.СвЗад getСвЗад() {
        return свЗад;
    }

    /**
     * Sets the value of the свЗад property.
     * 
     * @param value
     *     allowed object is
     *     {@link Документ.СвЗад }
     *     
     */
    public void setСвЗад(Документ.СвЗад value) {
        this.свЗад = value;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;choice>
     *           &lt;element name="ИННЮЛ" type="{http://ws.unisoft/FNSZDL/Rs2}ИННЮЛТип"/>
     *           &lt;element name="ИННФЛ" type="{http://ws.unisoft/FNSZDL/Rs2}ИННФЛТип"/>
     *         &lt;/choice>
     *         &lt;element name="ПерНО" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="КодИФНС" type="{http://ws.unisoft/FNSZDL/Rs2}СОНОТип" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="ДатаНа" use="required" type="{http://ws.unisoft/FNSZDL/Rs2}Дата1900ТипР" />
     *       &lt;attribute name="ПрЗад" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;length value="1"/>
     *             &lt;enumeration value="0"/>
     *             &lt;enumeration value="1"/>
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
        "\u0438\u043d\u043d\u044e\u043b",
        "\u0438\u043d\u043d\u0444\u043b",
        "\u043f\u0435\u0440\u041d\u041e"
    })
    public static class СвЗад {

        @XmlElement(name = "\u0418\u041d\u041d\u042e\u041b")
        protected String иннюл;
        @XmlElement(name = "\u0418\u041d\u041d\u0424\u041b")
        protected String иннфл;
        @XmlElement(name = "\u041f\u0435\u0440\u041d\u041e")
        protected Документ.СвЗад.ПерНО перНО;
        @XmlAttribute(name = "\u0414\u0430\u0442\u0430\u041d\u0430", required = true)
        protected String датаНа;
        @XmlAttribute(name = "\u041f\u0440\u0417\u0430\u0434", required = true)
        protected String прЗад;

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
         * Gets the value of the перНО property.
         * 
         * @return
         *     possible object is
         *     {@link Документ.СвЗад.ПерНО }
         *     
         */
        public Документ.СвЗад.ПерНО getПерНО() {
            return перНО;
        }

        /**
         * Sets the value of the перНО property.
         * 
         * @param value
         *     allowed object is
         *     {@link Документ.СвЗад.ПерНО }
         *     
         */
        public void setПерНО(Документ.СвЗад.ПерНО value) {
            this.перНО = value;
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

        /**
         * Gets the value of the прЗад property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getПрЗад() {
            return прЗад;
        }

        /**
         * Sets the value of the прЗад property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setПрЗад(String value) {
            this.прЗад = value;
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
         *       &lt;sequence>
         *         &lt;element name="КодИФНС" type="{http://ws.unisoft/FNSZDL/Rs2}СОНОТип" maxOccurs="unbounded"/>
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
            "\u043a\u043e\u0434\u0418\u0424\u041d\u0421"
        })
        public static class ПерНО {

            @XmlElement(name = "\u041a\u043e\u0434\u0418\u0424\u041d\u0421", required = true)
            protected List<String> кодИФНС;

            /**
             * Gets the value of the кодИФНС property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the кодИФНС property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getКодИФНС().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link String }
             * 
             * 
             */
            public List<String> getКодИФНС() {
                if (кодИФНС == null) {
                    кодИФНС = new ArrayList<String>();
                }
                return this.кодИФНС;
            }

        }

    }

}

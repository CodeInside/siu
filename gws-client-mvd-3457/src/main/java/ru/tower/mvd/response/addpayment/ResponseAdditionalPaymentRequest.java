/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */


package ru.tower.mvd.response.addpayment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "\u0444\u0438\u043e",
    "\u0434\u0430\u0442\u0430\u0420\u043e\u0436\u0434\u0435\u043d\u0438\u044f",
    "\u0441\u0442\u0440\u0430\u0445\u043e\u0432\u043e\u0439\u041d\u043e\u043c\u0435\u0440",
    "\u043b\u0438\u0447\u043d\u044b\u0439\u0414\u043e\u043a\u0443\u043c\u0435\u043d\u0442",
    "\u0434\u0430\u0442\u0430\u041f\u043e\u0421\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u044e\u041d\u0430",
    "\u043d\u0430\u043b\u0438\u0447\u0438\u0435\u0414\u0430\u043d\u043d\u044b\u0445",
    "\u043f\u043e\u0434\u0440\u0430\u0437\u0434\u0435\u043b\u0435\u043d\u0438\u0435",
    "\u043f\u0440\u0435\u043a\u0440\u0430\u0449\u0435\u043d\u0438\u0435\u0412\u044b\u043f\u043b\u0430\u0442",
    "\u0432\u0441\u0435\u0412\u044b\u043f\u043b\u0430\u0442\u044b",
    "\u0434\u0430\u0442\u0430\u0424\u043e\u0440\u043c\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u044f"
})
@XmlRootElement(name = "\u041e\u0422\u0412\u0415\u0422_\u041d\u0410_\u0417\u0410\u041f\u0420\u041e\u0421_\u0421\u0412\u0415\u0414\u0415\u041d\u0418\u0419_\u041e_\u041f\u041e\u041b\u0423\u0427\u0415\u041d\u0418\u0418_\u041f\u0415\u041d\u0421\u0418\u0418_\u041d\u0410_\u041c\u0415\u0421\u042f\u0426_\u0423\u0421\u0422\u0410\u041d\u041e\u0412\u041b\u0415\u041d\u0418\u042f_\u0414\u041e\u041f\u041b\u0410\u0422\u042b")
public class ResponseAdditionalPaymentRequest {

    @XmlElement(name = "\u0424\u0418\u041e", required = true)
    protected ResponseAdditionalPaymentRequest.ФИО фио;
    @XmlElement(name = "\u0414\u0430\u0442\u0430\u0420\u043e\u0436\u0434\u0435\u043d\u0438\u044f", required = true)
    protected String датаРождения;
    @XmlElement(name = "\u0421\u0442\u0440\u0430\u0445\u043e\u0432\u043e\u0439\u041d\u043e\u043c\u0435\u0440", required = true)
    protected String страховойНомер;
    @XmlElement(name = "\u041b\u0438\u0447\u043d\u044b\u0439\u0414\u043e\u043a\u0443\u043c\u0435\u043d\u0442", required = true)
    protected ЛичныйДокумент личныйДокумент;
    @XmlElement(name = "\u0414\u0430\u0442\u0430\u041f\u043e\u0421\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u044e\u041d\u0430", required = true)
    protected String датаПоСостояниюНа;
    @XmlElement(name = "\u041d\u0430\u043b\u0438\u0447\u0438\u0435\u0414\u0430\u043d\u043d\u044b\u0445", required = true)
    protected String наличиеДанных;
    @XmlElement(name = "\u041f\u043e\u0434\u0440\u0430\u0437\u0434\u0435\u043b\u0435\u043d\u0438\u0435", required = true)
    protected Подразделение подразделение;
    @XmlElement(name = "\u041f\u0440\u0435\u043a\u0440\u0430\u0449\u0435\u043d\u0438\u0435\u0412\u044b\u043f\u043b\u0430\u0442")
    protected ПрекращениеВыплат прекращениеВыплат;
    @XmlElement(name = "\u0412\u0441\u0435\u0412\u044b\u043f\u043b\u0430\u0442\u044b")
    protected ResponseAdditionalPaymentRequest.ВсеВыплаты всеВыплаты;
    @XmlElement(name = "\u0414\u0430\u0442\u0430\u0424\u043e\u0440\u043c\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u044f", required = true)
    protected String датаФормирования;

    /**
     * Gets the value of the фио property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseAdditionalPaymentRequest.ФИО }
     *     
     */
    public ResponseAdditionalPaymentRequest.ФИО getФИО() {
        return фио;
    }

    /**
     * Sets the value of the фио property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseAdditionalPaymentRequest.ФИО }
     *     
     */
    public void setФИО(ResponseAdditionalPaymentRequest.ФИО value) {
        this.фио = value;
    }

    /**
     * Gets the value of the датаРождения property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getДатаРождения() {
        return датаРождения;
    }

    /**
     * Sets the value of the датаРождения property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setДатаРождения(String value) {
        this.датаРождения = value;
    }

    /**
     * Gets the value of the страховойНомер property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getСтраховойНомер() {
        return страховойНомер;
    }

    /**
     * Sets the value of the страховойНомер property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setСтраховойНомер(String value) {
        this.страховойНомер = value;
    }

    /**
     * Gets the value of the личныйДокумент property.
     * 
     * @return
     *     possible object is
     *     {@link ЛичныйДокумент }
     *     
     */
    public ЛичныйДокумент getЛичныйДокумент() {
        return личныйДокумент;
    }

    /**
     * Sets the value of the личныйДокумент property.
     * 
     * @param value
     *     allowed object is
     *     {@link ЛичныйДокумент }
     *     
     */
    public void setЛичныйДокумент(ЛичныйДокумент value) {
        this.личныйДокумент = value;
    }

    /**
     * Gets the value of the датаПоСостояниюНа property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getДатаПоСостояниюНа() {
        return датаПоСостояниюНа;
    }

    /**
     * Sets the value of the датаПоСостояниюНа property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setДатаПоСостояниюНа(String value) {
        this.датаПоСостояниюНа = value;
    }

    /**
     * Gets the value of the наличиеДанных property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getНаличиеДанных() {
        return наличиеДанных;
    }

    /**
     * Sets the value of the наличиеДанных property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setНаличиеДанных(String value) {
        this.наличиеДанных = value;
    }

    /**
     * Gets the value of the подразделение property.
     * 
     * @return
     *     possible object is
     *     {@link Подразделение }
     *     
     */
    public Подразделение getПодразделение() {
        return подразделение;
    }

    /**
     * Sets the value of the подразделение property.
     * 
     * @param value
     *     allowed object is
     *     {@link Подразделение }
     *     
     */
    public void setПодразделение(Подразделение value) {
        this.подразделение = value;
    }

    /**
     * Gets the value of the прекращениеВыплат property.
     * 
     * @return
     *     possible object is
     *     {@link ПрекращениеВыплат }
     *     
     */
    public ПрекращениеВыплат getПрекращениеВыплат() {
        return прекращениеВыплат;
    }

    /**
     * Sets the value of the прекращениеВыплат property.
     * 
     * @param value
     *     allowed object is
     *     {@link ПрекращениеВыплат }
     *     
     */
    public void setПрекращениеВыплат(ПрекращениеВыплат value) {
        this.прекращениеВыплат = value;
    }

    /**
     * Gets the value of the всеВыплаты property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseAdditionalPaymentRequest.ВсеВыплаты }
     *     
     */
    public ResponseAdditionalPaymentRequest.ВсеВыплаты getВсеВыплаты() {
        return всеВыплаты;
    }

    /**
     * Sets the value of the всеВыплаты property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseAdditionalPaymentRequest.ВсеВыплаты }
     *     
     */
    public void setВсеВыплаты(ResponseAdditionalPaymentRequest.ВсеВыплаты value) {
        this.всеВыплаты = value;
    }

    /**
     * Gets the value of the датаФормирования property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getДатаФормирования() {
        return датаФормирования;
    }

    /**
     * Sets the value of the датаФормирования property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setДатаФормирования(String value) {
        this.датаФормирования = value;
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
     *         &lt;element name="КоличествоВыплат">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="99"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Выплата" maxOccurs="99">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="ВидВыплаты">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
     *                         &lt;maxLength value="30"/>
     *                         &lt;minLength value="1"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="СуммаВыплаты">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         &lt;fractionDigits value="2"/>
     *                         &lt;totalDigits value="15"/>
     *                         &lt;pattern value="[0-9]+\.\d{2}"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "\u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e\u0412\u044b\u043f\u043b\u0430\u0442",
        "\u0432\u044b\u043f\u043b\u0430\u0442\u0430"
    })
    public static class ВсеВыплаты {

        @XmlElement(name = "\u041a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e\u0412\u044b\u043f\u043b\u0430\u0442", required = true)
        protected String количествоВыплат;
        @XmlElement(name = "\u0412\u044b\u043f\u043b\u0430\u0442\u0430", required = true)
        protected List<ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата> выплата;

        /**
         * Gets the value of the количествоВыплат property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getКоличествоВыплат() {
            return количествоВыплат;
        }

        /**
         * Sets the value of the количествоВыплат property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setКоличествоВыплат(String value) {
            this.количествоВыплат = value;
        }

        /**
         * Gets the value of the выплата property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the выплата property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getВыплата().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата }
         * 
         * 
         */
        public List<ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата> getВыплата() {
            if (выплата == null) {
                выплата = new ArrayList<ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата>();
            }
            return this.выплата;
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
         *         &lt;element name="ВидВыплаты">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
         *               &lt;maxLength value="30"/>
         *               &lt;minLength value="1"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="СуммаВыплаты">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *               &lt;fractionDigits value="2"/>
         *               &lt;totalDigits value="15"/>
         *               &lt;pattern value="[0-9]+\.\d{2}"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
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
            "\u0432\u0438\u0434\u0412\u044b\u043f\u043b\u0430\u0442\u044b",
            "\u0441\u0443\u043c\u043c\u0430\u0412\u044b\u043f\u043b\u0430\u0442\u044b"
        })
        public static class Выплата {

            @XmlElement(name = "\u0412\u0438\u0434\u0412\u044b\u043f\u043b\u0430\u0442\u044b", required = true)
            @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
            protected String видВыплаты;
            @XmlElement(name = "\u0421\u0443\u043c\u043c\u0430\u0412\u044b\u043f\u043b\u0430\u0442\u044b", required = true)
            protected BigDecimal суммаВыплаты;

            /**
             * Gets the value of the видВыплаты property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getВидВыплаты() {
                return видВыплаты;
            }

            /**
             * Sets the value of the видВыплаты property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setВидВыплаты(String value) {
                this.видВыплаты = value;
            }

            /**
             * Gets the value of the суммаВыплаты property.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getСуммаВыплаты() {
                return суммаВыплаты;
            }

            /**
             * Sets the value of the суммаВыплаты property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setСуммаВыплаты(BigDecimal value) {
                this.суммаВыплаты = value;
            }

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
     *       &lt;sequence>
     *         &lt;element name="Фамилия">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
     *               &lt;maxLength value="40"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Имя">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
     *               &lt;maxLength value="40"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Отчество" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
     *               &lt;maxLength value="40"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
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
        "\u0444\u0430\u043c\u0438\u043b\u0438\u044f",
        "\u0438\u043c\u044f",
        "\u043e\u0442\u0447\u0435\u0441\u0442\u0432\u043e"
    })
    public static class ФИО {

        @XmlElement(name = "\u0424\u0430\u043c\u0438\u043b\u0438\u044f", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String фамилия;
        @XmlElement(name = "\u0418\u043c\u044f", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String имя;
        @XmlElement(name = "\u041e\u0442\u0447\u0435\u0441\u0442\u0432\u043e")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String отчество;

        /**
         * Gets the value of the фамилия property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getФамилия() {
            return фамилия;
        }

        /**
         * Sets the value of the фамилия property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setФамилия(String value) {
            this.фамилия = value;
        }

        /**
         * Gets the value of the имя property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getИмя() {
            return имя;
        }

        /**
         * Sets the value of the имя property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setИмя(String value) {
            this.имя = value;
        }

        /**
         * Gets the value of the отчество property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getОтчество() {
            return отчество;
        }

        /**
         * Sets the value of the отчество property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setОтчество(String value) {
            this.отчество = value;
        }

    }

}

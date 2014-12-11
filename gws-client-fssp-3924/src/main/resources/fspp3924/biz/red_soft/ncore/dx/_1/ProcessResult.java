//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.ncore.dx._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProcessResult.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ProcessResult">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SUCCESS"/>
 *     &lt;enumeration value="PARTIALLY"/>
 *     &lt;enumeration value="FAIL"/>
 *     &lt;enumeration value="WAIT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ProcessResult")
@XmlEnum
public enum ProcessResult {


    /**
     * Обработка завершена успешно
     * 
     */
    SUCCESS,

    /**
     * Обработка завершена с ошибками, данные приняты частично
     * 
     */
    PARTIALLY,

    /**
     * Обработка завершилась с ошибкой, данные не приня??ы
     * 
     */
    FAIL,

    /**
     * Данные обрабатываются. Подождите.
     * 
     */
    WAIT;

    public String value() {
        return name();
    }

    public static ProcessResult fromValue(String v) {
        return valueOf(v);
    }

}
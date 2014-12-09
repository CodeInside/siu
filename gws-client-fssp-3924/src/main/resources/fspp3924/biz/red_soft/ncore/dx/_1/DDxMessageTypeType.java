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
 * <p>Java class for DDxMessageTypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DDxMessageTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;maxLength value="16"/>
 *     &lt;enumeration value="ERROR_MSG"/>
 *     &lt;enumeration value="ARBITRARY"/>
 *     &lt;enumeration value="PACK_SEND"/>
 *     &lt;enumeration value="PACK_RECV"/>
 *     &lt;enumeration value="FILE_RECV"/>
 *     &lt;enumeration value="FILE_SEND"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DDxMessageTypeType")
@XmlEnum
public enum DDxMessageTypeType {


    /**
     * Сообщение об ошибке
     * 
     */
    ERROR_MSG,

    /**
     * Произвольное сообщение
     * 
     */
    ARBITRARY,

    /**
     * Отправка пакета документов
     * 
     */
    PACK_SEND,

    /**
     * Прием пакета документов
     * 
     */
    PACK_RECV,

    /**
     * Прием файла
     * 
     */
    FILE_RECV,

    /**
     * Отправка файла
     * 
     */
    FILE_SEND;

    public String value() {
        return name();
    }

    public static DDxMessageTypeType fromValue(String v) {
        return valueOf(v);
    }

}

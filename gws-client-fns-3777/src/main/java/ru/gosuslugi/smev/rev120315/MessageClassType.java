//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.19 at 05:39:55 PM MSK 
//


package ru.gosuslugi.smev.rev120315;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageClassType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageClassType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="REQUEST"/>
 *     &lt;enumeration value="RESPONSE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MessageClassType")
@XmlEnum
public enum MessageClassType {


    /**
     * Запрос от потребителя к поставщику
     *                     
     * 
     */
    REQUEST,

    /**
     * Ответ поставщика потребителю
     * 
     */
    RESPONSE;

    public String value() {
        return name();
    }

    public static MessageClassType fromValue(String v) {
        return valueOf(v);
    }

}

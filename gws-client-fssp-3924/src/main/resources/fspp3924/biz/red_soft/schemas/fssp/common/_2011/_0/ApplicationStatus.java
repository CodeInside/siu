//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.schemas.fssp.common._2011._0;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApplicationStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ApplicationStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NEW"/>
 *     &lt;enumeration value="REGISTERED"/>
 *     &lt;enumeration value="ASSIGNED"/>
 *     &lt;enumeration value="COMPLETED"/>
 *     &lt;enumeration value="REJECTED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ApplicationStatus")
@XmlEnum
public enum ApplicationStatus {


    /**
     * Заявление перешло на статус "Новый".
     * 
     */
    NEW,

    /**
     * Заявление зарегистрировано.
     * 
     */
    REGISTERED,

    /**
     * Заявлению назначено ответственное лицо.
     * 
     */
    ASSIGNED,

    /**
     * Заявление исполнено или отказано.
     * 
     */
    COMPLETED,

    /**
     * Отказано в обработке.
     * 
     */
    REJECTED;

    public String value() {
        return name();
    }

    public static ApplicationStatus fromValue(String v) {
        return valueOf(v);
    }

}
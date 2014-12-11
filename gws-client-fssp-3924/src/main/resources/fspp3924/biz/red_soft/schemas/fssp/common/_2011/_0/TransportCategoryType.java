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
 * <p>Java class for TransportCategoryType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TransportCategoryType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="B"/>
 *     &lt;enumeration value="C"/>
 *     &lt;enumeration value="D"/>
 *     &lt;enumeration value="E"/>
 *     &lt;enumeration value="F"/>
 *     &lt;enumeration value="G"/>
 *     &lt;enumeration value="H"/>
 *     &lt;enumeration value="K"/>
 *     &lt;enumeration value="T"/>
 *     &lt;enumeration value="J"/>
 *     &lt;enumeration value="L"/>
 *     &lt;enumeration value="M"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TransportCategoryType")
@XmlEnum
public enum TransportCategoryType {


    /**
     * Мотоцикл
     * 
     */
    A,

    /**
     * Легковой автомобиль
     * 
     */
    B,

    /**
     * Гpузовой автомобиль
     * 
     */
    C,

    /**
     * Автобус
     * 
     */
    D,

    /**
     * Пpицеп
     * 
     */
    E,

    /**
     * Трамвай
     * 
     */
    F,

    /**
     * Троллейбус
     * 
     */
    G,

    /**
     * Трактор
     * 
     */
    H,

    /**
     * Самоходный механизм
     * 
     */
    K,

    /**
     * Подвижной состав (железнодорожный)
     * 
     */
    T,

    /**
     * Велосипед
     * 
     */
    J,

    /**
     * Гужевой
     * 
     */
    L,

    /**
     * Мопед, скутер
     * 
     */
    M;

    public String value() {
        return name();
    }

    public static TransportCategoryType fromValue(String v) {
        return valueOf(v);
    }

}
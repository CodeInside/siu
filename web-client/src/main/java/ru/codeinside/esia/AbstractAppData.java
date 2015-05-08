
package ru.codeinside.esia;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for abstractAppData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractAppData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://smev.gosuslugi.ru/rev120315}appDataType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractAppData")
@XmlSeeAlso({
    AppData.class,
    ResultAppData.class
})
public abstract class AbstractAppData
    extends AppDataType
{


}

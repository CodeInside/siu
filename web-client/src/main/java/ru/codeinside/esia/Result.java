
package ru.codeinside.esia;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for Result complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Result">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataRow" type="{http://oep-penza.ru/com/oep}DataRow" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="params" type="{http://oep-penza.ru/com/oep}SystemParams" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Result", namespace = "http://oep-penza.ru/com/oep", propOrder = {
    "dataRow",
    "params"
})
public class Result {

    @XmlElement(nillable = true)
    protected List<DataRow> dataRow;
    protected SystemParams params;

    /**
     * Gets the value of the dataRow property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataRow property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataRow().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataRow }
     * 
     * 
     */
    public List<DataRow> getDataRow() {
        if (dataRow == null) {
            dataRow = new ArrayList<DataRow>();
        }
        return this.dataRow;
    }

    /**
     * Gets the value of the params property.
     * 
     * @return
     *     possible object is
     *     {@link SystemParams }
     *     
     */
    public SystemParams getParams() {
        return params;
    }

    /**
     * Sets the value of the params property.
     * 
     * @param value
     *     allowed object is
     *     {@link SystemParams }
     *     
     */
    public void setParams(SystemParams value) {
        this.params = value;
    }

}

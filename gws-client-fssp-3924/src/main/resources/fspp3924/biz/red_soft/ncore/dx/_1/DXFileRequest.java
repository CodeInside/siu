//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 12:01:56 PM MSK 
//


package biz.red_soft.ncore.dx._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DXFileRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DXFileRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.red-soft.biz/ncore/dx/1.1}DXBlock">
 *       &lt;sequence>
 *         &lt;element name="FileBlockInfo" type="{http://www.red-soft.biz/ncore/dx/1.1}FileBlockInfo"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DXFileRequest", propOrder = {
    "fileBlockInfo"
})
public class DXFileRequest
    extends DXBlock
{

    @XmlElement(name = "FileBlockInfo", required = true)
    protected FileBlockInfo fileBlockInfo;

    /**
     * Gets the value of the fileBlockInfo property.
     * 
     * @return
     *     possible object is
     *     {@link FileBlockInfo }
     *     
     */
    public FileBlockInfo getFileBlockInfo() {
        return fileBlockInfo;
    }

    /**
     * Sets the value of the fileBlockInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileBlockInfo }
     *     
     */
    public void setFileBlockInfo(FileBlockInfo value) {
        this.fileBlockInfo = value;
    }

}
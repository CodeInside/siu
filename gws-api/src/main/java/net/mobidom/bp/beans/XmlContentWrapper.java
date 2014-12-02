package net.mobidom.bp.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.w3c.dom.Element;

@XmlRootElement(name = "xmlContent", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "XmlContentType")
public class XmlContentWrapper implements Serializable {
  private static final long serialVersionUID = 8967908290696702114L;

  Element xmlContent;

  @XmlAnyElement
  public Element getXmlContent() {
    return xmlContent;
  }

  public void setXmlContent(Element xmlContent) {
    this.xmlContent = xmlContent;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((xmlContent == null) ? 0 : xmlContent.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    XmlContentWrapper other = (XmlContentWrapper) obj;
    if (xmlContent == null) {
      if (other.xmlContent != null)
        return false;
    } else if (!xmlContent.getTextContent().equals(other.xmlContent.getTextContent()))
      return false;
    return true;
  }

}

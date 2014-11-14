package net.mobidom.bp.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "document", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "DocumentType", propOrder = { "type", "binaryContent", "xmlContent", "kind" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Document implements Serializable {
  private static final long serialVersionUID = -2951102600558480199L;

  String type;

  DocumentKind kind;

  BinaryContent binaryContent;

  XmlContentWrapper xmlContent;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public BinaryContent getBinaryContent() {
    return binaryContent;
  }

  public void setBinaryContent(BinaryContent binaryContent) {
    this.binaryContent = binaryContent;
  }

  public XmlContentWrapper getXmlContent() {
    return xmlContent;
  }

  public void setXmlContent(XmlContentWrapper xmlContent) {
    this.xmlContent = xmlContent;
  }

  public DocumentKind getKind() {
    return kind;
  }

  public void setKind(DocumentKind kind) {
    this.kind = kind;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((binaryContent == null) ? 0 : binaryContent.hashCode());
    result = prime * result + ((kind == null) ? 0 : kind.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    Document other = (Document) obj;
    if (binaryContent == null) {
      if (other.binaryContent != null)
        return false;
    } else if (!binaryContent.equals(other.binaryContent))
      return false;
    if (kind != other.kind)
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    if (xmlContent == null) {
      if (other.xmlContent != null)
        return false;
    } else if (!xmlContent.equals(other.xmlContent))
      return false;
    return true;
  }

}

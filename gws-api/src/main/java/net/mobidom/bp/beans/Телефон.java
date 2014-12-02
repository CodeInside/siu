package net.mobidom.bp.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "телефон", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "Телефон")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Телефон implements Serializable {

  private static final long serialVersionUID = -3073877918787708716L;

  String префикс;
  String номер;

  public String getПрефикс() {
    return префикс;
  }

  public void setПрефикс(String префикс) {
    this.префикс = префикс;
  }

  public String getНомер() {
    return номер;
  }

  public void setНомер(String номер) {
    this.номер = номер;
  }

  public String toGeneralString() {
    return String.format("(%s) %s", префикс, номер);
  }

}

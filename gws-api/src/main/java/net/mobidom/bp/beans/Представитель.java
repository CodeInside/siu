package net.mobidom.bp.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "представитель", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "Представитель")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Представитель implements Serializable {

  private static final long serialVersionUID = -8512539807893233206L;

  String идентификатор;

  public String getИдентификатор() {
    return идентификатор;
  }

  public void setИдентификатор(String идентификатор) {
    this.идентификатор = идентификатор;
  }

}

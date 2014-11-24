package net.mobidom.bp.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "руководитель", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "Руководитель")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Руководитель implements Serializable {
  private static final long serialVersionUID = 2284313403107020671L;

  ФИО фио;
  String должность;

  public String toGeneralString() {
    return String.format("%s %s", должность, фио.toFullString());
  }

  public ФИО getФио() {
    return фио;
  }

  public void setФио(ФИО фио) {
    this.фио = фио;
  }

  public String getДолжность() {
    return должность;
  }

  public void setДолжность(String должность) {
    this.должность = должность;
  }

}

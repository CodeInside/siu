package net.mobidom.bp.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "главныйБухгалтер", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "ГлавныйБухгалтер")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ГлавныиБухгалтер implements Serializable {
  private static final long serialVersionUID = 6488513772029045770L;

  ФИО фио;

  public ФИО getФио() {
    return фио;
  }

  public void setФио(ФИО фио) {
    this.фио = фио;
  }

  public String toGeneralString() {
    return фио.toFullString();
  }

}

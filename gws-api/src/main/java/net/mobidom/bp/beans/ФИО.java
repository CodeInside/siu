package net.mobidom.bp.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "фио", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "ФИО")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ФИО implements Serializable {
  private static final long serialVersionUID = -8906099606776025608L;

  String фамилия;
  String имя;
  String отчество;

  public String getФамилия() {
    return фамилия;
  }

  public void setФамилия(String фамилия) {
    this.фамилия = фамилия;
  }

  public String getИмя() {
    return имя;
  }

  public void setИмя(String имя) {
    this.имя = имя;
  }

  public String getОтчество() {
    return отчество;
  }

  public void setОтчество(String отчество) {
    this.отчество = отчество;
  }

  public String toFullString() {
    return String.format("%s %s %s", фамилия, имя, отчество);
  }

}

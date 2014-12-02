package net.mobidom.bp.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "адрес", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "Адрес")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Адрес implements Serializable {
  private static final long serialVersionUID = 4502967190860798899L;

  String страна;
  String почтовыйИндекс;
  String регион;
  String район;
  String населенныйПункт;
  String улица;
  String дом;
  String корпус;

  String квартира;
  String комната;

  String офис;

  public String toGeneralString() {
    return toLabel(почтовыйИндекс) 
          + toLabel(страна) 
          + toLabel(регион) 
          + toLabel(район) 
          + toLabel(населенныйПункт) 
          + toLabel(улица)
          + toLabel(дом) 
          + toLabel(корпус) 
          + toLabel(квартира) 
          + toLabel(комната) 
          + toLabel(офис);
  }

  private String toLabel(String line) {
    return line != null ? line + ", " : "";
  }

  public String getСтрана() {
    return страна;
  }

  public void setСтрана(String страна) {
    this.страна = страна;
  }

  public String getПочтовыйИндекс() {
    return почтовыйИндекс;
  }

  public void setПочтовыйИндекс(String почтовыйИндекс) {
    this.почтовыйИндекс = почтовыйИндекс;
  }

  public String getРегион() {
    return регион;
  }

  public void setРегион(String регион) {
    this.регион = регион;
  }

  public String getРайон() {
    return район;
  }

  public void setРайон(String район) {
    this.район = район;
  }

  public String getНаселенныйПункт() {
    return населенныйПункт;
  }

  public void setНаселенныйПункт(String населенныйПункт) {
    this.населенныйПункт = населенныйПункт;
  }

  public String getУлица() {
    return улица;
  }

  public void setУлица(String улица) {
    this.улица = улица;
  }

  public String getДом() {
    return дом;
  }

  public void setДом(String дом) {
    this.дом = дом;
  }

  public String getКорпус() {
    return корпус;
  }

  public void setКорпус(String корпус) {
    this.корпус = корпус;
  }

  public String getКвартира() {
    return квартира;
  }

  public void setКвартира(String квартира) {
    this.квартира = квартира;
  }

  public String getКомната() {
    return комната;
  }

  public void setКомната(String комната) {
    this.комната = комната;
  }

  public String getОфис() {
    return офис;
  }

  public void setОфис(String офис) {
    this.офис = офис;
  }

}

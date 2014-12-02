package net.mobidom.bp.beans;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "физическоеЛицо", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "ФизическоеЛицо")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ФизическоеЛицо implements Serializable {
  private static final long serialVersionUID = 1923652539540370451L;

  String идентификатор;
  ФИО фио;
  Пол пол;
  Date датаРождения;
  String местоРождения;

  Адрес адрес;

  Телефон телефон;

  public ФИО getФио() {
    return фио;
  }

  public void setФио(ФИО фио) {
    this.фио = фио;
  }

  public Пол getПол() {
    return пол;
  }

  public void setПол(Пол пол) {
    this.пол = пол;
  }

  public Date getДатаРождения() {
    return датаРождения;
  }

  public void setДатаРождения(Date датаРождения) {
    this.датаРождения = датаРождения;
  }

  public String getМестоРождения() {
    return местоРождения;
  }

  public void setМестоРождения(String местоРождения) {
    this.местоРождения = местоРождения;
  }

  public Адрес getАдрес() {
    return адрес;
  }

  public void setАдрес(Адрес адрес) {
    this.адрес = адрес;
  }

  public String getИдентификатор() {
    return идентификатор;
  }

  public void setИдентификатор(String идентификатор) {
    this.идентификатор = идентификатор;
  }

  public Телефон getТелефон() {
    return телефон;
  }

  public void setТелефон(Телефон телефон) {
    this.телефон = телефон;
  }

}

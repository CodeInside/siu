package net.mobidom.bp.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "юридическоеЛицо", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "ЮридическоеЛицо")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ЮридическоеЛицо implements Serializable {
  private static final long serialVersionUID = 9197736308141012302L;

  String идентификатор;
  String название;
  String полноеНазвание;

  Руководитель руководитель;
  ГлавныйБухгалтер главныйБухгалтер;

  Адрес юридическийАдрес;
  Адрес почтовыйАдрес;

  Телефон телефон;

  public String getИдентификатор() {
    return идентификатор;
  }

  public void setИдентификатор(String идентификатор) {
    this.идентификатор = идентификатор;
  }

  public String getНазвание() {
    return название;
  }

  public void setНазвание(String название) {
    this.название = название;
  }

  public String getПолноеНазвание() {
    return полноеНазвание;
  }

  public void setПолноеНазвание(String полноеНазвание) {
    this.полноеНазвание = полноеНазвание;
  }

  public Руководитель getРуководитель() {
    return руководитель;
  }

  public void setРуководитель(Руководитель руководитель) {
    this.руководитель = руководитель;
  }

  public ГлавныйБухгалтер getГлавныйБухгалтер() {
    return главныйБухгалтер;
  }

  public void setГлавныйБухгалтер(ГлавныйБухгалтер главныйБухгалтер) {
    this.главныйБухгалтер = главныйБухгалтер;
  }

  public Адрес getЮридическийАдрес() {
    return юридическийАдрес;
  }

  public void setЮридическийАдрес(Адрес юридическийАдрес) {
    this.юридическийАдрес = юридическийАдрес;
  }

  public Адрес getПочтовыйАдрес() {
    return почтовыйАдрес;
  }

  public void setПочтовыйАдрес(Адрес почтовыйАдрес) {
    this.почтовыйАдрес = почтовыйАдрес;
  }

  public Телефон getТелефон() {
    return телефон;
  }

  public void setТелефон(Телефон телефон) {
    this.телефон = телефон;
  }

}

package net.mobidom.bp.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "обращение", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "Обращение")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Обращение implements Serializable {
  private static final long serialVersionUID = 5376336745512016698L;

  String идентификатор;
  String номер;
  String услуга;
  String ОГВ;

  ФизическоеЛицо физическоеЛицо;

  ЮридическоеЛицо юридическоеЛицо;
  Представитель представитель;

  Date датаПриема;
  Date планируемойВыдачиРезультата;

  List<СсылкаНаДокумент> ссылкиНаДокументы;
  List<Документ> документы;

  @XmlElementWrapper(name = "документы")
  @XmlElement(name = "документ")
  public List<Документ> getДокументы() {
    return документы;
  }

  public void setДокументы(List<Документ> документы) {
    this.документы = документы;
  }

  @XmlElementWrapper(name = "ссылкиНаДокументы")
  @XmlElements({ 
      @XmlElement(name = "инн", type = ИНН.class),
      @XmlElement(name = "удостоверениеЛичности", type = СсылкаНаУдостоверениеЛичности.class), 
      @XmlElement(name = "снилс", type = СНИЛС.class) })
  public List<СсылкаНаДокумент> getСсылкиНаДокументы() {
    return ссылкиНаДокументы;
  }

  public void setСсылкиНаДокументы(List<СсылкаНаДокумент> ссылкиНаДокументы) {
    this.ссылкиНаДокументы = ссылкиНаДокументы;
  }

  public String getИдентификатор() {
    return идентификатор;
  }

  public void setИдентификатор(String идентификатор) {
    this.идентификатор = идентификатор;
  }

  public String getНомер() {
    return номер;
  }

  public void setНомер(String номер) {
    this.номер = номер;
  }

  public String getУслуга() {
    return услуга;
  }

  public void setУслуга(String услуга) {
    this.услуга = услуга;
  }

  public String getОГВ() {
    return ОГВ;
  }

  public void setОГВ(String огв) {
    ОГВ = огв;
  }

  public ФизическоеЛицо getФизическоеЛицо() {
    return физическоеЛицо;
  }

  public void setФизическоеЛицо(ФизическоеЛицо физЛицо) {
    this.физическоеЛицо = физЛицо;
  }

  public ЮридическоеЛицо getЮридическоеЛицо() {
    return юридическоеЛицо;
  }

  public void setЮридическоеЛицо(ЮридическоеЛицо юрЛицо) {
    this.юридическоеЛицо = юрЛицо;
  }

  public Представитель getПредставитель() {
    return представитель;
  }

  public void setПредставитель(Представитель представитель) {
    this.представитель = представитель;
  }

  public Date getДатаПриема() {
    return датаПриема;
  }

  public void setДатаПриема(Date датаПриема) {
    this.датаПриема = датаПриема;
  }

  public Date getПланируемойВыдачиРезультата() {
    return планируемойВыдачиРезультата;
  }

  public void setПланируемойВыдачиРезультата(Date планируемойВыдачиРезультата) {
    this.планируемойВыдачиРезультата = планируемойВыдачиРезультата;
  }

}

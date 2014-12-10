package net.mobidom.bp.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import net.mobidom.bp.beans.types.ТипСсылкиНаДокумент;
import net.mobidom.bp.beans.types.ТипУдостоверенияЛичности;

@XmlRootElement(name = "ссылкаНаУдостоверениеЛичности", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "СсылкаНаУдостоверениеЛичности")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class СсылкаНаУдостоверениеЛичности extends СсылкаНаДокумент {

  private static final long serialVersionUID = 1899459799941109372L;

  String серия;
  String номер;
  Date датаВыдачи;
  Date датаОкончанияСрокаДействия;
  String местоВыдачи;
  ТипУдостоверенияЛичности тип;

  public String getМестоВыдачи() {
    return местоВыдачи;
  }

  public void setМестоВыдачи(String issuer) {
    this.местоВыдачи = issuer;
  }

  public ТипУдостоверенияЛичности getТип() {
    return тип;
  }

  public void setТип(ТипУдостоверенияЛичности тип) {
    this.тип = тип;
  }

  @Override
  public String getLabelString() {
    return String.format("Паспорт: %s %s\nВыдан: %s", серия, номер, new SimpleDateFormat("dd.MM.yy").format(датаВыдачи));
  }

  public String getСерия() {
    return серия;
  }

  public void setСерия(String серия) {
    this.серия = серия;
  }

  public String getНомер() {
    return номер;
  }

  public void setНомер(String номер) {
    this.номер = номер;
  }

  public Date getДатаВыдачи() {
    return датаВыдачи;
  }

  public void setДатаВыдачи(Date датаВыдачи) {
    this.датаВыдачи = датаВыдачи;
  }

  public Date getДатаОкончанияСрокаДействия() {
    return датаОкончанияСрокаДействия;
  }

  public void setДатаОкончанияСрокаДействия(Date датаОкончанияСрокаДействия) {
    this.датаОкончанияСрокаДействия = датаОкончанияСрокаДействия;
  }

  @Override
  public ТипСсылкиНаДокумент getТипСсылкиНаДокумент() {
    return ТипСсылкиНаДокумент.УДОСТОВЕРЕНИЕ_ЛИЧНОСТИ;
  }

  @Override
  public Map<String, Serializable> getDocumentRequestParams() {
    Map<String, Serializable> params = new HashMap<String, Serializable>();
    params.put("тип", getТип());
    params.put("номер", getНомер());
    params.put("серия", getСерия());
    params.put("место_выдачи", getМестоВыдачи());
    params.put("дата_выдачи", getДатаВыдачи());
    params.put("дата_окончания_действия", getДатаОкончанияСрокаДействия());
    return params;
  }

}

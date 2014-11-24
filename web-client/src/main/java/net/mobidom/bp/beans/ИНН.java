package net.mobidom.bp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import net.mobidom.bp.beans.types.ТипСсылкиНаДокумент;

@XmlRootElement(name = "инн", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "ИНН")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ИНН extends СсылкаНаДокумент {

  private static final long serialVersionUID = -3756250081366622602L;

  String номер;

  public String getНомер() {
    return номер;
  }

  public void setНомер(String номер) {
    this.номер = номер;
  }

  @Override
  public String getLabelString() {
    return String.format("ИНН: %s", номер);
  }

  @Override
  public ТипСсылкиНаДокумент getТипСсылкиНаДокумент() {
    return ТипСсылкиНаДокумент.ИНН;
  }

}

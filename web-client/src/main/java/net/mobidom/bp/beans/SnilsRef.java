package net.mobidom.bp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://www.mobidom.net/", name = "SnilsRefType")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SnilsRef extends DocumentRef {

  private static final long serialVersionUID = 4358706063204231072L;

  String number;

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  @Override
  public String getLabelString() {
    return String.format("СНИЛС: %s", number);
  }

}

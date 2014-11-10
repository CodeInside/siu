package net.mobidom.bp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://www.mobidom.net/", name = "InnRefType")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class InnRef extends DocumentRef {

  private static final long serialVersionUID = -3756250081366622602L;

  String number;

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  @Override
  public String getLabelString() {
    return String.format("ИНН: %s", number);
  }
  
  @Override
  public DocumentType getDocumentType() {
    return DocumentType.INN;
  }

}

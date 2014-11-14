package net.mobidom.bp.beans;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://www.mobidom.net/", name = "IDCardRefType", propOrder = { "serial", "number", "issueDate", "issuer", "type" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class IDCardRef extends DocumentRef {

  private static final long serialVersionUID = 1899459799941109372L;

  String serial;
  String number;
  Date issueDate;
  String issuer;

  IDCardType type;

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public IDCardType getType() {
    return type;
  }

  public void setType(IDCardType type) {
    this.type = type;
  }

  @Override
  public String getLabelString() {
    return String.format("Паспорт: %s %s\nВыдан: %s", serial, number, issueDate);
  }

  public String getSerial() {
    return serial;
  }

  public void setSerial(String serial) {
    this.serial = serial;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public Date getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(Date issueDate) {
    this.issueDate = issueDate;
  }

  @Override
  public DocumentType getDocumentType() {
    return DocumentType.ПАСПОРТ;
  }

}

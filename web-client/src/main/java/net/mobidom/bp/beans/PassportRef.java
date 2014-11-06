package net.mobidom.bp.beans;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://www.mobidom.net/", name = "PassportRefType", propOrder = { "serial", "number", "issueDate" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PassportRef extends DocumentRef {
  
  private static final long serialVersionUID = 1899459799941109372L;

  String serial;
  String number;
  Date issueDate;

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

}

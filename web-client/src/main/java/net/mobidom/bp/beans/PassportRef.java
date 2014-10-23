package net.mobidom.bp.beans;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "passport", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "PassportRefType", propOrder = { "serial", "number", "issueDate" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PassportRef implements Serializable {
  private static final long serialVersionUID = 1899459799941109372L;

  String serial;
  String number;
  Date issueDate;

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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((issueDate == null) ? 0 : issueDate.hashCode());
    result = prime * result + ((number == null) ? 0 : number.hashCode());
    result = prime * result + ((serial == null) ? 0 : serial.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PassportRef other = (PassportRef) obj;
    if (issueDate == null) {
      if (other.issueDate != null)
        return false;
    } else if (!issueDate.equals(other.issueDate))
      return false;
    if (number == null) {
      if (other.number != null)
        return false;
    } else if (!number.equals(other.number))
      return false;
    if (serial == null) {
      if (other.serial != null)
        return false;
    } else if (!serial.equals(other.serial))
      return false;
    return true;
  }

}

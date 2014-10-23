package net.mobidom.bp.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "document-refs", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "DocumentRefsType", propOrder = { "snils", "inn", "passport" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DocumentRefs implements Serializable {
  private static final long serialVersionUID = 88628238564800772L;

  String snils;
  String inn;
  PassportRef passport;

  @XmlElement(name = "snils")
  public String getSnils() {
    return snils;
  }

  public void setSnils(String snils) {
    this.snils = snils;
  }

  @XmlElement(name = "inn")
  public String getInn() {
    return inn;
  }

  public void setInn(String inn) {
    this.inn = inn;
  }

  public PassportRef getPassport() {
    return passport;
  }

  public void setPassport(PassportRef passport) {
    this.passport = passport;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((inn == null) ? 0 : inn.hashCode());
    result = prime * result + ((passport == null) ? 0 : passport.hashCode());
    result = prime * result + ((snils == null) ? 0 : snils.hashCode());
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
    DocumentRefs other = (DocumentRefs) obj;
    if (inn == null) {
      if (other.inn != null)
        return false;
    } else if (!inn.equals(other.inn))
      return false;
    if (passport == null) {
      if (other.passport != null)
        return false;
    } else if (!passport.equals(other.passport))
      return false;
    if (snils == null) {
      if (other.snils != null)
        return false;
    } else if (!snils.equals(other.snils))
      return false;
    return true;
  }

}

package net.mobidom.bp.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "sign-data")
@XmlType(name = "SignData")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ПодписьОбращения implements Serializable {

  private static final long serialVersionUID = -7756807022952486894L;

  byte[] signature;
  byte[] certificate;

  private boolean signatureValid;
  private String ownerInfo;

  public ПодписьОбращения() {
  }

  public byte[] getSignature() {
    return signature;
  }

  public void setSignature(byte[] signature) {
    this.signature = signature;
  }

  public byte[] getCertificate() {
    return certificate;
  }

  public void setCertificate(byte[] certificate) {
    this.certificate = certificate;
  }

  @XmlTransient
  public boolean isSignatureValid() {
    return signatureValid;
  }

  public void setSignatureValid(boolean signatureValid) {
    this.signatureValid = signatureValid;
  }

  @XmlTransient
  public String getOwnerInfo() {
    return ownerInfo;
  }

  public void setOwnerInfo(String ownerInfo) {
    this.ownerInfo = ownerInfo;
  }

}

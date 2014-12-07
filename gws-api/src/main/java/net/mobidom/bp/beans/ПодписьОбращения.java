package net.mobidom.bp.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "sign-data")
@XmlType(name = "SignData")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ПодписьОбращения implements Serializable {

  private static final long serialVersionUID = -7756807022952486894L;

  byte[] signature;
  byte[] certificate;

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

  public static void main(String[] args) throws Exception {

//    String path = "D:/work/siu/_test_documents/50.signature";
//
//    JAXBContext jaxbContext = JAXBContext.newInstance(ПодписьОбращения.class);
//    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//    ПодписьОбращения sign = (ПодписьОбращения) unmarshaller.unmarshal(new File(path));
//
//    System.out.println(sign);

  }

}

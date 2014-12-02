package net.mobidom.bp.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

  private final static QName _Request_QNAME = new QName("http://www.mobidom.net/", "request");

  public ObjectFactory() {
  }

  public Обращение createRequest() {
    return new Обращение();
  }

  @XmlElementDecl(name = "обращение", namespace = "http://www.mobidom.net/")
  public JAXBElement<Обращение> createRequest(Обращение value) {
    return new JAXBElement<Обращение>(_Request_QNAME, Обращение.class, null, value);
  }

}

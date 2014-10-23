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

  public Request createRequest() {
    return new Request();
  }

  @XmlElementDecl(name = "request", namespace = "http://www.mobidom.net/")
  public JAXBElement<Request> createRequest(Request value) {
    return new JAXBElement<Request>(_Request_QNAME, Request.class, null, value);
  }

}

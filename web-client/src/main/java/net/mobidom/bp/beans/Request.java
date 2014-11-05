package net.mobidom.bp.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "request", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "RequestType", propOrder = { "createTime", "governmentAgencyName", "serviceName",
    "declarer", "documentRefs", "documents" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Request implements Serializable {
  private static final long serialVersionUID = 5376336745512016698L;

  Date createTime;
  String governmentAgencyName;
  String serviceName;

  Declarer declarer;

  List<DocumentRef> documentRefs;

  List<Document> documents;

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getGovernmentAgencyName() {
    return governmentAgencyName;
  }

  public void setGovernmentAgencyName(String governmentAgencyName) {
    this.governmentAgencyName = governmentAgencyName;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public Declarer getDeclarer() {
    return declarer;
  }

  public void setDeclarer(Declarer declarer) {
    this.declarer = declarer;
  }

  @XmlElementWrapper(name = "documents")
  @XmlElement(name = "document")
  public List<Document> getDocuments() {
    return documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }

  @XmlElementWrapper(name = "document-refs")
  @XmlElements({ 
            @XmlElement(name = "inn-ref", type = InnRef.class), 
            @XmlElement(name = "passport-ref", type = PassportRef.class),
            @XmlElement(name = "snils-ref", type = SnilsRef.class)})
  public List<DocumentRef> getDocumentRefs() {
    return documentRefs;
  }

  public void setDocumentRefs(List<DocumentRef> documentRefs) {
    this.documentRefs = documentRefs;
  }

}

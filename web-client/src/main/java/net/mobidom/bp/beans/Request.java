package net.mobidom.bp.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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

  DocumentRefs documentRefs;

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

  @XmlElement(name = "document-refs")
  public DocumentRefs getDocumentRefs() {
    return documentRefs;
  }

  public void setDocumentRefs(DocumentRefs documentRefs) {
    this.documentRefs = documentRefs;
  }

  @XmlElementWrapper(name = "documents")
  @XmlElement(name = "document")
  public List<Document> getDocuments() {
    return documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
    result = prime * result + ((declarer == null) ? 0 : declarer.hashCode());
    result = prime * result + ((documentRefs == null) ? 0 : documentRefs.hashCode());
    result = prime * result + ((documents == null) ? 0 : documents.hashCode());
    result = prime * result + ((governmentAgencyName == null) ? 0 : governmentAgencyName.hashCode());
    result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
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
    Request other = (Request) obj;
    if (createTime == null) {
      if (other.createTime != null)
        return false;
    } else if (!createTime.equals(other.createTime))
      return false;
    if (declarer == null) {
      if (other.declarer != null)
        return false;
    } else if (!declarer.equals(other.declarer))
      return false;
    if (documentRefs == null) {
      if (other.documentRefs != null)
        return false;
    } else if (!documentRefs.equals(other.documentRefs))
      return false;
    if (documents == null) {
      if (other.documents != null)
        return false;
    } else if (!documents.equals(other.documents))
      return false;
    if (governmentAgencyName == null) {
      if (other.governmentAgencyName != null)
        return false;
    } else if (!governmentAgencyName.equals(other.governmentAgencyName))
      return false;
    if (serviceName == null) {
      if (other.serviceName != null)
        return false;
    } else if (!serviceName.equals(other.serviceName))
      return false;
    return true;
  }

}

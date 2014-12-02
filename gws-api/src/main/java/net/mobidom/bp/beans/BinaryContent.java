package net.mobidom.bp.beans;

import java.io.Serializable;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "binaryContent", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "BinaryContentType", propOrder = { "mimeType", "binaryData" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class BinaryContent implements Serializable {
  private static final long serialVersionUID = -1301569714157984694L;

  String mimeType;
  byte[] binaryData;

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public byte[] getBinaryData() {
    return binaryData;
  }

  public void setBinaryData(byte[] binaryData) {
    this.binaryData = binaryData;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(binaryData);
    result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
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
    BinaryContent other = (BinaryContent) obj;
    if (!Arrays.equals(binaryData, other.binaryData))
      return false;
    if (mimeType == null) {
      if (other.mimeType != null)
        return false;
    } else if (!mimeType.equals(other.mimeType))
      return false;
    return true;
  }

}

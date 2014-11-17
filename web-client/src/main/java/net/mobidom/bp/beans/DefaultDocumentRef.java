package net.mobidom.bp.beans;

import net.mobidom.bp.beans.types.DocumentRefType;

public class DefaultDocumentRef extends DocumentRef {

  private static final long serialVersionUID = -3129034744061565395L;

  String label;

  public DefaultDocumentRef(String label) {
    this.label = label;
  }

  public void setLabelString(String label) {
    this.label = label;
  }

  @Override
  public String getLabelString() {
    return label;
  }
  
  @Override
  public DocumentRefType getDocumentRefType() {
    return DocumentRefType.DEFAULT;
  }

}

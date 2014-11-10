package net.mobidom.bp.beans;

public class DefaultDocumentRef extends DocumentRef {

  private static final long serialVersionUID = -3129034744061565395L;

  String label;

  public DefaultDocumentRef() {
  }

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
  public DocumentType getDocumentType() {
    return DocumentType.UNKNOWN;
  }

}

package net.mobidom.bp.beans;

import java.io.Serializable;

public abstract class DocumentRef implements Serializable {

  private static final long serialVersionUID = -5009645188422247844L;

  boolean needToLoad;

  Document document;

  public abstract String getLabelString();

  public Document getDocument() {
    return document;
  }

  public void setDocument(Document document) {
    this.document = document;
  }

  public boolean isNeedToLoad() {
    return needToLoad;
  }

  public void setNeedToLoad(boolean needToLoad) {
    this.needToLoad = needToLoad;
  }

}

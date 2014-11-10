package net.mobidom.bp.beans.request;

import java.io.Serializable;

import net.mobidom.bp.beans.Document;

/**
 * Запрос документа.
 * 
 * @author dmitryermolaev
 *
 */
public class DocumentRequest implements Serializable {
  private static final long serialVersionUID = -3656900864737770127L;

  String type;

  String customData;

  DocumentRequestParams params;

  boolean isReady;

  Document document;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCustomData() {
    return customData;
  }

  public void setCustomData(String customData) {
    this.customData = customData;
  }

  public DocumentRequestParams getParams() {
    return params;
  }

  public void setParams(DocumentRequestParams params) {
    this.params = params;
  }

  public boolean isReady() {
    return isReady;
  }

  public void setReady(boolean isReady) {
    this.isReady = isReady;
  }

  public Document getDocument() {
    return document;
  }

  public void setDocument(Document document) {
    this.document = document;
  }

}

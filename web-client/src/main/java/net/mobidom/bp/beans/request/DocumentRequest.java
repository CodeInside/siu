package net.mobidom.bp.beans.request;

import java.io.Serializable;

import net.mobidom.bp.beans.Document;
import net.mobidom.bp.beans.DocumentType;

/**
 * Запрос документа.
 * 
 * @author dmitryermolaev
 *
 */
public class DocumentRequest implements Serializable {
  private static final long serialVersionUID = -3656900864737770127L;

  DocumentType type;

  String customData;

  String label;

  DocumentRequestParams params;

  boolean isReady;

  Document document;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public DocumentType getType() {
    return type;
  }

  public void setType(DocumentType type) {
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

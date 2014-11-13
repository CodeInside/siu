package net.mobidom.bp.beans.request;

import java.io.Serializable;

import net.mobidom.bp.beans.Document;
import net.mobidom.bp.beans.DocumentRef;
import net.mobidom.bp.beans.DocumentType;

/**
 * Запрос документа.
 * 
 * @author dmitryermolaev
 *
 */
public class DocumentRequest implements Serializable {
  private static final long serialVersionUID = -3656900864737770127L;

  private DocumentType type;

  private DocumentRef docRef;

  private String label;

  private boolean isReady;

  private Object fault;

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

  public boolean isReady() {
    return isReady;
  }

  public void setReady(boolean isReady) {
    this.isReady = isReady;
  }

  public DocumentRef getDocRef() {
    return docRef;
  }

  public void setDocRef(DocumentRef docRef) {
    this.docRef = docRef;
  }

  public Object getFault() {
    return fault;
  }

  public void setFault(Object fault) {
    this.fault = fault;
  }

}

package net.mobidom.bp.beans.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.mobidom.bp.beans.СсылкаНаДокумент;
import net.mobidom.bp.beans.types.ТипДокумента;

/**
 * Запрос документа.
 * 
 * @author dmitryermolaev
 *
 */
public class DocumentRequest implements Serializable {

  private static final long serialVersionUID = -3656900864737770127L;

  private Map<String, Object> requestParams = new HashMap<String, Object>();

  private ТипДокумента type;

  private СсылкаНаДокумент docRef;

  private String label;

  private boolean isReady;

  private Object fault;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public ТипДокумента getType() {
    return type;
  }

  public void setType(ТипДокумента type) {
    this.type = type;
  }

  public boolean isReady() {
    return isReady;
  }

  public void setReady(boolean isReady) {
    this.isReady = isReady;
  }

  public СсылкаНаДокумент getDocRef() {
    return docRef;
  }

  public void setDocRef(СсылкаНаДокумент docRef) {
    this.docRef = docRef;
  }

  public Object getFault() {
    return fault;
  }

  public void setFault(Object fault) {
    this.fault = fault;
  }

  public void addRequestParam(String key, Object value) {
    requestParams.put(key, value);
  }

  public Object getRequestParam(String key) {
    return requestParams.get(key);
  }

  public Map<String, Object> getRequestParams() {
    return requestParams;
  }

  public void setRequestParams(Map<String, Object> requestParams) {
    this.requestParams = requestParams;
  }

}

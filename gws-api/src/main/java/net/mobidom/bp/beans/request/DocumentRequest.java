package net.mobidom.bp.beans.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.mobidom.bp.beans.Документ;
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

  private DocumentRequestType requestType;
  private ResponseType responseType;

  private Map<String, Object> requestParams = new HashMap<String, Object>();

  /**
   * Идентификатор запроса, назначаемый сервисом для последующих обращений
   */
  private String requestId;

  private ТипДокумента type;

  private СсылкаНаДокумент docRef;

  private String label;

  private boolean isReady;

  private Object fault;

  private Документ документ;

  private String testMessage;

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

  public DocumentRequestType getRequestType() {
    return requestType;
  }

  public void setRequestType(DocumentRequestType requestType) {
    this.requestType = requestType;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public void setRequestParameter(String name, Object value) {
    requestParams.put(name, value);
  }

  public Object getRequestParameter(String name) {
    return requestParams.get(name);
  }

  public String getTestMessage() {
    return testMessage;
  }

  public void setTestMessage(String testMessage) {
    this.testMessage = testMessage;
  }

  public Документ getДокумент() {
    return документ;
  }

  public void setДокумент(Документ документ) {
    this.документ = документ;
  }

  public ResponseType getResponseType() {
    return responseType;
  }

  public void setResponseType(ResponseType responseType) {
    this.responseType = responseType;
  }

}

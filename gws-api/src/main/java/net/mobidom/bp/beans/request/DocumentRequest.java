package net.mobidom.bp.beans.request;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

  private Map<String, Serializable> requestParams = new HashMap<String, Serializable>();

  /**
   * Идентификатор запроса, назначаемый сервисом для последующих обращений
   */
  private String requestId;

  private ТипДокумента type;

  private СсылкаНаДокумент docRef;

  private String label;

  private boolean isReady;

  private Serializable fault;

  private Документ документ;

  private String testMessage;

  private Date createDate;

  private Date completeDate;

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

  public Serializable getFault() {
    return fault;
  }

  public void setFault(Serializable fault) {
    this.fault = fault;
  }

  public void addRequestParam(String key, Serializable value) {
    requestParams.put(key, value);
  }

  public Object getRequestParam(String key) {
    return requestParams.get(key);
  }

  public Map<String, Serializable> getRequestParams() {
    return requestParams;
  }

  public void setRequestParams(Map<String, Serializable> requestParams) {
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

  public void setRequestParameter(String name, Serializable value) {
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

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getCompleteDate() {
    return completeDate;
  }

  public void setCompleteDate(Date completeDate) {
    this.completeDate = completeDate;
  }

  static SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");

  public String requestParamsToLabel() {

    StringBuilder sb = new StringBuilder();
    sb.append(getLabel());

    if (requestParams.size() > 0) {
      sb.append(": ");
      int i = 0;
      for (Entry<String, Serializable> en : requestParams.entrySet()) {
        if (5 <= i) {
          break;
        }

        String value = null;
        if (en.getValue() instanceof Date) {
          value = SDF.format((Date) en.getValue());
        } else {
          value = String.valueOf(en.getValue());
        }

        sb.append(value).append(" ");
        i++;
      }
    }

    return sb.toString();
  }

}

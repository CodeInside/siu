package net.mobidom.bp.beans.request;

import java.io.Serializable;

/**
 * Параметры для запроса документа.
 * 
 * @author dmitryermolaev
 *
 */
public class DocumentRequestParams implements Serializable {
  private static final long serialVersionUID = -1064188574310239378L;

  String number;
  String reason;

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

}

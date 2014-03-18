/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "soap_packet")
@SequenceGenerator(name = "soap_packet_seq", sequenceName = "soap_packet_seq")
public class SoapPacket implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "soap_packet_seq")
  private Long id;
  private String sender;
  private String recipient;
  private String originator;
  private String service;
  private String typeCode;
  private String status;
  @Temporal(TemporalType.TIMESTAMP)
  private Date date;
  private String requestIdRef;
  private String originRequestIdRef;
  private String serviceCode;
  private String caseNumber;
  private String exchangeType;

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public String getOriginator() {
    return originator;
  }

  public void setOriginator(String originator) {
    this.originator = originator;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getTypeCode() {
    return typeCode;
  }

  public void setTypeCode(String typeCode) {
    this.typeCode = typeCode;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getRequestIdRef() {
    return requestIdRef;
  }

  public void setRequestIdRef(String requestIdRef) {
    this.requestIdRef = requestIdRef;
  }

  public String getOriginRequestIdRef() {
    return originRequestIdRef;
  }

  public void setOriginRequestIdRef(String originRequestIdRef) {
    this.originRequestIdRef = originRequestIdRef;
  }

  public String getServiceCode() {
    return serviceCode;
  }

  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  public String getCaseNumber() {
    return caseNumber;
  }

  public void setCaseNumber(String caseNumber) {
    this.caseNumber = caseNumber;
  }

  public String getExchangeType() {
    return exchangeType;
  }

  public void setExchangeType(String exchangeType) {
    this.exchangeType = exchangeType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}

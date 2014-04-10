/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import org.eclipse.persistence.annotations.Index;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "smev_log")
@SequenceGenerator(name = "oep_log_seq", sequenceName = "oep_log_seq")
public class SmevLog {

  //скоректировать длину полей

  @Id
  @GeneratedValue(generator = "oep_log_seq")
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  private Date date;                  // Дата записи в базу
  private Long bidId;               // Номер заявки

  @Index(name = "marker_idx")
  private String marker;               // Маркер

  private String infoSystem;          // Информационная система
  private boolean client;             // Тип запроса (клиент / поставщик)

  @Temporal(TemporalType.TIMESTAMP)
  private Date logDate;             // Время первого запроса

  @Column(columnDefinition = "text")
  private String error;             // стек трейс ошибки

  @JoinColumn(name = "send_packet")
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private SoapPacket sendPacket;

  @JoinColumn(name = "receive_packet")
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private SoapPacket receivePacket;

  @JoinColumn(name = "send_http")
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private HttpLog sendHttp;

  @JoinColumn(name = "receive_http")
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private HttpLog receiveHttp;

  private String component;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Long getBidId() {
    return bidId;
  }

  public void setBidId(Long bidId) {
    this.bidId = bidId;
  }

  public String getInfoSystem() {
    return infoSystem;
  }

  public void setInfoSystem(String informationSystem) {
    this.infoSystem = informationSystem;
  }

  public boolean isClient() {
    return client;
  }

  public void setClient(boolean client) {
    this.client = client;
  }

  public SoapPacket getSendPacket() {
    return sendPacket;
  }

  public void setSendPacket(SoapPacket sendPacket) {
    this.sendPacket = sendPacket;
  }

  public SoapPacket getReceivePacket() {
    return receivePacket;
  }

  public void setReceivePacket(SoapPacket receivePacket) {
    this.receivePacket = receivePacket;
  }

  public HttpLog getSendHttp() {
    return sendHttp;
  }

  public void setSendHttp(HttpLog sendSoap) {
    this.sendHttp = sendSoap;
  }

  public HttpLog getReceiveHttp() {
    return receiveHttp;
  }

  public void setReceiveHttp(HttpLog receiveSoap) {
    this.receiveHttp = receiveSoap;
  }

  public String getMarker() {
    return marker;
  }

  public void setMarker(String marker) {
    this.marker = marker;
  }

  public void setLogDate(Date logDate) {
    this.logDate = logDate;
  }

  public Date getLogDate() {
    return logDate;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }
}
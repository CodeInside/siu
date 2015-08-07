/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import org.eclipse.persistence.annotations.Index;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.log.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.namespace.QName;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@EntityListeners(Logger.class)
@Table(name = "service_response")
@SequenceGenerator(name = "service_response_seq", sequenceName = "service_response_seq")
@Index(name = "service_response_bidid_idx", unique = false, columnNames = "bidid")
public class ServiceResponseEntity {

  @Column(nullable = false)
  public String name;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "bidId", nullable = false)
  Bid bid;

  public String actionNs;
  public String action;
  @Column(length = 1024 * 1024)
  public String appData;
  // TODO: нужно более рациональный тип
  @Column(nullable = false)
  public String gservice;
  // TODO: нужно более рациональный тип
  @Column(nullable = false)
  public String status;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  public Date date;
  @Column(nullable = false)
  public String exchangeType;
  public String requestIdRef;
  public String originRequestIdRef;
  public String serviceCode;
  public String caseNumber;
  public String testMsg;
  public String docRequestCode;
  @Column(name = "requestmessage")
  public byte[] responseMessage;

  @Id
  @GeneratedValue(generator = "service_response_seq")
  private Long id;
  @OneToMany(mappedBy = "response", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private Set<EnclosureEntity> enclosures;

  /**
   * Для JPA движка
   */
  protected ServiceResponseEntity() {

  }

  public ServiceResponseEntity(Bid bid, ServerResponse serverResponse) {
    this.bid = bid;

    appData = serverResponse.appData;

    if (serverResponse.action != null) {
      action = serverResponse.action.getLocalPart();
      actionNs = serverResponse.action.getNamespaceURI();
    }

    docRequestCode = serverResponse.docRequestCode;
    responseMessage = serverResponse.responseMessage;

    processPacket(serverResponse.packet);
    name = "";
  }

  public Long getId() {
    return id;
  }

  public ServerResponse getServerResponse() {
    ServerResponse response = new ServerResponse();
    response.packet = new Packet();
    response.packet.serviceCode = serviceCode;
    response.appData = appData;
    response.packet.caseNumber = caseNumber;
    response.packet.typeCode = Packet.Type.valueOf(gservice);
    response.packet.status = Packet.Status.valueOf(status);
    response.packet.exchangeType = exchangeType;
    response.packet.requestIdRef = requestIdRef;
    response.packet.date = date;
    response.packet.originRequestIdRef = originRequestIdRef;
    response.packet.testMsg = testMsg;
    response.docRequestCode = docRequestCode;
    response.responseMessage = responseMessage;
    if (action != null) {
      response.action = new QName(actionNs, action);
    }
    return response;
  }

  public Set<EnclosureEntity> getEnclosures() {
    if (enclosures == null) {
      enclosures = new LinkedHashSet<EnclosureEntity>();
    }
    return enclosures;
  }

  public Bid getBid() {
    return bid;
  }

  private void processPacket(Packet packet) {
    gservice = packet.typeCode.name();
    status = packet.status.name();
    date = packet.date != null ? packet.date : new Date();
    exchangeType = packet.exchangeType;
    requestIdRef = packet.requestIdRef;
    originRequestIdRef = packet.originRequestIdRef;
    serviceCode = packet.serviceCode;
    caseNumber = packet.caseNumber;
    testMsg = packet.testMsg;
  }
}
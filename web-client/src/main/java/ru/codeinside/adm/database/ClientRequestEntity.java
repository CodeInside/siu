/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import ru.codeinside.log.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@EntityListeners(Logger.class)
@Table(name = "crequest")
@SequenceGenerator(name = "seq", sequenceName = "crequest_seq", allocationSize = 1)
public class ClientRequestEntity {

  @Id
  @GeneratedValue(generator = "seq")
  private Long id;

  public Long getId() {
    return id;
  }

  @Column(nullable = false)
  public String name;

  public String actionNs;
  public String action;

  public String serviceNs;
  public String service;

  public String portNs;
  public String port;

  @Column(name = "portaddress")
  public String portAddress;

  @Column(name = "requestmessage")
  public byte[] requestMessage;

  @Column(length = 1024 * 1024)
  public String appData;

  public boolean signRequired;
  @Column
  public String digest;

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

  @Column(name = "enclosure_descriptor")
  public String enclosureDescriptor;

}

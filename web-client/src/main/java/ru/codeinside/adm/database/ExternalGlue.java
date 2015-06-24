/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ExternalGlue {

  /**
   * Идентификация по Bid.id.
   */
  @Id
  Long id;

  /**
   * Имя компонента, обрабатыывающего запросы.
   */
  @Column(nullable = false)
  String name;

  /**
   * Идентификатор цепочки запросов.
   */
  @Column(unique = true, nullable = false)
  String requestIdRef;

  /**
   * Система-инициатор.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  InfoSystem origin;

  /**
   * Система-отправитель.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  InfoSystem sender;

  /**
   * Система-получатель.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  InfoSystem recipient;

  /**
   * Множество заявок на запрос.
   */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "glue")
  private Set<Bid> bids = new HashSet<Bid>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRequestIdRef() {
    return requestIdRef;
  }

  public void setRequestIdRef(String requestIdRef) {
    this.requestIdRef = requestIdRef;
  }

  public Set<Bid> getBids() {
    return bids;
  }

  public InfoSystem getOrigin() {
    return origin;
  }

  public void setOrigin(InfoSystem origin) {
    this.origin = origin;
  }

  public InfoSystem getSender() {
    return sender;
  }

  public void setSender(InfoSystem sender) {
    this.sender = sender;
  }

  public InfoSystem getRecipient() {
    return recipient;
  }

  public void setRecipient(InfoSystem recipient) {
    this.recipient = recipient;
  }
}

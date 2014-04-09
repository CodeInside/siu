/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ExternalGlue {

  /**
   * Идентификация по Bid.id.
   */
  @Id
  private Long id;

  /**
   * Имя компонента, обрабатыывающего запросы.
   */
  @Column(nullable = false)
  private String name;

  /**
   * Идентификатор цепочки запросов.
   */
  @Column(unique = true, nullable = false)
  private String requestIdRef;


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
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "task_dates")
public class TaskDates {

  @Id
  private String id;

  @ManyToOne
  private Bid bid;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "rest_date")
  private Date restDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "max_date")
  private Date maxDate;

  @Column(name = "inaction_days")
  private Integer inactionDays;

  public Date getRestDate() {
    return restDate;
  }

  public void setRestDate(Date restDate) {
    this.restDate = restDate;
  }

  public Date getMaxDate() {
    return maxDate;
  }

  public void setMaxDate(Date maxDate) {
    this.maxDate = maxDate;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Bid getBid() {
    return bid;
  }

  public void setBid(Bid bid) {
    this.bid = bid;
  }

  public Integer getInactionDays() {
    return inactionDays;
  }

  public void setInactionDays(Integer inactionDays) {
    this.inactionDays = inactionDays;
  }
}

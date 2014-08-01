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

  /**
   * Момент создания этапа.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "start_date", updatable = false)
  private Date startDate;

  /**
   * Момент, ДО которого разрешено бездействие по этапу.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "inaction_date")
  private Date inactionDate;

  /**
   * Момент окончания по плану.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "max_date")
  private Date maxDate;

  /**
   * Момент назначения этапа на исполнителя.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "assign_date")
  private Date assignDate;


  /**
   * Момент, НАЧИНАЯ с которого необходимо рассылать уведомления о приближении к сроку завершения.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "rest_date")
  private Date restDate;


  @Column(name = "worked_days", nullable = false, updatable = false)
  private boolean workedDays = false;

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

  public Date getInactionDate() {
    return inactionDate;
  }

  public void setInactionDate(Date inactionDays) {
    this.inactionDate = inactionDays;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getAssignDate() {
    return assignDate;
  }

  public void setAssignDate(Date assignDate) {
    this.assignDate = assignDate;
  }

  public boolean getWorkedDays() {
    return workedDays;
  }

  public void setWorkedDays(boolean workedDays) {
    this.workedDays = workedDays;
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Дата из производственного календаря
 */
@Entity
@Table(name = "business_calendar")
@NamedQueries({
  @NamedQuery(name = "future_days", query = "SELECT b FROM BusinessCalendarDate b where b.date >= :dt"),
  @NamedQuery(name = "all", query = "SELECT b FROM BusinessCalendarDate b")})
public class BusinessCalendarDate {

  @Id
  @Column(name = "business_day_date", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date date;

  @Column(name = "is_worked_day", nullable = false)
  private boolean workedDay;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public boolean getWorkedDay() {
    return workedDay;
  }

  public void setWorkedDay(boolean workedDay) {
    this.workedDay = workedDay;
  }
}

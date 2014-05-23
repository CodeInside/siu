/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import java.util.Date;

/**
 * Дата из производтственного календаря
 */
public class BusinessCalendarDate {
  private Date date;
  private Boolean isWorkedDay;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Boolean getWorkedDay() {
    return isWorkedDay;
  }

  public void setWorkedDay(Boolean workedDay) {
    isWorkedDay = workedDay;
  }
}

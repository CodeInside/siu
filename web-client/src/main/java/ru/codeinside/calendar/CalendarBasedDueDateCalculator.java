/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.calendar;

import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

/**
 * Рассчет даты окончания периода, длительность которого измеряется в каледарных днях
 */
public class CalendarBasedDueDateCalculator extends DueDateCalculator {
  /**
   * Рассчет даты окончания периода
   * @param startDate  дата начала периода
   * @param countDays  длительность периода в календарных днях. Значение данного параметра должно быть больше или равно 0
   * @return дату окончания периода
   */
  @Override
  public Date calculate(Date startDate, int countDays) {
    if(startDate == null) throw new IllegalArgumentException("Дата начала периода не должна быть NULL");
    if(countDays<0) throw new IllegalArgumentException("Длительность периода должна быть больше или равно нулю");
    if (countDays == 0) return startDate;
    return DateUtils.addDays(startDate, countDays);
  }
}

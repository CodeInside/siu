/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.calendar;

import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Рассчет даты окончания периода, длительность которого измеряется в каледарных днях
 */
public class CalendarBasedDueDateCalculator implements DueDateCalculator {

  /**
   * Рассчет даты окончания периода
   *
   * @param startDate дата начала периода
   * @param countDays длительность периода в календарных днях. Значение данного параметра должно быть больше или равна 0
   * @return дату окончания периода
   */
  @Override
  public Date calculate(Date startDate, int countDays) {
    if (startDate == null) throw new IllegalArgumentException("Дата начала периода не должна быть NULL");
    if (countDays < 0) throw new IllegalArgumentException("Длительность периода должна быть больше или равна нулю");
    Date alignedToStartDate = DateUtils.truncate(startDate, Calendar.DATE);
    if (countDays == 0) {
      return alignedToStartDate;
    }
    return DateUtils.addDays(alignedToStartDate, countDays);
  }

  /**
   * Рассчет количества календарных дней между датами
   *
   * @param startDate дата начала периода
   * @param endDate   дата окончания периода
   * @return количество дней между датами
   */
  @Override
  public int countDays(Date startDate, Date endDate) {
    if (startDate == null) throw new IllegalArgumentException("Дата начала периода не должна быть NULL");
    if (endDate == null) throw new IllegalArgumentException("Дата окончания периода не должна быть NULL");
    Date firstDate = DateUtils.truncate(startDate, Calendar.DATE);
    Date secondDate = DateUtils.truncate(endDate, Calendar.DATE);
    return (int) ((secondDate.getTime() - firstDate.getTime()) / DateUtils.MILLIS_PER_DAY);
  }
}

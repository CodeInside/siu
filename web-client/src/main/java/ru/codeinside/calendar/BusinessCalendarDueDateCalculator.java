/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.calendar;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * Рассчет окончания периода, длительность которого задана в рабочих днях.
 */
final public class BusinessCalendarDueDateCalculator implements DueDateCalculator {

  final Set<Date> workedDays;
  final Set<Date> holidays;

  /**
   * Конструктор калькулятора окончания периода
   *
   * @param workedDays даты дополнительных рабочих дней
   * @param holidays   даты дополнительных выходных дней
   */
  public BusinessCalendarDueDateCalculator(Set<Date> workedDays, Set<Date> holidays) {
    this.workedDays = ImmutableSet.copyOf(workedDays);
    this.holidays = ImmutableSet.copyOf(holidays);
  }

  @Override
  public Date calculate(Date startDate, int countDays) {
    if (startDate == null) throw new IllegalArgumentException("Дата начала периода не должна быть NULL");
    if (countDays < 0) throw new IllegalArgumentException("Длительность периода должна быть больше или равна нулю");

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);
    while (countDays > 0) {
      calendar.add(Calendar.DAY_OF_MONTH, 1);
      if (isWorkedDay(calendar)) {
        countDays--;
      }
    }
    return calendar.getTime();
  }

  /**
   * Рассчет количества рабочих дней между двумя датами
   *
   * @param startDate дата начала периода
   * @param endDate   дата окончания периода
   * @return количество рабочих дней между датами
   */
  @Override
  public int countDays(Date startDate, Date endDate) {
    if (startDate == null) throw new IllegalArgumentException("Дата начала периода не должна быть NULL");
    if (endDate == null) throw new IllegalArgumentException("Дата окончания периода не должна быть NULL");
    boolean inverse = false;
    if (endDate.before(startDate)) {
      Date tmp = endDate;
      endDate = startDate;
      startDate = tmp;
      inverse = true;
    }
    int countDays = 0;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);
    for (; ; ) {
      calendar.add(Calendar.DAY_OF_MONTH, 1);
      if (calendar.getTime().after(endDate)) {
        break;
      }
      if (isWorkedDay(calendar)) {
        countDays++;
      }
    }
    return inverse ? -countDays : countDays;
  }

  private boolean isWorkedDay(Calendar calendar) {
    Date date = DateUtils.truncate(calendar.getTime(), Calendar.DAY_OF_MONTH);
    boolean isHoliday = isWeekEnd(calendar) || holidays.contains(date);
    boolean isWorkDay = workedDays.contains(date);
    return !isHoliday || isWorkDay;
  }

  private boolean isWeekEnd(Calendar calendar) {
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    return day == Calendar.SUNDAY || day == Calendar.SATURDAY;
  }
}

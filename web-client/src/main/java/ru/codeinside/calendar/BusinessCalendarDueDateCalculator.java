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
import java.util.HashSet;
import java.util.Set;

/**
 * Рассчет окончания периода, длительность которого задана в рабочих днях.
 */
public class BusinessCalendarDueDateCalculator implements DueDateCalculator {
  private Set<Date> workedDays;
  private Set<Date> holidays;
  private Set<Integer> weekEndDay;

  /**
   * Конструктор калькулятора окончания периода
   *
   * @param workedDays даты дополнительных рабочих дней
   * @param holidays   даты дополнительных выходных дней
   */
  public BusinessCalendarDueDateCalculator(Set<Date> workedDays, Set<Date> holidays) {
    this.workedDays = workedDays;
    this.holidays = holidays;
    this.weekEndDay = new HashSet<Integer>();
    weekEndDay.add(Calendar.SUNDAY);
    weekEndDay.add(Calendar.SATURDAY);
  }

  @Override
  public Date calculate(Date startDate, int countDays) {
    if (startDate == null) throw new IllegalArgumentException("Дата начала периода не должна быть NULL");
    if (countDays < 0) throw new IllegalArgumentException("Длительность периода должна быть больше или равна нулю");
    Date alignedToStartDate = DateUtils.truncate(startDate, Calendar.DATE);
    if (countDays == 0) return alignedToStartDate;
    return moveDate(startDate, countDays);
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
    if (endDate.before(startDate))
      throw new IllegalArgumentException("Дата окончания периода должна быть после даты начала периода");
    Date alignedStartDate = DateUtils.truncate(startDate, Calendar.DATE);
    Date alignedEndDate = DateUtils.truncate(endDate, Calendar.DATE);
    int countDays = 0;
    Calendar cal = Calendar.getInstance();
    cal.setTime(alignedStartDate);
    while (!cal.getTime().equals(alignedEndDate)) {
      cal.add(Calendar.DAY_OF_MONTH, 1);
      if (isWorkedDay(cal)) {
        countDays++;
      }
    }
    return countDays;
  }

  private Date moveDate(Date startDate, int countDays) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);
    int days = countDays;
    while (days > 0) {
      calendar.add(Calendar.DAY_OF_MONTH, 1);
      if (isWorkedDay(calendar)) {
        days--;
      }
    }
    return calendar.getTime();
  }

  private boolean isWorkedDay(Calendar calendar) {
    boolean isWeekEnd = weekEndDay.contains(calendar.get(Calendar.DAY_OF_WEEK));
    Date dt = calendar.getTime();
    boolean isHoliday = holidays.contains(dt);
    boolean isAdditionalWorkDay = workedDays.contains(dt);
    return !(isWeekEnd || isHoliday) || isAdditionalWorkDay;
  }
}

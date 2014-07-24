/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.calendar;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class BusinessCalendarDueDateCalculatorTest {

  final Set<Date> emptyDates = ImmutableSet.of();
  final BusinessCalendarDueDateCalculator generalCalendar = new BusinessCalendarDueDateCalculator(emptyDates, emptyDates);
  final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  final SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");

  private Date asDate(String str) {
    try {
      return dateFormat.parse(str);
    } catch (ParseException e) {
      throw new IllegalStateException(e);
    }
  }

  private Date asDay(String str) {
    try {
      return dayFormat.parse(str);
    } catch (ParseException e) {
      throw new IllegalStateException(e);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void date_must_not_null() {
    generalCalendar.calculate(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void duration_must_more_than_zero() {
    generalCalendar.calculate(new Date(), -1);
  }

  @Test
  public void zero_period() {
    Date start = asDate("22/07/2014 10:50:31");
    assertEquals(start, generalCalendar.calculate(start, 0));
    assertEquals(0, generalCalendar.countDays(start, start));

    assertEquals(0, generalCalendar.countDays(start, asDate("23/07/2014 10:50:30")));
    assertEquals(1, generalCalendar.countDays(start, asDate("23/07/2014 10:50:31")));
    assertEquals(-1, generalCalendar.countDays(asDate("23/07/2014 10:50:32"), start));
  }

  @Test
  public void end_period_over_weekend() {
    Date start = asDate("22/07/2014 10:50:31");
    Date end = asDate("05/08/2014 10:50:31");
    assertEquals(end, generalCalendar.calculate(start, 10));
    assertEquals(10, generalCalendar.countDays(start, end));

    assertEquals(9, generalCalendar.countDays(start, asDate("05/08/2014 10:50:30")));
    assertEquals(10, generalCalendar.countDays(start, asDate("05/08/2014 10:50:32")));
    assertEquals(10, generalCalendar.countDays(start, asDate("06/08/2014 10:50:30")));
  }

  @Test
  public void end_period_over_weekend_and_start_in_weekend() {
    Date start = asDate("04/01/2014 23:30:01");
    Date end = asDate("17/01/2014 23:30:01");
    assertEquals(end, generalCalendar.calculate(start, 10));
    assertEquals(10, generalCalendar.countDays(start, end));

    assertEquals(9, generalCalendar.countDays(start, asDate("17/01/2014 23:30:00")));
    assertEquals(10, generalCalendar.countDays(start, asDate("17/01/2014 23:30:02")));
  }

  @Test
  public void end_period_over_weekend_and_period_contain_holiday() {
    ImmutableSet<Date> holidays = ImmutableSet.of(asDay("06/01/2014"));
    BusinessCalendarDueDateCalculator calendar = new BusinessCalendarDueDateCalculator(emptyDates, holidays);

    Date start = asDate("04/01/2014 23:30:01");
    Date end = asDate("20/01/2014 23:30:01");
    assertEquals(end, calendar.calculate(start, 10));
    assertEquals(10, calendar.countDays(start, end));

    assertEquals(9, calendar.countDays(start, asDate("20/01/2014 23:30:00")));
    assertEquals(10, calendar.countDays(start, asDate("20/01/2014 23:30:02")));
  }

  @Test
  public void end_period_over_weekend_and_period_contain_holiday_additional_workday() {
    ImmutableSet<Date> workdays = ImmutableSet.of(asDay("05/01/2014"));
    ImmutableSet<Date> holidays = ImmutableSet.of(asDay("06/01/2014"));
    BusinessCalendarDueDateCalculator calendar = new BusinessCalendarDueDateCalculator(workdays, holidays);

    Date start = asDate("04/01/2014 23:30:01");
    Date end = asDate("17/01/2014 23:30:01");
    assertEquals(end, calendar.calculate(start, 10));
    assertEquals(10, calendar.countDays(start, end));

    assertEquals(9, calendar.countDays(start, asDate("17/01/2014 23:30:00")));
    assertEquals(10, calendar.countDays(start, asDate("17/01/2014 23:30:02")));
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.calendar;

import com.google.common.collect.Sets;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class BusinessCalendarDueDateCalculatorTest {
  private Date startDate;
  private Date saturday_4jan;
  private Date monday_6jan;
  private Date sunday_5jan;
  private SimpleDateFormat df;

  @Before
  public void setUp() throws Exception {
    df = new SimpleDateFormat("dd/MM/yyyy");
    startDate = df.parse("01/01/2014");
    saturday_4jan = df.parse("04/01/2014");
    monday_6jan = df.parse("06/01/2014");
    sunday_5jan = df.parse("05/01/2014");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStart_Date_must_not_null() throws Exception {
    new CalendarBasedDueDateCalculator().calculate(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPeriod_Duration_must_more_than_zero() throws Exception {
    new CalendarBasedDueDateCalculator().calculate(new Date(), -1);
  }

  @Test
  public void testIf_period_zero_then_return_start_period() throws Exception {
    Date startPeriod = new Date();
    Date result = new CalendarBasedDueDateCalculator().calculate(startPeriod, 0);
    assertEquals(DateUtils.truncate(startPeriod, Calendar.DATE), result);
  }

  @Test
  public void testCalculate_end_period_over_weekend() throws Exception {


    // никаких дополнительных выходных и рабочих дней не задано
    BusinessCalendarDueDateCalculator businessCalendar = new BusinessCalendarDueDateCalculator(Sets.<Date>newHashSet(), Sets.<Date>newHashSet());
    Date result = businessCalendar.calculate(startDate, 10);
    Date expectedPeriod = df.parse("15/01/2014");
    assertEquals(expectedPeriod, result);
    assertEquals(10, businessCalendar.countDays(startDate, expectedPeriod));
  }

  @Test
  public void testCalculate_end_period_over_weekend_and_start_in_weekend() throws Exception {

    // никаких дополнительных выходных и рабочих дней не задано
    BusinessCalendarDueDateCalculator businessCalendar = new BusinessCalendarDueDateCalculator(Sets.<Date>newHashSet(), Sets.<Date>newHashSet());
    Date result = businessCalendar.calculate(saturday_4jan, 10);
    Date expectedPeriod = df.parse("17/01/2014");
    assertEquals(expectedPeriod, result);
    assertEquals(10, businessCalendar.countDays(saturday_4jan, expectedPeriod));
  }

  @Test
  public void testCalculate_end_period_over_weekend_and_period_contain_holiday() throws Exception {
    // задаем дополнительные выходные
    Set<Date> holidays = Sets.newHashSet();
    holidays.add(monday_6jan);
    BusinessCalendarDueDateCalculator businessCalendar = new BusinessCalendarDueDateCalculator(Sets.<Date>newHashSet(), holidays);
    Date result = businessCalendar.calculate(saturday_4jan, 10);
    Date expectedPeriod = df.parse("20/01/2014");
    assertEquals(expectedPeriod, result);
    assertEquals(10, businessCalendar.countDays(saturday_4jan, expectedPeriod));
  }

  @Test
  public void testCalculate_end_period_over_weekend_and_period_contain_holiday_additional_workday() throws Exception {
    // задаем дополнительные выходные
    Set<Date> holidays = Sets.newHashSet();
    holidays.add(monday_6jan);
    //дополнительные рабочие дни
    Set<Date> workedDays = Sets.newHashSet();
    workedDays.add(sunday_5jan);
    BusinessCalendarDueDateCalculator businessCalendar = new BusinessCalendarDueDateCalculator(workedDays, holidays);
    Date result = businessCalendar.calculate(saturday_4jan, 10);
    Date expectedPeriod = df.parse("17/01/2014");
    assertEquals(expectedPeriod, result);
    assertEquals(10, businessCalendar.countDays(saturday_4jan, expectedPeriod));
  }
}

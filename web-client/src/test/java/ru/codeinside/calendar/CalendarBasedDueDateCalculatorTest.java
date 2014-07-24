/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.calendar;


import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class CalendarBasedDueDateCalculatorTest {

  CalendarBasedDueDateCalculator calendar = new CalendarBasedDueDateCalculator();
  SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

  private Date asDate(String str) {
    try {
      return dateFormat.parse(str);
    } catch (ParseException e) {
      throw new IllegalStateException(e);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStart_Date_must_not_null() {
    calendar.calculate(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPeriod_Duration_must_more_than_zero() {
    calendar.calculate(new Date(), -1);
  }

  @Test
  public void testIf_period_zero_then_return_start_period() {
    Date start = asDate("22/07/2014 10:50:31");
    assertEquals(start, calendar.calculate(start, 0));
  }

  @Test
  public void testCalculate_end_period() {
    assertEquals(asDate("22/07/2014 10:50:31"), calendar.calculate(asDate("12/07/2014 10:50:31"), 10));
  }

  @Test
  public void testCalculateCountDays() {
    assertEquals(8, calendar.countDays(asDate("12/07/2014 10:50:31"), asDate("21/07/2014 10:50:30")));
    assertEquals(9, calendar.countDays(asDate("12/07/2014 10:50:31"), asDate("21/07/2014 10:50:31")));
  }
}

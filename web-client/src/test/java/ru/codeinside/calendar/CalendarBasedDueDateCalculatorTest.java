/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.calendar;


import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class CalendarBasedDueDateCalculatorTest {
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
  public void testCalculate_end_period() throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    Date startPeriod = dateFormat.parse("01/01/2014");
    Date result = new CalendarBasedDueDateCalculator().calculate(startPeriod, 10);
    Date expectedPeriod = dateFormat.parse("11/01/2014");
    assertEquals(expectedPeriod, result);
  }

  @Test
  public void testCalculateCountDays() throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date startPeriod = dateFormat.parse("01/01/2014");
    Date endPeriod = dateFormat.parse("10/01/2014");

    int result = new CalendarBasedDueDateCalculator().countDays(startPeriod, endPeriod);
    assertEquals(9, result);

  }
}

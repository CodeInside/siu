/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.parser;

import org.junit.Test;
import ru.codeinside.adm.database.BusinessCalendarDate;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

public class BusinessCalendarParserTest {

  @Test
  public void testParser() throws Exception {
    InputStream stream = this.getClass().getResourceAsStream("/business_calendar/cal.csv");
    try {
      BusinessCalendarParser parser = new BusinessCalendarParser();
      List<BusinessCalendarDate> dateList = parser.parseBusinessCalendarDate(stream);
      assertEquals(2, dateList.size());
      SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

      BusinessCalendarDate dt_1 = dateList.get(0);
      assertEquals(df.parse("25.11.2014"), dt_1.getDate());
      assertFalse(dt_1.getWorkedDay());


      BusinessCalendarDate dt_2 = dateList.get(1);
      assertEquals(df.parse("25.12.2014"), dt_2.getDate());
      assertTrue(dt_2.getWorkedDay());
    } finally {
      stream.close();
    }

  }
}
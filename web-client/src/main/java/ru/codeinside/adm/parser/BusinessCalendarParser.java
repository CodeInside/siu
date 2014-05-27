/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.parser;

import ru.codeinside.adm.database.BusinessCalendarDate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Разбор файла с датами дополнительных выходных и рабочих дней
 */
public class BusinessCalendarParser {
  private static Pattern LINE_PATTERN = Pattern.compile("(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.(21|20)(\\d\\d);(0|1)");

  /**
   * Разбор файла с датами дополнительных выходных и рабочих дней
   *
   * @param is поток из файла с датами. Каждая строка должна содержать данные о доп выходном в формате дата;0 или 1
   *           дата должна быть представлена в формате dd.MM.yyyy
   *           второе поле должно содержать 0 или 1. 0 - если дата дополнительный рабочий день, 1 - если дата дополнительный выходной день
   * @return даты дополнительных выходных и рабочих дней
   */
  List<BusinessCalendarDate> parseBusinessCalendarDate(InputStream is) throws IOException, ParseException {
    final BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
    List<BusinessCalendarDate> result = new LinkedList<BusinessCalendarDate>();
    String line;
    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    int lineNumber = 0;
    while ((line = reader.readLine()) != null) {
      Matcher matcher = LINE_PATTERN.matcher(line);
      if (matcher.matches()) {
        String day = matcher.group(1);
        String month = matcher.group(2);
        String century = matcher.group(3);
        String year = matcher.group(4);

        String isHoliday = matcher.group(5);
        BusinessCalendarDate dt = new BusinessCalendarDate();
        dt.setDate(df.parse(day + "." + month + "." + century + year));
        dt.setWorkedDay("0".equals(isHoliday));
        result.add(dt);
      }
    }
    return result;
  }
}

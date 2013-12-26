/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.grp;

import org.apache.commons.lang.time.DateUtils;

import java.util.Date;


public class TestUtils {
  public static Date getDateValue(String dateStr) {
    try {
      return DateUtils.parseDate(dateStr, new String[]{"yyyy-MM-dd"});
    } catch (Exception err) {
      return null;
    }
  }
}

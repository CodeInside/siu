/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import com.vaadin.ui.PopupDateField;
import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Возвращает введенную в поле дату ровно той точности, какая описана в формате.
 * Если в формате не указано время, то введенная дата не будет содержать время.
 */
public class ActivitiDateField extends PopupDateField {
  public ActivitiDateField(String name) {
    super(name);
  }

  @Override
  public Object getValue() {
    Object value = super.getValue();
    if (value != null) {
      value = DateUtils.truncate((Date)value, Calendar.DATE);
    }
    return value;
  }
}

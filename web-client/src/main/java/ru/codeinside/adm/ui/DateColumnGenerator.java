/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.ui.CustomTable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Генератор строки из даты по формату.
 */
public final class DateColumnGenerator implements CustomTable.ColumnGenerator {
  final SimpleDateFormat formatter;

  public DateColumnGenerator(final String format) {
    formatter = new SimpleDateFormat(format);
  }

  @Override
  public Object generateCell(final CustomTable source, final Object itemId, final Object columnId) {
    final Object object = source.getContainerDataSource().getContainerProperty(itemId, columnId).getValue();
    if (object instanceof Date) {
      return formatter.format((Date) object);
    }
    return null;
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.data.Property;
import com.vaadin.ui.CustomTable;

/**
 * Генератор строки из логического значение в текст Да / Нет
 */
public final class BooleanColumnGenerator implements CustomTable.ColumnGenerator {


  @Override
  public Object generateCell(final CustomTable source, final Object itemId, final Object columnId) {
    Property containerProperty = source.getContainerDataSource().getContainerProperty(itemId, columnId);
    if (containerProperty != null) {
      final Object object = containerProperty.getValue();
      if (object instanceof Boolean) {
        boolean val = (Boolean) object;
        return val ? "Да" : "Нет";
      }
    }
    return null;
  }
}

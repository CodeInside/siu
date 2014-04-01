/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.CustomTable;

final class YesColumnGenerator implements CustomTable.ColumnGenerator {
  @Override
  public Object generateCell(CustomTable source, Object itemId, Object columnId) {
    Container containerDataSource = source.getContainerDataSource();
    Property containerProperty = containerDataSource.getContainerProperty(itemId, columnId);
    if (containerProperty!=null) {
    Object object = containerProperty.getValue();
      return Boolean.TRUE.equals(object) ? "Да" :  null;
    }
    return null;
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.ui.CustomTable;

public final class YesNoColumnGenerator implements CustomTable.ColumnGenerator {

  @Override
  public Object generateCell(final CustomTable source, final Object itemId, final Object columnId) {
    Object object = source.getContainerDataSource().getContainerProperty(itemId, columnId).getValue();
    return Boolean.TRUE.equals(object) ? "Да" : null;
  }
}

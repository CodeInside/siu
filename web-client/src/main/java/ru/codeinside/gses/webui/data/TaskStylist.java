/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import com.vaadin.data.Property;
import com.vaadin.ui.Table;

final public class TaskStylist implements Table.CellStyleGenerator {

  final Table table;

  public TaskStylist(Table table) {
    this.table = table;
  }

  @Override
  public String getStyle(Object itemId, Object propertyId) {
    if ("bidDays".equals(propertyId)) {
      return getStyle(itemId, "bidStyle");
    }
    if ("taskDays".equals(propertyId)) {
      return getStyle(itemId, "taskStyle");
    }
    return getStyle(itemId, "style");
  }

  private String getStyle(Object itemId, String p) {
    Property style = table.getItem(itemId).getItemProperty(p);
    return style == null ? null : (String) style.getValue();
  }
}
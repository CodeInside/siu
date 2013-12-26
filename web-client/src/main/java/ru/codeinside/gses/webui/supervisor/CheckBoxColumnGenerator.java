/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;

final public class CheckBoxColumnGenerator implements Table.ColumnGenerator {

  private final String propertyId;

  public CheckBoxColumnGenerator(String propertyId) {
    this.propertyId = propertyId;
  }

  public CheckBoxColumnGenerator() {
    this(null);
  }


  public Object generateCell(final Table source, final Object itemId, final Object columnId) {
    final Property property = source.getItem(itemId).getItemProperty(propertyId == null ? columnId : propertyId);
    if (property != null && property.getValue() == Boolean.TRUE) {
      CheckBox checkBox = new CheckBox(null, true);
      checkBox.setReadOnly(true);
      return checkBox;
    }
    return null;
  }
}

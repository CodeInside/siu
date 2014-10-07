/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.vaadin.data.Property;
import com.vaadin.ui.CustomTable;

final public class SmevStylist implements CustomTable.CellStyleGenerator {

  final CustomTable table;

  public SmevStylist(CustomTable table) {
    this.table = table;
  }

  @Override
  public String getStyle(Object itemId, Object propertyId) {
    if (propertyId == null) {
      Boolean needUserReaction = getPropertyValue(itemId, "needUserReaction", Boolean.class);
      if (needUserReaction) {
        return "highlight-red";
      }
      Integer errorCount = getPropertyValue(itemId, "errorCount", Integer.class);
      if (errorCount > 0) {
        return "highlight-rosy";
      }
    }
    return null;
  }

  private <T> T getPropertyValue(Object itemId, String propertyId, Class<T> type) {
    Property property = table.getItem(itemId).getItemProperty(propertyId);
    return type.cast(property.getValue());
  }
}

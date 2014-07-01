/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.TextField;
import org.apache.commons.lang.StringUtils;

public class LongField extends TextField {

  public LongField() {

  }

  public LongField(Long strValue) {
    setValue(strValue);
  }


  /**
   * Необходимо гарантировать тип значения.
   */
  @Override
  public Object getValue() {
    Object value = null;

    String string = null;

    final Object original = super.getValue();
    if (original != null) {

      if (original instanceof Long) {
        return original;
      }

      if (original instanceof Integer) {
        return Long.valueOf((Integer) original);
      }

      string = StringUtils.trimToNull(original.toString());
    }

    if (string != null) {
      try {
        value = Long.parseLong(string);
      } catch (NumberFormatException e) {
        value = string;
      }
    }

    return value;
  }
}

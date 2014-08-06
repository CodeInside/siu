/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.types;

import java.util.Map;

final class MultilineType extends StringType {

  @Override
  public String getName() {
    return "multiline";
  }

  @Override
  public String toString() {
    return "MultilineType";
  }

  @Override
  public void validateParams(String pattern, Map<String, String> values) {
    if (pattern != null) {
      throw VariableTypes.badPattern(GsesTypes.STRING);
    }
    if (values != null) {
      try {
        String rows = values.get("rows");
        String columns = values.get("columns");
        Integer.parseInt(rows);
        Integer.parseInt(columns);
      } catch (NumberFormatException e) {
        throw new IllegalStateException("Параметры rows и columns в multiline должны быть целочисленные");
      }
    }
  }
}

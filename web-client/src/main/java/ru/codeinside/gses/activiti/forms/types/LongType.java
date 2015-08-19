/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.types;

import ru.codeinside.adm.database.FieldBuffer;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

import java.util.Map;

final class LongType implements VariableType<Long> {

  @Override
  public Long convertFormValueToModelValue(Object propertyValue, String pattern, Map<String, String> values) {
    if (propertyValue == null) {
      return null;
    }
    if (propertyValue instanceof Number) {
      return ((Number) propertyValue).longValue();
    }
    String property = propertyValue.toString();
    return property.isEmpty() ? null : Long.parseLong(property);
  }

  @Override
  public void validateParams(String pattern, Map<String, String> values, boolean sandbox) {
    if (pattern != null) {
      throw VariableTypes.badPattern(GsesTypes.LONG);
    }
    if (values != null) {
      throw VariableTypes.badValues(GsesTypes.LONG);
    }
  }

  @Override
  public Class<Long> getJavaType() {
    return Long.class;
  }

  @Override
  public Long convertBufferToModelValue(FieldBuffer fieldBuffer) {
    return fieldBuffer.getLongValue();
  }

  @Override
  public String getName() {
    return "long";
  }
}

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

final class BooleanType implements VariableType<Boolean> {

  @Override
  public Boolean convertFormValueToModelValue(Object propertyValue, String pattern, Map<String, String> values) {
    if (propertyValue == null) {
      return null;
    }
    if (propertyValue instanceof Boolean) {
      return (Boolean) propertyValue;
    }
    return Boolean.parseBoolean(propertyValue.toString());
  }

  @Override
  public void validateParams(String pattern, Map<String, String> values) {
    if (pattern != null) {
      throw VariableTypes.badPattern(GsesTypes.BOOLEAN);
    }
    if (values != null) {
      throw VariableTypes.badValues(GsesTypes.BOOLEAN);
    }
  }

  @Override
  public Class<Boolean> getJavaType() {
    return Boolean.class;
  }

  @Override
  public Boolean convertBufferToModelValue(FieldBuffer fieldBuffer) {
    Long longValue = fieldBuffer.getLongValue();
    return longValue == null ? null : longValue != 0L;
  }

  @Override
  public String getName() {
    return "boolean";
  }
}

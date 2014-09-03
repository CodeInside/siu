/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.types;

import ru.codeinside.adm.database.FieldBuffer;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Logger;

class StringType implements VariableType<String> {

  @Override
  public String convertFormValueToModelValue(Object propertyValue, String pattern, Map<String, String> values) {
    if (propertyValue instanceof byte[]) {
      try {
        return new String((byte[]) propertyValue, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        Logger.getAnonymousLogger().info("can't decode model!");
      }
    }
    return propertyValue == null ? null : propertyValue.toString();
  }

  @Override
  public void validateParams(String pattern, Map<String, String> values, boolean sandbox) {
    if (sandbox) {
      if (pattern != null) {
        throw VariableTypes.badPattern(GsesTypes.STRING);
      }
      if (values != null) {
        throw VariableTypes.badValues(GsesTypes.STRING);
      }
    }
  }

  @Override
  public Class<String> getJavaType() {
    return String.class;
  }

  @Override
  public String convertBufferToModelValue(FieldBuffer fieldBuffer) {
    return fieldBuffer.getTextValue();
  }

  @Override
  public String getName() {
    return "string";
  }

  @Override
  public String toString() {
    return "StringType";
  }
}

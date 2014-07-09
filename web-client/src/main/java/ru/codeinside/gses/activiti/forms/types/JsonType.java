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

final class JsonType implements VariableType<byte[]> {

  @Override
  public byte[] convertFormValueToModelValue(Object propertyValue, String pattern, Map<String, String> values) {
    if (propertyValue == null) {
      return null;
    }
    if (propertyValue instanceof byte[]) {
      return (byte[]) propertyValue;
    }
    try {
      return propertyValue.toString().getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      Logger.getAnonymousLogger().info("can't encode json!");
    }
    return null;
  }

  @Override
  public void validateParams(String pattern, Map<String, String> values) {
    if (pattern != null) {
      throw VariableTypes.badPattern(GsesTypes.JSON);
    }
    if (values != null) {
      throw VariableTypes.badValues(GsesTypes.JSON);
    }
  }

  @Override
  public Class<byte[]> getJavaType() {
    return byte[].class;
  }

  @Override
  public byte[] convertBufferToModelValue(FieldBuffer fieldBuffer) {
    if (fieldBuffer.getTextValue() != null) {
      try {
        return fieldBuffer.getTextValue().getBytes("UTF-8");
      } catch (UnsupportedEncodingException e) {
        Logger.getAnonymousLogger().info("can't encode json!");
      }
    }
    return null;
  }

  @Override
  public String getName() {
    return "json";
  }

  @Override
  public String toString() {
    return "JsonType";
  }
}

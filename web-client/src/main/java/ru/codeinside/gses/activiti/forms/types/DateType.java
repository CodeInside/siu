/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.types;

import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.database.FieldBuffer;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

final public class DateType implements VariableType<Date> {

  final public static String PATTERN1 = "dd/MM/yyyy";
  final public static String PATTERN2 = "yyyy.MM.dd";

  @Override
  public Date convertFormValueToModelValue(Object propertyValue, String pattern, Map<String, String> values) {
    if (propertyValue == null) {
      return null;
    }
    if (propertyValue instanceof Date) {
      return (Date) propertyValue;
    }
    if (StringUtils.trimToNull(pattern) == null) {
      pattern = PATTERN1;
    }
    String string = propertyValue.toString();
    try {
      SimpleDateFormat format = new SimpleDateFormat(pattern);
      format.setLenient(false);
      return format.parse(string);
    } catch (ParseException e1) {
      try {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN2);
        simpleDateFormat.setLenient(false);
        return simpleDateFormat.parse(string);
      } catch (ParseException e) {
        return null;
      }
    }
  }

  @Override
  public void validateParams(String pattern, Map<String, String> values, boolean sandbox) {
    pattern = StringUtils.trimToNull(pattern);
    if (pattern != null) {
      new SimpleDateFormat(pattern); // throws IllegalStateException on bad pattern
    }
    if (values != null) {
      throw VariableTypes.badValues(GsesTypes.DATE);
    }
  }

  @Override
  public Class<Date> getJavaType() {
    return Date.class;
  }

  @Override
  public Date convertBufferToModelValue(FieldBuffer fieldBuffer) {
    Long longValue = fieldBuffer.getLongValue();
    return longValue == null ? null : new Date(longValue);
  }

  @Override
  public String getName() {
    return "date";
  }
}

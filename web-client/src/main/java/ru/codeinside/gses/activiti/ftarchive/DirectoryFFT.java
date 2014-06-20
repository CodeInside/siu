/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.Field;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.forms.FieldConstructor;
import ru.codeinside.gses.activiti.forms.FieldConstructorBuilder;
import ru.codeinside.gses.beans.DirectoryBeanProvider;

import java.util.Map;

public class DirectoryFFT implements FieldConstructor, FieldConstructorBuilder {

  private static final long serialVersionUID = 1L;

  private final Map<String, String> values;


  public DirectoryFFT() {
    values = null;
  }

  public DirectoryFFT(Map<String, String> values) {
    this.values = values;
  }

  @Override
  public Field createField(final String taskId, final String fieldId, String name, final String value, boolean writable, boolean required) {
    if (values.get("directory_id") == null) {
      return new ReadOnly("Неверно указан directory_id");
    }
    String directoryId = values.get("directory_id").trim();
    if (writable) {
      DirectoryField field = new DirectoryField(directoryId, name);
      FieldHelper.setTextBufferSink(taskId, fieldId, field, true, value);
      FieldHelper.setCommonFieldProperty(field, true, name, required);
      return field;
    }
    String trimValue = value == null ? null : value.trim();
    String kName = DirectoryBeanProvider.get().getValue(directoryId, trimValue);
    if (StringUtils.isEmpty(kName)) {
      kName = trimValue;
    }
    ReadOnly readOnly = new ReadOnly(kName);
    FieldHelper.setCommonFieldProperty(readOnly, false, name, required);
    return readOnly;
  }

  @Override
  public String convertModelValueToFormValue(Object modelValue) {
    return modelValue != null ? modelValue.toString() : null;
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    return propertyValue;
  }


  @Override
  public FieldConstructor create(String patternText, Map<String, String> values) {
    return new DirectoryFFT(values);
  }
}

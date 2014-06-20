/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import ru.codeinside.gses.activiti.forms.FieldConstructor;
import ru.codeinside.gses.activiti.forms.FieldConstructorBuilder;

import java.util.Map;

public class EnumFFT implements FieldConstructor, FieldConstructorBuilder {

  private static final long serialVersionUID = 1L;

  private final Map<String, String> values;

  public EnumFFT() {
    values = null;
  }

  public EnumFFT(Map<String, String> values) {
    this.values = values;
  }

  @Override
  public Field createField(String taskId, String fieldId, String name, String value, boolean writable, boolean required) {
    ComboBox comboBox = new ComboBox(name);
    for (java.util.Map.Entry<String, String> enumEntry : values.entrySet()) {
      comboBox.addItem(enumEntry.getKey());
      if (enumEntry.getValue() != null) {
        comboBox.setItemCaption(enumEntry.getKey(), enumEntry.getValue());
      }
    }
    comboBox.setImmediate(true);// важно!
    comboBox.setWidth("400px");
    FieldHelper.setTextBufferSink(taskId, fieldId, comboBox, writable, value);
    FieldHelper.setCommonFieldProperty(comboBox, writable, name, required);
    return comboBox;
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
    return new EnumFFT(values);
  }
}

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
import ru.codeinside.gses.vaadin.MaskedTextField;

import java.util.Map;

public class MaskedFFT implements FieldConstructor, FieldConstructorBuilder {

  private static final long serialVersionUID = 1L;

  public final String mask;

  public MaskedFFT() {
    mask = null;
  }

  public MaskedFFT(String mask) {
    this.mask = mask;
  }

  @Override
  public Field createField(final String taskId, final String fieldId, final String name, final String value, boolean writable, boolean required) {
    Field result;
    if (!writable) {
      result = new ReadOnly(value);
    } else {
      MaskedTextField textField = new MaskedTextField();
      textField.setImmediate(true);
      textField.setMask(mask);
      FieldHelper.setTextBufferSink(taskId, fieldId, textField, true, StringUtils.trimToEmpty(value));
      result = textField;
    }

    FieldHelper.setCommonFieldProperty(result, writable, name, required);
    return result;
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
    return new MaskedFFT(patternText);
  }
}
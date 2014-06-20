/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.forms.FieldConstructor;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public class StringFFT implements FieldConstructor {

  private static final long serialVersionUID = 1L;

  @Override
  public Field createField(final String taskId, final String fieldId, final String name, final String value, boolean writable, boolean required) {
    Field result;
    if (!writable) {
      result = new ReadOnly(value);
    } else {
      TextField textField = new TextField();
      textField.setColumns(25);
      FieldHelper.setTextBufferSink(taskId, fieldId, textField, true, StringUtils.trimToEmpty(value));
      result = textField;
    }
    FieldHelper.setCommonFieldProperty(result, writable, name, required);
    return result;
  }

  @Override
  public String convertModelValueToFormValue(Object modelValue) {
    if (modelValue instanceof byte[]) {
      try {
        return new String((byte[]) modelValue, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        Logger.getAnonymousLogger().info("can't decode model!");
      }
    }
    return modelValue != null ? modelValue.toString() : null;
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    return propertyValue;
  }


}

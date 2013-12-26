/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.ftarchive.helpers.FieldHelper;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Logger;

public class StringFFT implements FieldFormType, Serializable, FieldConstructor {

  private static final long serialVersionUID = 1L;

  @Override
  public String getFromType() {
    return "string";
  }

  @Override
  public Field createField(final String name, final String value, Layout layout, boolean writable, boolean required) {
    Field result;
    if (!writable) {
      result = new ReadOnly(value);
    } else {
      TextField textField = new TextField();
      textField.setValue(StringUtils.trimToEmpty(value));
      textField.setColumns(25);
      //textField.setMaxLength(4000);// ограниечение Acitviti на размер строки!
      // if (required) {
      // textField.setInputPrompt("Заполните!");
      // }
      result = textField;
    }
    FieldHelper.setCommonFieldProperty(result, writable, name, required);
    return result;
  }

  @Override
  public String getFieldValue(String formPropertyId, Form form) {
    throw new UnsupportedOperationException();
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

  @Override
  public boolean usePattern() {
    return false;
  }

  @Override
  public boolean useMap() {
    return false;
  }

  @Override
  public FieldConstructor createConstructorOfField() {
    return this;
  }

  @Override
  public void setMap(Map<String, String> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setPattern(String patternText) {
    throw new UnsupportedOperationException();
  }

}

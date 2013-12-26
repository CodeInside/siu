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
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.ftarchive.helpers.FieldHelper;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;
import ru.codeinside.gses.vaadin.MaskedTextField;

import java.io.Serializable;
import java.util.Map;

public class MaskedFFT implements FieldFormType, Serializable, FieldConstructor {

  private static final long serialVersionUID = 1L;

  @Override
  public String getFromType() {
    return "masked";
  }

  public String mask = "";

  @Override
  public Field createField(final String name, final String value, Layout layout, boolean writable, boolean required) {
    Field result;
    if (!writable) {
      result = new ReadOnly(value);
    } else {
      MaskedTextField textField = new MaskedTextField();
      textField.setImmediate(true);
      textField.setMask(mask);
      textField.setValue(StringUtils.trimToEmpty(value));
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
    return modelValue != null ? modelValue.toString() : null;
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    return propertyValue;
  }

  @Override
  public boolean usePattern() {
    return true;
  }

  @Override
  public boolean useMap() {
    return false;
  }

  @Override
  public FieldConstructor createConstructorOfField() {
    return new MaskedFFT();
  }

  @Override
  public void setMap(Map<String, String> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setPattern(String patternText) {
    mask = patternText;
  }

}
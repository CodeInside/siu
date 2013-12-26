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
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;

import java.io.Serializable;
import java.util.Map;

public class SmevResponseEnclosuresFFT implements FieldFormType, Serializable, FieldConstructor {
  @Override
  public String getFromType() {
    return "smevResponseEnclosure";
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
    return false;
  }

  @Override
  public boolean useMap() {
    return false;
  }

  @Override
  public FieldConstructor createConstructorOfField() {
    return new SmevResponseEnclosuresFFT();
  }

  @Override
  public Field createField(String name, String value, Layout layout, boolean writable, boolean required) {
    return new ReadOnly(value);
  }

  @Override
  public void setPattern(String patternText) {
  }

  @Override
  public void setMap(Map<String, String> values) {
  }
}

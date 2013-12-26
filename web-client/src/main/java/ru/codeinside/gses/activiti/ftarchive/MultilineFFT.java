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
import com.vaadin.ui.TextArea;
import org.activiti.engine.delegate.BpmnError;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ftarchive.helpers.FieldHelper;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MultilineFFT implements FieldFormType, Serializable, FieldConstructor {

  private Map<String, String> values;

  public MultilineFFT() {
    values = new HashMap<String, String>();
  }

  public MultilineFFT(Map<String, String> values) {
    this.values = values;
  }

  @Override
  public String getFromType() {
    return "multiline";
  }

  @Override
  public Field createField(final String name, final String value, Layout layout, boolean writable, boolean required) {
    TextArea textField = new TextArea();
    textField.setValue(StringUtils.trimToEmpty(value));

    String rows = values.get("rows");
    String columns = values.get("columns");

    textField.setRows(countOf(rows));
    textField.setColumns(countOf(columns));
    textField.setMaxLength(3995);
    //if (required) {
    //  textField.setInputPrompt("Заполните!");
    //}

    FieldHelper.setCommonFieldProperty(textField, writable, name, required);
    return textField;
  }

  private int countOf(String columns) {
    final int count;
    if (StringUtils.isEmpty(columns)) {
      count = 25;
    } else {
      try {
        count = Integer.parseInt(columns);
      } catch (NumberFormatException e) {
        throw new BpmnError("not.number");
      }
    }
    return count;
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
    return true;
  }

  @Override
  public FieldConstructor createConstructorOfField() {
    return new MultilineFFT(values);
  }

  @Override
  public void setMap(Map<String, String> values) {
    this.values = values;
  }

  @Override
  public void setPattern(String patternText) {
    throw new UnsupportedOperationException();
  }


}

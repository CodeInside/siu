/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;


import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import org.activiti.engine.delegate.BpmnError;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

final public class MultilineFFT  {

  private static final long serialVersionUID = 1L;

  private final Map<String, String> values;

  public MultilineFFT() {
    values = null;
  }

  public MultilineFFT(Map<String, String> values) {
    this.values = values;
  }

  //@Override
  public Field createField(final String taskId, final String fieldId, final String name, final String value, boolean writable, boolean required) {
    TextArea textField = new TextArea();
    String rows = values.get("rows");
    String columns = values.get("columns");
    textField.setRows(countOf(rows));
    textField.setColumns(countOf(columns));
    textField.setMaxLength(3995);
    FieldHelper.setTextBufferSink(taskId, fieldId, textField, writable, StringUtils.trimToEmpty(value));
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

  //@Override
  public String convertModelValueToFormValue(Object modelValue) {
    return modelValue != null ? modelValue.toString() : null;
  }

  //@Override
  public Object convertFormValueToModelValue(String propertyValue) {
    return propertyValue;
  }

  //@Override
  public Object create(String patternText, Map<String, String> values) {
    return new MultilineFFT(values);
  }
}

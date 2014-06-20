/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.data.Property;
import com.vaadin.ui.Field;
import com.vaadin.ui.PopupDateField;
import ru.codeinside.gses.activiti.ActivitiDateField;
import ru.codeinside.gses.activiti.forms.FieldConstructor;
import ru.codeinside.gses.activiti.forms.FieldConstructorBuilder;
import ru.codeinside.gses.service.Some;
import ru.codeinside.gses.webui.Flash;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

public class DateFFT implements FieldConstructor, FieldConstructorBuilder {

  private static final long serialVersionUID = 1L;

  final public static String pattern = "dd/MM/yyyy";
  final public static String pattern2 = "yyyy.MM.dd";

  final String localPattern;
  final Format dateFormat;

  public DateFFT() {
    this.localPattern = null;
    this.dateFormat = null;
  }

  public DateFFT(String patterText) {
    if (patterText == null) {
      patterText = pattern;
    }
    localPattern = patterText;
    dateFormat = new SimpleDateFormat(patterText);
  }

  @Override
  public Field createField(final String taskId, final String fieldId, String name, String value, boolean writable, boolean required) {
    PopupDateField dateField = new ActivitiDateField(name);
    dateField.setDateFormat(localPattern);
    if (value != null) {
      try {
        Date date = new SimpleDateFormat(localPattern).parse(value);
        dateField.setValue(date);
      } catch (ParseException e) {
        Logger.getLogger(getClass().getName()).info("'" + name + "' - " + e.getMessage());
      }
    }
    if (writable && taskId != null) {
      Some<Long> optionalLong = Flash.flash().getExecutorService().getLongBuffer(taskId, fieldId);
      if (optionalLong.isPresent()) {
        dateField.setValue(new Date(optionalLong.get()));
      }
      dateField.setImmediate(true);
      dateField.addListener(new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
          Date newValue = (Date) event.getProperty().getValue(); // сохраняться будет лишь правильная дата
          Flash.flash().getExecutorService().saveBuffer(taskId, fieldId, newValue.getTime());
        }
      });
    }
    FieldHelper.setCommonFieldProperty(dateField, writable, name, required);
    return dateField;
  }

  public Object convertFormValueToModelValue(String propertyValue) {
    if (propertyValue == null || "".equals(propertyValue)) {
      return null;
    }
    return tryParse(localPattern, propertyValue);
  }

  private Object tryParse(String formatPattern, String value) {
    try {
      return new SimpleDateFormat(formatPattern).parseObject(value);
    } catch (ParseException e) {
      if (pattern.equals(formatPattern)) {
        Logger.getLogger(getClass().getName()).info("'" + value + "' not match '" + formatPattern + "'");
        try {
          return new SimpleDateFormat(pattern2).parseObject(value);
        } catch (ParseException e1) {
          Logger.getLogger(getClass().getName()).info("and not match '" + pattern2 + "'");
        }
      }
      return null;
    }
  }

  public String convertModelValueToFormValue(Object modelValue) {
    if (modelValue == null) {
      return null;
    }
    try {
      return new SimpleDateFormat(localPattern).format(modelValue);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return "";
    }
  }

  @Override
  public FieldConstructor create(String patternText, Map<String, String> values) {
    return new DateFFT(patternText);
  }
}

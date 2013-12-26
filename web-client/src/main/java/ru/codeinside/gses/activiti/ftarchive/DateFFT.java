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
import com.vaadin.ui.PopupDateField;
import ru.codeinside.gses.activiti.ActivitiDateField;
import ru.codeinside.gses.activiti.ftarchive.helpers.FieldHelper;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

public class DateFFT implements FieldFormType, FieldConstructor {

  public static String pattern = "dd/MM/yyyy";
  public static String pattern2 = "yyyy.MM.dd";
  public String localPattern = pattern;

  protected Format dateFormat;

  public DateFFT() {
    this.localPattern = pattern;
    this.dateFormat = new SimpleDateFormat(localPattern);
  }

  @Override
  public String getFromType() {
    return "date";
  }

  @Override
  public Field createField(String name, String value, Layout layout, boolean writable, boolean required) {
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
    FieldHelper.setCommonFieldProperty(dateField, writable, name, required);
    return dateField;
  }

  @Override
  public String getFieldValue(String formPropertyId, Form form) {
    PopupDateField dateField = (PopupDateField) form.getField(formPropertyId);
    Date selectedDate = (Date) dateField.getValue();

    if (selectedDate != null) {
      SimpleDateFormat dateFormat = new SimpleDateFormat(localPattern);
      return dateFormat.format(selectedDate);
    }

    return null;
  }

  public Object convertFormValueToModelValue(String propertyValue) {
    if (propertyValue == null || "".equals(propertyValue)) {
      return null;
    }
    Object date = tryParse(localPattern, propertyValue);
    return date;
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
  public boolean usePattern() {
    return true;
  }

  @Override
  public boolean useMap() {
    return false;
  }

  @Override
  public FieldConstructor createConstructorOfField() {
    return new DateFFT();
  }

  @Override
  public void setMap(Map<String, String> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setPattern(String patternText) {
    localPattern = patternText;
    this.dateFormat = new SimpleDateFormat(localPattern);
  }

}

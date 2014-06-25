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
import ru.codeinside.gses.beans.DirectoryBeanProvider;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//TODO: а как же спиоск вариантов?!
public class DirectoryFFT implements FieldFormType, FieldConstructor, Serializable {

  private static final long serialVersionUID = -6022623500161277978L;


  @Override
  public String getFromType() {
    return "directory";
  }

  private Map<String, String> values;


  public DirectoryFFT() {
    values = new HashMap<String, String>();
  }

  public DirectoryFFT(Map<String, String> values) {
    this.values = values;
  }

  @Override
  public Field createField(String name, final String value, Layout layout, boolean writable, boolean required) {
    if (values.get("directory_id") == null) {
      return new ReadOnly("Неверно указан directory_id");
    }
    String directoryId = values.get("directory_id").trim();
    if (writable) {
      DirectoryField field = new DirectoryField(directoryId, name);
      field.setValue(value);
      FieldHelper.setCommonFieldProperty(field, true, name, required);
      return field;
    }
    String trimValue = value == null ? null : value.trim();
    String kName = DirectoryBeanProvider.get().getValue(directoryId, trimValue);
    if (StringUtils.isEmpty(kName)) {
      kName = trimValue;
    }
    ReadOnly readOnly = new ReadOnly(kName);
    FieldHelper.setCommonFieldProperty(readOnly, false, name, required);
    return readOnly;
  }

  @Override
  public String getFieldValue(String formPropertyId, Form form) {
    Field field = form.getField(formPropertyId);
    Object value = field.getValue();
    if (value != null) {
      return value.toString();
    }
    return null;
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
    return new DirectoryFFT(values);
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

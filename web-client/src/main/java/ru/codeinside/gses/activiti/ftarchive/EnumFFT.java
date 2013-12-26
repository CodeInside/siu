/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.ftarchive.helpers.FieldHelper;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//TODO: а как же спиоск вариантов?!
public class EnumFFT implements FieldFormType, FieldConstructor, Serializable {

  private static final long serialVersionUID = -6022623500161277978L;


  @Override
  public String getFromType() {
    return "enum";
  }

  private Map<String, String> values;


  public EnumFFT() {
    values = new HashMap<String, String>();
  }

  public EnumFFT(Map<String, String> values) {
    this.values = values;
  }

  @Override
  public Field createField(String name, String value, Layout layout, boolean writable, boolean required) {
    Field result;

      ComboBox comboBox = new ComboBox(name);
      for (java.util.Map.Entry<String, String> enumEntry : values.entrySet()) {
        comboBox.addItem(enumEntry.getKey());
        if (enumEntry.getValue() != null) {
          comboBox.setItemCaption(enumEntry.getKey(), enumEntry.getValue());
        }
      }
      comboBox.setValue(value);
      comboBox.setImmediate(true);// важно!
      result = comboBox;
      comboBox.setReadOnly(!writable);

    FieldHelper.setCommonFieldProperty(result, writable, name, required);
    return result;
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
    return new EnumFFT(values);
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

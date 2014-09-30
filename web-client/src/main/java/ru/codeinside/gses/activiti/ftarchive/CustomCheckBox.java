/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.CheckBox;
import ru.codeinside.gses.vaadin.customfield.CustomField;

import java.lang.reflect.Method;


public class CustomCheckBox extends CustomField {

  private final CheckBox checkBox;

  public CustomCheckBox() {
    checkBox = new CheckBox();
    checkBox.setImmediate(true);
    setCompositionRoot(checkBox);
  }

  @Override
  public Class<?> getType() {
    return Void.class;
  }

  @Override
  public Object getValue() {
    Object value = checkBox.getValue();
    if (this.isRequired()) {
      return Boolean.TRUE.equals(value) ? Boolean.TRUE : null;
    } else {
      return value == null ? Boolean.FALSE : value; // #1228 - считать NULL как false
    }
  }

  @Override
  public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
    super.setValue(newValue);
    checkBox.setValue(newValue);
  }

  @Override
  public void setRequired(boolean required) {
    super.setRequired(required);
    checkBox.setRequired(required);
  }

  @Override
  public void setReadOnly(boolean readOnly) {
    super.setReadOnly(readOnly);
    checkBox.setReadOnly(readOnly);
  }

  @Override
  public String getDescription() {
    return checkBox.getDescription();
  }

  @Override
  public void setDescription(String description) {
    checkBox.setDescription(description);
  }

  @Override
  public void addListener(ValueChangeListener listener) {
    checkBox.addListener(listener);
  }


  public void addListener(Class<?> eventType, Object target, Method method) {
    checkBox.addListener(eventType, target, method);
  }

  public void addListener(Class<?> eventType, Object target, String methodName) {
    checkBox.addListener(eventType, target, methodName);
  }

  @Override
  public String getRequiredError() {
    return checkBox.getRequiredError();
  }

  @Override
  public void setRequiredError(String requiredMessage) {
    super.setRequiredError(requiredMessage);
    checkBox.setRequiredError(requiredMessage);
  }

  @Override
  public void addListener(Listener listener) {
    checkBox.addListener(listener);
  }

}

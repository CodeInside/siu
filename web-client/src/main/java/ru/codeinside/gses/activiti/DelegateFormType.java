/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import org.activiti.engine.impl.form.AbstractFormType;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;

final public class DelegateFormType extends AbstractFormType {

  private final FieldFormType type;
  private FieldConstructor fieldConstructor;

  public DelegateFormType(FieldFormType type) {
    this.type = type;
  }

  public DelegateFormType(FieldFormType type, FieldConstructor fieldConstructor) {
    this.type = type;
    this.fieldConstructor = fieldConstructor;
  }

  public FieldFormType getType() {
    return type;
  }

  public FieldConstructor getFieldConstructor() {
    if (fieldConstructor == null) {
      throw new RuntimeException("Нельзя вызывать не инстанцированный AbstractFormType для " + type);
    }
    return fieldConstructor;
  }

  @Override
  public String getName() {
    return type.getFromType();
  }

  @Override
  public String convertModelValueToFormValue(Object modelValue) {
    return type.convertModelValueToFormValue(modelValue);
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    return type.convertFormValueToModelValue(propertyValue);
  }
}

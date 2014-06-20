/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import org.activiti.engine.impl.form.AbstractFormType;
import ru.codeinside.gses.activiti.forms.FieldConstructor;
import ru.codeinside.gses.activiti.forms.FieldConstructorBuilder;
import ru.codeinside.gses.activiti.forms.ValueType;

import java.util.Map;

final public class DelegateFormType extends AbstractFormType implements ValueType {

  private final FieldConstructor constructor;
  private final String name;

  public DelegateFormType(String name, FieldConstructor constructor) {
    this.name = name;
    this.constructor = constructor;
  }

  public FieldConstructor getConstructor() {
    return constructor;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String convertModelValueToFormValue(Object modelValue) {
    return constructor.convertModelValueToFormValue(modelValue);
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    return constructor.convertFormValueToModelValue(propertyValue);
  }

  public DelegateFormType withParams(String patternText, Map<String, String> values) {
    if (constructor instanceof FieldConstructorBuilder) {
      FieldConstructorBuilder withParams = (FieldConstructorBuilder) constructor;
      return new DelegateFormType(name, withParams.create(patternText, values));
    } else {
      return this;
    }
  }
}

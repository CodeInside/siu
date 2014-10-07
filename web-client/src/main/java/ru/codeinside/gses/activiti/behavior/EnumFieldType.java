/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import commons.Named;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.el.FixedValue;
import org.apache.commons.lang.StringUtils;

final class EnumFieldType<T extends Enum<T> & Named> implements FieldType<T> {
  final String name;
  final Class<T> type;

  public EnumFieldType(String name, Class<T> type) {
    this.name = name;
    this.type = type;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Usage getUsage() {
    return Usage.REQUIRED;
  }

  @Override
  public Field<T> createField(final Expression expression) {
    if (expression == null) {
      throw missedValueException();
    }
    if (expression instanceof FixedValue) {
      return StaticField.of(cast(expression.getValue(null)));
    }
    return new Field<T>() {
      @Override
      public T getValue(VariableScope scope) {
        return cast(expression.getValue(scope));
      }
    };
  }

  T cast(Object object) {
    if (object == null) {
      throw missedValueException();
    }
    String value = StringUtils.trimToNull(object.toString());
    for (T name : type.getEnumConstants()) {
      if (name.getName().equalsIgnoreCase(value)) {
        return name;
      }
    }
    throw new IllegalArgumentException(String.format(
      "Значение {%s} не совместимо с типом поля {%s}",
      value, name
    ));
  }

  IllegalArgumentException missedValueException() {
    return new IllegalArgumentException(String.format(
      "Пропущено значения поля {%s}", name
    ));
  }
}

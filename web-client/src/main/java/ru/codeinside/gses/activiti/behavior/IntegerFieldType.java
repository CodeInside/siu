/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.el.FixedValue;
import org.apache.commons.lang.StringUtils;

final class IntegerFieldType implements FieldType<Integer> {
  final String name;
  final int defaultValue;

  public IntegerFieldType(String name, int defaultValue) {
    this.name = name;
    this.defaultValue = defaultValue;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Usage getUsage() {
    return Usage.OPTIONAL;
  }

  @Override
  public Field<Integer> createField(final Expression expression) {
    if (expression == null) {
      return StaticField.of(defaultValue);
    }
    if (expression instanceof FixedValue) {
      return StaticField.of(cast(expression.getValue(null)));
    }
    return new Field<Integer>() {
      @Override
      public Integer getValue(VariableScope scope) {
        return cast(expression.getValue(scope));
      }
    };
  }

  int cast(Object object) {
    if (object == null) {
      throw missedValueException();
    }
    if (object instanceof Number) {
      return ((Number) object).intValue();
    }
    String value = StringUtils.trimToNull(object.toString());
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(String.format(
        "Значение {%s} не совместимо с типом поля {%s}",
        value, name
      ));
    }
  }

  IllegalArgumentException missedValueException() {
    return new IllegalArgumentException(String.format(
      "Пропущено значения поля {%s}", name
    ));
  }
}

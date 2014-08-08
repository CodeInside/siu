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

final class StringFieldType implements FieldType<String> {
  final String name;
  final Usage usage;

  public StringFieldType(String name, Usage usage) {
    this.name = name;
    this.usage = usage;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Usage getUsage() {
    return usage;
  }

  @Override
  public Field<String> createField(final Expression expression) {
    if (expression == null) {
      if (usage == Usage.OPTIONAL) {
        return StaticField.of(null);
      }
      throw missedValueException();
    }
    if (expression instanceof FixedValue) {
      return StaticField.of(cast(expression.getValue(null)));
    }
    return new Field<String>() {
      @Override
      public String getValue(VariableScope scope) {
        return cast(expression.getValue(scope));
      }
    };
  }

  String cast(Object object) {
    if (object == null) {
      throw missedValueException();
    }
    String value = StringUtils.trimToNull(object.toString());
    if (value == null) {
      throw missedValueException();
    }
    return value;
  }

  IllegalArgumentException missedValueException() {
    return new IllegalArgumentException(String.format(
      "Пропущено значение поля {%s}", name
    ));
  }
}

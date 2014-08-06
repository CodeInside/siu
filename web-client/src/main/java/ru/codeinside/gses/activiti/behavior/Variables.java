/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import com.google.common.base.Joiner;
import commons.Named;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.VariableScope;
import org.apache.commons.lang.StringUtils;

final class Variables {

  final VariableScope scope;

  Variables(VariableScope scope) {
    this.scope = scope;
  }

  public int integer(Expression expression, int defaultValue) {
    if (expression == null) {
      return defaultValue;
    }
    Object object = expression.getValue(scope);
    if (object == null) {
      return defaultValue;
    }
    if (object instanceof Number) {
      return ((Number) object).intValue();
    }
    String value = StringUtils.trimToNull(object.toString());
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Ошибка в числовом выражении '" + expression + "'");
    }
  }

  public String string(Expression expression) {
    Object object = expression.getValue(scope);
    if (!(object instanceof String)) {
      throw new IllegalArgumentException("Ошибка типа в выражении '" + expression.getExpressionText() + "'");
    }
    String value = StringUtils.trimToNull((String) object);
    if (value == null) {
      throw new IllegalArgumentException("Отсутсвует значение в выражении '" + expression.getExpressionText() + "'");
    }
    return value;
  }

  public <T extends Named> T named(Expression expression, T[] names) {
    String value = string(expression);
    for (T name : names) {
      if (name.getName().equalsIgnoreCase(value)) {
        return name;
      }
    }
    throw new IllegalArgumentException(String.format(
      "Значение выражения '%s' = '%s' не входит в диапазон значений {%s}",
      expression.getExpressionText(), value, Joiner.on(" | ").join(names)
    ));
  }

}

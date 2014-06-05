/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.duration;

import org.apache.commons.lang.StringUtils;

/**
 * Разбор выражения, в котором заданы длительность периода выполнения задач
 */
public class DurationPreferenceParser {

  public static void parseTaskPreference(String expression, DurationPreference result) throws IllegalDurationExpression {
    if (StringUtils.isNotBlank(expression)) {
      String[] expressions = expression.split("/");
      if (expressions.length == 3) {
        try {
          result.notificationPeriod = Integer.parseInt(expressions[0]);
          result.executionPeriod = Integer.parseInt(expressions[1]);
          result.inactivePeriod = Integer.parseInt(expressions[2]);
          result.dataExists = true;
        } catch (NumberFormatException e) {
          throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s\n Причина: \n%s", expression, e.getMessage()));
        }
      } else {
        throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s\n Причина: \n%s", expression, "В выражении должно быть задано 3 периода. Подробности см. в документации"));
      }
    } else {
      throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s", expression));
    }
  }

  public static void parseProcessPreference(String expression, DurationPreference result) throws IllegalDurationExpression {
    if (StringUtils.isNotBlank(expression)) {
      String[] expressions = expression.split("/");
      if (expressions.length == 2) {
        try {
          result.notificationPeriod = Integer.parseInt(expressions[0]);
          result.executionPeriod = Integer.parseInt(expressions[1]);
          result.dataExists = true;
        } catch (NumberFormatException e) {
          throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s\n Причина: \n%s", expression, e.getMessage()));
        }
      } else {
        throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s\n Причина: \n%s", expression, "В выражении должно быть задано 2 периода. Подробности см. в документации"));
      }
    } else {
      throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s", expression));
    }
  }

  public static void parseTaskDefaultPreference(String expression, DurationPreference result) throws IllegalDurationExpression {
    if (StringUtils.isNotBlank(expression)) {
      String[] expressions = expression.split("/");
      if (expressions.length == 2) {
        try {
          result.defaultNotificationPeriod = Integer.parseInt(expressions[0]);
          result.defaultExecutionPeriod = Integer.parseInt(expressions[1]);
          result.defaultDataExists = true;
        } catch (NumberFormatException e) {
          throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s\n Причина: \n%s", expression, e.getMessage()));
        }
      } else {
        throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s\n Причина: \n%s", expression, "В выражении должно быть задано 2 периода. Подробности см. в документации"));
      }
    } else {
      throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s", expression));
    }
  }

  public static void parseWorkedDaysPreference(String expression, DurationPreference result) {
    result.workedDays = "w".equals(expression);
  }
}

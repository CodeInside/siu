/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.duration;

import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.TaskDates;
import ru.codeinside.calendar.DueDateCalculator;

import java.io.Serializable;
import java.util.Date;

/**
 * Граничные периоды выполнения процессов и задач
 */
public class DurationPreference implements Serializable {

  /**
   * длительность интервала в днях, по истечении которого необходимо выслать оповещение о приближающейся просрочке
   * выполнения задачи
   */
  public int notificationInterval;

  /**
   * длительность интервала в днях, которое отведено на выполнение задачи
   */
  public int executionInterval;

  /**
   * длительность интервала в днях, в течении которого задача может не поступмить на исполнение.
   */
  public int inactiveInterval;

  /**
   * длительность интервала в днях, по истечении которого необходимо выслать оповещение о приближающейся просрочке
   * выполнения задачи, по умолчанию
   */
  public int defaultNotificationInterval;

  /**
   * длительность интервала в днях, которое отведено на выполнение задачи, по умолчанию
   */
  public int defaultExecutionInterval;

  /**
   * дни указываются в рабочих или календарных днях
   */
  public boolean workedDays;

  /**
   * наличие данных о сроках
   */
  public boolean dataExists;

  /**
   * наличие данных о сроках по умолчанию
   */
  public boolean defaultDataExists;


  /**
   * Инициализация контрольных дат задачи
   *
   * @param taskDates контрольные даты задачи
   */
  public void updateTaskDates(TaskDates taskDates, LazyCalendar lazyCalendar) {
    Date startDate = taskDates.getStartDate();
    if (dataExists) {
      DueDateCalculator calendar = lazyCalendar.getCalendar(workedDays);
      taskDates.setInactionDate(calendar.calculate(startDate, inactiveInterval));
      taskDates.setRestDate(calendar.calculate(startDate, notificationInterval));
      taskDates.setMaxDate(calendar.calculate(startDate, executionInterval));
      taskDates.setWorkedDays(workedDays);
    } else {
      Bid bid = taskDates.getBid();
      if (bid.getDefaultRestInterval() != null && bid.getDefaultMaxInterval() != null) {
        DueDateCalculator calendar = lazyCalendar.getCalendar(bid.getWorkedDays());
        taskDates.setRestDate(calendar.calculate(startDate, bid.getDefaultRestInterval())); // inactive interval
        taskDates.setMaxDate(calendar.calculate(startDate, bid.getDefaultMaxInterval())); // execution interval
        taskDates.setWorkedDays(bid.getWorkedDays());
      }
    }
  }

  public void updateProcessDates(Bid bid, LazyCalendar lazyCalendar) {
    DueDateCalculator calculator = lazyCalendar.getCalendar(workedDays);
    bid.setWorkedDays(workedDays);
    if (dataExists) {
      bid.setRestDate(calculator.calculate(bid.getDateCreated(), notificationInterval));
      bid.setMaxDate(calculator.calculate(bid.getDateCreated(), executionInterval));
    }
    if (defaultDataExists) {
      bid.setDefaultRestInterval(defaultNotificationInterval);
      bid.setDefaultMaxInterval(defaultExecutionInterval);
    }
  }


  public void parseTaskPreference(String expression) throws IllegalDurationExpression {
    if (StringUtils.isNotBlank(expression)) {
      String[] expressions = expression.split("/");
      if (expressions.length == 3) {
        try {
          inactiveInterval = Integer.parseInt(expressions[0]);
          notificationInterval = Integer.parseInt(expressions[1]);
          executionInterval = Integer.parseInt(expressions[2]);
          dataExists = true;
        } catch (NumberFormatException e) {
          throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s\n Причина: \n%s", expression, e.getMessage()));
        }
      } else {
        throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s\n Причина: \n%s", expression, "В выражении должно быть задано 3 интервала. Подробности см. в документации"));
      }
    } else {
      throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s", expression));
    }
  }

  public void parseProcessPreference(String expression) throws IllegalDurationExpression {
    if (StringUtils.isNotBlank(expression)) {
      String[] expressions = expression.split("/");
      if (expressions.length == 2) {
        try {
          notificationInterval = Integer.parseInt(expressions[0]);
          executionInterval = Integer.parseInt(expressions[1]);
          dataExists = true;
        } catch (NumberFormatException e) {
          throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s\n Причина: \n%s", expression, e.getMessage()));
        }
      } else {
        throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s\n Причина: \n%s", expression, "В выражении должно быть задано 2 интервала. Подробности см. в документации"));
      }
    } else {
      throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s", expression));
    }
  }

  public void parseTaskDefaultPreference(String expression) throws IllegalDurationExpression {
    if (StringUtils.isNotBlank(expression)) {
      String[] expressions = expression.split("/");
      if (expressions.length == 2) {
        try {
          defaultNotificationInterval = Integer.parseInt(expressions[0]);
          defaultExecutionInterval = Integer.parseInt(expressions[1]);
          defaultDataExists = true;
        } catch (NumberFormatException e) {
          throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s\n Причина: \n%s", expression, e.getMessage()));
        }
      } else {
        throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s\n Причина: \n%s", expression, "В выражении должно быть задано 2 интервала. Подробности см. в документации"));
      }
    } else {
      throw new IllegalDurationExpression(String.format("Ошибка при разборе выражения ==> %s", expression));
    }
  }

  public void parseWorkedDaysPreference(String expression) {
    workedDays = "w".equals(expression);
  }

}

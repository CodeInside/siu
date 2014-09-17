/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.api.duration;

import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.TaskDates;
import ru.codeinside.calendar.DueDateCalculator;

import java.io.Serializable;
import java.util.Date;

// TODO: разделить на API и реализацию

/**
 * Граничные периоды выполнения заявок и этапов
 */
public class DurationPreference implements Serializable {

  /**
   * длительность интервала в днях, по истечении которого необходимо выслать оповещение о приближающейся просрочке
   * выполнения этапа
   */
  public int notificationInterval;

  /**
   * длительность интервала в днях, которое отведено на выполнение этапа
   */
  public int executionInterval;

  /**
   * длительность интервала в днях, в течении которого этап может не поступить на исполнение.
   */
  public int inactiveInterval;

  /**
   * длительность интервала в днях, по истечении которого необходимо выслать оповещение о приближающейся просрочке
   * выполнения этапа, по умолчанию
   */
  public int defaultNotificationInterval;

  /**
   * длительность интервала в днях, которое отведено на выполнение этапа, по умолчанию
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
  public void initializeTaskDates(TaskDates taskDates, LazyCalendar lazyCalendar) {
    if (dataExists) {
      Date startDate = taskDates.getStartDate();
      DueDateCalculator calendar = lazyCalendar.getCalendar(workedDays);
      taskDates.setInactionDate(calendar.calculate(startDate, inactiveInterval));
      taskDates.setRestDate(calendar.calculate(startDate, notificationInterval));
      taskDates.setMaxDate(calendar.calculate(startDate, executionInterval));
      taskDates.setWorkedDays(workedDays);
    } else {
      Bid bid = taskDates.getBid();
      if (bid.hasDefaultInterval()) {
        Date startDate = taskDates.getStartDate();
        DueDateCalculator calendar = lazyCalendar.getCalendar(bid.getWorkedDays());
        taskDates.setRestDate(calendar.calculate(startDate, bid.getDefaultRestInterval()));
        taskDates.setMaxDate(calendar.calculate(startDate, bid.getDefaultMaxInterval()));
        taskDates.setWorkedDays(bid.getWorkedDays());
      }
    }
  }

  /**
   * Обновление контрольных дат этапа по производственному каледарю.
   */
  public boolean updateTaskDates(TaskDates taskDates, LazyCalendar lazyCalendar) {
    if (dataExists && workedDays && taskDates.getWorkedDays()) {
      Date start = taskDates.getStartDate();
      DueDateCalculator calendar = lazyCalendar.getCalendar(true);
      taskDates.setInactionDate(calendar.calculate(start, inactiveInterval));
      taskDates.setRestDate(calendar.calculate(start, notificationInterval));
      taskDates.setMaxDate(calendar.calculate(start, executionInterval));
      return true;
    }
    if (!dataExists && taskDates.getBid().hasDefaultWorkInterval()) {
      Date start = taskDates.getStartDate();
      Bid bid = taskDates.getBid();
      DueDateCalculator calendar = lazyCalendar.getCalendar(true);
      taskDates.setRestDate(calendar.calculate(start, bid.getDefaultRestInterval()));
      taskDates.setMaxDate(calendar.calculate(start, bid.getDefaultMaxInterval()));
      return true;
    }
    return false;
  }


  /**
   * Инициализация контрольных дат заявки и интервалов задач по умолчанию
   *
   * @param bid новая заявка
   */
  public void initializeProcessDates(Bid bid, LazyCalendar lazyCalendar) {
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

  /**
   * Обновление контрольных дат заявки по производственному календарю.
   */
  public boolean updateProcessDates(Bid bid, LazyCalendar lazyCalendar) {
    if (dataExists && workedDays && bid.getWorkedDays()) {
      DueDateCalculator calendar = lazyCalendar.getCalendar(true);
      bid.setRestDate(calendar.calculate(bid.getDateCreated(), notificationInterval));
      bid.setMaxDate(calendar.calculate(bid.getDateCreated(), executionInterval));
      return true;
    }
    return false;
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

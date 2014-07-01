/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.api.duration;

import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.TaskDates;
import ru.codeinside.calendar.DueDateCalculator;

import java.io.Serializable;

// TODO: разделить на API и реализацию

/**
 * Граничные периоды выполнения процессов и задач
 */
public class DurationPreference implements Serializable {
  /**
   * длительность периода в днях, по истечении которого необходимо выслать оповещение о приближающейся просрочке
   * выполнения задачи
   */
  public int notificationPeriod;
  /**
   * длительность периода в днях, которое отведено на выполнение задачи
   */
  public int executionPeriod;
  /**
   * длительность периода в днях, в течении которого задача может не выполняться.
   */
  public int inactivePeriod;
  /**
   * длительность периода в днях, по истечении которого необходимо выслать оповещение о приближающейся просрочке
   * выполнения задачи, по умолчанию
   */
  public int defaultNotificationPeriod;
  /**
   * длительность периода в днях, которое отведено на выполнение задачи, по умолчанию
   */
  public int defaultExecutionPeriod;
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


  public void updateInActionTaskDate(TaskDates task) {
    if (dataExists) {
      DueDateCalculator calculator = AdminServiceProvider.get().getCalendarBasedDueDateCalculator(workedDays);
      task.setInactionDate(calculator.calculate(task.getStartDate(), inactivePeriod));
      task.setWorkedDays(workedDays);
    }
  }

  public void updateExecutionsDate(TaskDates task) {
    DueDateCalculator endDateCalculator;
    if (dataExists) {
      endDateCalculator = AdminServiceProvider.get().getCalendarBasedDueDateCalculator(workedDays);
      task.setRestDate(endDateCalculator.calculate(task.getAssignDate(), notificationPeriod));
      task.setMaxDate(endDateCalculator.calculate(task.getAssignDate(), executionPeriod));
      task.setWorkedDays(workedDays);
      return;
    }
    Bid bid = task.getBid();
    if (bid.getDefaultRestInterval() != null && bid.getDefaultMaxInterval() != null) {
      endDateCalculator = AdminServiceProvider.get().getCalendarBasedDueDateCalculator(bid.getWorkedDays());
      task.setRestDate(endDateCalculator.calculate(task.getAssignDate(), bid.getDefaultRestInterval()));
      task.setMaxDate(endDateCalculator.calculate(task.getAssignDate(), bid.getDefaultMaxInterval()));
      task.setWorkedDays(bid.getWorkedDays());
    }
  }

  public void updateExecutionDatesForProcess(Bid bid) {
    DueDateCalculator calculator = AdminServiceProvider.get().getCalendarBasedDueDateCalculator(workedDays);
    bid.setWorkedDays(workedDays);
    if (dataExists) {
      bid.setRestDate(calculator.calculate(bid.getDateCreated(), notificationPeriod));
      bid.setMaxDate(calculator.calculate(bid.getDateCreated(), executionPeriod));
    }
    if (defaultDataExists) {
      bid.setDefaultRestInterval(defaultNotificationPeriod);
      bid.setDefaultMaxInterval(defaultExecutionPeriod);
    }
  }


  public void parseTaskPreference(String expression) throws IllegalDurationExpression {
    if (StringUtils.isNotBlank(expression)) {
      String[] expressions = expression.split("/");
      if (expressions.length == 3) {
        try {
          notificationPeriod = Integer.parseInt(expressions[0]);
          executionPeriod = Integer.parseInt(expressions[1]);
          inactivePeriod = Integer.parseInt(expressions[2]);
          dataExists = true;
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

  public void parseProcessPreference(String expression) throws IllegalDurationExpression {
    if (StringUtils.isNotBlank(expression)) {
      String[] expressions = expression.split("/");
      if (expressions.length == 2) {
        try {
          notificationPeriod = Integer.parseInt(expressions[0]);
          executionPeriod = Integer.parseInt(expressions[1]);
          dataExists = true;
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

  public void parseTaskDefaultPreference(String expression) throws IllegalDurationExpression {
    if (StringUtils.isNotBlank(expression)) {
      String[] expressions = expression.split("/");
      if (expressions.length == 2) {
        try {
          defaultNotificationPeriod = Integer.parseInt(expressions[0]);
          defaultExecutionPeriod = Integer.parseInt(expressions[1]);
          defaultDataExists = true;
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

  public void parseWorkedDaysPreference(String expression) {
    workedDays = "w".equals(expression);
  }

}

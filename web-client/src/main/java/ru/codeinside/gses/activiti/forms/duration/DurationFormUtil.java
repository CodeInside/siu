/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.duration;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.TaskDates;
import ru.codeinside.calendar.DueDateCalculator;

/**
 * Утилиты для упрощения работы с вычислением конечных дат
 */
public class DurationFormUtil {
  public static DueDateCalculator getDueDateCalculator(String propertyName) {   // todo дублирование кода см ru.codeinside.adm.AdminServiceImpl.getDueDateCalculatorByPropertyName()
    return AdminServiceProvider.get().getCalendarBasedDueDateCalculator("w".equals(propertyName));
  }

  public static void updateInActionTaskDate(TaskDates task, DurationPreference durationPreference) {
    if (durationPreference.dataExists) {
      DueDateCalculator calculator = AdminServiceProvider.get().getCalendarBasedDueDateCalculator(durationPreference.workedDays);
      task.setInactionDate(calculator.calculate(task.getStartDate(), durationPreference.inactivePeriod));
      task.setWorkedDays(durationPreference.workedDays);
    }
  }

  public static void updateExecutionsDate(TaskDates task, DurationPreference durationPreference) {
    DueDateCalculator endDateCalculator;
    if (durationPreference.dataExists) {
      endDateCalculator = AdminServiceProvider.get().getCalendarBasedDueDateCalculator(durationPreference.workedDays);
      task.setRestDate(endDateCalculator.calculate(task.getAssignDate(), durationPreference.notificationPeriod));
      task.setMaxDate(endDateCalculator.calculate(task.getAssignDate(), durationPreference.executionPeriod));
      task.setWorkedDays(durationPreference.workedDays);
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

  public static void updateExecutionDatesForProcess(Bid bid, DurationPreference durationPreference) {
    DueDateCalculator calculator = AdminServiceProvider.get().getCalendarBasedDueDateCalculator(durationPreference.workedDays);
    bid.setWorkedDays(durationPreference.workedDays);
    if (durationPreference.dataExists) {
      bid.setRestDate(calculator.calculate(bid.getDateCreated(), durationPreference.notificationPeriod));
      bid.setMaxDate(calculator.calculate(bid.getDateCreated(), durationPreference.executionPeriod));
    }
    if (durationPreference.defaultDataExists) {
      bid.setDefaultRestInterval(durationPreference.defaultNotificationPeriod);
      bid.setDefaultMaxInterval(durationPreference.defaultExecutionPeriod);
    }
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.duration;

import org.activiti.engine.impl.form.FormPropertyHandler;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.calendar.DueDateCalculator;

import java.util.List;

/**
 * Утилиты для упрощения работы с вычислением конечных дат
 */
public class DurationFormUtil {
  public static DueDateCalculator getDueDateCalculator(String propertyName) {   // todo дублирование кода см ru.codeinside.adm.AdminServiceImpl.getDueDateCalculatorByPropertyName()
    DueDateCalculator calculator;
    if ("w".equals(propertyName)) {
      calculator = AdminServiceProvider.get().getBusinessCalendarBasedDueDateCalculator();
    } else {
      calculator = AdminServiceProvider.get().getCalendarBasedDueDateCalculator();
    }
    return calculator;
  }

  public static FormPropertyHandler searchFormDurationRestriction(List<FormPropertyHandler> formPropertyHandlers) {
    for (FormPropertyHandler formPropertyHandler : formPropertyHandlers) {
      if ("!".equals(formPropertyHandler.getId())) {
        return formPropertyHandler;
      }
    }
    return null;
  }

  public static boolean isBusinessDaysUsed(String propertyName) {
    return "w".equals(propertyName);
  }
}

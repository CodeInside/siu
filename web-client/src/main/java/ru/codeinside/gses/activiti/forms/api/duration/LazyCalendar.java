/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.api.duration;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.calendar.CalendarBasedDueDateCalculator;
import ru.codeinside.calendar.DueDateCalculator;

import java.io.Serializable;

/**
 * Ленивое создание календарей.
 */
final public class LazyCalendar implements Serializable {

  private static DueDateCalculator generalCalendar = new CalendarBasedDueDateCalculator();

  private DueDateCalculator workCalendar;

  public DueDateCalculator getCalendar(boolean work) {
    return work ? getWorkCalendar() : getGeneralCalendar();
  }

  private DueDateCalculator getGeneralCalendar() {
    return generalCalendar;
  }

  private DueDateCalculator getWorkCalendar() {
    if (workCalendar == null) {
      workCalendar = AdminServiceProvider.get().getCalendarBasedDueDateCalculator(true);
    }
    return workCalendar;
  }
}

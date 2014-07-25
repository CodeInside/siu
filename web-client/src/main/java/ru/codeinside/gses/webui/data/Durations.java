/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.TaskDates;
import ru.codeinside.calendar.DueDateCalculator;
import ru.codeinside.gses.activiti.forms.duration.LazyCalendar;

import java.util.Date;

import static ru.codeinside.gses.webui.utils.Components.stringProperty;

final public class Durations {

  final LazyCalendar lazyCalendar = new LazyCalendar();
  final Date currentDate = new Date();

  public void fillBidAndTask(Bid bid, TaskDates task, PropertysetItem item) {

    boolean bidPresent = bid != null && bid.getMaxDate() != null;
    int maxBidInterval;
    int curBidInterval;
    if (bidPresent) {
      DueDateCalculator calendar = lazyCalendar.getCalendar(bid.getWorkedDays());
      maxBidInterval = calendar.countDays(bid.getDateCreated(), bid.getMaxDate());
      curBidInterval = calendar.countDays(bid.getDateCreated(), currentDate);
    } else {
      maxBidInterval = 0;
      curBidInterval = 0;
    }

    boolean taskPresent = task != null && task.getMaxDate() != null;
    int maxTaskInterval;
    int curTaskInterval;
    if (taskPresent) {
      DueDateCalculator calendar = lazyCalendar.getCalendar(task.getWorkedDays());
      maxTaskInterval = calendar.countDays(task.getStartDate(), task.getMaxDate());
      curTaskInterval = calendar.countDays(task.getStartDate(), currentDate);
    } else {
      maxTaskInterval = 0;
      curTaskInterval = 0;
    }

    if (bidPresent) {
      item.addItemProperty("bidDays", durationText(maxBidInterval, curBidInterval, bid.getWorkedDays()));
    }

    if (taskPresent) {
      item.addItemProperty("taskDays", durationText(maxTaskInterval, curTaskInterval, task.getWorkedDays()));
    }

    int bidLevel = 0;
    if (bidPresent) {
      if (currentDate.after(bid.getMaxDate())) {
        bidLevel = 2;
      } else if (currentDate.after(bid.getRestDate())) {
        bidLevel = 1;
      }
      if (bidLevel > 0) {
        item.addItemProperty("bidStyle", stringProperty("highlight"));
      }
    }

    int taskLevel = 0;
    if (taskPresent) {
      if (currentDate.after(task.getMaxDate())) {
        taskLevel = 2;
      } else if (currentDate.after(task.getRestDate())) {
        taskLevel = 1;
      } else if (task.getAssignDate() == null && task.getInactionDate() != null && currentDate.after(task.getInactionDate())) {
        // точка бездействия может отсуствовать, если используются интервалы по умолчанию
        taskLevel = 1;
      }
      if (taskLevel > 0) {
        item.addItemProperty("taskStyle", stringProperty("highlight"));
      }
    }

    if (bidLevel > 0 || taskLevel > 0) {
      int level = Math.max(bidLevel, taskLevel);
      item.addItemProperty("style", stringProperty(level == 2 ? "highlight-red" : "highlight-rosy"));
    }

  }

  private ObjectProperty<String> durationText(int maxBidInterval, int curBidInterval, boolean workDays) {
    return stringProperty(
      curBidInterval + "/" + maxBidInterval + (workDays ? "р" : "k")
    );
  }

}

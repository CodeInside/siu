/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * Объектное отражение TaskListener.EVENTNAME_*
 */
public enum Event {
  Create, Assignment, Complete;

  static Event valueOf(final DelegateTask execution) {
    final Event event;
    if (TaskListener.EVENTNAME_CREATE.equals(execution.getEventName())) {
      event = Create;
    } else if (TaskListener.EVENTNAME_COMPLETE.equals(execution.getEventName())) {
      event = Complete;
    } else if (TaskListener.EVENTNAME_ASSIGNMENT.equals(execution.getEventName())) {
      event = Assignment;
    } else {
      event = null;
    }
    return event;
  }
}

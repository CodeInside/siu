/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import org.activiti.engine.impl.TaskQueryImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;

public class TaskQueryImpl2 extends TaskQueryImpl {

  private boolean ignoreAssignee;
  private boolean overdue;

  public TaskQueryImpl2() {
  }

  public TaskQueryImpl2(CommandExecutor commandExecutor) {
    super(commandExecutor);
  }

  public boolean getIgnoreAssignee() {
    return ignoreAssignee;
  }

  public void setIgnoreAssignee(boolean ignoreAssignee) {
    this.ignoreAssignee = ignoreAssignee;
  }

  public boolean getOverdue() {
    return overdue;
  }

  public void setOverdue(boolean overdue) {
    this.overdue = overdue;
  }
}

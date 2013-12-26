/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import java.io.Serializable;

/**
 * Идентификация формы по задаче либо по процессу.
 */
final public class FormID implements Serializable {
  private static final long serialVersionUID = 1L;
  final public String taskId;
  final public String processDefinitionId;

  private FormID(final String taskId, final String processDefinitionId) {
    this.taskId = taskId;
    this.processDefinitionId = processDefinitionId;
  }

  @Override
  public String toString() {
    return taskId != null ? taskId : processDefinitionId;
  }

  public static FormID byTaskId(String taskId) {
    if (taskId == null) {
      throw new NullPointerException();
    }
    return new FormID(taskId, null);
  }

  public static FormID byProcessDefinitionId(String processDefinitionId) {
    if (processDefinitionId == null) {
      throw new NullPointerException();
    }
    return new FormID(null, processDefinitionId);
  }

}


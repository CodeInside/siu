/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.history;

/**
 * После сохранения строковых значений в истории формы,
 * taskId теряется!
 */
final public class ExecutionId {
  final public String processDefinitionId;
  final public String processInstanceId;
  final public String executionId;
  final public String taskId;


  public ExecutionId(String processDefinitionId) {
    this.processDefinitionId = processDefinitionId;
    processInstanceId = null;
    executionId = null;
    taskId = null;
  }

  public ExecutionId(String processInstanceId, String executionId, String taskId) {
    processDefinitionId = null;
    this.processInstanceId = processInstanceId;
    this.executionId = executionId;
    this.taskId = taskId;
  }

  @Override
  public String toString() {
    return "{" +
      "processDefinitionId='" + processDefinitionId + '\'' +
      ", processInstanceId='" + processInstanceId + '\'' +
      ", executionId='" + executionId + '\'' +
      ", taskId='" + taskId + '\'' +
      '}';
  }
}

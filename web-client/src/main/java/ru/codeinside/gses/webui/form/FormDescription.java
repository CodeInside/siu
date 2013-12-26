/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.collect.ImmutableList;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.activiti.FormID;

final public class FormDescription {
  final public Task task;
  final public ProcessDefinition processDefinition;
  final public FormID id;
  final public ImmutableList<FormSeq> flow;
  final public String procedureName;


  public FormDescription(Task task, ProcessDefinition processDefinition, FormID id, ImmutableList<FormSeq> flow, String procedureName) {
    this.task = task;
    this.processDefinition = processDefinition;
    this.id = id;
    this.flow = flow;
    this.procedureName = procedureName;
  }
}

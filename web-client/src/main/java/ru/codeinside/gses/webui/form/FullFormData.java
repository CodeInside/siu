/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.activiti.FormDecorator;

final public class FullFormData {
  final Task task;
  final ProcessDefinition processDefinition;
  final public FormDecorator decorator;

  public FullFormData(Task task, ProcessDefinition processDefinition, FormDecorator decorator) {
    this.task = task;
    this.processDefinition = processDefinition;
    this.decorator = decorator;
  }
}

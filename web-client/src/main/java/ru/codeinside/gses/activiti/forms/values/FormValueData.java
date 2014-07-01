/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.values;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.api.values.FormValue;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;

import java.util.List;

public final class FormValueData implements FormValue {

  final Task task;
  final ProcessDefinition processDefinition;
  final List<PropertyValue<?>> values;
  final PropertyTree definition;

  public FormValueData(Task task, ProcessDefinition processDefinition, PropertyTree definition, List<PropertyValue<?>> values) {
    this.task = task;
    this.processDefinition = processDefinition;
    this.definition = definition;
    this.values = values;
  }

  @Override
  public Task getTask() {
    return task;
  }

  @Override
  public ProcessDefinition getProcessDefinition() {
    return processDefinition;
  }

  @Override
  public List<PropertyValue<?>> getPropertyValues() {
    return values;
  }


  @Override
  public PropertyTree getFormDefinition() {
    return definition;
  }
}

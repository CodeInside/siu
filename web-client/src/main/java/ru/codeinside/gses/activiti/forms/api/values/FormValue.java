/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.api.values;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface FormValue extends Serializable {

  Task getTask();

  ProcessDefinition getProcessDefinition();

  List<PropertyValue<?>> getPropertyValues();

  PropertyTree getFormDefinition();

  boolean isArchiveMode();
}

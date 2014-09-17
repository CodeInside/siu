/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.util.xml.Element;
import ru.codeinside.gses.activiti.forms.api.definitions.FormDefinitionProvider;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.definitions.FormParser;

import java.util.Map;

public class CustomTaskFormHandler implements TaskFormHandler, FormDefinitionProvider {

  PropertyTree propertyTree;

  @Override
  public void parseConfiguration(Element element, DeploymentEntity deployment,
                                 ProcessDefinitionEntity processDefinition, BpmnParse bpmnParse) {
    propertyTree = new FormParser().parseProperties(element, processDefinition, deployment, bpmnParse);
  }

  public TaskFormData createTaskForm(TaskEntity task) {
    throw new UnsupportedOperationException();
  }

  public TaskFormData createTaskForm(Map<String, String> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void submitFormProperties(Map<String, String> properties, ExecutionEntity execution) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PropertyTree getPropertyTree() {
    return propertyTree;
  }
}

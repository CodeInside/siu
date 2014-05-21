/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.form.DefaultTaskFormHandler;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.util.xml.Element;

import java.util.Map;

public class CustomTaskFormHandler extends DefaultTaskFormHandler implements CloneSupport {

  PropertyTree propertyTree;

  @Override
  public void parseConfiguration(final Element element,
                                 final DeploymentEntity deployment,
                                 final ProcessDefinitionEntity processDefinition,
                                 final BpmnParse bpmnParse) {
    super.parseConfiguration(element, deployment, processDefinition, bpmnParse);
    propertyTree = Builder.buildTree(element, formPropertyHandlers, bpmnParse);
  }

  public TaskFormData createTaskForm(TaskEntity task) {
    return createTaskForm(task, null);
  }

  public TaskFormData createTaskForm(Map<String, String> values) {
    return createTaskForm(null, values);
  }

  private TaskFormData createTaskForm(TaskEntity task, Map<String, String> values) {
    CustomTaskFormData taskFormData = new CustomTaskFormData();
    for (FormPropertyHandler formPropertyHandler : formPropertyHandlers) {
      if("!".equals(formPropertyHandler.getId())) {
        if (formPropertyHandler.getVariableExpression().getExpressionText() != null) {
          String[] expressions = formPropertyHandler.getVariableExpression().getExpressionText().split("/");
          if (expressions.length == 3) {
            try {
              int rest = Integer.parseInt(expressions[0]);
              int max = Integer.parseInt(expressions[1]);
              int inaction = Integer.parseInt(expressions[2]);
              taskFormData.restInterval = rest;
              taskFormData.maxInterval = max;
              taskFormData.inactionInterval = inaction;
            } catch (NumberFormatException e) {
              //
            }
          }
        }
        if ("w".equals(formPropertyHandler.getName())) {
          taskFormData.workDays = true;
        }
      }
    }
    taskFormData.setFormKey(formKey);
    taskFormData.setDeploymentId(deploymentId);
    taskFormData.setTask(task);
    final CloneTree cloneTree;
    if (task != null) {
      cloneTree = PropertyNodes.createFormProperties(propertyTree, formPropertyHandlers, task.getExecution());
    } else {
      cloneTree = PropertyNodes.createFormProperties(propertyTree, formPropertyHandlers, values);
    }
    taskFormData.setFormProperties(cloneTree.properties);
    taskFormData.setPropertyTree(propertyTree);
    taskFormData.setCloneTree(cloneTree);
    return taskFormData;
  }

  @Override
  public void submitFormProperties(Map<String, String> properties, ExecutionEntity execution) {
    PropertyNodes.submitFormProperties(propertyTree, formPropertyHandlers, properties, execution);
  }

  @Override
  public CloneTree cloneTree(ExecutionEntity entity, String pid, String path) {
    return PropertyNodes.cloneTree(propertyTree, formPropertyHandlers, entity, pid, path);
  }

  @Override
  public TypeTree getTypeTree() {
    return PropertyNodes.createTypeTree(propertyTree, formPropertyHandlers);
  }
}

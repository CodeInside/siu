/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
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
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.TaskDates;
import ru.codeinside.calendar.DueDateCalculator;
import ru.codeinside.gses.activiti.forms.duration.DurationFormUtil;
import ru.codeinside.gses.activiti.forms.duration.DurationPreference;
import ru.codeinside.gses.activiti.forms.duration.DurationPreferenceParser;
import ru.codeinside.gses.activiti.forms.duration.IllegalDurationExpression;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomTaskFormHandler extends DefaultTaskFormHandler implements CloneSupport {
  private static final Logger LOGGER = Logger.getLogger(CustomTaskFormHandler.class.getName());
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

  public void setInactionDate(TaskDates task) {
    DueDateCalculator calculator;
    FormPropertyHandler propertyWithDurationRestriction = DurationFormUtil.searchFormDurationRestriction(formPropertyHandlers);
    if (propertyWithDurationRestriction != null) {
      try {
        String expressionText = propertyWithDurationRestriction.getVariableExpression().getExpressionText();
        DurationPreferenceParser parser = new DurationPreferenceParser();
        DurationPreference preference = parser.parseTaskPreference(expressionText);
        calculator = DurationFormUtil.getDueDateCalculator(propertyWithDurationRestriction.getName());
        task.setInactionDate(calculator.calculate(task.getStartDate(), preference.inactivePeriod));
      } catch (IllegalDurationExpression err) {
        LOGGER.log(Level.SEVERE, err.getMessage(), err); // todo выдать в лог данные о задаче.
      }
    }
  }

  public void setExecutionDate(TaskDates task) {
    DueDateCalculator endDateCalculator;
    FormPropertyHandler propertyWithDurationRestriction = DurationFormUtil.searchFormDurationRestriction(formPropertyHandlers);
    DurationPreference preference = getDurationPreference();
    if (preference != null) {
      endDateCalculator = DurationFormUtil.getDueDateCalculator(propertyWithDurationRestriction.getName());
      task.setRestDate(endDateCalculator.calculate(task.getAssignDate(), preference.notificationPeriod));
      task.setMaxDate(endDateCalculator.calculate(task.getAssignDate(), preference.executionPeriod));
      return;
    }
    Bid bid = task.getBid();
    if (bid.getDefaultRestInterval() != null && bid.getDefaultMaxInterval() != null) {
      if (bid.getWorkDays()) {
        endDateCalculator = AdminServiceProvider.get().getBusinessCalendarBasedDueDateCalculator();
      } else {
        endDateCalculator = AdminServiceProvider.get().getCalendarBasedDueDateCalculator();
      }
      task.setRestDate(endDateCalculator.calculate(task.getAssignDate(), bid.getDefaultRestInterval()));
      task.setMaxDate(endDateCalculator.calculate(task.getAssignDate(), bid.getDefaultMaxInterval()));
    }
  }


  public DurationPreference getDurationPreference() {
    FormPropertyHandler propertyWithDurationRestriction = DurationFormUtil.searchFormDurationRestriction(formPropertyHandlers);
    if (propertyWithDurationRestriction != null) {
      try {
        String expressionText = propertyWithDurationRestriction.getVariableExpression().getExpressionText();
        DurationPreferenceParser parser = new DurationPreferenceParser();
        return parser.parseTaskPreference(expressionText);
      } catch (IllegalDurationExpression err) {
        LOGGER.log(Level.SEVERE, err.getMessage(), err); // todo выдать в лог данные о задаче.
        return null;
      }
    } else {
      return null;
    }
  }
}

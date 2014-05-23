/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.form.StartFormData;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.form.DefaultStartFormHandler;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.util.xml.Element;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.calendar.CalendarBasedDueDateCalculator;

import java.util.Map;

public class CustomStartFormHandler extends DefaultStartFormHandler implements CloneSupport {

  PropertyTree propertyTree;

  @Override
  public void parseConfiguration(final Element element,
                                 final DeploymentEntity deployment,
                                 final ProcessDefinitionEntity processDefinition,
                                 final BpmnParse bpmnParse) {
    super.parseConfiguration(element, deployment, processDefinition, bpmnParse);
    propertyTree = Builder.buildTree(element, formPropertyHandlers, bpmnParse);
  }

  @Override
  public StartFormData createStartFormData(ProcessDefinitionEntity processDefinition) {
    return createStartFormData(processDefinition, null);
  }

  public StartFormData createStartFormData(ProcessDefinitionEntity processDefinition, Map<String, String> values) {
    CustomStartFormData startFormData = new CustomStartFormData();
    startFormData.setFormKey(formKey);
    startFormData.setDeploymentId(deploymentId);
    startFormData.setProcessDefinition(processDefinition);
    final CloneTree cloneTree;
    if (values == null) {
      cloneTree = PropertyNodes.createFormProperties(propertyTree, formPropertyHandlers);
    } else {
      cloneTree = PropertyNodes.createFormProperties(propertyTree, formPropertyHandlers, values);
    }
    startFormData.setFormProperties(cloneTree.properties);
    startFormData.setPropertyTree(propertyTree);
    startFormData.setCloneTree(cloneTree);
    return startFormData;
  }

  @Override
  public void submitFormProperties(Map<String, String> properties, ExecutionEntity execution) {
    PropertyNodes.submitFormProperties(propertyTree, formPropertyHandlers, properties, execution);
  }

  @Override
  public CloneTree cloneTree(ExecutionEntity entity, String pid, String path) {
    return PropertyNodes.cloneTree(propertyTree, formPropertyHandlers, null, pid, path);
  }

  @Override
  public TypeTree getTypeTree() {
    return PropertyNodes.createTypeTree(propertyTree, formPropertyHandlers);
  }

  public void setExecutionDates(Bid bid) {
    for (FormPropertyHandler formPropertyHandler : formPropertyHandlers) {
      if ("!".equals(formPropertyHandler.getId())) {
        if ("w".equals(formPropertyHandler.getName())) {
          bid.setWorkDays(true);
        }
        if (formPropertyHandler.getVariableExpression().getExpressionText() != null) {
          String[] expressions = formPropertyHandler.getVariableExpression().getExpressionText().split("/");
          if (expressions.length == 2) {
            try {
              int rest = Integer.parseInt(expressions[0]);
              int max = Integer.parseInt(expressions[1]);
              CalendarBasedDueDateCalculator calculator = new CalendarBasedDueDateCalculator();
              bid.setRestDate(calculator.calculate(bid.getDateCreated(), rest));
              bid.setMaxDate(calculator.calculate(bid.getDateCreated(), max));
            } catch (NumberFormatException e) {
              //
            }
          }
        }
        if (formPropertyHandler.getDefaultExpression().getExpressionText() != null) {
          String[] expressions = formPropertyHandler.getDefaultExpression().getExpressionText().split("/");
          if (expressions.length == 2) {
            try {
              int rest = Integer.parseInt(expressions[0]);
              int max = Integer.parseInt(expressions[1]);
              bid.setDefaultRestInterval(rest);
              bid.setDefaultMaxInterval(max);
            } catch (NumberFormatException e) {
              //
            }
          }
        }
      }
    }
  }
}

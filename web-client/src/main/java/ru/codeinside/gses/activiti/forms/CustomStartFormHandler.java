/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.form.StartFormData;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.form.DefaultStartFormHandler;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.util.xml.Element;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.gses.activiti.forms.duration.DurationPreference;

import java.util.Map;

import static ru.codeinside.gses.activiti.forms.duration.DurationFormUtil.updateExecutionDatesForProcess;

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
    DurationPreference durationPreference = propertyTree.getDurationPreference();
    updateExecutionDatesForProcess(bid, durationPreference);
  }
}

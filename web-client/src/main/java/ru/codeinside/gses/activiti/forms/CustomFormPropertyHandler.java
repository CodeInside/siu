/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.form.FormPropertyImpl;
import org.activiti.engine.impl.form.StartFormVariableScope;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import java.util.Map;
import java.util.logging.Logger;

public class CustomFormPropertyHandler extends FormPropertyHandler {

  final Logger logger = Logger.getLogger(getClass().getName());

  /**
   * Основанно на коде submitFormProperty.
   */
  public void submitFormNode(PropertyNode node, ExecutionEntity execution, Map<String, String> properties) {

    if (!isWritable) {
      if (properties.containsKey(id)) {
        throw new ActivitiException("form property '" + id + "' is not writable");
      }
    }

    if (isRequired && !properties.containsKey(id) && defaultExpression == null) {
      throw new ActivitiException("form property '" + id + "' is required");
    }

    if (!node.isWritable()) {
      logger.info("skip " + id + " by #write=false");
      return;
    }

    Object modelValue = null;
    if (properties.containsKey(id)) {
      final String propertyValue = properties.remove(id);
      if (type != null) {
        modelValue = type.convertFormValueToModelValue(propertyValue);
      } else {
        modelValue = propertyValue;
      }
    } else if (defaultExpression != null) {
      final Object expressionValue = defaultExpression.getValue(execution);
      if (type != null && expressionValue != null) {
        modelValue = type.convertFormValueToModelValue(expressionValue.toString());
      } else if (expressionValue != null) {
        modelValue = expressionValue.toString();
      } else if (isRequired) {
        throw new ActivitiException("form property '" + id + "' is required");
      }
    }

    if (modelValue != null) {
      if (variableName != null) {
        execution.setVariable(variableName, modelValue);
      } else if (variableExpression != null) {
        variableExpression.setValue(modelValue, execution);
      } else {
        execution.setVariable(id, modelValue);
      }
    } else {
      applyNullValue(execution, node.getNullAction());
    }
  }

  private void applyNullValue(final ExecutionEntity execution, final NullAction nullAction) {
    if (!isWritable || NullAction.skip == nullAction) {
      return;
    }
    if (isRequired) {
      throw new ActivitiException("Значение для свойства '" + id + "' обязательно!");
    }
    if (NullAction.set == nullAction) {
      if (variableName != null) {
        execution.setVariable(variableName, null);
      } else if (variableExpression != null) {
        variableExpression.setValue(null, execution);
      } else {
        execution.setVariable(id, null);
      }
    } else if (NullAction.remove == nullAction) {
      if (variableName != null) {
        execution.removeVariable(variableName);
      } else if (variableExpression != null) {
        // в выражении не можем удалить, ставим NULL
        variableExpression.setValue(null, execution);
      } else {
        execution.removeVariable(id);
      }
    } else {
      throw new IllegalStateException("Не известное поведение для null");
    }
  }

  public FormProperty createFormProperty(ExecutionEntity execution, boolean readable) {
    FormPropertyImpl formProperty = new FormPropertyImpl(this);
    Object modelValue = null;
    if (!readable) {
      logger.info("skip variable for " + id + " by #read=false");
    }
    if (readable && execution != null) {
      if (variableName != null || variableExpression == null) {
        final String varName = variableName != null ? variableName : id;
        if (execution.hasVariable(varName)) {
          modelValue = execution.getVariable(varName);
        } else if (defaultExpression != null) {
          modelValue = defaultExpression.getValue(execution);
        }
      } else {
        modelValue = variableExpression.getValue(execution);
      }
    } else {
      if (defaultExpression != null) {
        modelValue = defaultExpression.getValue(StartFormVariableScope.getSharedInstance());
      }
    }
    if (modelValue instanceof String) {
      formProperty.setValue((String) modelValue);
    } else if (type != null) {
      String formValue = type.convertModelValueToFormValue(modelValue);
      formProperty.setValue(formValue);
    } else if (modelValue != null) {
      formProperty.setValue(modelValue.toString());
    }
    return formProperty;
  }
}



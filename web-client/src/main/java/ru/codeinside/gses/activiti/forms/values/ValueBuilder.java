/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.values;

import com.google.common.collect.ImmutableList;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.activiti.forms.api.definitions.BlockNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.api.values.FormValue;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;

import java.util.List;

final class ValueBuilder {

  String id;
  PropertyNode node;
  Object value;
  List<ValueBuilder> valueBuilders;
  AuditBuilder auditBuilder;

  FormValue toValues(Task task, ProcessDefinition processDefinition, PropertyTree propertyTree, boolean archiveMode) {
    List<PropertyValue<?>> values = toCollection();
    return new FormValueData(task, processDefinition, propertyTree, values, archiveMode);
  }

  List<PropertyValue<?>> toCollection() {
    ImmutableList.Builder<PropertyValue<?>> builder = ImmutableList.builder();
    for (ValueBuilder valueBuilderObject : valueBuilders) {
      PropertyValue<?> propertyValue = valueBuilderObject.toPropertyValue();
      builder.add(propertyValue);
    }
    return builder.build();
  }

  private PropertyValue<?> toPropertyValue() {
    ValueAudit audit = null;
    if (auditBuilder != null) {
      audit = new ValueAudit(auditBuilder.login, auditBuilder.verified, auditBuilder.ownerName, auditBuilder.organizationName);
    }
    if (node instanceof BlockNode) {
      ImmutableList<List<PropertyValue<?>>> build;
      if (valueBuilders == null) {
        build = ImmutableList.of();
      } else {
        ImmutableList.Builder<List<PropertyValue<?>>> builder = ImmutableList.builder();
        for (ValueBuilder valueBuilderClone : valueBuilders) {
          List<PropertyValue<?>> propertyValue = valueBuilderClone.toCollection();
          builder.add(propertyValue);
        }
        build = builder.build();
      }
      return new Block(id, node, build, audit);
    } else {
      return new Simple<Object>(id, node, value, audit);
    }
  }
}

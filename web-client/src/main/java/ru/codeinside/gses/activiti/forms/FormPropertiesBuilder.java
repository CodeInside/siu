/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import com.google.common.base.Splitter;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.form.FormPropertyImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.DelegateFormType;
import ru.codeinside.gses.activiti.ftarchive.EnclosureItemFFT;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

final class FormPropertiesBuilder {

  final PropertyTree propertyTree;
  final ExecutionEntity execution;
  final Map<String, String> values;
  final Map<String, FormPropertyHandler> templates;
  private final List<FormPropertyHandler> formPropertyHandlers;

  final static DelegateFormType ENCLOSURE = new DelegateFormType("enclosure", new EnclosureItemFFT());

  FormPropertiesBuilder(PropertyTree propertyTree, List<FormPropertyHandler> formPropertyHandlers) {
    this(propertyTree, formPropertyHandlers, null, null);
  }

  FormPropertiesBuilder(PropertyTree propertyTree, List<FormPropertyHandler> formPropertyHandlers, ExecutionEntity execution) {
    this(propertyTree, formPropertyHandlers, execution, null);
  }

  public FormPropertiesBuilder(PropertyTree propertyTree, List<FormPropertyHandler> formPropertyHandlers, Map<String, String> values) {
    this(propertyTree, formPropertyHandlers, null, values);
  }

  private FormPropertiesBuilder(PropertyTree propertyTree, List<FormPropertyHandler> formPropertyHandlers, ExecutionEntity execution, Map<String, String> values) {
    this.propertyTree = propertyTree;
    this.execution = execution;
    this.values = values;
    templates = PropertyNodes.createHandlersMap(formPropertyHandlers);
    this.formPropertyHandlers = formPropertyHandlers;
  }

  CloneTree build() {
    final CloneTree acc = new CloneTree();
    for (final PropertyNode node : propertyTree.getNodes()) {
      build(acc, node, "");
    }
    return acc;
  }

  void build(final CloneTree acc, final PropertyNode node, final String suffix) {
    final PropertyType type = node.getPropertyType();
    if (type == PropertyType.TOGGLE || type == PropertyType.VISIBILITY_TOGGLE) {
      return;
    }
    final FormPropertyHandler originalHandler = templates.get(node.getId());
    if (!originalHandler.isReadable()) { // TODO: проверять ли для блоков на этапе загрузки?
      return;
    }
    final boolean archiveMode = execution == null && values != null;
    final CustomFormPropertyHandler propertyHandler = PropertyNodes.cloneHandler(originalHandler, suffix, archiveMode);
    final FormProperty formProperty;
    if (archiveMode) {
      propertyHandler.setWritable(false);    //для отображения в архиве
      formProperty = createFormProperty(propertyHandler, values);
    } else {
      formProperty = propertyHandler.createFormProperty(execution, node.isReadable());
    }

    acc.add(propertyHandler, formProperty);
    if (type == PropertyType.BLOCK) {
      if (formProperty.getValue() != null) {
        int n = 0;
        try {
          n = Integer.parseInt(formProperty.getValue());
        } catch (NumberFormatException e) {
          Logger.getLogger(getClass().getName()).warning("Значение блока " + formProperty.getId() + " не число: " + formProperty.getValue());
        }
        final BlockNode block = (BlockNode) node;
        final int items = n < block.getMinimum() ? block.getMinimum() : (n <= block.getMaximum() ? n : block.getMaximum());
        // коррекция значения по описателю блока
        if (n != items && formProperty instanceof FormPropertyImpl) {
          ((FormPropertyImpl) formProperty).setValue(Integer.toString(items));
        }
        for (int i = 1; i <= items; i++) {
          for (final PropertyNode child : block.getNodes()) {
            build(acc, child, suffix + "_" + i);
          }
        }
      }
    }

    if (type == PropertyType.ENCLOSURE) {
      // подпись вложений в запрос SMEV
      if (StringUtils.isEmpty(formProperty.getValue())) return;
      final Iterable<String> varNamesForRefToAttachment = Splitter.on(';').omitEmptyStrings().trimResults().split(formProperty.getValue());
      final Map<String, PropertyNode> childrenMap = ((EnclosureItem) node).enclosures;
      for (String varName : varNamesForRefToAttachment) { // генерация полей, для просмотра сгененерированных вложений к запросу
        addPropertyNode(acc, node, suffix, childrenMap, varName, true);
      }
    }
  }

  private void addPropertyNode(CloneTree acc,
                               PropertyNode node,
                               String suffix,
                               Map<String, PropertyNode> childrenMap,
                               String varName, boolean writable) {
    CustomFormPropertyHandler formPropertyHandler = new CustomFormPropertyHandler();
    formPropertyHandler.setType(ENCLOSURE);
    formPropertyHandler.setId(varName);
    formPropertyHandler.setName("Данные для подписи");
    formPropertyHandler.setReadable(true);
    formPropertyHandler.setWritable(writable);
    formPropertyHandler.setRequired(false);
    formPropertyHandlers.add(formPropertyHandler);
    templates.put(varName, formPropertyHandler);

    PropertyNode propertyNode;
    if (!childrenMap.keySet().contains(varName)) { // генерация поля, для подписания вложений
      propertyNode = new NItem(varName, node.getUnderline(), node.getTip(), node.getNullAction(), node.isReadable(), node.isWritable());
      childrenMap.put(varName, propertyNode);
    } else {
      propertyNode = childrenMap.get(varName);
    }
    build(acc, propertyNode, suffix);
  }

  private FormProperty createFormProperty(FormPropertyHandler handler, Map<String, String> values) {
    final String modelValue;
    if (values.containsKey(handler.getId())) {
      modelValue = values.get(handler.getId());
    } else {
      modelValue = values.get(handler.getVariableName());
    }
    FormPropertyImpl formProperty = new FormPropertyImpl(handler);
    formProperty.setValue(modelValue);
    return formProperty;
  }

  public CloneTree cloneTree(String pid, String suffix) {
    final PropertyNode node = propertyTree.getIndex().get(pid);
    if (node == null || node.getPropertyType() != PropertyType.BLOCK) {
      throw new IllegalArgumentException("Invalid pattern id '" + pid + "'");
    }
    final CloneTree acc = new CloneTree();
    final BlockNode block = (BlockNode) node;
    for (final PropertyNode child : block.getNodes()) {
      build(acc, child, suffix);
    }
    return acc;
  }
}

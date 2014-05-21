/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSession;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class PropertyNodes {

  final static Logger LOG = Logger.getLogger(PropertyNode.class.getName());

  static CloneTree createFormProperties(final PropertyTree propertyTree,
                                        final List<FormPropertyHandler> formPropertyHandlers) {
    return new FormPropertiesBuilder(propertyTree, formPropertyHandlers).build();
  }

  static CloneTree createFormProperties(final PropertyTree propertyTree,
                                        final List<FormPropertyHandler> formPropertyHandlers, final ExecutionEntity execution) {
    return new FormPropertiesBuilder(propertyTree, formPropertyHandlers, execution).build();
  }

  public static CloneTree createFormProperties(PropertyTree propertyTree,
                                               List<FormPropertyHandler> formPropertyHandlers, Map<String, String> values) {
    return new FormPropertiesBuilder(propertyTree, formPropertyHandlers, values).build();
  }

  static CloneTree cloneTree(final PropertyTree propertyTree, final List<FormPropertyHandler> formPropertyHandlers,
                             final ExecutionEntity execution, String pid, String path) {
    return new FormPropertiesBuilder(propertyTree, formPropertyHandlers, execution).cloneTree(pid, path);
  }

  public static void submitFormProperties(final PropertyTree nodeMap,
                                          final List<FormPropertyHandler> formPropertyHandlers, final Map<String, String> properties,
                                          final ExecutionEntity execution) {

    final Map<String, FormPropertyHandler> handlers = createHandlersMap(formPropertyHandlers);

    // Замена требований
    for (final PropertyNode node : nodeMap.getNodes()) {
      if (node.getPropertyType() == PropertyType.TOGGLE) {
        final ToggleNode toggle = (ToggleNode) node;
        boolean required = !toggle.getToggleTo();
        final String value = properties.get(toggle.getToggler().getId());
        if (toggle.getToggleValue().equals(value)) {
          required = !required;
        }
        if (LOG.isLoggable(Level.FINE)) {
          LOG.log(Level.FINE, "set required " + required + " for " + toggle.getId() + " by "
            + (value == null ? "NULL" : ("'" + value + "'")));
        }
        for (final PropertyNode target : toggle.getToggleNodes()) {
          final FormPropertyHandler handler = handlers.get(target.getId());
          if (required != handler.isRequired()) {
            final FormPropertyHandler copy = copyFormPropertyHandler(handler);
            copy.setRequired(required);
            handlers.put(target.getId(), copy);
          }
        }
      }
    }
    final Map<String, String> propertiesCopy = new HashMap<String, String>(properties);
    new Submitter(handlers, propertiesCopy, execution).submit(nodeMap);
    for (final String propertyId : propertiesCopy.keySet()) {
      LOG.fine("out of form: " + propertyId);
      PropertyContext.withWritePath(propertyId, new PropertyContext.Action() {
        @Override
        public void inContext() {
          execution.setVariable(propertyId, propertiesCopy.get(propertyId));
        }
      });
    }
    final CommandContext ctx = Context.getCommandContext();

    final DbSqlSession dbSqlSession = ctx.getDbSqlSession();
    if (dbSqlSession instanceof HistoricDbSqlSession) {
      final HistoricDbSqlSession session = (HistoricDbSqlSession) dbSqlSession;
      session.linkPropertyValuesWithSignatures();
    }
  }

  static Map<String, FormPropertyHandler> createHandlersMap(final List<FormPropertyHandler> formPropertyHandlers) {
    final Map<String, FormPropertyHandler> handlers = new LinkedHashMap<String, FormPropertyHandler>();
    for (final FormPropertyHandler handler : formPropertyHandlers) {
      handlers.put(handler.getId(), handler);
    }
    return handlers;
  }

  static CustomFormPropertyHandler copyFormPropertyHandler(FormPropertyHandler o) {
    final CustomFormPropertyHandler c = new CustomFormPropertyHandler();
    c.setId(o.getId());
    c.setName(o.getName());
    c.setType(o.getType());
    c.setReadable(o.isReadable());
    c.setWritable(o.isWritable());
    c.setRequired(o.isRequired());
    c.setVariableName(o.getVariableName());
    c.setVariableExpression(o.getVariableExpression());
    c.setDefaultExpression(o.getDefaultExpression());
    return c;
  }

  public static TypeTree createTypeTree(PropertyTree propertyTree, List<FormPropertyHandler> formPropertyHandlers) {
    final TypeTree typeTree = new TypeTree(propertyTree);
    for (final FormPropertyHandler h : formPropertyHandlers) {
      typeTree.types.put(h.getId(), h.getType());
      if (h.isReadable() && h.isWritable()) {
        typeTree.writeable.add(h.getId());
      }
    }
    return typeTree;
  }

  static CustomFormPropertyHandler cloneHandler(final FormPropertyHandler template, String suffix, boolean archiveMode) {
    if (!archiveMode && suffix.isEmpty()) {
      if (template instanceof CustomFormPropertyHandler) {
        return (CustomFormPropertyHandler) template;
      }
      return copyFormPropertyHandler(template);
    }
    final CustomFormPropertyHandler handler = copyFormPropertyHandler(template);
    handler.setId(handler.getId() + suffix);
    if (handler.getVariableName() != null) {
      handler.setVariableName(handler.getVariableName() + suffix);
    }
    return handler;
  }

  final static class Submitter {
    final Map<String, FormPropertyHandler> handlers;
    final Map<String, String> props;
    final ExecutionEntity execution;

    Submitter(final Map<String, FormPropertyHandler> handlers, final Map<String, String> props,
              final ExecutionEntity execution) {
      this.handlers = handlers;
      this.props = props;
      this.execution = execution;
    }

    void submit(final PropertyTree tree) {
      for (final PropertyNode node : tree.getNodes()) {
        if (!"!".equals(node.getId())) {
          submit(node, "");
        }
      }
    }

    void submit(final PropertyNode node, final String suffix) {
      final PropertyType type = node.getPropertyType();
      if (type == PropertyType.TOGGLE || type == PropertyType.VISIBILITY_TOGGLE) {
        return;
      }
      final FormPropertyHandler original = handlers.get(node.getId());
      final CustomFormPropertyHandler handler = cloneHandler(original, suffix, false);
      final String id = handler.getId();

      String blockValue = null;
      if (type == PropertyType.BLOCK) {
        // упрощение идентификации блоков, когда есть заначние без префикса '+'
        if (!props.containsKey(id) && props.containsKey(id.substring(1))) {
          props.put(id, props.remove(id.substring(1)));
        }
        if (props.containsKey(id)) {
          blockValue = props.get(id);
        } else {
          Expression expression = handler.getDefaultExpression();
          if (expression != null) {
            blockValue = expression.getExpressionText();
          }
        }

      } else if (type == PropertyType.ENCLOSURE) {
        // этот тип поля мы не показываем на форме, поэтому его значение мы возьмем из контекста
        // в противном случае его значение в контексте станет равно null
        props.put(id, (String) execution.getVariable(id));
      }

      PropertyContext.withWritePath(id, new PropertyContext.Action() {
        @Override
        public void inContext() {
          handler.submitFormNode(node, execution, props);
        }
      });

      if (type == PropertyType.BLOCK) {
        int size = 0;
        if (blockValue != null) {
          try {
            size = Integer.parseInt(blockValue);
          } catch (NumberFormatException e) {
            throw new ActivitiException("Size = '" + blockValue + "' for " + id + " is not integer!");
          }
        }
        BlockNode block = (BlockNode) node;
        if (size < block.getMinimum()) {
          throw new ActivitiException("Size = " + size + " for " + id + " is less then minimum " + block.getMinimum());
        }
        if (size > block.getMaximum()) {
          throw new ActivitiException("Size = " + size + " for " + id + " is greater then minimum " + block.getMaximum());
        }
        for (int i = 1; i <= size; i++) {
          for (PropertyNode child : block.getNodes()) {
            submit(child, suffix + "_" + i);
          }
        }

      } else if (type == PropertyType.ENCLOSURE) {
        EnclosureItem enclosureItem = (EnclosureItem) node;
        for (String enclosureId : enclosureItem.enclosures.keySet()) {
          PropertyNode enclosure = enclosureItem.enclosures.get(enclosureId);
          // т.к. этот тип содержит список переменных с уже созданных attachment, мы подразумеваем, что содержимое переменное не менялось
          // поэтому получение значение из контекста в этом месте допустимо
          props.put(enclosure.getId(), (String) execution.getVariable(enclosure.getId()));
          submit(enclosure, "");
        }
      }
    }
  }

}

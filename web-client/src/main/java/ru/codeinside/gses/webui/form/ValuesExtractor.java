/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import ru.codeinside.gses.activiti.forms.api.definitions.BlockNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyType;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class ValuesExtractor {
  final Map<String, Object> values = new LinkedHashMap<String, Object>();
  final PropertyTree propertyTree;
  final Form form;

  public ValuesExtractor(PropertyTree propertyTree, Form form) {
    this.propertyTree = propertyTree;
    this.form = form;
  }

  public Map<String, Object> extract() {
    for (PropertyNode node : propertyTree.getNodes()) {
      extract(node, "");
    }
    return values;
  }

  void extract(PropertyNode node, String suffix) {
    final PropertyType type = node.getPropertyType();
    if (Arrays.asList(PropertyType.TOGGLE, PropertyType.VISIBILITY_TOGGLE, PropertyType.ENCLOSURE).contains(type)) {
      return;
    }
    boolean writable = node.isFieldWritable();
    if (type != PropertyType.BLOCK && !writable) {
      return;
    }
    final String propertyId = node.getId() + suffix;
    final Field field = getFieldById(propertyId);

    // Только записываемые значения!
    if (writable && !field.isReadOnly() && formIsParent(field)) {
      // должны поддержать требование NULL для невидимых/отсоединённых
      addNodeToFormPropertyValue(node, propertyId, field);
    }
    if (type == PropertyType.BLOCK) {
      if (field.getValue() != null) {
        int items = toInt(field);
        BlockNode block = (BlockNode) node;
        for (int i = 1; i <= items; i++) {
          for (PropertyNode child : block.getNodes()) {
            extract(child, suffix + "_" + i);
          }
        }
      }
    }
  }

  private Field getFieldById(String id) {
    Field field = form.getField(id);
    if (field != null) {
      return field;
    }
    throw new IllegalStateException("Не найдено поле " + id);
  }

  private boolean formIsParent(Component com) {
    if (form instanceof GridForm) {
      while (com != null) {
        if (com == form.getLayout()) {
          return true;
        }
        com = com.getParent();
      }
    } else {
      return true;
    }
    return false;
  }

  private int toInt(Field field) {
    final Object value = field.getValue();
    if (value instanceof Number) {
      return ((Number) value).intValue();
    }
    return Integer.parseInt(value.toString());
  }

  private boolean isFormless(Component component) {
    Component parent = component.getParent();
    while (parent != null) {
      if (parent instanceof Form) {
        return false;
      }
      component = parent;
      parent = component.getParent();
    }
    return false;
  }

  private void addNodeToFormPropertyValue(PropertyNode node, String propertyId, Field field) {
    if (isFormless(field)) {
      values.put(propertyId, null);
    } else {
      values.put(propertyId, field.getValue());
    }
  }
}

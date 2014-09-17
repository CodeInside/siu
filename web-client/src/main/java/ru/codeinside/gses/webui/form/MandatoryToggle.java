/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.data.Property;
import com.vaadin.ui.Field;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.ToggleNode;

import java.io.Serializable;
import java.util.List;

final class MandatoryToggle implements Serializable {

  private final ToggleNode toggleDef;
  private final FieldTree.Entry source;
  private final FieldTree fieldTree;

  public MandatoryToggle(ToggleNode toggleDef, FieldTree.Entry source, FieldTree fieldTree) {
    this.toggleDef = toggleDef;
    this.source = source;
    this.fieldTree = fieldTree;
  }

  public void toggle(final Property property) {

    Object value = property.getValue();
    if (value == null) {
      value = "NULL";
    }
    boolean required = !toggleDef.getToggleTo();
    if (toggleDef.getToggleValue().equals(value.toString())) {
      required = !required;
    }
    if (source.field.getParent() != null) {
      source.field.getParent().requestRepaint();
    }
    for (final PropertyNode targetNode : toggleDef.getToggleNodes()) {
      final List<FieldTree.Entry> targets = fieldTree.getEntries(targetNode.getId());
      for (FieldTree.Entry target : targets) {
        // на одном уровне
        if (target.parent == source.parent) {
          final Field field = target.field;
          field.setRequired(required);
          field.setCaption(field.isRequired() ? "" : null);
          //field.setRequiredError("Обязательно для заполнения: " + target.getCaption());
        }
      }
    }
  }
}

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

final class VisibilityToggle implements Serializable {


  private final ToggleNode node;
  private final FieldTree.Entry source;

  public VisibilityToggle(ToggleNode toggleDef, FieldTree.Entry source) {
    this.node = toggleDef;
    this.source = source;
  }

  public void toggle(final GridForm form, final Property property) {
    Object value = property.getValue();
    if (value == null) {
      value = "NULL";
    }
    boolean isVisible = !node.getToggleTo();
    if (node.getToggleValue().equals(value.toString())) {
      isVisible = !isVisible;
    }

    for (final PropertyNode it : node.getToggleNodes()) {
      if (it == null) {
        continue;
      }
      it.setVisible(isVisible);
      final List<FieldTree.Entry> targets = form.fieldTree.getEntries(it.getId());
      for (FieldTree.Entry target : targets) {
        // на одном уровне
        if (target.parent == source.parent) {
          final Field field = target.field;
          if (!isVisible && form.isAttachedField(target)) {
            // похоже на удаление
            int index = target.index;
            int count = target.getControlsCount();
            if (target.type == FieldTree.Type.BLOCK && !target.readOnly) {
              FieldTree.Entry owner = target.parent;
              int ownerPos = owner.items.indexOf(target);
              FieldTree.Entry entry = owner.items.get(ownerPos + 1);
              //System.out.println("entry1: " + entry.pid + " , type: " + entry.type + ", count:" + count);
              if (entry.type == FieldTree.Type.CONTROLS) {
                form.gridLayout.removeRow(entry.index);
                entry.hidden = true;
              }
            }
            for (int i = 0; i < count; i++) {
              form.gridLayout.removeRow(index);
            }
            target.hidden = true;
            form.removeItemProperty(target.path);
            form.fieldTree.updateColumnIndex();
            form.updateExpandRatios();
          } else if (isVisible && !form.isAttachedField(target)) {
            // похоже на добавление
            target.hidden = false;
            form.fieldTree.updateColumnIndex();
            int count = target.getControlsCount();
            // вставка пустого места
            for (int i = 0; i < count; i++) {
              form.gridLayout.insertRow(target.index);
            }
            form.buildControls(target, target.getLevel());
            if (target.type == FieldTree.Type.BLOCK && !target.readOnly) {
              FieldTree.Entry owner = target.parent;
              int ownerPos = owner.items.indexOf(target);
              FieldTree.Entry entry = owner.items.get(ownerPos + 1);
              //System.out.println("entry2: " + entry.pid + " , type: " + entry.type + ", count:" + count);
              if (entry.type == FieldTree.Type.CONTROLS) {
                form.gridLayout.insertRow(entry.index);
                entry.hidden = false;
                form.buildControls(entry, entry.getLevel());
              }
            }
            form.fieldTree.updateColumnIndex();
            form.updateExpandRatios();
          }
        }
      }
    }
  }
}

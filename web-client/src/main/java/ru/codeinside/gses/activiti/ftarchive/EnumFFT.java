/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;

public class EnumFFT implements FieldType<String> {

  private static final long serialVersionUID = 1L;

  @Override
  public Field createField(String taskId, String fieldId, String name, String value, PropertyNode node, boolean archive) {
    ComboBox comboBox = new ComboBox(name);
    for (java.util.Map.Entry<String, String> enumEntry : node.getParams().entrySet()) {
      comboBox.addItem(enumEntry.getKey());
      if (enumEntry.getValue() != null) {
        comboBox.setItemCaption(enumEntry.getKey(), enumEntry.getValue());
      }
    }
    comboBox.setImmediate(true);// важно!
    comboBox.setWidth("400px");
    FieldHelper.setTextBufferSink(taskId, fieldId, comboBox, node.isFieldWritable() && !archive, value);
    FieldHelper.setCommonFieldProperty(comboBox, node.isFieldWritable() && !archive, name, node.isFiledRequired());
    return comboBox;
  }
}

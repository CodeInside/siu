/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.data.Property;
import com.vaadin.ui.Field;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.webui.Flash;

public class BooleanFFT implements FieldType<Boolean> {

  private static final long serialVersionUID = 1L;

  @Override
  public Field createField(final String taskId, final String fieldId, String name, Boolean value, PropertyNode node, boolean archive) {
    CustomCheckBox result = new CustomCheckBox();
    result.setValue(value);
    if (node.isFieldWritable() && !archive && taskId != null) {
      result.addListener(new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
          Boolean newValue = (Boolean) event.getProperty().getValue();
          Flash.flash().getExecutorService().saveBuffer(taskId, fieldId, Boolean.TRUE.equals(newValue) ? 1L : 0L);
        }
      });
    }
    FieldHelper.setCommonFieldProperty(result, node.isFieldWritable() && !archive, name, node.isFiledRequired());
    return result;
  }
}

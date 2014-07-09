/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.data.Property;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.activiti.ftarchive.validators.LongValidator;
import ru.codeinside.gses.webui.Flash;

public class LongFFT implements FieldType<Long> {

  private static final long serialVersionUID = 1L;

  @Override
  public Field createField(final String taskId, final String fieldId, String name, Long value, PropertyNode node, boolean archive) {
    final TextField textField = new LongField();
    textField.setNullRepresentation("");
    textField.addValidator(new LongValidator("Должно быть число"));
    textField.setImmediate(true);
    textField.setValue(value);
    if (node.isFieldWritable() && !archive && taskId != null) {
      textField.addListener(new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
          Object newValue = event.getProperty().getValue();
          if (!(newValue instanceof Long)) {
            newValue = null;
          }
          Flash.flash().getExecutorService().saveBuffer(taskId, fieldId, (Long) newValue);
        }
      });
    }
    FieldHelper.setCommonFieldProperty(textField, node.isFieldWritable() && !archive, name, node.isFiledRequired());
    return textField;
  }
}

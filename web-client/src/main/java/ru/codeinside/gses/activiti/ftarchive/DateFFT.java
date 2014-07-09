/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.data.Property;
import com.vaadin.ui.Field;
import com.vaadin.ui.PopupDateField;
import ru.codeinside.gses.activiti.ActivitiDateField;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.DateType;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.webui.Flash;

import java.util.Date;

public class DateFFT implements FieldType<Date> {

  @Override
  public Field createField(final String taskId, final String fieldId, String name, Date value, PropertyNode node, boolean archive) {
    PopupDateField dateField = new ActivitiDateField(name);
    dateField.setDateFormat(node.getPattern() == null ? DateType.PATTERN1 : node.getPattern());
    dateField.setValue(value);
    if (node.isFieldWritable() && !archive && taskId != null) {
      dateField.setImmediate(true);
      dateField.addListener(new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
          Date newValue = (Date) event.getProperty().getValue(); // сохраняться будет лишь правильная дата
          Flash.flash().getExecutorService().saveBuffer(taskId, fieldId, newValue.getTime());
        }
      });
    }
    FieldHelper.setCommonFieldProperty(dateField, node.isFieldWritable() && !archive, name, node.isFiledRequired());
    return dateField;

  }
}

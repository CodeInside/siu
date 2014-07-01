/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.data.Property;
import com.vaadin.ui.Field;
import ru.codeinside.gses.webui.Flash;

/**
 * Содержит методы для донастройки полей формы на основе данных о форме из Activiti
 */
public class FieldHelper {
  /**
   * Заполняет поля
   *
   * @param field
   * @param writable
   * @param propertyName
   * @param required
   */
  public static void setCommonFieldProperty(Field field, boolean writable, String propertyName, boolean required) {
    field.setCaption(propertyName);
    if (!writable) {
      field.setReadOnly(true);
    } else {
      field.setRequired(required);
    }
  }

  public static void setTextBufferSink(final String taskId, final String fieldId, Field field, boolean writeable, String defaultValue) {
    if (!writeable || taskId == null) {
      field.setValue(defaultValue);
    } else {
      field.setValue(defaultValue);
      field.addListener(new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
          String newValue = (String) event.getProperty().getValue();
          Flash.flash().getExecutorService().saveBuffer(taskId, fieldId, newValue);
        }
      });
    }
  }
}

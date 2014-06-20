/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.data.Property;
import com.vaadin.ui.Field;
import org.activiti.engine.ActivitiException;
import ru.codeinside.gses.activiti.CustomCheckBox;
import ru.codeinside.gses.activiti.forms.FieldConstructor;
import ru.codeinside.gses.service.Some;
import ru.codeinside.gses.webui.Flash;

public class BooleanFFT implements FieldConstructor {

  private static final long serialVersionUID = 1L;

  @Override
  public Field createField(final String taskId, final String fieldId, String name, String value, boolean writable, boolean required) {
    Field result;
    result = new CustomCheckBox();
    if (value != null) {
      result.setValue(Boolean.parseBoolean(value));
    }
    if (writable && taskId != null) {
      Some<Long> optionalLong = Flash.flash().getExecutorService().getLongBuffer(taskId, fieldId);
      if (optionalLong.isPresent()) {
        result.setValue(optionalLong.get() != 0L);
      }
      result.addListener(new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
          Boolean newValue = (Boolean) event.getProperty().getValue();
          Flash.flash().getExecutorService().saveBuffer(taskId, fieldId, Boolean.TRUE.equals(newValue) ? 1L : 0L);
        }
      });
    }
    FieldHelper.setCommonFieldProperty(result, writable, name, required);
    return result;
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    if (propertyValue == null || "".equals(propertyValue)) {
      return null;
    }
    return Boolean.valueOf(propertyValue);
  }

  @Override
  public String convertModelValueToFormValue(Object modelValue) {
    if (modelValue == null) {
      return null;
    }

    if (Boolean.class.isAssignableFrom(modelValue.getClass())
      || boolean.class.isAssignableFrom(modelValue.getClass())) {
      return modelValue.toString();
    }
    throw new ActivitiException("Model value is not of type boolean, but of type "
      + modelValue.getClass().getName());
  }
}

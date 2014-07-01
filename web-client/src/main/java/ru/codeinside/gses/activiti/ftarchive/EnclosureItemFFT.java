/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.Field;
import ru.codeinside.gses.activiti.EnclosureField;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.forms.types.AttachmentType;

/**
 * Файловые вложения к запросу в Smev
 */
public class EnclosureItemFFT {

  public static final String SPLITER = ":";
  private static final long serialVersionUID = 1L;

  //@Override
  public Field createField(String taskId, String fieldId, final String name, final String value, boolean writable, boolean required) {
    Field field = getField(value);
    FieldHelper.setCommonFieldProperty(field, writable, name, required);
    return field;
  }

  private Field getField(final String value) {
    if (value == null) {
      return new ReadOnly("Нет приложенных к запросу файлов");
    }
    FileValue attachment = new AttachmentType().convertFormValueToModelValue(value, null, null);
    if (attachment == null) {
      return new ReadOnly("удалено (" + value + ")");
    }
    return new EnclosureField(attachment);
  }

  //@Override
  public String convertModelValueToFormValue(Object modelValue) {
    return modelValue != null ? modelValue.toString() : null;
  }

  //@Override
  public Object convertFormValueToModelValue(String propertyValue) {
    return propertyValue;
  }

}

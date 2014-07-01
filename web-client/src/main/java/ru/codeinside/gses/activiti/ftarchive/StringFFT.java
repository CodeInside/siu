/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;

final public class StringFFT implements FieldType<String> {

  private static final long serialVersionUID = 1L;

  @Override
  public Field createField(String taskId, String fieldId, String name, String value, PropertyNode node) {
    Field result;
    if (!node.isFieldWritable()) {
      result = new ReadOnly(value);
    } else {
      TextField textField = new TextField();
      textField.setColumns(25);
      FieldHelper.setTextBufferSink(taskId, fieldId, textField, true, StringUtils.trimToEmpty(value));
      result = textField;
    }
    FieldHelper.setCommonFieldProperty(result, node.isFieldWritable(), name, node.isFiledRequired());
    return result;
  }
}

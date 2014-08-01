/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.Field;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.vaadin.MaskedTextField;

public class MaskedFFT implements FieldType<String> {

  @Override
  public Field createField(String taskId, String fieldId, String name, String value, PropertyNode node, boolean archive) {
    Field result;
    if (!node.isFieldWritable() || archive) {
      result = new ReadOnly(value);
    } else {
      MaskedTextField textField = new MaskedTextField();
      textField.setImmediate(true);
      textField.setMask(node.getPattern());
      FieldHelper.setTextBufferSink(taskId, fieldId, textField, true, StringUtils.trimToEmpty(value));
      result = textField;
    }

    FieldHelper.setCommonFieldProperty(result, node.isFieldWritable() && !archive, name, node.isFieldRequired());
    return result;
  }
}
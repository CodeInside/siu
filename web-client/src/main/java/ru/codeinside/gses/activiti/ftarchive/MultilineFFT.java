/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;


import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import org.activiti.engine.delegate.BpmnError;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;

final public class MultilineFFT implements FieldType<String> {

  private static final long serialVersionUID = 1L;

  @Override
  public Field createField(String taskId, String fieldId, String name, String value, PropertyNode node, boolean archive) {
    TextArea textField = new TextArea();
    String rows = node.getParams().get("rows");
    String columns = node.getParams().get("columns");
    textField.setRows(countOf(rows));
    textField.setColumns(countOf(columns));
    textField.setMaxLength(3995);
    FieldHelper.setTextBufferSink(taskId, fieldId, textField, node.isFieldWritable() && !archive, StringUtils.trimToEmpty(value));
    FieldHelper.setCommonFieldProperty(textField, node.isFieldWritable() && !archive, name, node.isFieldRequired());
    return textField;
  }

  private int countOf(String columns) {
    final int count;
    if (StringUtils.isEmpty(columns)) {
      count = 25;
    } else {
      try {
        count = Integer.parseInt(columns);
      } catch (NumberFormatException e) {
        throw new BpmnError("not.number");
      }
    }
    return count;
  }
}

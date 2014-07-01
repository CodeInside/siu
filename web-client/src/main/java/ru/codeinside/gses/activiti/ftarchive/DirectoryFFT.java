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
import ru.codeinside.gses.beans.DirectoryBeanProvider;

public class DirectoryFFT implements FieldType<String> {

  private static final long serialVersionUID = 1L;

  @Override
  public Field createField(String taskId, String fieldId, String name, String value, PropertyNode node) {
    String directoryId = node.getParams().get("directory_id").trim();
    if (node.isFieldWritable()) {
      DirectoryField field = new DirectoryField(directoryId, name);
      FieldHelper.setTextBufferSink(taskId, fieldId, field, true, value);
      FieldHelper.setCommonFieldProperty(field, true, name, node.isFiledRequired());
      return field;
    }
    String trimValue = value == null ? null : value.trim();
    String kName = DirectoryBeanProvider.get().getValue(directoryId, trimValue);
    if (StringUtils.isEmpty(kName)) {
      kName = trimValue;
    }
    ReadOnly readOnly = new ReadOnly(kName);
    FieldHelper.setCommonFieldProperty(readOnly, false, name, node.isFiledRequired());
    return readOnly;
  }
}

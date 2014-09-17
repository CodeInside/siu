/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.Field;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SimpleField;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.Components;

public class EnclosureFFT implements FieldType<FileValue> {

  private Field getField(String taskId, String fieldId, String name, FileValue value, boolean writable) {
    if (writable) {
      return new AttachmentField(taskId, fieldId, name, value, value != null);
    }
    if (value == null) {
      return new ReadOnly(null);
    }
    return new SimpleField(Components.createAttachShowButton(value, Flash.app()), value.getFileName());
  }

  @Override
  public Field createField(String taskId, String fieldId, String name, FileValue value, PropertyNode node, boolean archive) {
    Field field = getField(taskId, fieldId, name, value, node.isFieldWritable() && !archive);
    FieldHelper.setCommonFieldProperty(field, node.isFieldWritable() && !archive, name, node.isFieldRequired());
    return field;
  }
}

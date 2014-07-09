/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public class JsonFFT implements FieldType<byte[]> {

  final private static long serialVersionUID = 1L;
  final private static ThreadLocal<String> TMP = new ThreadLocal<String>();

  @Override
  public Field createField(String taskId, String fieldId, String name, byte[] value, PropertyNode node, boolean archive) {
    TMP.remove();
    Field result;
    String stringValue = "";
    try {
      if  (value != null) {
        stringValue = new String(value, "UTF-8");
      }
    } catch (UnsupportedEncodingException e) {
      Logger.getAnonymousLogger().info("can't decode model!");
    }
    if (!node.isFieldWritable() || archive) {
      result = new ReadOnly(stringValue);
    } else {
      TextArea json = new TextArea();
//      json.setColumns(10);
      json.setSizeFull();
      json.setRows(50);
      json.setImmediate(true);
      String defaultValue = StringUtils.trimToEmpty(stringValue);
      FieldHelper.setTextBufferSink(taskId, fieldId, json, true, defaultValue);
      result = json;
    }
    FieldHelper.setCommonFieldProperty(result, node.isFieldWritable() && !archive, name, node.isFiledRequired());
    return result;
  }
}


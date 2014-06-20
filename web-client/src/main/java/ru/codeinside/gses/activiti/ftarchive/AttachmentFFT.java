/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.Field;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.Attachment;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SimpleField;
import ru.codeinside.gses.activiti.forms.FieldConstructor;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.Components;

public class AttachmentFFT implements FieldConstructor {

  private static final long serialVersionUID = 1L;

  public static final String SPLITTER = ":";
  public static final String TYPE_NAME = "attachment";
  public static final String SUFFIX = SPLITTER + TYPE_NAME;

  public static boolean isAttachment(FormProperty fp) {
    return TYPE_NAME.equals(fp.getType().getName());
  }

  public static boolean isAttachmentValue(final Object value) {
    if (value != null && value instanceof String) {
      String[] split = value.toString().split(AttachmentFFT.SPLITTER);
      return (split.length == 2 && split[1].equals(AttachmentFFT.TYPE_NAME));
    }
    return false;
  }

  public static String getAttachmentIdByValue(final Object value) {
    if (isAttachmentValue(value)) {
      String[] split = value.toString().split(AttachmentFFT.SPLITTER);
      return split[0];
    }
    return null;
  }

  public static String stringValue(Attachment a) {
    return a.getId() + SUFFIX;
  }


  public static Attachment getAttachmentByValue(final String value) {
    if (value == null) {
      return null;
    }
    return Functions.withEngine(new PF<Attachment>() {
      public Attachment apply(ProcessEngine engine) {
        String[] split = value.split(SPLITTER);
        return engine.getTaskService().getAttachment(split[0]);
      }
    });
  }

  @Override
  public Field createField(String taskId, String fieldId, final String name, final String value, boolean writable, boolean required) {
    Field field = getField(taskId, fieldId, name, value, writable);
    FieldHelper.setCommonFieldProperty(field, writable, name, required);
    return field;
  }

  private Field getField(String taskId, String fieldId, String name, final String value, boolean writable) {
    Attachment attachment = getAttachmentByValue(value);
    if (writable) {
      return new AttachmentField(taskId, fieldId, name, attachment);
    }
    if (attachment == null) {
      return new ReadOnly(null);
    }
    return new SimpleField(Components.createAttachShowButton(attachment, Flash.app()), attachment.getName());
  }

  @Override
  public String convertModelValueToFormValue(Object modelValue) {
    return modelValue != null ? modelValue.toString() : null;
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    return propertyValue;
  }
}

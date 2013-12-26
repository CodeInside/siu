/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import java.io.Serializable;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.Attachment;

import ru.codeinside.gses.activiti.AttachmentField;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SimpleField;
import ru.codeinside.gses.activiti.ftarchive.helpers.FieldHelper;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.vaadin.FieldFormType;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.Components;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;

public class AttachmentFFT implements FieldFormType, Serializable, FieldConstructor {

	public static final String SPLITER = ":";
	public static final String TYPE_NAME = "attachment";
	public static final String SUFFIX = SPLITER + TYPE_NAME;

	private static final long serialVersionUID = 1L;

	public static boolean isAttachment(FormProperty fp) {
		return TYPE_NAME.equals(fp.getType().getName());
	}

	public static boolean isAttachmentValue(final Object value) {
		if (value != null && value instanceof String) {
			String[] split = value.toString().split(AttachmentFFT.SPLITER);
			return (split.length == 2 && split[1].equals(AttachmentFFT.TYPE_NAME));
		}
		return false;
	}

	public static String getAttachmentIdByValue(final Object value) {
		if (isAttachmentValue(value)) {
			String[] split = value.toString().split(AttachmentFFT.SPLITER);
			return split[0];
		}
		return null;
	}

	public static String stringValue(Attachment a) {
		return a.getId() + SUFFIX;
	}

	@Override
	public String getFromType() {
		return TYPE_NAME;
	}

	@Override
	public Field createField(final String name, final String value, Layout layout, boolean writable, boolean required) {
    Field field = getField(name, value, writable);
    FieldHelper.setCommonFieldProperty(field, writable, name, required);
    return field;
	}

  private Field getField(String name, final String value, boolean writable) {
    if (writable) {
      return new AttachmentField(name);
    }
    if (value == null) {
      return new ReadOnly("не загружен");
    }
    Attachment attachment = Functions.withEngine(new PF<Attachment>() {
      private static final long serialVersionUID = 1L;

      public Attachment apply(ProcessEngine engine) {
        String[] split = value.split(SPLITER);
        return engine.getTaskService().getAttachment(split[0]);
      }
    });
    if (attachment == null) {
      return new ReadOnly("удалено (" + value + ")");
    }
    return new SimpleField(Components.createAttachShowButton(attachment, Flash.app()));
  }

  @Override
	public String getFieldValue(String formPropertyId, Form form) {
		Field field = form.getField(formPropertyId);
		Object value = field.getValue();
		if (value != null) {
			return value.toString();
		}
		return null;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		return modelValue != null ? modelValue.toString() : null;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		return propertyValue;
	}
	
	@Override
	public boolean usePattern() {
	    return false;
	}

	@Override
	public boolean useMap() {
		return false;
	}

	@Override
	public FieldConstructor createConstructorOfField() {
		return this;
	}
	
	@Override
	public void setMap(Map<String, String> values) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setPattern(String patternText) {
		throw new UnsupportedOperationException();			
	}

}

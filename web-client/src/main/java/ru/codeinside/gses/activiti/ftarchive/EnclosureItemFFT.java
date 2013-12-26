/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Attachment;
import ru.codeinside.gses.activiti.AttachmentField;
import ru.codeinside.gses.activiti.EnclosureField;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.ftarchive.helpers.FieldHelper;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;

import java.io.Serializable;
import java.util.Map;
/**
 * Файловые вложения к запросу в Smev
 */
public class EnclosureItemFFT implements FieldFormType, Serializable, FieldConstructor {

	public static final String SPLITER = ":";
	public static final String TYPE_NAME = "enclosure";

	private static final long serialVersionUID = 1L;

	@Override
	public String getFromType() {
		return TYPE_NAME;
	}

	@Override
	public Field createField(final String name, final String value, Layout layout, boolean writable, boolean required) {
    Field field = getField(value, name, writable);
    FieldHelper.setCommonFieldProperty(field, writable, name, required);
    return field;
	}

  private Field getField(final String value, String name, boolean writable) {

    if (value == null) {
      return new ReadOnly("Нет приложенных к запросу файлов");
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
    return new EnclosureField(attachment);
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

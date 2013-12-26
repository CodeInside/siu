/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import java.util.Map;

import org.activiti.engine.ActivitiException;

import ru.codeinside.gses.activiti.CustomCheckBox;
import ru.codeinside.gses.activiti.ftarchive.helpers.FieldHelper;
import ru.codeinside.gses.vaadin.FieldFormType;
import ru.codeinside.gses.vaadin.FieldConstructor;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;

public class BooleanFFT implements FieldFormType, FieldConstructor {

	public String getFromType() {
		return "boolean";
	}

	public Field createField(String name, String value, Layout layout, boolean writable, boolean required) {
		Field result;
    result = new CustomCheckBox();
    if (value != null) {
      result.setValue(Boolean.parseBoolean(value));
    }
    FieldHelper.setCommonFieldProperty(result, writable, name, required);
		return result;
	}

	public String getFieldValue(String formPropertyId, Form form) {
		CheckBox field = (CheckBox) form.getField(formPropertyId);
		return field.getValue().toString();
	}

	public Object convertFormValueToModelValue(String propertyValue) {
		if (propertyValue == null || "".equals(propertyValue)) {
			return null;
		}
		return Boolean.valueOf(propertyValue);
	}

	public String convertModelValueToFormValue(Object modelValue) {

		if (modelValue == null) {
			return null;
		}

		if (Boolean.class.isAssignableFrom(modelValue.getClass())
				|| boolean.class.isAssignableFrom(modelValue.getClass())) {
			return modelValue.toString();
		}
		throw new ActivitiException("Model value is not of type boolean, but of type "
				+ modelValue.getClass().getName());
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

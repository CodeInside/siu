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
import com.vaadin.ui.TextField;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ftarchive.helpers.FieldHelper;
import ru.codeinside.gses.activiti.ftarchive.validators.LongValidator;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;

import java.io.Serializable;
import java.util.Map;

public class LongFFT implements FieldFormType, Serializable, FieldConstructor {

    @Override
    public String getFromType() {
        return "long";
    }

    @Override
    public Field createField(String name, String value, Layout layout, boolean writable, boolean required) {
        final TextField textField = new LongField();
        textField.setNullRepresentation("");
        textField.addValidator(new LongValidator("Должно быть число"));
        textField.setImmediate(true);
        textField.setValue(value);
        FieldHelper.setCommonFieldProperty(textField, writable, name, required);
        return textField;
    }

    @Override
    public String getFieldValue(String formPropertyId, Form form) {
        throw new UnsupportedOperationException();
    }

    public Object convertFormValueToModelValue(String formValue) {
        formValue = StringUtils.trimToNull(formValue);
        if (formValue == null) {
            return null;
        }
        return Long.parseLong(formValue);
    }

    public String convertModelValueToFormValue(Object modelValue) {
        if (modelValue == null) {
            return null;
        }
        return modelValue.toString();
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

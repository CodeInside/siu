/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Logger;

final class JsonFormBuilder implements FormSeq {

  String templateRef;
  String valueId;
  String valueName;
  String value;

  JsonForm jsonForm;

  public JsonFormBuilder(List<PropertyValue<?>> propertyValues) {
    for (PropertyValue<?> propertyValue : propertyValues) {
      if (propertyValue.getId().equals(API.JSON_FORM)) {
        templateRef = (String) propertyValue.getValue();
      } else {
        valueId = propertyValue.getId();
        try {
          value = new String((byte[]) propertyValue.getValue(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
          Logger.getAnonymousLogger().info("can't decode model!");
        }
        valueName = propertyValue.getNode().getName();
        break;
      }
    }
  }

  @Override
  public String getCaption() {
    return "";//"Ввод данных";
  }

  @Override
  public List<FormField> getFormFields() {
    return jsonForm.getFormFields();
  }

  @Override
  public Form getForm(FormID formId, FormSeq previous) {
    if (jsonForm == null) {
      jsonForm = new JsonForm(formId, templateRef, valueId, valueName, value);
      templateRef = null;
      valueId = null;
      valueName = null;
      value = null;
    }
    return jsonForm;
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import org.activiti.engine.form.FormProperty;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.FormID;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class JsonFormBuilder implements FormSeq {

  String templateRef;
  String valueId;
  String valueName;
  String value;

  JsonForm jsonForm;

  public JsonFormBuilder(Map<String, FormProperty> properties) {
    templateRef = properties.get(API.JSON_FORM).getValue();
    Set<String> keys = new HashSet<String>(properties.keySet());
    keys.remove(API.JSON_FORM);
    valueId = keys.iterator().next();
    FormProperty property = properties.get(valueId);
    valueName = property.getName();
    value = property.getValue();
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

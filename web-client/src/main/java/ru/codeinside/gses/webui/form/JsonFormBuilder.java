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
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;
import ru.codeinside.gses.webui.wizard.TransitionAction;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Logger;

final class JsonFormBuilder extends AbstractFormSeq {

  String templateRef;
  String valueId;
  PropertyNode node;
  String value;

  JsonForm jsonForm;

  public JsonFormBuilder(List<PropertyValue<?>> propertyValues) {
    for (PropertyValue<?> propertyValue : propertyValues) {
      if (propertyValue.getId().equals(API.JSON_FORM)) {
        templateRef = (String) propertyValue.getValue();
      } else {
        valueId = propertyValue.getId();
        if (propertyValue.getValue() == null) {
          value = "{}";
        } else {
          try {
            value = new String((byte[]) propertyValue.getValue(), "UTF-8");
          } catch (UnsupportedEncodingException e) {
            Logger.getAnonymousLogger().info("can't decode model!");
          }
        }
        node = propertyValue.getNode();
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
      jsonForm = new JsonForm(formId, templateRef, valueId, node, value);
      templateRef = null;
      valueId = null;
      node = null;
      value = null;
    }
    return jsonForm;
  }

  /**
   * Получить действие перехода
   */
  @Override
  public TransitionAction getTransitionAction() {
    return new EmptyAction();
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.impl.form.DefaultTaskFormHandler;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.DelegateFormType;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.ftarchive.StringFFT;
import ru.codeinside.gses.vaadin.FieldConstructor;

import java.util.List;
import java.util.Map;

public class TaskFormHandlerShowUi extends CustomComponent {

  private static final long serialVersionUID = 6459114964767465314L;

  public TaskFormHandlerShowUi(FormData formData, Map<String, String> values) {
    setCompositionRoot(buildMainLayout(formData, null, values));
    setWidth(800 - 50, Sizeable.UNITS_PIXELS);
    setHeight(600 - 50, Sizeable.UNITS_PIXELS);
  }

  //TODO постараться унифицировать две формы
  public TaskFormHandlerShowUi(TaskFormHandler taskFormHandler, Map<String, String> values) {
    setCompositionRoot(buildMainLayout(null, (DefaultTaskFormHandler) taskFormHandler, values));
    setWidth(800 - 50, Sizeable.UNITS_PIXELS);
    setHeight(600 - 50, Sizeable.UNITS_PIXELS);
  }

  static Component buildMainLayout(FormData formData, DefaultTaskFormHandler taskFormHandler, Map<String, String> values) {
    VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    layout.setWidth(800, Sizeable.UNITS_PIXELS);
    Label label = new Label(taskFormHandler != null ? taskFormHandler.getFormKey() : formData.getFormKey());
    label.setStyleName("h2");
    layout.addComponent(label);

    Form form = createForm(formData, taskFormHandler, values);
    layout.addComponent(form);
    return layout;
  }

  static Form createForm(FormData formData, DefaultTaskFormHandler taskFormHandler, Map<String, String> values) {
    final Form form = new Form();
    form.setSizeFull();
    form.getLayout().setStyleName("liquid1");

    TempForm baseForm = createBaseForm("", "", form, getFormProperties(formData, taskFormHandler, values), "", values);
    baseForm.fillForm(form);
    return form;
  }

  public static List<FormProperty> getFormProperties(FormData formData, DefaultTaskFormHandler taskFormHandler, Map<String, String> values) {
    final List<FormProperty> formProperties;
    if (taskFormHandler != null) {
      formProperties = Lists.newArrayList();
      for (FormPropertyHandler formProperty : taskFormHandler.getFormPropertyHandlers()) {
        formProperties.add(new HackFormPropertyHandler(formProperty, values.get(formProperty.getVariableName())));
      }
    } else if (formData != null) {
      formProperties = formData.getFormProperties();
    } else {
      formProperties = Lists.newArrayList();
    }
    return formProperties;
  }

  public static TempForm createBaseForm(String idOfBlock, String nameOfBlock, final Form form, List<FormProperty> formProperties, String suf, Map<String, String> values) {
    List<FormProperty> otherForm = Lists.newArrayList();
    String startBlockId = "";
    boolean haveValues = true;
    String curSuf = "";
    int number = 1;
    TempForm result = new TempForm();
    while (haveValues) {
      haveValues = false;
      TempForm tempResult = new TempForm();
      if (StringUtils.isEmpty(suf)) {
        curSuf = suf;
      } else {
        curSuf = suf + (number++);
      }
      for (FormProperty formProperty : formProperties) {
        String id = formProperty.getId();
        if (id.startsWith("+") && StringUtils.isEmpty(startBlockId)) {
          startBlockId = id.substring(1);
          continue;
        }
        if (id.startsWith("-") && startBlockId.equals(id.substring(1))) {
          TempForm baseForm = createBaseForm(id.substring(1), formProperty.getName(), form, Lists.newArrayList(otherForm), curSuf + "_", values);
          haveValues = haveValues || baseForm.hasFields();

          tempResult.copy(baseForm);

          startBlockId = null;
          otherForm = Lists.newArrayList();
          continue;
        }
        if (StringUtils.isNotEmpty(startBlockId)) {
          otherForm.add(formProperty);
        } else {
          FormType type = formProperty.getType();
          String name = formProperty.getName();
          boolean isRequired = formProperty.isRequired();

          FieldConstructor fieldConstructor = getFieldConstructor(type);
          String defaultValue = formProperty.getValue();
          String propId = id + curSuf;
          String currentValue = values.get(propId);
          haveValues = haveValues || StringUtils.isNotEmpty(currentValue);

          tempResult.add(propId, createFieldByType(form, fieldConstructor, false, isRequired, name, Objects.firstNonNull(currentValue, defaultValue == null ? "" : defaultValue)));
        }
      }
      if (haveValues) {
        if (StringUtils.isNotEmpty(idOfBlock)) {
          result.add("+" + idOfBlock + curSuf, line(nameOfBlock, true));
        }
        result.copy(tempResult);
        if (StringUtils.isNotEmpty(idOfBlock)) {
          result.add("-" + idOfBlock + curSuf, line(nameOfBlock, false));
        }
      }
      if (StringUtils.isEmpty(suf)) {
        haveValues = false;
      }
    }

    return result;
  }

  private static ReadOnly line(String propertyName, boolean isStart) {
    String edgeSymbol = isStart ? ("{ " + propertyName) : (propertyName + "} ");
    ReadOnly readOnly = new ReadOnly(isStart ? (" <hr style='height: 7px; margin-bottom: 0px;margin-top: 0px;'/> " + edgeSymbol) : (edgeSymbol + " <hr style='height: 7px;'/> "));
    Label next = (Label) readOnly.getComponentIterator().next();
    next.setStyleName("");
    next.setContentMode(Label.CONTENT_XHTML);
    return readOnly;
  }

  private static Field createFieldByType(Form form, FieldConstructor fieldConstructor, boolean isWritable, boolean isRequired, String name, String value) {
    Field field = fieldConstructor.createField(name, value, form.getLayout(), isWritable, isRequired);
    field.setCaption(name);
    field.setReadOnly(true);
    field.setRequired(isRequired);
    return field;
  }

  public static FieldConstructor getFieldConstructor(FormType formType) {
    if (formType instanceof DelegateFormType) {
      return ((DelegateFormType) formType).getFieldConstructor();
    }
    return new StringFFT().createConstructorOfField();
  }


}

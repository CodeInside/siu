/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.data.Validator;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.activiti.engine.form.FormType;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.forms.api.values.FormValue;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;
import ru.codeinside.gses.activiti.forms.values.Block;
import ru.codeinside.gses.vaadin.JsonFormIntegration;
import ru.codeinside.gses.webui.ActivitiApp;
import ru.codeinside.gses.webui.form.api.FieldValuesSource;
import ru.codeinside.gses.webui.wizard.ExpandRequired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

// типы:
// string
// multiline
// long
// date (+format)
// masked (+format)
// boolean
// enum (+map)
// directory (+service)
// attachment
// - signature
// - smevRequest
// enclosure == attachment
// - smevRequestEnclosure
// - smevResponseEnclosure
// json
final public class EForm extends Form implements AsyncCompletable, ExpandRequired, FieldValuesSource {

  final String templateRef;
  final eform.Form form;
  final Map<String, EField> fields = new LinkedHashMap<String, EField>();

  AsyncCompleter asyncCompleter;
  boolean hasAnyResult;
  JsonFormIntegration integration;
  String lastError;
  Long serial;
  FormValue formValue;

  public EForm(String templateRef, eform.Form form, FormValue formValue) {
    super(new VerticalLayout());
    this.templateRef = templateRef;
    this.form = form;
    this.formValue = formValue;
    setSizeFull();
    setImmediate(true);
    VerticalLayout layout = (VerticalLayout) getLayout();
    layout.setSizeFull();
  }

  @Override
  public Field getField(Object propertyId) {
    return fields.get(propertyId);
  }

  @Override
  public void attach() {
    ActivitiApp app = (ActivitiApp) getApplication();
    hasAnyResult = false;

    if (serial == null) {
      serial = app.nextId();
      putFields(formValue.getPropertyValues());
    }

    app.getForms().put(serial, form);

    VerticalLayout layout = (VerticalLayout) getLayout();
    try {
      integration = createIntegration();
      layout.addComponent(integration);
      layout.setExpandRatio(integration, 1f);
      integration.setErrorReceiver(new ErrorReceiver());
      integration.setValueReceiver(new ValueReceiver());
    } catch (RuntimeException e) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "ошибка загрузки шаблона", e);
      layout.addComponent(new Label("Ошибка загрузки формы ввода данных " + e.getLocalizedMessage()));
    }
    super.attach();
  }

  private void putFields(List<PropertyValue<?>> propertyValues) {
    for (PropertyValue propertyValue : propertyValues) {
      fields.put(propertyValue.getId(), new EField(propertyValue.getId(), form.getProperty(propertyValue.getId()), propertyValue.getNode().getVariableType()));
      if (propertyValue instanceof Block) {
        for (List<PropertyValue<?>> clones : ((Block) propertyValue).getClones()) {
          putFields(clones);
        }
      }
    }
  }

  @Override
  public void detach() {
    VerticalLayout layout = (VerticalLayout) getLayout();
    if (integration != null) {
      layout.removeComponent(integration);
      integration = null;
    }
    if (serial != null) {
      ActivitiApp app = (ActivitiApp) getApplication();
      app.getForms().remove(serial);
    }
    super.detach();
  }

  public List<FormField> getFormFields() {
    return new ArrayList<FormField>(fields.values());
  }

  @Override
  public void commit() throws SourceException, Validator.InvalidValueException {
    if (lastError != null) {
      String msg = "Сбой " + lastError;
      lastError = null;
      hasAnyResult = false;
      throw new Validator.InvalidValueException(msg);
    }
    if (!hasAnyResult) {
      throw new Validator.InvalidValueException("Результат не получен!");
    }
  }

  @Override
  public boolean isAsyncRequiredForComplete(AsyncCompleter asyncCompleter) {
    if (hasAnyResult) {
      return false;
    }
    if (asyncCompleter != null) {
      this.asyncCompleter = asyncCompleter;
    }
    if (integration != null) {
      integration.fireJson();
    }
    return true;
  }

  JsonFormIntegration createIntegration() {
    ActivitiApp app = (ActivitiApp) getApplication();
    String ref = templateRef;
    String template = JsonForm.loadTemplate(app, ref);

    // прямое получения ресурса
    // WebApplicationContext context = (WebApplicationContext) app.getContext();
    // HttpSession session = context.getHttpSession();
    // System.out.println("content: " + session.getServletContext().getContext("/").getResourceAsStream(templateRef));

    StreamResource.StreamSource htmlStreamSource = new JsonForm.InMemoryResource(template);
    StreamResource resource = new StreamResource(htmlStreamSource, "form-" + serial + "-" + System.currentTimeMillis() + ".html", app);
    resource.setCacheTime(0);
    JsonFormIntegration integration = new JsonFormIntegration();
    integration.setSource(resource);
    integration.setSizeFull();
    integration.setImmediate(true);
    integration.setValidationMode(true);
    return integration;
  }

  @Override
  public Map<String, Object> getFieldValues() {
    Map<String, Object> values = new LinkedHashMap<String, Object>();
    for (EField eField : fields.values()) {
      values.put(eField.id, eField.getValue());
    }
    return values;
  }

  final class ErrorReceiver implements JsonFormIntegration.Receiver, Serializable {
    @Override
    public void onReceive(String value) {
      hasAnyResult = true;
      lastError = JsonForm.simplifyErrorStack(value);
      asyncCompleter.onComplete(false);
    }
  }

  final class ValueReceiver implements JsonFormIntegration.Receiver, Serializable {
    @Override
    public void onReceive(String value) {
      value = StringUtils.trimToEmpty(value);
      hasAnyResult = Boolean.valueOf(value);
      lastError = null;
      if (hasAnyResult) {
        asyncCompleter.onComplete(true);
      }
    }
  }

}

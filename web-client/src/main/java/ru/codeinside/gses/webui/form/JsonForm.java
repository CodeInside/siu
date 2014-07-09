/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.collect.ImmutableList;
import com.vaadin.data.Validator;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import commons.Streams;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.vaadin.JsonFormIntegration;
import ru.codeinside.gses.webui.ActivitiApp;
import ru.codeinside.gses.webui.form.api.FieldValuesSource;
import ru.codeinside.gses.webui.wizard.ExpandRequired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class JsonForm extends Form implements AsyncCompletable, ExpandRequired, FieldValuesSource {

  final FormID formId;
  final String templateRef;
  final String valueId;
  final JsonFormField formField;

  AsyncCompleter asyncCompleter;
  boolean hasAnyResult;
  JsonFormIntegration integration;
  String lastError;
  String startValue;


  public JsonForm(FormID formId, String templateRef, String valueId, String valueName, String value) {
    super(new VerticalLayout());

    this.formId = formId;
    this.templateRef = templateRef;
    this.valueId = valueId;
    formField = new JsonFormField(valueId, valueName);

    startValue = value;
    setSizeFull();
    setImmediate(true);
    VerticalLayout layout = (VerticalLayout) getLayout();
    layout.setSizeFull();

  }

  @Override
  public void attach() {
    hasAnyResult = false;
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

  @Override
  public void detach() {
    VerticalLayout layout = (VerticalLayout) getLayout();
    if (integration != null) {
      layout.removeComponent(integration);
      integration = null;
    }

    super.detach();
  }

  @Override
  public Field getField(Object propertyId) {
    if (valueId.equals(propertyId)) {
      TextArea field = new TextArea();
      field.setNullRepresentation("");
      field.setValue(formField.getValue());
      return field;
    }
    if (API.JSON_FORM.equals(propertyId)) {
      ReadOnly field = new ReadOnly(templateRef);
      field.setCaption("Форма");
      return field;
    }
    return null;
  }

  static String simplifyErrorStack(String value) {
    value = StringUtils.trimToEmpty(value);
    StringBuilder sb = new StringBuilder();
    for (String part : value.split("\n")) {
      if (part.contains("/ru.codeinside.gses.vaadin.WidgetSet/")) {
        break;
      }
      if (sb.length() > 0) {
        sb.append("<br/>\n");
      }
      sb.append(part);
    }
    return sb.toString();
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
    if (!formField.isValid()) {
      hasAnyResult = false;
      throw new Validator.InvalidValueException(formField.getValue());
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
    String value = formField.isValid() ? formField.getValue() : startValue;
    return createIntegration(formId, (ActivitiApp) getApplication(), templateRef, value, false);
  }

  public List<FormField> getFormFields() {
    return ImmutableList.<FormField>of(formField, new JsonFormMarker("Форма", templateRef));
  }

  static String loadTemplate(ActivitiApp app, String ref) {
    try {
      URL serverUrl = app.getServerUrl();
      URL url = new URL(serverUrl, ref);
      logger().info("fetch template " + url);
      URLConnection connection = url.openConnection();
      connection.setDoOutput(false);
      connection.setDoInput(true);
      connection.setConnectTimeout(5000);
      connection.setReadTimeout(5000);
      connection.setUseCaches(false);
      connection.connect();
      return Streams.toString(connection.getInputStream());
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static JsonFormIntegration createIntegration(FormID formId, ActivitiApp app, String ref, String value, boolean archive) {
    String template = loadTemplate(app, ref);
    String template1 = template.replace("'#archiveMode#'", Boolean.toString(archive));
    boolean hasArchiveSupport = archive && !template1.equals(template);
    template = template1.replace("'#jsonField#'", value == null ? "{}" : value);
    StreamResource.StreamSource htmlStreamSource = new InMemoryResource(template);
    Resource resource = new StreamResource(htmlStreamSource, "jf-" + formId + "-" + System.currentTimeMillis() + ".html", app);
    JsonFormIntegration integration = new JsonFormIntegration();
    integration.setSource(resource);
    integration.setSizeFull();
    integration.setImmediate(true);
    if (archive) {
      integration.setReadOnly(true);
      if (!hasArchiveSupport) {
        integration.setFixArchiveSupport(true);
      }
    }
    return integration;
  }

  private static Logger logger() {
    return Logger.getLogger(JsonForm.class.getName());
  }

  @Override
  public Map<String, Object> getFieldValues() {
    Map<String, Object> values = new LinkedHashMap<String, Object>();
    values.put(formField.id, formField.value);
    return values;
  }

  final static class JsonFormField implements FormField, Serializable {

    private final String id;
    private final String name;
    private String value;

    public JsonFormField(String id, String name) {
      this.id = id;
      this.name = name;
    }

    @Override
    public String getPropId() {
      return id;
    }

    @Override
    public String getName() {
      return name;
    }

    void setValue(String value) {
      this.value = value;
    }

    @Override
    public String getValue() {
      return value;
    }

    boolean isValid() {
      return value != null && !value.isEmpty() && value.startsWith("{");
    }
  }

  final static class JsonFormMarker implements FormField, Serializable {

    private final String name;
    private String value;

    public JsonFormMarker(String name, String value) {
      this.name = name;
      this.value = value;
    }

    @Override
    public String getPropId() {
      return API.JSON_FORM;
    }

    @Override
    public String getName() {
      return name;
    }


    @Override
    public String getValue() {
      return value;
    }

  }


  final static class InMemoryResource implements StreamResource.StreamSource {

    final String html;

    public InMemoryResource(String html) {
      this.html = html;
    }

    public InputStream getStream() {
      try {
        return new ByteArrayInputStream(html.getBytes("UTF-8"));
      } catch (UnsupportedEncodingException e) {
        return null;
      }
    }
  }

  final class ErrorReceiver implements JsonFormIntegration.Receiver, Serializable {
    @Override
    public void onReceive(String value) {
      hasAnyResult = true;
      lastError = simplifyErrorStack(value);
      asyncCompleter.onComplete(true);
    }
  }

  final class ValueReceiver implements JsonFormIntegration.Receiver, Serializable {
    @Override
    public void onReceive(String value) {
      value = StringUtils.trimToEmpty(value);
      formField.setValue(value);
      hasAnyResult = !value.isEmpty();
      lastError = null;
      if (hasAnyResult) {
        startValue = null;
        asyncCompleter.onComplete(true);
      }
    }
  }
}

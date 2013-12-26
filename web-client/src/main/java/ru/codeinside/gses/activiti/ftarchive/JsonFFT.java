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
import com.vaadin.ui.TextArea;
import org.activiti.engine.impl.context.Context;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.ftarchive.helpers.FieldHelper;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Logger;

public class JsonFFT implements FieldFormType, Serializable, FieldConstructor {

  final private static long serialVersionUID = 1L;
  final private static Logger logger = Logger.getLogger(JsonFFT.class.getName());
  final private static ThreadLocal<String> TMP = new ThreadLocal<String>();

  @Override
  public String getFromType() {
    return "json";
  }

  @Override
  public Field createField(final String name, final String value, Layout layout, boolean writable, boolean required) {
    TMP.remove();
    Field result;
    if (!writable) {
      result = new ReadOnly(value);
    } else {
      TextArea json = new TextArea();
      json.setValue(StringUtils.trimToEmpty(value));
      json.setColumns(25);
      json.setSizeFull();
      json.setRows(25);
      json.setImmediate(true);
      result = json;
    }
    FieldHelper.setCommonFieldProperty(result, writable, name, required);
    return result;
  }

  @Override
  public String getFieldValue(String formPropertyId, Form form) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String convertModelValueToFormValue(Object model) {
    if (insideActiviti()) {
      logger.info("toForm in Activiti context");
      TMP.remove();
      return toForm(model);
    }
    logger.fine("toForm in UI context");
    String value = toForm(model);
    TMP.set(value);
    if (value == null) {
      return null;
    }
    value = value.replace('\n', ' ');
    return value.length() > 4000 ? value.substring(0, 4000 - 2) + ".." : value;
  }

  @Override
  public Object convertFormValueToModelValue(String form) {
    if (insideActiviti()) {
      logger.info("toModel in Activiti context");
      form = TMP.get();
      TMP.remove();
    } else {
      logger.info("toModel in UI context");
    }
    return toModel(form);
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

  private boolean insideActiviti() {
    return Context.getCommandContext() != null;
  }

  private String toForm(Object model) {
    String result = null;
    if (model instanceof byte[]) {
      try {
        result = new String((byte[]) model, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        Logger.getAnonymousLogger().info("can't decode json!");
      }
    }
    if (result == null) {
      result = model != null ? model.toString() : null;
    }
    return result;
  }

  private Object toModel(String form) {
    if (form != null) {
      try {
        return form.getBytes("UTF-8");
      } catch (UnsupportedEncodingException e) {
        Logger.getAnonymousLogger().info("can't encode json!");
      }
    }
    return null;
  }

}


/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import ru.codeinside.gses.vaadin.customfield.CustomField;

import java.io.Serializable;

final public class ReadOnly extends CustomField implements Serializable {
  private static final long serialVersionUID = 1L;
  private boolean valid;

  public ReadOnly(String value) {
    this(value, true);
  }

  public ReadOnly(String value, boolean valid) {
    this(value, value, valid);
  }

  public ReadOnly(String labelValue, String value, boolean valid) {
    this.valid = valid;
    if (value == null || value.length() < 4000) {
      setSizeFull();
      Label label = new Label(labelValue);
      label.setSizeFull();
      label.setStyleName("left");
      HorizontalLayout layout = new HorizontalLayout(); // обход бага GridLayout
      layout.setSizeFull();
      layout.addComponent(label);
      layout.setExpandRatio(label, 1f);
      setCompositionRoot(layout);
    } else {
      setSizeFull();
      TextArea area = new TextArea();
      area.setValue(value);
      area.setReadOnly(true);
      area.setSizeFull();
      area.setRows(25);
      setCompositionRoot(area);
    }
    if (valid) {
      setValue(value);
    }
  }

  @Override
  public Class<?> getType() {
    return String.class;
  }

  @Override
  public boolean isValid() {
    return valid;
  }

  @Override
  public boolean isRequired() {
    return !valid;
  }

  @Override
  public void addStyleName(String style) {
    getCompositionRoot().addStyleName(style);
  }

  @Override
  public void setDescription(String description) {
    ((AbstractComponent) getCompositionRoot()).setDescription(description);
  }

  @Override
  public String getDescription() {
    return ((AbstractComponent) getCompositionRoot()).getDescription();
  }

}
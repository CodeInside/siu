/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import ru.codeinside.gses.activiti.ftarchive.LongField;
import ru.codeinside.gses.vaadin.customfield.CustomField;
import ru.codeinside.gses.webui.Flash;

import java.io.Serializable;

final public class BlockField extends CustomField implements Serializable {

  private static final long serialVersionUID = 1L;

  final LongField view;
  final String taskId;
  final String fieldId;

  public BlockField(String taskId, String fieldId, Long value, boolean readOnly) {
    this.taskId = taskId;
    this.fieldId = fieldId;
    view = new LongField(value);
    view.setNullRepresentation("0");
    view.setReadOnly(true);
    setReadOnly(readOnly);
    setCompositionRoot(view);
  }

  @Override
  public Class<?> getType() {
    return Long.class;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public boolean isRequired() {
    return false;
  }

  @Override
  public void addStyleName(String style) {
    view.addStyleName(style);
  }

  @Override
  public String getDescription() {
    return view.getDescription();
  }

  @Override
  public void setDescription(String description) {
    view.setDescription(description);
  }

  @Override
  public Object getValue() {
    return view.getValue();
  }

  @Override
  public void setValue(final Object newValue) throws ReadOnlyException, ConversionException {
    view.setReadOnly(false);
    try {
      view.setValue(newValue);
    } finally {
      view.setReadOnly(true);
    }
    if (taskId != null) {
      Long v = null;
      if (newValue instanceof Long) {
        v = (Long) newValue;
      } else if (newValue instanceof Integer) {
        v = Long.valueOf((Integer) newValue);
      }
      Flash.flash().getExecutorService().saveBuffer(taskId, fieldId, v);
    }
  }
}
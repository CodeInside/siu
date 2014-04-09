/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import java.io.Serializable;

import ru.codeinside.gses.vaadin.customfield.CustomField;

import com.vaadin.ui.Component;

final public class SimpleField extends CustomField implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String fileName;

  public SimpleField(Component component, String fileName) {
    this.fileName = fileName;
    setCompositionRoot(component);
  }

  @Override
  public Class<?> getType() {
    return Void.class;
  }

  public String getFileName() {
    return fileName;
  }
}

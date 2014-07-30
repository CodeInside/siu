/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import eform.Property;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.vaadin.customfield.CustomField;

import java.io.File;

final class EField extends CustomField implements FormField {

  final String id;
  final Property property;
  final PropertyNode node;

  EField(String id, Property property, PropertyNode node) {
    this.id = id;
    this.property = property;
    this.node = node;
  }

  @Override
  public Object getValue() {
    if (isAttachment()) {
      Object[] args = property.content();
      if (args == null) {
        return null;
      }
      return new EFileValue(property.value, (String) args[1], (File) args[0]);
    } else {
      return property.value;
    }
  }

  @Override
  public Class<?> getType() {
    return isAttachment() ? FileValue.class : String.class;
  }

  public boolean isAttachment() {
    return "attachment".equals(property.type);
  }

  @Override
  public String getPropId() {
    return id;
  }

  @Override
  public String getName() {
    return property.label;
  }

}

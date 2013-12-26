/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.FormPropertyHandler;

import java.util.ArrayList;
import java.util.List;

final public class CloneTree {
  final public List<FormProperty> properties = new ArrayList<FormProperty>();
  final public List<FormPropertyHandler> handlers = new ArrayList<FormPropertyHandler>();

  public void add(FormPropertyHandler handler, FormProperty property) {
    handlers.add(handler);
    properties.add(property);
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.form.FormType;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class TypeTree {
  final public PropertyTree tree;
  final public Map<String, FormType> types = new LinkedHashMap<String, FormType>();
  final public Set<String> writeable = new LinkedHashSet<String>();

  public TypeTree(PropertyTree tree) {
    this.tree = tree;
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.values;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.values.Audit;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;

final class Simple<T> implements PropertyValue<T> {

  final String id;
  final PropertyNode node;
  final T value;
  final Audit audit;

  Simple(String id, PropertyNode node, T value, Audit audit) {
    this.id = id;
    this.node = node;
    this.value = value;
    this.audit = audit;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public PropertyNode getNode() {
    return node;
  }

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public Audit getAudit() {
    return audit;
  }

  @Override
  public String toString() {
    return "{" +
      "id='" + id + '\'' +
      //", node='" + node.getName() + '\'' +
      ", value='" + value + '\'' +
      (audit == null ? "" : (", audit=" + audit)) +
      '}';
  }
}

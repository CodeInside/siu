/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.values;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.values.Audit;
import ru.codeinside.gses.activiti.forms.api.values.BlockValue;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;

import java.util.List;

final public class Block implements BlockValue {

  final String id;
  final PropertyNode node;
  final List<List<PropertyValue<?>>> clones;
  final Audit audit;

  Block(String id, PropertyNode node, List<List<PropertyValue<?>>> clones, Audit audit) {
    this.id = id;
    this.node = node;
    this.clones = clones;
    this.audit = audit;
  }

  @Override
  public List<List<PropertyValue<?>>> getClones() {
    return clones;
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
  public Long getValue() {
    return clones == null ? 0L : clones.size();
  }

  @Override
  public Audit getAudit() {
    return audit;
  }

  @Override
  public String toString() {
    return "{" +
      "id='" + id + '\'' +
      //", node=" + node +
      ", clones=" + clones +
      ((audit == null) ? "" : ", audit=" + audit) +
      '}';
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import com.google.common.collect.ImmutableMap;
import ru.codeinside.gses.activiti.forms.duration.DurationPreference;

final class EmptyTree implements PropertyTree {

  final PropertyNode[] nodes = new PropertyNode[0];

  @Override
  public PropertyNode[] getNodes() {
    return nodes;
  }

  @Override
  public ImmutableMap<String, PropertyNode> getIndex() {
    return ImmutableMap.of();
  }

  @Override
  public DurationPreference getDurationPreference() {
    return null;
  }
}

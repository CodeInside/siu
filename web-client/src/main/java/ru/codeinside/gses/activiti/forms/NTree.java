/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import com.google.common.collect.ImmutableMap;
import ru.codeinside.gses.activiti.forms.duration.DurationPreference;

import java.util.Map;

final class NTree implements PropertyTree {

  final PropertyNode[] nodes;
  final ImmutableMap<String, PropertyNode> index;
  DurationPreference durationPreference;

  public NTree(final PropertyNode[] nodes, final Map<String, PropertyNode> index, DurationPreference durationPreference) {
    this.nodes = nodes;
    this.index = ImmutableMap.copyOf(index);
    this.durationPreference = durationPreference;
  }

  @Override
  public PropertyNode[] getNodes() {
    return nodes;
  }

  @Override
  public ImmutableMap<String, PropertyNode> getIndex() {
    return index;
  }

  @Override
  public DurationPreference getDurationPreference() {
    return durationPreference;
  }
}

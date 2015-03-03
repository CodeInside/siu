/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.lazyquerycontainer.QueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.QueryFactory;

import java.io.Serializable;
import java.util.Set;

public class GroupMembersFactory implements QueryFactory, Serializable {
  private GroupMembersQueryDefinition definition;
  private final Set<String> taskIds;

  public GroupMembersFactory( Set<String> taskIds) {
   this.taskIds = taskIds;
  }

    @Override
  public void setQueryDefinition(QueryDefinition queryDefinition) {
    definition = (GroupMembersQueryDefinition) queryDefinition;
  }
  @Override
  public Query constructQuery(Object[] sortPropertyIds, boolean[] sortStates) {
    return new GroupMembersQuery(definition.getGroupName(), definition.getMode(), taskIds);
  }
}

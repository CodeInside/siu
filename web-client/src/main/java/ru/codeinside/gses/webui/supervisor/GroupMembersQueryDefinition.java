/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;

public class GroupMembersQueryDefinition extends LazyQueryDefinition{
  private static final long serialVersionUID = 1L;
  private String groupName;
  private GroupMembersQuery.Mode mode;
  public GroupMembersQueryDefinition(String groupName, GroupMembersQuery.Mode mode){
    super(false, 10);
    this.groupName = groupName;
    this.mode = mode;
    addProperty("login", String.class, null, true, true);
    addProperty("fio", String.class, null, true, true);
  }
  public String getGroupName() {
    return groupName;
  }
  public GroupMembersQuery.Mode getMode(){
    return mode;
  }
}

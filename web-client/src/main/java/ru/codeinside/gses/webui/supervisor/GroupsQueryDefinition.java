/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;

public class GroupsQueryDefinition extends LazyQueryDefinition{
  private static final long serialVersionUID = 1L;
  private Mode mode;
  private String login;

  public Mode getMode() {
    return mode;
  }

  public String getLogin() {
    return login;
  }

  public enum Mode{
    ORG, EMP
  }
  public GroupsQueryDefinition(String login, Mode mode){
    super(false, 15);
    this.mode = mode;
    this.login = login;
    addProperty("id", String.class, null, true, true);
    addProperty("name", String.class, null, true, true);
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;

public class HistoricTaskInstancesQueryDefinition extends LazyQueryDefinition {
  private static final long serialVersionUID = 1L;
  private String pid;
  private String tid;

  public HistoricTaskInstancesQueryDefinition(String pid, String tid) {
    super(false, 10);
    this.pid = pid;
    this.tid = tid;
    addProperty("id", String.class, null, true, true);
    addProperty("name", String.class, null, true, true);
    addProperty("procedure", String.class, null, true, true);
    addProperty("startDate", String.class, null, true, true);
    addProperty("endDate", String.class, null, true, true);
    addProperty("assignee", String.class, null, true, true);
  }
  public String getProcessDefinitionId() {
    return pid;
  }
  public String getTaskId() {
    return tid;
  }
}

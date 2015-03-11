/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;
import ru.codeinside.gses.webui.supervisor.SupervisorWorkplace;

import java.util.Map;

public class HistoricTaskInstancesQueryDefinition extends LazyQueryDefinition {
  private static final long serialVersionUID = 1L;
  private Map<String, String> ids;
  private SupervisorWorkplace workspace;

  public HistoricTaskInstancesQueryDefinition(Map<String, String> ids, SupervisorWorkplace workspace) {
    // заявки в эту таблицу добавляются вручную
    super(false, 1000);
    this.ids = ids;
    this.workspace = workspace;
    addProperty("id", String.class, null, true, true);
    addProperty("name", String.class, null, true, true);
    addProperty("procedure", String.class, null, true, true);
    addProperty("startDate", String.class, null, true, true);
    addProperty("endDate", String.class, null, true, true);
    addProperty("assignee", String.class, null, true, true);
  }
  public Map<String, String> getIds() {
    return ids;
  }
  public SupervisorWorkplace getWorkspace() {
    return workspace;
  }
}

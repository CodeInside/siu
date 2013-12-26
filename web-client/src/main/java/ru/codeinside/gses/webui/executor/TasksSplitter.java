/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.executor;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalSplitPanel;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.api.IRefresh;
import ru.codeinside.gses.webui.eventbus.TaskChanged;

final class TasksSplitter extends VerticalSplitPanel implements TabSheet.CloseHandler {
  private static final long serialVersionUID = 1L;
  private final IRefresh[] refreshes;

  TasksSplitter(IRefresh... refreshes) {
    this.refreshes = refreshes;
    for (IRefresh r : refreshes) {
      Flash.bind(TaskChanged.class, r, "refresh");
    }
  }

  @Override
  public void onTabClose(TabSheet tabsheet, Component self) {
    for (IRefresh r : refreshes) {
      Flash.unbind(TaskChanged.class, r, "refresh");
    }
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TabSheet;
import ru.codeinside.gses.webui.components.api.IRefresh;

/**
 * Вкладка, обрабатывающая событие активации.
 */
final class RefreshableTab extends CustomComponent implements TabSheet.SelectedTabChangeListener {

  final IRefresh[] refresh;

  RefreshableTab(Layout layout, IRefresh... refresh) {
    setSizeFull();
    setCompositionRoot(layout);
    this.refresh = refresh;
  }

  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    if (this == event.getTabSheet().getSelectedTab()) {
      for (IRefresh r : refresh) {
        r.refresh();
      }
    }
  }
}

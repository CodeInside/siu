/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.Reindeer;

final public class GwsLazyTab extends CustomComponent implements TabSheet.SelectedTabChangeListener {

  CustomTabSheet tabSheet;

  GwsLazyTab() {
    setSizeFull();
    setCompositionRoot(new Label("Загрузка..."));
  }

  void lazyCreation() {
    tabSheet = new CustomTabSheet();
    tabSheet.addStyleName(Reindeer.TABSHEET_MINIMAL);
    tabSheet.setSizeFull();

    GwsClientsTab gwsClientsTab = new GwsClientsTab();
    ServicesTab servicesTab = new ServicesTab();
    tabSheet.addListener(gwsClientsTab);
    tabSheet.addListener(servicesTab);

    tabSheet.addTab(gwsClientsTab, "Потребители");
    tabSheet.addTab(servicesTab, "Поставщики");

    setCompositionRoot(tabSheet);
  }

  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    if (this == event.getTabSheet().getSelectedTab()) {
      if (tabSheet == null) {
        lazyCreation();
      }
      tabSheet.fireSelectedTabChange();
    }
  }

  final static class CustomTabSheet extends TabSheet {
    @Override
    public void fireSelectedTabChange() {
      super.fireSelectedTabChange();
    }
  }

}

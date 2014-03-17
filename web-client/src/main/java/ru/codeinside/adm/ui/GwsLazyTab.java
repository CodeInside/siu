/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;

final public class GwsLazyTab extends CustomComponent implements TabSheet.SelectedTabChangeListener {

  boolean lazy = true;

  GwsLazyTab() {
    setSizeFull();

    HorizontalLayout center = new HorizontalLayout();
    center.setSizeFull();
    Label loading = new Label("Загрузка...");
    center.addComponent(loading);
    center.setComponentAlignment(loading, Alignment.MIDDLE_CENTER);

    setCompositionRoot(center);
  }

  void lazyCreation() {
    TabSheet tabSheet = new TabSheet();
    tabSheet.setSizeFull();

    GwsClientsTab gwsClientsTab = new GwsClientsTab();
    ServicesTab servicesTab = new ServicesTab();
    tabSheet.addListener(gwsClientsTab);
    tabSheet.addListener(servicesTab);

    tabSheet.addTab(gwsClientsTab, "Клиенты");
    tabSheet.addTab(servicesTab, "Поставщики");

    setCompositionRoot(tabSheet);
  }

  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    if (lazy) {
      if (this == event.getTabSheet().getSelectedTab()) {
        lazy = false;
        lazyCreation();
      }
    }
  }
}

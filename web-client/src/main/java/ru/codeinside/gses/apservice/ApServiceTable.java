/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.apservice;


import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.database.Service;
import ru.codeinside.adm.ui.LazyLoadingContainer2;
import ru.codeinside.adm.ui.TableEmployeeFilterDecorator;
import ru.codeinside.gses.manager.ManagerService;

import java.util.Date;

public class ApServiceTable extends VerticalLayout {

  private static final long serialVersionUID = -3060552897820352215L;

  private static final String[] NAMES = new String[]{"Код", "Наименование", "Дата"};

  private final ApServiceForm form;
  final FilterTable listAp;

  public ApServiceTable(final ApServiceForm serviceForm) {
    this.form = serviceForm;
    listAp = new FilterTable();
    listAp.setPageLength(5);
    listAp.setStyleName("clickable-item-table");
    listAp.setFilterBarVisible(true);
    listAp.setFilterDecorator(new TableEmployeeFilterDecorator());
    LazyLoadingContainer2 newDataSource = new LazyLoadingContainer2(new ApServiceQuery(serviceForm));
    serviceForm.addDependentContainer(newDataSource);
    newDataSource.addContainerProperty("id", Component.class, null);
    newDataSource.addContainerProperty("name", String.class, null);
    newDataSource.addContainerProperty("dateCreated", Date.class, null);
    listAp.setContainerDataSource(newDataSource);
    listAp.setColumnHeaders(NAMES);

    addComponent(listAp);
    listAp.setColumnExpandRatio("id", 0.3f);
    listAp.setColumnExpandRatio("name", 3);
    listAp.setColumnExpandRatio("dateCreated", 0.3f);

    listAp.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        if (event.getItem().getItemProperty("id") != null && event.getItem().getItemProperty("id").getType() == Button.class) {
          String id = ((Button) event.getItem().getItemProperty("id").getValue()).getCaption();
          Service s = ManagerService.get().getApService(id);
          if (s != null) {
            form.showForm(s);
          }
        }
      }
    });

    Container.ItemSetChangeListener listener = new Container.ItemSetChangeListener() {

      private static final long serialVersionUID = 4042381260704014883L;

      @Override
      public void containerItemSetChange(ItemSetChangeEvent event) {
        refreshTable();
      }
    };

    newDataSource.addListener(listener);
    listAp.setSizeFull();
    setSizeFull();

  }

  public void refreshTable() {
    listAp.getContainerDataSource().removeAllItems();
    listAp.refreshRowCache();
    int pageLength = listAp.getPageLength();
    listAp.setPageLength(pageLength - 1);
    listAp.setPageLength(pageLength);
  }

}

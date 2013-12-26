/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.manager;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.database.Service;

import java.util.List;

public class ProcedureFilter extends Form implements Container.ItemSetChangeListener {

  private static final long serialVersionUID = 7129466623849271470L;
  private final ComboBox comboBox;
  private final Button filterButton;
  private ProcedureTable procedureTable;

  public void setServiceFilterEnabled(boolean enabled) {
    comboBox.setEnabled(enabled);
    filterButton.setEnabled(enabled);
  }

  public ProcedureFilter(String name) {
    super();
    setCaption(name);
    comboBox = ProcedureForm.createServicesComboBox("По услуге");
    addField("serviceId", comboBox);
    filterButton = new Button("Фильтровать");
    filterButton.addListener(new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(ClickEvent event) {
        Service value = (Service) getServiceSelect().getValue();
        procedureTable.getProcedureQuery().setServiceId(value == null ? null : value.getId());
        FilterTable pagedTable = procedureTable.getProceduresTable();
        pagedTable.removeAllItems();
        pagedTable.refreshRowCache();
        int pageLength = pagedTable.getPageLength();
        pagedTable.setPageLength(pageLength - 1);
        pagedTable.setPageLength(pageLength);
      }
    });
    addField("submit", filterButton);
    setWidth("100%");
  }

  private ComboBox getServiceSelect() {
    return comboBox;
  }

  public void setProcedureTable(ProcedureTable procedureTable) {
    this.procedureTable = procedureTable;
  }

  @Override
  public void containerItemSetChange(ItemSetChangeEvent event) {
    getServiceSelect().removeAllItems();
    ManagerService managerService = ManagerService.get();

    List<Service> apServices = managerService.getApServices(0, managerService.getApServiceCount(),
        new String[]{ProcedureForm.NAME}, new boolean[]{true});

    BeanItemContainer<Service> objects = new BeanItemContainer<Service>(Service.class, apServices);

    getServiceSelect().setContainerDataSource(objects);
  }
}

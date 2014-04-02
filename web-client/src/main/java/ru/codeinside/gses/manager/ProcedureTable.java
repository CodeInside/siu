/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.manager;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.adm.ui.FilterDecorator_;
import ru.codeinside.adm.ui.LazyLoadingContainer2;

import java.util.Date;

public class ProcedureTable extends VerticalLayout {
  private static final long serialVersionUID = -3060552897820352215L;

  private ProcedureQuery procedureQuery;
  private FilterTable procedures;
  private final FilterTable proceduresTable;
  private ProcedureForm procedureForm;
  private final Container.ItemSetChangeListener listener;

  public ProcedureTable(final ProcedureForm procedureForm) {
    this.procedureForm = procedureForm;
    proceduresTable = new FilterTable();
    proceduresTable.setStyleName("clickable-item-table");
    procedures = proceduresTable;
    proceduresTable.setFilterBarVisible(true);
    proceduresTable.setFilterDecorator(new FilterDecorator_());
    bindToDataSource(ProcedureType.Administrative);
    proceduresTable.setPageLength(5);
    proceduresTable.setSizeFull();

    listener = new Container.ItemSetChangeListener() {
      @Override
      public void containerItemSetChange(ItemSetChangeEvent event) {
        proceduresTable.refreshRowCache();
      }
    };

    proceduresTable.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        if (event.getItem().getItemProperty("id") != null && event.getItem().getItemProperty("id").getType() == Button.class) {
          String id = ((Button) event.getItem().getItemProperty("id").getValue()).getCaption();
          Procedure s = ManagerService.get().getProcedure(id);
          if (s != null) {
            procedureForm.showProcedureInfo(s);
          }
        }
      }
    });
    addComponent(proceduresTable);
    setSizeFull();
    setMargin(false);
    setSpacing(false);
  }

  public void bindToDataSource(ProcedureType type) {
    proceduresTable.setContainerDataSource(getLazyLoadingContainer(procedureForm, listener, type));
    setColumns(proceduresTable);
  }

  private void setColumns(FilterTable proceduresTable) {
    proceduresTable.setColumnExpandRatio("id", 0.05f);
    proceduresTable.setColumnExpandRatio("name", 0.2f);
    proceduresTable.setColumnExpandRatio("description", 0.56f);
    proceduresTable.setColumnExpandRatio("version", 0.05f);
    proceduresTable.setColumnExpandRatio("status", 0.06f);
    proceduresTable.setColumnExpandRatio("dateCreated", 0.08f);
    proceduresTable.setColumnHeaders(new String[]{"Код", "Наименование", "Описание", "Версия", "Статус", "Дата"});
  }

  private LazyLoadingContainer2 getLazyLoadingContainer(ProcedureForm procedureForm, Container.ItemSetChangeListener listener, ProcedureType type) {
    procedureQuery = new ProcedureQuery(procedureForm, type);
    LazyLoadingContainer2 newDataSource = new LazyLoadingContainer2(procedureQuery);
    procedureForm.setDependentContainer(newDataSource);
    newDataSource.addListener(listener);
    newDataSource.addContainerProperty("id", Component.class, null);
    newDataSource.addContainerProperty("name", String.class, null);
    newDataSource.addContainerProperty("description", String.class, null);
    newDataSource.addContainerProperty("version", String.class, null);
    newDataSource.addContainerProperty("status", String.class, null);
    newDataSource.addContainerProperty("dateCreated", Date.class, null);
    return newDataSource;
  }

  public ProcedureQuery getProcedureQuery() {
    return procedureQuery;
  }

  public FilterTable getProceduresTable() {
    return procedures;
  }
}

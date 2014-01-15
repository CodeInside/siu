/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui.employee;

import com.google.common.collect.ImmutableList;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.HorizontalLayout;
import ru.codeinside.adm.ui.EmployeeQuery;
import ru.codeinside.adm.ui.LazyLoadingContainer2;

import java.util.List;

/**
 * Виджет для отображения сотрудников организации
 */
public class TableOrganizationEmployee extends TableEmployee {
  final static String[] NAMES = {"Логин", "ФИО", "Права", "Дата регистрации", "Создатель"};

  public TableOrganizationEmployee(long orgId) {
    setWidth("100%");
    this.lockedFilterValue = false;
    final CustomTable table = new CustomTable();

    table.setSizeFull();
    table.setSelectable(true);
    table.setMultiSelect(false);

    table.setImmediate(true);
    addComponent(table);

    LazyLoadingContainer2 employees = new LazyLoadingContainer2(new EmployeeQuery(orgId, lockedFilterValue));
    table.setContainerDataSource(employees);
    addContainerProperty(table, employees);

    table.setColumnHeaders(NAMES);
    addContextMenu(table);

    final EmployeeEditorButtonGroup buttonGroup = new EmployeeEditorButtonGroup(this, table);
    addComponent(buttonGroup, 0);
  }

  private void addContainerProperty(final CustomTable table, LazyLoadingContainer2 employees) {
    table.addContainerProperty("login", String.class, null);
    table.addContainerProperty("fio", String.class, null);
    table.addContainerProperty("roles", String.class, null);
    employees.removeSortarableId("roles");
    table.addContainerProperty("date", String.class, null);
    table.addContainerProperty("creator", String.class, null);
  }

  protected void refresh(final CustomTable table) {
    table.setValue(null);
    final Container container = table.getContainerDataSource();
    ((LazyLoadingContainer2) container).fireItemSetChange();
    table.setValue(null);
    table.refreshRowCache();
  }

  class EmployeeEditorButtonGroup extends HorizontalLayout {
    private List<Button> buttons;

    EmployeeEditorButtonGroup(final TableEmployee tableEmployee, final CustomTable customTable) {
      setSpacing(true);
      setMargin(false, true, true, false);
      buttons = ImmutableList.of(createEditButton(tableEmployee, customTable),
        createViewButton(tableEmployee, customTable),
        createLockButton(tableEmployee, customTable));
      for (Button button : buttons) {
        this.addComponent(button);
      }
      setEnabled(customTable.getValue() != null);
      customTable.addListener(new Property.ValueChangeListener() {

        private static final long serialVersionUID = 1L;

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
          setEnabled(customTable.getValue() != null);
        }
      });
      customTable.addListener(new RepaintRequestListener() {
        @Override
        public void repaintRequested(RepaintRequestEvent event) {
          setVisible(customTable.isVisible());
        }
      });
    }

    private Button createEditButton(final TableEmployee tableEmployee, final CustomTable customTable) {
      Button editButton = new Button("Редактировать");
      editButton.addListener(new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
          tableEmployee.edit(customTable);
          setEnabled(false);
        }
      });
      return editButton;
    }

    private Button createViewButton(final TableEmployee tableEmployee, final CustomTable customTable) {
      Button viewButton = new Button("Просмотр");
      viewButton.addListener(new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
          tableEmployee.view(customTable);
          setEnabled(false);
        }
      });
      return viewButton;
    }

    private Button createLockButton(final TableEmployee tableEmployee, final CustomTable customTable) {
      Button lockButton = new Button("Заблокировать");
      lockButton.addListener(new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
          tableEmployee.lockUserActionHandler(customTable);
        }
      });
      return lockButton;
    }

    @Override
    public void setEnabled(boolean enabled) {
      super.setEnabled(enabled);
      for (Button button : buttons) {
        button.setEnabled(enabled);
      }
    }
  }
}


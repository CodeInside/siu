/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui.employee;

import com.google.common.collect.ImmutableList;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.ui.DateColumnGenerator;
import ru.codeinside.adm.ui.FilterDecorator_;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Виджет для отображения сотрудников организации
 */
public class TableOrganizationEmployee extends TableEmployee {
  final static String[] NAMES = {"Логин", "ФИО", "Права", "Дата регистрации", "Создатель"};

  public TableOrganizationEmployee(long orgId) {
    setWidth("100%");
    this.lockedFilterValue = false;
    final FilterTable table = new FilterTable();

    table.setSizeFull();
    table.setSelectable(true);
    table.setMultiSelect(false);
    table.setRowHeaderMode(Table.ROW_HEADER_MODE_HIDDEN);
    table.setColumnCollapsingAllowed(true);
    table.setColumnReorderingAllowed(true);
    table.setImmediate(true);
    addComponent(table);

    addContainerProperty(table, orgId);
    table.setFilterBarVisible(true);

    table.setColumnHeaders(NAMES);
    table.setFilterDecorator(new FilterDecorator_());
    addContextMenu(table);

    final EmployeeEditorButtonGroup buttonGroup = new EmployeeEditorButtonGroup(this, table);
    addComponent(buttonGroup, 0);
  }

  private void addContainerProperty(final CustomTable table, long orgId) {
    EntityManagerFactory myPU = AdminServiceProvider.get().getMyPU();
    final JPAContainer<Employee> container = new JPAContainer<Employee>(Employee.class);
    container.setReadOnly(true);
    container.setEntityProvider(new CachingLocalEntityProvider<Employee>(Employee.class, myPU.createEntityManager()));
    container.addContainerFilter(new And(new Compare.Equal("locked", lockedFilterValue), new Compare.Equal("organization.id", orgId)));
    table.setContainerDataSource(container);
    table.setVisibleColumns(new Object[]{"login", "fio", "roles", "date", "creator"});
    table.addGeneratedColumn("date", new DateColumnGenerator("dd.MM.yyyy HH:mm:ss"));
    table.addGeneratedColumn("roles", new RolesColumn());
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


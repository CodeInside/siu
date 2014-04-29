/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.vaadin.data.Item;
import com.vaadin.data.util.PropertysetItem;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.containers.LazyLoadingQuery;
import ru.codeinside.gses.webui.utils.Components;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EmployeeQuery implements LazyLoadingQuery {

  private static final long serialVersionUID = 1L;

  final long orgId;
  final boolean locked;
  final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

  String[] sortProps = {};
  boolean[] sortAsc = {};

  public EmployeeQuery(long orgId, boolean locked) {
    this.orgId = orgId;
    this.locked = locked;
  }

  @Override
  public int size() {
    return AdminServiceProvider.get().getEmployeesCount(orgId, locked);
  }

  @Override
  public List<Item> loadItems(int start, int count) {
    return AdminServiceProvider.get().withEmployees(orgId, locked, start, count, sortProps, sortAsc,
        new Function<List<Employee>, List<Item>>() {
          public List<Item> apply(List<Employee> employees) {
            final ArrayList<Item> items = new ArrayList<Item>();
            for (Employee employee : employees) {
              items.add(createItem(employee));
            }
            return items;
          }
        });
  }

  @Override
  public Item loadSingleResult(String login) {
    return AdminServiceProvider.get().withEmployee(orgId, login, new Function<Employee, Item>() {
      public Item apply(Employee employee) {
        return createItem(employee);
      }
    });
  }

  PropertysetItem createItem(final Employee employee) {
    PropertysetItem item = new PropertysetItem();
    item.addItemProperty("login", Components.stringProperty(employee.getLogin()));
    item.addItemProperty("fio", Components.stringProperty(employee.getFio()));
    item.addItemProperty("roles", Components.stringProperty(Joiner.on(',').join(employee.getRoleNames())));
    item.addItemProperty("date", Components.stringProperty(formatter.format(employee.getDate())));
    item.addItemProperty("creator", Components.stringProperty(employee.getCreator()));
    item.addItemProperty("locked", Components.stringProperty(Boolean.toString(employee.isLocked())));
    return item;
  }

  @Override
  public void setLazyLoadingContainer(LazyLoadingContainer paramLazyLoadingContainer) {
  }

  @Override
  public void setSorting(Object[] propertyIds, boolean[] ascending) {
    String[] props = new String[propertyIds.length];
    for (int i = 0; i < propertyIds.length; i++) {
      props[i] = propertyIds[i].toString();
    }
    sortProps = props;
    sortAsc = ascending;
  }

}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.declarant;

import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.adm.database.Service;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.webui.Flash;

import java.io.Serializable;
import java.util.List;

final public class ServiceQuery implements Query, Serializable {

  private static final long serialVersionUID = 1L;

  final ProcedureType type;
  private boolean showActive;

  public ServiceQuery(ProcedureType type, boolean showActive) {
    this.type = type;
    this.showActive = showActive;
  }

  @Override
  public int size() {
    if (showActive) {
      return Flash.flash().getDeclarantService().activeServicesCount(type);
    }
    return Flash.flash().getDeclarantService().filteredActiveServicesCount(type, Flash.login());
  }

  @Override
  public List<Item> loadItems(final int start, final int count) {
    List<Service> serviceList = showActive
        ? Flash.flash().getDeclarantService().selectActiveServices(type, start, count)
        : Flash.flash().getDeclarantService().selectFilteredActiveServices(type, Flash.login(), start, count);
    final List<Item> items = Lists.newArrayListWithExpectedSize(serviceList.size());
    for (Service s : serviceList) {
      final PropertysetItem item = new PropertysetItem();
      item.addItemProperty("id", new ObjectProperty<Long>(s.getId()));
      item.addItemProperty("name", new ObjectProperty<String>(s.getName()));
      items.add(item);
    }
    return items;
  }

  @Override
  public void saveItems(List<Item> addedItems, List<Item> modifiedItems, List<Item> removedItems) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean deleteAllItems() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Item constructItem() {
    throw new UnsupportedOperationException();
  }

}

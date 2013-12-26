/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.executor;

import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import ru.codeinside.gses.beans.DirectoryBeanProvider;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.service.impl.DeclarantServiceImpl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DeclarantTypeQuery implements Query, Serializable {
  @Override
  public int size() {
      return DirectoryBeanProvider.get().getValues(DeclarantServiceImpl.DECLARANT_TYPES).size();
  }

  @Override
  public List<Item> loadItems(int startIndex, int count) {
    List<Item> items = Lists.newArrayListWithExpectedSize(size());
      Map<String,String> declarantTypes = DirectoryBeanProvider.get().getValues(DeclarantServiceImpl.DECLARANT_TYPES);
      for(Map.Entry<String, String> dt : declarantTypes.entrySet()){
      PropertysetItem item = new PropertysetItem();
      item.addItemProperty("name", new ObjectProperty<String>(dt.getKey()));
      item.addItemProperty("value", new ObjectProperty<String>(dt.getValue()));
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
    return false;
  }

  @Override
  public Item constructItem() {
    throw new UnsupportedOperationException();
  }
}

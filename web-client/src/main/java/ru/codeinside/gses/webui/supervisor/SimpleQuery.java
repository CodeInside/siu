/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import com.vaadin.data.Item;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.lazyquerycontainer.QueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.QueryFactory;

import java.io.Serializable;
import java.util.List;

public abstract class SimpleQuery extends LazyQueryDefinition implements QueryFactory, Query, Serializable {

  protected String sortBy;
  protected Boolean sortAsc;


  SimpleQuery(final boolean compositeItems, final int batchSize) {
    super(compositeItems, batchSize);
  }

  final public LazyQueryContainer createContainer() {
    return new LazyQueryContainer(this, this);
  }

  @Override
  final public void setQueryDefinition(final QueryDefinition self) {
    if (this != self) {
      throw new IllegalStateException();
    }
  }

  @Override
  final public Query constructQuery(Object[] sortPropertyIds, boolean[] sortStates) {
    sortBy = null;
    sortAsc = null;
    if (sortPropertyIds != null && sortPropertyIds.length > 0) {
      sortBy = sortPropertyIds[0].toString();
      sortAsc = sortStates[0];
    }
    return this;
  }

  @Override
  final public void saveItems(List<Item> addedItems, List<Item> modifiedItems, List<Item> removedItems) {
    throw new UnsupportedOperationException();
  }

  @Override
  final public boolean deleteAllItems() {
    throw new UnsupportedOperationException();
  }

  @Override
  final public Item constructItem() {
    throw new UnsupportedOperationException();
  }
}

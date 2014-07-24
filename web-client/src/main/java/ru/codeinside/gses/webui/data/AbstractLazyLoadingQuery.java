/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import org.activiti.engine.query.Query;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.containers.LazyLoadingQuery;

import java.util.List;

public abstract class AbstractLazyLoadingQuery<T> implements LazyLoadingQuery {

  private static final long serialVersionUID = 4269788861279171943L;

  protected final ItemBuilder<T> itemBuilder;

  protected String orderBy = "name";
  protected boolean asc = true;

  public AbstractLazyLoadingQuery(ItemBuilder<T> itemBuilder) {
    this.itemBuilder = itemBuilder;
  }

  public void setLazyLoadingContainer(LazyLoadingContainer lazyLoadingContainer) {
  }

  public void setSorting(Object[] propertyIds, boolean[] ascending) {
    for (Object o : propertyIds) {
      orderBy = (String) o;
    }
    for (boolean o : ascending) {
      asc = o;
    }
  }

  List<T> listPage(Query<?, T> query, int start, int count) {
    if (asc) {
      query.asc();
    } else {
      query.desc();
    }
    return query.listPage(start, count);
  }

  public List<Item> loadItems(int start, int count) {
    List<Item> items = Lists.newArrayList();
    if (itemBuilder instanceof BatchBuilder) {
      ((BatchBuilder) itemBuilder).batchStart();
    }
    for (T domainObject : items(start, count)) {
      items.add(itemBuilder.createItem(domainObject));
    }
    if (itemBuilder instanceof BatchBuilder) {
      ((BatchBuilder) itemBuilder).batchFinish();
    }
    return items;
  }

  public Item loadSingleResult(String id) {
    T domainObject = singleResult(id);
    if (domainObject != null) {
      return itemBuilder.createItem(domainObject);
    }
    return null;
  }

  abstract List<T> items(int start, int count);

  abstract T singleResult(String id);

}
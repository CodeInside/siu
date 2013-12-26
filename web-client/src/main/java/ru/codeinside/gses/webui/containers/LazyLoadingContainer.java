/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.containers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vaadin.addon.jpacontainer.filter.util.AdvancedFilterableSupport;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import org.activiti.engine.ActivitiException;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class LazyLoadingContainer implements Container.Indexed, Container.Sortable {
  private static final long serialVersionUID = 1L;
  protected LazyLoadingQuery lazyLoadingQuery;
  public AdvancedFilterableSupport sender;
  protected int batchSize;

  protected int size = -1;
  protected List<Object> containerPropertyIds = Lists.newArrayList();
  protected List<Object> sortablePropertyIds = Lists.newArrayList();
  protected Map<Object, Class<?>> containerPropertyTypes = Maps.newHashMap();
  protected Map<Object, Object> containerPropertyDefaultValues = Maps.newHashMap();
  protected Map<Integer, Item> itemCache = Maps.newHashMap();

  public LazyLoadingContainer(LazyLoadingQuery lazyLoadingQuery, int batchSize, AdvancedFilterableSupport newSender) {
    this.lazyLoadingQuery = lazyLoadingQuery;
    this.batchSize = batchSize;
    this.sender = newSender;
    lazyLoadingQuery.setLazyLoadingContainer(this);
  }

  public LazyLoadingContainer(LazyLoadingQuery lazyLoadingQuery, int batchSize) {
    this(lazyLoadingQuery, batchSize, null);
  }

  public LazyLoadingContainer(LazyLoadingQuery lazyLoadingQuery, AdvancedFilterableSupport sender) {
    this(lazyLoadingQuery, 15, sender);
  }

  public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue)
      throws UnsupportedOperationException {
    containerPropertyIds.add(propertyId);
    sortablePropertyIds.add(propertyId);
    containerPropertyTypes.put(propertyId, type);
    containerPropertyDefaultValues.put(propertyId, defaultValue);
    return true;
  }

  public void removeSortarableId(Object propertyId) {
    sortablePropertyIds.remove(propertyId);
  }

  public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
    containerPropertyIds.remove(propertyId);
    sortablePropertyIds.remove(propertyId);
    containerPropertyTypes.remove(propertyId);
    containerPropertyDefaultValues.remove(propertyId);
    return true;
  }

  public Collection<?> getContainerPropertyIds() {
    return this.containerPropertyIds;
  }

  public Class<?> getType(Object propertyId) {
    return (Class<?>) this.containerPropertyTypes.get(propertyId);
  }

  public Object getDefaultValue(Object propertyId) {
    return this.containerPropertyDefaultValues.get(propertyId);
  }

  public Property getContainerProperty(Object itemId, Object propertyId) {
    return getItem(itemId).getItemProperty(propertyId);
  }

  public boolean containsId(Object itemId) {
    Integer index = (Integer) itemId;
    return (index.intValue() >= 0) && (index.intValue() < size());
  }

  public Item getItem(Object itemId) {
    if (itemId != null) {
      int start;
      if (!this.itemCache.containsKey(itemId)) {
        Integer index = (Integer) itemId;
        start = index.intValue() - index.intValue() % this.batchSize;

        List<Item> batch = this.lazyLoadingQuery.loadItems(start, this.batchSize);
        for (Item batchItem : batch) {
          this.itemCache.put(Integer.valueOf(start), batchItem);
          start++;
        }
      }
      return (Item) this.itemCache.get(itemId);
    }

    return null;
  }

  public int size() {
    if (this.size == -1) {
      this.size = this.lazyLoadingQuery.size();
    }
    return this.size;
  }

  public Collection<?> getItemIds() {
    return new AbstractList<Integer>() {
      public int size() {
        return size();
      }

      public Integer get(int index) {
        return Integer.valueOf(index);
      }
    };
  }

  public Object firstItemId() {
    return Integer.valueOf(0);
  }

  public Object lastItemId() {
    return Integer.valueOf(size() - 1);
  }

  public boolean isFirstId(Object itemId) {
    return ((Integer) itemId).equals(Integer.valueOf(0));
  }

  public boolean isLastId(Object itemId) {
    return ((Integer) itemId).equals(Integer.valueOf(size() - 1));
  }

  public Object nextItemId(Object itemId) {
    Integer index = (Integer) itemId;
    return Integer.valueOf(index.intValue() + 1);
  }

  public Object prevItemId(Object itemId) {
    Integer index = (Integer) itemId;
    return Integer.valueOf(index.intValue() - 1);
  }

  public int indexOfId(Object itemId) {
    return ((Integer) itemId).intValue();
  }

  public Object getIdByIndex(int index) {
    return index;
  }

  public Collection<?> getSortableContainerPropertyIds() {
    return sortablePropertyIds;
  }

  public void sort(Object[] propertyIds, boolean[] ascending) {
    removeAllItems();
    this.lazyLoadingQuery.setSorting(propertyIds, ascending);
  }

  public boolean removeAllItems() throws UnsupportedOperationException {
    this.itemCache.clear();
    this.size = -1;
    return true;
  }

  public int getIndexForObjectId(String id) {
    Item searched = this.lazyLoadingQuery.loadSingleResult(id);
    if (searched == null) {
      return -1;
    }
    return getIndexForObjectId(searched, 0, size() - 1);
  }

  public int getIndexForObjectId(Item searched, int low, int high) {
    if (high < low) {
      return -1;
    }

    int middle = low + (high - low) / 2;
    Item result = null;

    if (this.itemCache.containsKey(Integer.valueOf(middle))) {
      result = (Item) this.itemCache.get(Integer.valueOf(middle));
    } else {
      result = (Item) this.lazyLoadingQuery.loadItems(middle, 1).get(0);
      this.itemCache.put(Integer.valueOf(middle), result);
    }

    int comparison = compareItem(searched, result);
    if (comparison < 0)
      return getIndexForObjectId(searched, low, middle - 1);
    if (comparison > 0) {
      return getIndexForObjectId(searched, middle + 1, high);
    }
    return middle;
  }

  @SuppressWarnings("unchecked")
  private int compareItem(Item searched, Item result) {
    if ((!(searched instanceof Comparable)) || (!(result instanceof Comparable))) {
      throw new ActivitiException("Cannot use the getIndexForObjectId method for non-Comparables");
    }
    return ((Comparable<Item>) searched).compareTo(result);
  }

  public Object addItem() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  public Object addItemAt(int index) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  public boolean removeItem(Object itemId) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  public Item addItem(Object itemId) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

}
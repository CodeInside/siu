/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.addon.jpacontainer.AdvancedFilterable;
import com.vaadin.addon.jpacontainer.filter.util.AdvancedFilterableSupport;
import com.vaadin.data.Container;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.containers.LazyLoadingQuery;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class LazyLoadingContainer2 extends LazyLoadingContainer implements Container.ItemSetChangeNotifier, Container.Filterable, AdvancedFilterable {

  private static final long serialVersionUID = 1L;
  private Collection<Container.ItemSetChangeListener> propertySetChangeListeners = new LinkedList<Container.ItemSetChangeListener>();
  private AdvancedFilterableSupport filterSupport;

  public LazyLoadingContainer2(LazyLoadingQuery lazyLoadingQuery, AdvancedFilterableSupport newSender) {
    super(lazyLoadingQuery, newSender);
    this.sender = newSender;
    this.filterSupport = new AdvancedFilterableSupport();

    this.filterSupport.addListener(new AdvancedFilterableSupport.ApplyFiltersListener() {
      public void filtersApplied(AdvancedFilterableSupport newSender2) {
        sender = newSender2;
        fireItemSetChange();
      }
    });
  }

  public LazyLoadingContainer2(LazyLoadingQuery lazyLoadingQuery) {
    this(lazyLoadingQuery, null);
  }

  public void fireItemSetChange() {
    removeAllItems();
    if (sender == null) {
      for (Container.ItemSetChangeListener l : new LinkedList<Container.ItemSetChangeListener>(propertySetChangeListeners)) {
        l.containerItemSetChange(new ItemSetChangeEvent() {
          @Override
          public Container getContainer() {
            return LazyLoadingContainer2.this;
          }
        });
      }
    } else {
      for (Container.ItemSetChangeListener l : new LinkedList<Container.ItemSetChangeListener>(propertySetChangeListeners)) {
        l.containerItemSetChange(new ItemSetChangeEvent() {
          @Override
          public Container getContainer() {
            return new LazyLoadingContainer2(lazyLoadingQuery, sender);
          }
        });
      }
    }
  }

  @Override
  public void addListener(Container.ItemSetChangeListener listener) {
    propertySetChangeListeners.add(listener);
  }

  @Override
  public void removeListener(Container.ItemSetChangeListener listener) {
    if (propertySetChangeListeners != null) {
      propertySetChangeListeners.remove(listener);
    }
  }

  @Override
  public void addContainerFilter(Filter filter) {
    filterSupport.addFilter(filter);
  }

  @Override
  public void removeContainerFilter(Filter filter) {
    filterSupport.removeFilter(filter);
  }

  @Override
  public void removeAllContainerFilters() {
    filterSupport.removeAllFilters();
  }

  @Override
  public Collection<Object> getFilterablePropertyIds() {
    return filterSupport.getFilterablePropertyIds();
  }

  @Override
  public boolean isFilterable(Object propertyId) {
    return filterSupport.isFilterable(propertyId);
  }

  @Override
  public List<Filter> getFilters() {
    return filterSupport.getFilters();
  }

  @Override
  public List<Filter> getAppliedFilters() {
    return filterSupport.getAppliedFilters();
  }

  @Override
  public void setApplyFiltersImmediately(boolean applyFiltersImmediately) {
    filterSupport.setApplyFiltersImmediately(applyFiltersImmediately);
  }

  @Override
  public boolean isApplyFiltersImmediately() {
    return filterSupport.isApplyFiltersImmediately();
  }

  @Override
  public void applyFilters() {
    filterSupport.applyFilters();
  }

  @Override
  public boolean hasUnappliedFilters() {
    return filterSupport.hasUnappliedFilters();
  }
}

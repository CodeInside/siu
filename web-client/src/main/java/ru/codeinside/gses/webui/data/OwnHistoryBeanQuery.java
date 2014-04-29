/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.util.PropertysetItem;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.containers.LazyLoadingQuery;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import static ru.codeinside.gses.webui.utils.Components.stringProperty;

final public class OwnHistoryBeanQuery implements LazyLoadingQuery, Serializable {

  final static private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

  private static final long serialVersionUID = 1L;
  private LazyLoadingContainer container;
  private String[] sortProps = {};
  private boolean[] sortAsc = {};

  public OwnHistoryBeanQuery() {
  }

  @Override
  public int size() {
    return Flash.flash().getAdminService().countOfBidByEmail(Flash.login(), container.sender);
  }

  @Override
  public List<Item> loadItems(final int startIndex, final int count) {
    List<Bid> bidIds = Flash.flash().getAdminService().bidsByLogin(Flash.login(), startIndex, count, sortProps, sortAsc, container.sender);
    final List<Item> items = Lists.newArrayListWithExpectedSize(bidIds.size());
    for (Bid bid : bidIds) {
      items.add(createItem(bid));
    }
    return items;
  }

  private PropertysetItem createItem(Bid bid) {
    final PropertysetItem item = new PropertysetItem();
    item.addItemProperty("id", stringProperty(bid.getId().toString()));
    if (bid.getTag().isEmpty()) {
      item.addItemProperty("procedure.name", stringProperty(bid.getProcedure().getName()));
    } else {
      item.addItemProperty("procedure.name", stringProperty(bid.getTag() + " - " + bid.getProcedure().getName()));
    }

    item.addItemProperty("dateCreated", stringProperty(formatter.format(bid.getDateCreated())));
    item.addItemProperty("dateFinished", stringProperty(bid.getDateFinished() == null ? "" : formatter.format(bid.getDateFinished())));
    return item;
  }

  @Override
  public Item loadSingleResult(String paramString) {
    Bid bid = Flash.flash().getAdminService().getBid(paramString);
    return createItem(bid);
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

  @Override
  public void setLazyLoadingContainer(LazyLoadingContainer container) {
    this.container = container;
  }

}

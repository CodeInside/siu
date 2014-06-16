/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import ru.codeinside.adm.database.Group;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.containers.LazyLoadingQuery;

import java.io.Serializable;
import java.util.List;

public class GroupsQuery implements Query, LazyLoadingQuery, Serializable {
  private static final long serialVersionUID = 1L;
  private GroupsQueryDefinition.Mode mode;
  private String login;
  private LazyLoadingContainer container;
  private String[] sortProps = {};
  private boolean[] sortAsc = {};

  public GroupsQuery(GroupsQueryDefinition.Mode mode, String login) {
    this.mode = mode;
    this.login = login;
  }

  @Override
  public int size() {
    if (mode == GroupsQueryDefinition.Mode.ORG) {
      return Flash.flash().getAdminService().getControlledOrgGroupsCount(login, container == null ? null : container.sender);
    } else {
      return Flash.flash().getAdminService().getControlledEmpGroupsCount(login, container == null ? null : container.sender);
    }
  }

  @Override
  public List<Item> loadItems(int startIndex, int count) {
    final List<Group> groups;
    if (mode == GroupsQueryDefinition.Mode.ORG) {
      groups = Flash.flash().getAdminService().getControlledOrgGroupsOf(login, startIndex, count, sortProps, sortAsc, container == null ? null : container.sender);
    } else {
      groups = Flash.flash().getAdminService().getControlledEmpGroupsOf(login, startIndex, count, sortProps, sortAsc, container == null ? null : container.sender);
    }

    List<Item> itemsList = Lists.newArrayListWithCapacity(groups.size());
    for (Group g : groups) {
      final PropertysetItem item = new PropertysetItem();
      item.addItemProperty("name", new ObjectProperty<String>(g.getName()));
      item.addItemProperty("title", new ObjectProperty<String>(g.getTitle()));
      itemsList.add(item);
    }
    return itemsList;
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

  @Override
  public Item loadSingleResult(String paramString) {
    System.out.println("paramString = [" + paramString + "]");
    return null;
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

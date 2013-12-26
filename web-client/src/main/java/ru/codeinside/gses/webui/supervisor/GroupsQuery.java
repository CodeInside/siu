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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GroupsQuery implements Query, Serializable {
    private static final long serialVersionUID = 1L;
    private GroupsQueryDefinition.Mode mode;
    private String login;

    public GroupsQuery(GroupsQueryDefinition.Mode mode, String login) {
        this.mode = mode;
        this.login = login;
    }

    @Override
    public int size() {
        if (mode == GroupsQueryDefinition.Mode.ORG) {
            return Flash.flash().getAdminService().getControlledOrgGroupsOf(login).size();
        } else {
            return Flash.flash().getAdminService().getControlledEmpGroupsOf(login).size();
        }
    }

    @Override
    public List<Item> loadItems(int startIndex, int count) {
        final Set<Group> groups;
        if (mode == GroupsQueryDefinition.Mode.ORG) {
            groups = Flash.flash().getAdminService().getControlledOrgGroupsOf(login);
        } else {
            groups = Flash.flash().getAdminService().getControlledEmpGroupsOf(login);
        }
        List<Group> list = new ArrayList<Group>(groups);

        Collections.sort(list, new NameComparator());

        list = list.subList(startIndex, startIndex + count);
        List<Item> itemsList = Lists.newArrayListWithCapacity(list.size());
        for (Group g : list) {
            final PropertysetItem item = new PropertysetItem();
            item.addItemProperty("id", new ObjectProperty<String>(g.getName()));
            item.addItemProperty("name", new ObjectProperty<String>(g.getTitle()));
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

    private static class NameComparator implements Comparator<Group> {
        @Override
        public int compare(final Group g1, final Group g2) {
            return g1.getName().compareTo(g2.getName());
        }
    }
}

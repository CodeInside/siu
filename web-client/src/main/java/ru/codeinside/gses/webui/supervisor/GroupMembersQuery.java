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
import ru.codeinside.adm.database.Employee;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.webui.Flash;

import java.io.Serializable;
import java.util.List;

public class GroupMembersQuery implements Query, Serializable {
    private String groupName;
    private Mode mode;
    private String taskId;

    public GroupMembersQuery(String groupName, Mode mode, String taskId) {
        this.groupName = groupName;
        this.mode = mode;
        this.taskId = taskId;
    }

    @Override
    public int size() {
        int size = mode == Mode.ORG ?
                Flash.flash().getAdminService().getOrgGroupMembersCount( groupName, taskId) :
                Flash.flash().getAdminService().getEmpGroupMembersCount( groupName, taskId);
        return size;
    }

    @Override
    public List<Item> loadItems(int startIndex, int count) {
        List<Employee> members = mode == Mode.ORG ?
                Flash.flash().getAdminService().getOrgGroupMembers( groupName, taskId, startIndex, count) :
                Flash.flash().getAdminService().getEmpGroupMembers( groupName, taskId, startIndex, count);
        List<Item> items = Lists.newArrayListWithExpectedSize(members.size());
        for (Employee e : members) {
            PropertysetItem item = new PropertysetItem();
            item.addItemProperty("login", new ObjectProperty<String>(e.getLogin()));
            item.addItemProperty("fio", new ObjectProperty<String>(e.getFio()));
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

    public enum Mode {
        ORG, SOC
    }
}

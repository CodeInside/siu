/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui.employee;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.UserItem;
import ru.codeinside.adm.database.Group;

import java.util.Collection;
import java.util.TreeSet;

final public class ExecutorGroupsBlock extends CustomComponent {

  final FilterTable currentExecutorGroups;

  public ExecutorGroupsBlock(UserItem userItem) {
    HorizontalLayout executorGroups = new HorizontalLayout();
    executorGroups.setMargin(true, false, true, false);
    executorGroups.setSpacing(true);
    executorGroups.setCaption("Группы исполнителей:");
    FilterTable allExecutorGroups = new FilterTable();
    allExecutorGroups.setCaption("Доступные:");
    TableEmployee.table(executorGroups, allExecutorGroups);
    currentExecutorGroups = new FilterTable();
    currentExecutorGroups.setCaption("Группы, к которым принадлежит пользователь:");
    TableEmployee.table(executorGroups, currentExecutorGroups);
    for (String groupName : AdminServiceProvider.get().getEmpGroupNames()) {
      for (Group group : AdminServiceProvider.get().findGroupByName(groupName)) {
        if (userItem.getGroups().contains(groupName)) {
          currentExecutorGroups.addItem(new Object[]{groupName, group.getTitle()}, groupName);
        } else {
          allExecutorGroups.addItem(new Object[]{groupName, group.getTitle()}, groupName);
        }
      }
    }
    TableEmployee.addListener(allExecutorGroups, currentExecutorGroups);
    TableEmployee.addListener(currentExecutorGroups, allExecutorGroups);

    VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);
    layout.setMargin(true);
    layout.setSizeFull();

    layout.addComponent(executorGroups);
    layout.setExpandRatio(executorGroups, 1f);

    setCompositionRoot(layout);
    setWidth(100f, UNITS_PERCENTAGE);
  }

  public TreeSet<String> getGroups() {
    return new TreeSet<String>((Collection<String>) currentExecutorGroups.getItemIds());
  }

}

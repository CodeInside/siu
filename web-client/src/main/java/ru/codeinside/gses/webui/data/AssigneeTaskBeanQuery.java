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
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.BidStatus;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.Flash;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import static ru.codeinside.gses.webui.utils.Components.stringProperty;

final public class AssigneeTaskBeanQuery implements Query, Serializable {

  private static final long serialVersionUID = 1L;
  final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");

  final String orderBy;
  final boolean asc;

  public AssigneeTaskBeanQuery(String orderBy, boolean asc) {
    this.orderBy = orderBy;
    this.asc = asc;
  }

  @Override
  public int size() {
    return Functions.withEngine(new PF<Long>() {

      private static final long serialVersionUID = 8134221821378394955L;

      public Long apply(ProcessEngine engine) {
        engine.getIdentityService().setAuthenticatedUserId(Flash.login());
        return engine.getTaskService().createTaskQuery().taskAssignee(Flash.login()).count();
      }

      ;
    }).intValue();
  }

  @Override
  public List<Item> loadItems(final int startIndex, final int count) {
    return Functions.withEngine(new PF<List<Item>>() {

      private static final long serialVersionUID = 5187290510402601546L;

      public List<Item> apply(ProcessEngine engine) {
        engine.getIdentityService().setAuthenticatedUserId(Flash.login());
        final TaskQuery query = engine.getTaskService().createTaskQuery();
        if ("name".equals(orderBy)) {
          query.orderByTaskName();
        } else if ("id".equals(orderBy)) {
          query.orderByTaskCreateTime();
        } else if ("process".equals(orderBy)) {
          query.orderByProcessInstanceId();
        } else {
          query.orderByDueDate();
        }
        if (asc) {
          query.asc();
        } else {
          query.desc();
        }
        query.taskAssignee(Flash.login());
        final List<Task> tasks = query.listPage(startIndex, count);
        final List<Item> items = Lists.newArrayListWithExpectedSize(tasks.size());
        for (final Task task : tasks) {
          final Bid bid = getBid(task);
          final PropertysetItem item = new PropertysetItem();
          item.addItemProperty("id", stringProperty(task.getId()));
          item.addItemProperty("name", stringProperty(task.getName()));
          item.addItemProperty("startDate", stringProperty(formatter.format(bid.getDateCreated())));
          Procedure procedure = bid.getProcedure();
          if (procedure != null) {
            if (bid.getTag().isEmpty()) {
            item.addItemProperty("process", stringProperty(procedure.getName()));
            } else {
              item.addItemProperty("process", stringProperty(bid.getTag()+" - "+procedure.getName()));
            }
          }
          item.addItemProperty("declarant", stringProperty(bid.getDeclarant()));
          item.addItemProperty("version", stringProperty(StringUtils.trimToEmpty(bid.getVersion())));
          item.addItemProperty("status", stringProperty(bid.getStatus().getName()));
          items.add(item);
        }
        return items;
      }

      private Bid getBid(final Task task) {
        Bid bid = AdminServiceProvider.get().getBidByTask(task.getId());

        if (bid == null) {
          bid = new Bid();
          bid.setDeclarant("");
          bid.setVersion("");
          bid.setStatus(BidStatus.Execute);
        }
        return bid;
      }

      ;
    });
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

}

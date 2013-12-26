/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vaadin.data.Item;
import com.vaadin.data.util.PropertysetItem;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
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
import ru.codeinside.gses.webui.components.HistoricTaskInstancesQuery;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static ru.codeinside.gses.webui.utils.Components.stringProperty;

final public class OwnHistoryBeanQuery implements Query, Serializable {

  final static private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");

  private static final long serialVersionUID = 1L;

  public OwnHistoryBeanQuery() {
  }

  @Override
  public int size() {
    return Flash.flash().getAdminService().countOfBidByEmail(Flash.login());
  }

  @Override
  public List<Item> loadItems(final int startIndex, final int count) {
      List<Bid> bidIds = Flash.flash().getAdminService().bidsByLogin(Flash.login(), startIndex, count);
      final List<Item> items = Lists.newArrayListWithExpectedSize(bidIds.size());
      for( Bid bid : bidIds){
          final PropertysetItem item = new PropertysetItem();
          item.addItemProperty("id", stringProperty(bid.getId().toString()));
          item.addItemProperty("procedure", stringProperty(bid.getProcedure().getName()));

          item.addItemProperty("startDate", stringProperty(formatter.format(bid.getDateCreated())));
          item.addItemProperty("finishDate", stringProperty(bid.getDateFinished() == null ? "" : formatter.format(bid.getDateFinished())));
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

}

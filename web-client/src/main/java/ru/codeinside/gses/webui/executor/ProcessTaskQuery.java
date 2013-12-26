/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.executor;

import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.webui.Flash;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ProcessTaskQuery implements Query, Serializable {

  Long procedureId;

  public ProcessTaskQuery(Long procedureId){
    this.procedureId = procedureId;
  }

  @Override
  public int size() {
    return Flash.flash().getExecutorService().countTasksByProcedureId(procedureId);
  }

  @Override
  public List<Item> loadItems(int startIndex, int count) {
    Map<String, TaskDefinition> tasks = Flash.flash().getExecutorService().selectTasksByProcedureId(procedureId);
    final List<Item> items = Lists.newArrayListWithExpectedSize(tasks.size());
    for(String key : tasks.keySet()){
      String taskDefKey = tasks.get(key).getKey();
      String taskName = tasks.get(key).getNameExpression().getExpressionText();
      final PropertysetItem item = new PropertysetItem();
      item.addItemProperty("name", new ObjectProperty<String>(taskName));
      item.addItemProperty("taskDefKey", new ObjectProperty<String>(taskDefKey));
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

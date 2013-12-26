/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.webui.components.api.IRefresh;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.data.AbstractLazyLoadingQuery;

public class ControlledTasksTable extends Table implements IRefresh{
  public static final long serialVersionUID = 1L;
  public ControlledTasksTable(AbstractLazyLoadingQuery<Task> query){
    setSizeFull();
    Container container = new LazyLoadingContainer(query, 15);
    setContainerDataSource(container);
  }
  @Override
  public void refresh() {
    getContainerDataSource().removeAllItems();
    refreshRowCache();
  }
}

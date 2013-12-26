/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import org.activiti.engine.task.Task;

import ru.codeinside.gses.webui.components.api.IRefresh;
import ru.codeinside.gses.webui.data.AbstractLazyLoadingQuery;
import ru.codeinside.gses.webui.data.TaskListQuery;

import com.vaadin.ui.Component;

public class TaskListUi extends BaseListUi<Task> implements IRefresh {

	private static final long serialVersionUID = -4193312576457459527L;

	public TaskListUi(AbstractLazyLoadingQuery<Task> loadingQuery) {
		super(loadingQuery);

		itemsTable.addContainerProperty("id", Component.class, null);
		itemsTable.addContainerProperty("name", String.class, null);
		itemsTable.addContainerProperty("exe", Component.class, null);
		itemsTable.addContainerProperty("assignee", Component.class, null);

		itemsTable.setVisibleColumns(TaskListQuery.NATURAL_COL_ORDER);
		itemsTable.setColumnHeaders(TaskListQuery.COL_HEADERS_RUS);
	}

}

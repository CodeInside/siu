/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import org.activiti.engine.task.Task;

import ru.codeinside.gses.webui.components.api.IRefresh;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.data.AbstractLazyLoadingQuery;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;

final public class TableForExecutor extends Table implements IRefresh {

	private static final long serialVersionUID = 1L;

	public TableForExecutor(AbstractLazyLoadingQuery<Task> loadingQuery) {
		setSizeFull();
		Container newDataSource = new LazyLoadingContainer(loadingQuery, 15);
		setContainerDataSource(newDataSource);
	}

	@Override
	public void refresh() {
		getContainerDataSource().removeAllItems();
		refreshRowCache();
	}

}

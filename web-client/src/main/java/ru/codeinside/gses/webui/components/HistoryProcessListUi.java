/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import org.activiti.engine.history.HistoricProcessInstance;

import com.vaadin.ui.Component;

import ru.codeinside.gses.webui.data.HistoryProcessListQuery;
import ru.codeinside.gses.webui.data.HistoryTaskListQuery;

public class HistoryProcessListUi extends BaseListUi<HistoricProcessInstance> {

	private static final long serialVersionUID = 5017128760884353558L;

	public HistoryProcessListUi(HistoryProcessListQuery loadingQuery) {
		super(loadingQuery);		
		itemsTable.addContainerProperty("id", Component.class, null);
		itemsTable.addContainerProperty("name", String.class, null);
		itemsTable.addContainerProperty("exe", String.class, null);

		itemsTable.setColumnHeaders(HistoryTaskListQuery.COL_HEADERS_ENGLISH);
	}
}
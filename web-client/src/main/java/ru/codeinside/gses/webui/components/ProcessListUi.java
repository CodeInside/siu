/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import org.activiti.engine.runtime.ProcessInstance;

import ru.codeinside.gses.webui.data.ProcessListQuery;

import com.vaadin.ui.Component;

public class ProcessListUi extends BaseListUi<ProcessInstance> {

	private static final long serialVersionUID = -463002049767709705L;

	public ProcessListUi(ProcessListQuery loadingQuery) {
		super(loadingQuery);

		itemsTable.addContainerProperty("id", Component.class, null);
		itemsTable.addContainerProperty("name", String.class, null);
		itemsTable.addContainerProperty("exe", Component.class, null);

		itemsTable.setColumnHeaders(new String[] { "Номер", "Название Процедуры", "Действие" });
	}

}

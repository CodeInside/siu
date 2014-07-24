/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.items.builders;

import org.activiti.engine.runtime.ProcessInstance;

import ru.codeinside.gses.webui.data.ItemBuilder;
import ru.codeinside.gses.webui.actions.ShowProcessUiListener;
import ru.codeinside.gses.webui.items.ProcessListItem;

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickListener;

public class ProcessItemBuilder implements ItemBuilder<ProcessInstance> {

	private static final long serialVersionUID = -8192612911902082715L;
	private final String user;

	public ProcessItemBuilder(String user) {
		this.user = user;
	}

	@Override
	public Item createItem(ProcessInstance task) {
		ClickListener showListener = new ShowProcessUiListener(task);
		return new ProcessListItem(task, showListener, user);
	}
}

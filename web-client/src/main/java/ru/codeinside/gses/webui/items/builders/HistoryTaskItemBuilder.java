/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.items.builders;

import org.activiti.engine.history.HistoricTaskInstance;

import ru.codeinside.gses.webui.actions.ItemBuilder;
import ru.codeinside.gses.webui.actions.ShowHistoryTaskUiListener;
import ru.codeinside.gses.webui.items.HistoryTaskListItem;

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickListener;

public class HistoryTaskItemBuilder implements ItemBuilder<HistoricTaskInstance> {

	private static final long serialVersionUID = -3933149327940014302L;

	@Override
	public Item createItem(HistoricTaskInstance task) {
		ClickListener showListener = new ShowHistoryTaskUiListener(task);
		return new HistoryTaskListItem(task, showListener);
	}
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.items.builders;

import org.activiti.engine.history.HistoricProcessInstance;

import ru.codeinside.gses.webui.actions.ItemBuilder;
import ru.codeinside.gses.webui.actions.ShowHistoryProcessUiListener;
import ru.codeinside.gses.webui.items.HistoryProcessListItem;

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickListener;

public class HistoryProcessItemBuilder implements ItemBuilder<HistoricProcessInstance> {

	private static final long serialVersionUID = 7129615252952745163L;

	@Override
	public Item createItem(HistoricProcessInstance task) {
		ClickListener showListener = new ShowHistoryProcessUiListener(task);
		return new HistoryProcessListItem(task, showListener);
	}
}

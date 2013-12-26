/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.actions;

import org.activiti.engine.history.HistoricProcessInstance;

import ru.codeinside.gses.webui.components.HistoryProcessShowUi;
import ru.codeinside.gses.webui.utils.Components;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ShowHistoryProcessUiListener implements ClickListener {

	private static final long serialVersionUID = -1195762164863251895L;
	private final HistoricProcessInstance task;

	public ShowHistoryProcessUiListener(final HistoricProcessInstance task) {
		this.task = task;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		HistoryProcessShowUi showUi = new HistoryProcessShowUi(task);
		Components.showComponent(event, showUi, "Просмотр");
	}
}

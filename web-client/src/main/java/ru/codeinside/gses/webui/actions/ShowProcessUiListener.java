/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.actions;

import org.activiti.engine.runtime.ProcessInstance;

import ru.codeinside.gses.webui.components.ProcessShowUi;
import ru.codeinside.gses.webui.utils.Components;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ShowProcessUiListener implements ClickListener {

	private static final long serialVersionUID = -2391735023583799514L;
	private final ProcessInstance task;

	public ShowProcessUiListener(final ProcessInstance task) {
		this.task = task;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		ProcessShowUi showUi = new ProcessShowUi(task);
		Components.showComponent(event, showUi, "Процедура");
	}
}

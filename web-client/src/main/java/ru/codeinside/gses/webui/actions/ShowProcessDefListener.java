/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.actions;

import org.activiti.engine.repository.ProcessDefinition;

import ru.codeinside.gses.webui.components.ContentWindowChanger;
import ru.codeinside.gses.webui.components.ProcessDefinitionShowUi;
import ru.codeinside.gses.webui.utils.Components;

import com.vaadin.Application;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

public class ShowProcessDefListener implements ClickListener {

	private static final long serialVersionUID = 641082715130283341L;
	private final ProcessDefinition processDefinition;
	private final Application application;

	public ShowProcessDefListener(ProcessDefinition processDefinition, Application application) {
		this.processDefinition = processDefinition;
		this.application = application;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Window mainWindow = event.getButton().getApplication().getMainWindow();
		String caption = "Процедура";
		Window win = Components.createWindow(mainWindow, caption);
		ContentWindowChanger changer = new ContentWindowChanger(win);
		ProcessDefinitionShowUi putComponent = new ProcessDefinitionShowUi(mainWindow.getApplication(), processDefinition, changer);
		changer.set(putComponent, caption);
	}
}
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.actions;

import java.util.logging.Logger;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;

public class RunStepFormListener implements Button.ClickListener {
	private static final long serialVersionUID = 1L;
	private final String taskId;
	private final Form form;
	private final String user;
	static final String errorDescription = "Не удалось запустить этап";

	private final static Logger logger = Logger.getLogger(RunStepFormListener.class.getName());

	public RunStepFormListener(String user, String taskId, String processInsId, Form form) {
		this.user = user;
		this.taskId = taskId;
		this.form = form;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		throw new UnsupportedOperationException();
	}
}

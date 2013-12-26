/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.actions.deployment;

import org.apache.commons.lang.StringUtils;

import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Window.Notification;

public class DeploymentStartListener implements StartedListener {

	private static final long serialVersionUID = -7877966789213030169L;

	@Override
	public void uploadStarted(StartedEvent event) {
		String name = event.getFilename();
		boolean isFileEmpty = StringUtils.isEmpty(name);
		if (isFileEmpty || !(name.endsWith(".xml") || name.endsWith(".bpmn"))) {
			event.getUpload().interruptUpload();
			event.getUpload()
					.getWindow()
					.showNotification("Ошибка", "Неверный формат файла, файл должен заканчиваться на 'bpmn20.xml', '.xml', '.bpmn'",
							Notification.TYPE_ERROR_MESSAGE);
		}
	}

}

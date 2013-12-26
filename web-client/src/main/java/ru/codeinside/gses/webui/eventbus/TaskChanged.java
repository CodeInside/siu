/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.eventbus;

import java.util.EventObject;

final public class TaskChanged extends EventObject {

	private static final long serialVersionUID = 1L;

	public final String taskId;

	public TaskChanged(Object source, String taskId) {
		super(source);
		this.taskId = taskId;
	}

}

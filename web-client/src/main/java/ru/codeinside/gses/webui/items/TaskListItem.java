/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.items;

import static ru.codeinside.gses.webui.utils.Components.buttonProperty;
import static ru.codeinside.gses.webui.utils.Components.stringProperty;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Button.ClickListener;

public class TaskListItem extends PropertysetItem implements Comparable<TaskListItem> {

	private static final long serialVersionUID = 1723435156622003161L;

	public TaskListItem(final ProcessDefinition processDefinition, final Task task,//
	        final ClickListener showTaskListener, //
	        final ClickListener nextStepTaskListener,//
	        final ClickListener claimListener) {
		addItemProperty("id", buttonProperty(task.getId(), showTaskListener));
		addItemProperty("name", stringProperty(task.getName() + "(" + processDefinition.getName() + ")"));
		addItemProperty("exe", buttonProperty(task.getName(), nextStepTaskListener));
		addItemProperty("assignee", buttonProperty(task.getAssignee(), "Забрать", claimListener));
	}

	public int compareTo(TaskListItem other) {
		String taskId = (String) getItemProperty("id").getValue();
		String otherTaskId = (String) other.getItemProperty("id").getValue();
		return taskId.compareTo(otherTaskId);
	}
}
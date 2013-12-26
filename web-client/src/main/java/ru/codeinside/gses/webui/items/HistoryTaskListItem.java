/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.items;

import static ru.codeinside.gses.webui.utils.Components.buttonProperty;
import static ru.codeinside.gses.webui.utils.Components.stringProperty;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;

import ru.codeinside.gses.webui.Flash;

import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Button.ClickListener;

public class HistoryTaskListItem extends PropertysetItem implements Comparable<HistoryTaskListItem> {
	
	private static final long serialVersionUID = -133775134065702705L;

	public HistoryTaskListItem(final HistoricTaskInstance task, ClickListener showTaskListener) {
		ProcessDefinition processDefinition = ((ProcessDefinition) Flash.flash().getProcessEngine().getRepositoryService()
		        .createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult());

		addItemProperty("id", buttonProperty(task.getId(), showTaskListener));
		addItemProperty("name", stringProperty(task.getName() + "(" + processDefinition.getName() + ")"));
		addItemProperty("exe", stringProperty(task.getName()));
	}

	public int compareTo(HistoryTaskListItem other) {
		String taskId = (String) getItemProperty("id").getValue();
		String otherTaskId = (String) other.getItemProperty("id").getValue();
		return taskId.compareTo(otherTaskId);
	}
}
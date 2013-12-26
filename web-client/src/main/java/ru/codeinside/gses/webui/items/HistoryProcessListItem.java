/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.items;

import static ru.codeinside.gses.webui.utils.Components.buttonProperty;
import static ru.codeinside.gses.webui.utils.Components.stringProperty;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;

import ru.codeinside.gses.webui.Flash;

import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Button.ClickListener;

public class HistoryProcessListItem extends PropertysetItem implements Comparable<HistoryProcessListItem> {

	private static final long serialVersionUID = 4879859651046810120L;

	public HistoryProcessListItem(final HistoricProcessInstance task, ClickListener showTaskListener) {
		ProcessDefinition processDefinition = ((ProcessDefinition) Flash.flash().getProcessEngine()
		        .getRepositoryService().createProcessDefinitionQuery()
		        .processDefinitionId(task.getProcessDefinitionId()).singleResult());

		addItemProperty("id", buttonProperty(task.getId(), showTaskListener));
		addItemProperty("name", stringProperty(processDefinition.getName()));
		addItemProperty("exe", stringProperty(task.getEndActivityId()));
	}

	public int compareTo(HistoryProcessListItem other) {
		String taskId = (String) getItemProperty("id").getValue();
		String otherTaskId = (String) other.getItemProperty("id").getValue();
		return taskId.compareTo(otherTaskId);
	}
}
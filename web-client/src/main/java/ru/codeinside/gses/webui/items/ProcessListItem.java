/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.items;

import static ru.codeinside.gses.webui.utils.Components.buttonProperty;
import static ru.codeinside.gses.webui.utils.Components.createButton;
import static ru.codeinside.gses.webui.utils.Components.stringProperty;

import java.util.List;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.actions.NextStepTaskUiListener;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class ProcessListItem extends PropertysetItem implements Comparable<ProcessListItem> {

	private static final long serialVersionUID = 3295051351277367650L;

	public ProcessListItem(final ProcessInstance proccess, ClickListener showTaskListener, final String user) {
		ProcessDefinition processDefinition = ((ProcessDefinition) Flash.flash().getProcessEngine()
		        .getRepositoryService().createProcessDefinitionQuery()
		        .processDefinitionId(proccess.getProcessDefinitionId()).singleResult());

		addItemProperty("id", buttonProperty(proccess.getId(), showTaskListener));
		addItemProperty("name", stringProperty(processDefinition.getName()));

		List<Task> list = Flash.flash().getProcessEngine().getTaskService().createTaskQuery()
		        .processInstanceId(proccess.getProcessInstanceId()).list();

		Panel p = new Panel();
		p.setContent(new HorizontalLayout());
		p.setWidth("450.0px");
		for (final Task t : list) {
			p.addComponent(createButton(t.getName(), new NextStepTaskUiListener(user, t)));
			p.addComponent(new Label("|"));
		}
		addItemProperty("exe", new ObjectProperty<Component>(p));
	}

	public int compareTo(ProcessListItem other) {
		String taskId = (String) getItemProperty("id").getValue();
		String otherTaskId = (String) other.getItemProperty("id").getValue();
		return taskId.compareTo(otherTaskId);
	}
}
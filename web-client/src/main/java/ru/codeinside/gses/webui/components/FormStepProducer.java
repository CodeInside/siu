/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.actions.RunStepFormListener;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public final class FormStepProducer implements PF<Component> {
	private static final long serialVersionUID = 1L;
	private final String taskId;
	private final String user;

	FormStepProducer(String taskId, String user) {
		this.taskId = taskId;
		this.user = user;
	}

	public Component apply(ProcessEngine pe) {
		pe.getIdentityService().setAuthenticatedUserId(user);

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		TaskFormData taskFormData = pe.getFormService().getTaskFormData(taskId);

		Task task = taskFormData.getTask();
		ProcessDefinition processDef = pe.getRepositoryService().createProcessDefinitionQuery()
		        .processDefinitionId(task.getProcessDefinitionId()).singleResult();

		Label label = new Label(processDef.getName());
		label.setStyleName("h2");

		Label label2 = new Label(task.getName());
		Form form = FormStepRunUi.createForm(taskFormData, task.getProcessInstanceId());

		Button button = new Button("Обработать");
		button.addListener(new RunStepFormListener(user, taskId, task.getProcessInstanceId(), form));

		Panel panel = new Panel();
		panel.getContent().setSizeUndefined();
		panel.addComponent(form);
		panel.setSizeFull();
		panel.setScrollable(true);

		layout.addComponent(label);
		layout.addComponent(label2);
		layout.addComponent(panel);
		layout.addComponent(button);

		layout.setExpandRatio(label, 1);
		layout.setExpandRatio(label2, 1);
		layout.setExpandRatio(panel, 93);
		layout.setExpandRatio(button, 1);

		return layout;
	}
}
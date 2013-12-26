/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.actions;

import java.io.Serializable;

import org.activiti.engine.repository.ProcessDefinition;

import ru.codeinside.gses.webui.components.api.Changer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

public class FormShowListener implements ClickListener, Serializable {

	private static final long serialVersionUID = 8271735255424886607L;

	private ProcessDefinition processDefinition;
	private Changer changer;

	public FormShowListener(ProcessDefinition processDefinition, Changer changer) {
		this.processDefinition = processDefinition;
		this.changer = changer;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button back = new Button("Назад");
		back.addListener(new ClickListener() {

			private static final long serialVersionUID = -2254232821122927192L;

			@Override
			public void buttonClick(ClickEvent event) {
				changer.back();

			}
		});
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(back);
		verticalLayout.setWidth("900px");
		changer.change(verticalLayout);
	}

}

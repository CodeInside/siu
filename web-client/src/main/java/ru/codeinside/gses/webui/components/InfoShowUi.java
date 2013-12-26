/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;

import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.Components;

import com.google.common.base.Function;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

public abstract class InfoShowUi extends CustomComponent {
	
	private static final long serialVersionUID = 4467209258993970997L;
	private final ProcessDefinitionEntity entity;

	abstract String getLabelIdValue();

	abstract String getRouteLeg();

	abstract List<Object[]> getHistories();

	abstract List<Object[]> getVariables();

	abstract List<Object[]> getAttachments();

	abstract List<String> getHistoryActivitiIds();

	public InfoShowUi(final String processDefinitionId) {
		entity = Functions.withRepository(Flash.login(),
		        new Function<RepositoryService, ProcessDefinitionEntity>() {
			        public ProcessDefinitionEntity apply(final RepositoryService srv) {
				        RepositoryServiceImpl impl = (RepositoryServiceImpl) srv;
				        return (ProcessDefinitionEntity) impl.getDeployedProcessDefinition(processDefinitionId);
			        }
		        });
		setWidth(1000, Sizeable.UNITS_PIXELS);
		setHeight(600, Sizeable.UNITS_PIXELS);
	}

	Component buildMainLayout() {
		GridLayout layout = new GridLayout(2, 4);
		layout.setSizeFull();
		layout.setSpacing(true);

		HorizontalLayout h = new HorizontalLayout();
		h.setWidth(100, UNITS_PERCENTAGE);
		{
			final Label h1 = new Label(getLabelIdValue());
			final Label h2 = new Label(entity.getProcessDefinition().getName());
			h2.setStyleName("h2");

			final Label h3 = new Label(getRouteLeg());
			h3.setStyleName("light");

			h.setSpacing(true);
			h.addComponent(h1);
			h.addComponent(h2);
			h.addComponent(h3);

			h.setComponentAlignment(h1, Alignment.BOTTOM_RIGHT);
			h.setComponentAlignment(h2, Alignment.BOTTOM_CENTER);
			h.setComponentAlignment(h3, Alignment.BOTTOM_RIGHT);

			h.setExpandRatio(h1, 1);
			h.setExpandRatio(h2, 4);
			h.setExpandRatio(h3, 1);
		}

		Table table_2 = Components.createProcessRouteTable(entity, "100%", "100%");
		table_2.setCaption("Маршрут");

		Table table_1 = Components.createTable("100%", "100%");
		table_1.setCaption("История выполнения");
		table_1.addContainerProperty("id", String.class, null);
		table_1.addContainerProperty("name", String.class, null);
		table_1.addContainerProperty("startDate", String.class, null);
		table_1.addContainerProperty("endDate", String.class, null);
		table_1.addContainerProperty("assignee", String.class, null);
		table_1.addContainerProperty("owner", String.class, null);
		table_1.setColumnHeaders(new String[] { "Номер", "Название", "Начало", "Окончание", "Назначен", "Владелец" });
		int index = 0;
		for (Object[] item : getHistories()) {
			table_1.addItem(item, index++);
		}

		Panel panel = new Panel();
		panel.getContent().setSizeUndefined();
		panel.addComponent(new EmbeddedGraph(entity, getHistoryActivitiIds(), Flash.app()));
		panel.setSizeFull();
		panel.setScrollable(true);

		Table table_3 = Components.createTable("100%", "100%");
		table_3.setCaption("Параметры Процедуры");
		table_3.addContainerProperty("id", String.class, null);
		table_3.addContainerProperty("name", String.class, null);
		table_3.setColumnHeaders(new String[] { "Номер", "Значение" });
		index = 0;
		for (Object[] m : getVariables()) {
			table_3.addItem(m, index++);
		}

		Table table_4 = Components.createTable("100%", "100%");
		table_4.setCaption("Вложения");
		table_4.addContainerProperty("name", String.class, null);
		table_4.addContainerProperty("exe", Component.class, null);
		table_4.setColumnHeaders(new String[] { "Имя", "Значение" });
		index = 0;
		for (Object[] m : getAttachments()) {
			table_4.addItem(m, index++);
		}

		layout.addComponent(h, 0, 0, 1, 0);
		layout.addComponent(table_2, 0, 1);
		layout.addComponent(table_1, 1, 1);
		layout.addComponent(panel, 0, 2, 0, 3);
		layout.addComponent(table_3, 1, 2);
		layout.addComponent(table_4, 1, 3);

		layout.setRowExpandRatio(1, 10);
		layout.setRowExpandRatio(2, 5);
		layout.setRowExpandRatio(3, 5);

		return layout;
	}

}

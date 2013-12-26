/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import java.util.List;
import java.util.Set;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Group;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class TableGroup extends VerticalLayout implements Property.ValueChangeListener {

	private static final long serialVersionUID = 1L;
	private String typeGroup;
	private final Table table;
	private final Panel panel = new Panel();

	TableGroup(String typeGroup) {
		setSizeFull();
		this.typeGroup = typeGroup;
		setMargin(false, false, false, true);
		table = new Table();
		table.setSizeFull();
		table.setSelectable(true);
		table.setMultiSelect(false);
		table.addListener(this);
		table.setImmediate(true);

		table.addContainerProperty("Код", String.class, "");
		table.addContainerProperty("Название", String.class, "");
		Set<String> groupNames = null;
		if (typeGroup == GroupTab.ORGANIZATION) {
			groupNames = AdminServiceProvider.get().getOrgGroupNames();
		} else if (typeGroup == GroupTab.EMPLOYEE) {
			groupNames = AdminServiceProvider.get().getEmpGroupNames();
		}
		for (String groupName : groupNames) {
			List<Group> groups = AdminServiceProvider.get().findGroupByName(groupName);
			for (Group group : groups) {
				table.addItem(new Object[]{groupName, group.getTitle()}, groupName);
			}
		}

		panel.setStyleName(Reindeer.PANEL_LIGHT);
		panel.setSizeFull();

		final HorizontalSplitPanel horiz = new HorizontalSplitPanel();
		horiz.setSplitPosition(25); // percent
		horiz.setSizeFull();
		addComponent(horiz);
		horiz.addComponent(table);
		horiz.addComponent(panel);
	}

	public void addItem(String group, String title) {
		table.addItem(new Object[]{group, title}, group);
	}

	public void valueChange(ValueChangeEvent event) {
		Object valuePropertyEvent = event.getProperty().getValue();
		if (valuePropertyEvent != null) {
			showGroupEditor(valuePropertyEvent.toString());
		} else {
			panel.removeAllComponents();
		}
	}

	private void showGroupEditor(String group) {
		panel.removeAllComponents();
		panel.addComponent(new GroupEditor(typeGroup, group, table));
	}
}

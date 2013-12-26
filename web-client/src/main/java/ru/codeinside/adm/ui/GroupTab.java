/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class GroupTab extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	public static final String EMPLOYEE = "Сотрудники";
	public static final String ORGANIZATION = "Организации";

	public GroupTab() {
		setSizeFull();
		setSpacing(true);
		TabSheet t = new TabSheet();
		addComponent(t);
		t.setSizeFull();
		showGroupTab(t, ORGANIZATION);
		showGroupTab(t, EMPLOYEE);
	}

	private void showGroupTab(TabSheet t, final String typeGroup) {
		VerticalLayout l = new VerticalLayout();
		l.setSizeFull();
		TableGroup tableGroup = new TableGroup(typeGroup);
		ButtonCreateGroup buttonCreateGroup = new ButtonCreateGroup(typeGroup, tableGroup);
		l.addComponent(buttonCreateGroup);
		l.addComponent(tableGroup);
		l.setExpandRatio(buttonCreateGroup, 0.1f);
		l.setExpandRatio(tableGroup, 100f);
		t.addTab(l, typeGroup);
	}
}

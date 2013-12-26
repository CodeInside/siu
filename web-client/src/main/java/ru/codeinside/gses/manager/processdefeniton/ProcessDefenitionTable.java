/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.manager.processdefeniton;

import ru.codeinside.adm.ui.LazyLoadingContainer2;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class ProcessDefenitionTable extends VerticalLayout {

	private static final long serialVersionUID = -3060552897820352215L;

	private static final String[] NAMES = new String[] { "Версия", "Статус", "Изменен", "Пользователь", " ", " " };

	private LazyLoadingContainer2 container;
	
	public LazyLoadingContainer2 getTableContainer(){
		return container;
	}
	
	public ProcessDefenitionTable(String procedureId, LazyLoadingContainer2 proceduresContainer) {
		final Table listAp = new Table();
		Container.ItemSetChangeListener listener = new Container.ItemSetChangeListener() {
						
			private static final long serialVersionUID = 1391138329800139622L;

			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				listAp.refreshRowCache();				
			}
		};
		LazyLoadingContainer2 newDataSource = new LazyLoadingContainer2(new ProcessDefenitionQuery(procedureId, proceduresContainer));
		newDataSource.addListener(listener);
		container = newDataSource;
		
		listAp.setContainerDataSource(newDataSource);
		listAp.addContainerProperty("version", Component.class, "");
		listAp.addContainerProperty("status", Component.class, "");
		listAp.addContainerProperty("date", String.class, "");
		listAp.addContainerProperty("user", String.class, "");
		listAp.addContainerProperty("getRoute", Component.class, "");
		listAp.addContainerProperty("download", Component.class, "");
		listAp.setColumnExpandRatio("download", 1f);
		listAp.sort(new String[] { "version" }, new boolean[] { false });
		listAp.setHeight("220px");
		listAp.setWidth("100%");
		listAp.setSortDisabled(true);
		listAp.setColumnHeaders(NAMES);
        listAp.setColumnWidth("status", 255);
		listAp.setColumnExpandRatio("version", 0.08f);
//		listAp.setColumnExpandRatio("status", 0.3f);
		listAp.setColumnExpandRatio("date", 0.1f);
		listAp.setColumnExpandRatio("user", 0.1f);
		listAp.setColumnExpandRatio("download", 0.4f);
		addComponent(listAp);
	}

}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import ru.codeinside.gses.vaadin.FieldFormType;
import ru.codeinside.gses.webui.Configurator;
import ru.codeinside.gses.webui.components.api.IRefresh;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class FieldFormTypeListUi extends CustomComponent implements IRefresh {

	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;

	public Table pagedTable;

	public FieldFormTypeListUi() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);

		setWidth("910px");

		pagedTable = new Table();
		pagedTable.setImmediate(false);
		pagedTable.setWidth("100%");
		pagedTable.setEditable(false);
		pagedTable.setImmediate(true);
		pagedTable.setSelectable(true);
		pagedTable.setNullSelectionAllowed(false);

		pagedTable.addContainerProperty(TYPE_NAME, String.class, null);
		pagedTable.addContainerProperty(FIELD_VIEW, Component.class, null);

		BeanItemContainer<FFTBean> beans = new BeanItemContainer<FFTBean>(FFTBean.class);

		for (FieldFormType fieldFormType : Configurator.getFields()) {
			try {
				FFTBean bean = new FFTBean();
				bean.name = fieldFormType.getFromType();
				bean.component = fieldFormType.createConstructorOfField().createField("", "", null, true, false);
				beans.addBean(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		pagedTable.setContainerDataSource(beans);

		pagedTable.setVisibleColumns(new String[] { TYPE_NAME, FIELD_VIEW });
		pagedTable.setColumnHeaders(new String[] { "Название типа", "Вид по-умолчанию" });

		mainLayout.addComponent(pagedTable);
		pagedTable.setPageLength(25);

		return mainLayout;
	}

	private static String TYPE_NAME = "name";
	private static String FIELD_VIEW = "component";

	@Override
	public void refresh() {
		pagedTable.getContainerDataSource().removeAllItems();
		pagedTable.refreshRowCache();
	}

}
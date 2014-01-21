/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Organization;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

public class ButtonCreateOrganization extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final String NAME = "Название организации";
	private TreeTable treetable;

	public ButtonCreateOrganization(final TreeTable treetable) {
		this.treetable = treetable;
		setSpacing(true);
		setMargin(false, true, true, false);
		showButtonCreateOrganization();
	}

	private void showButtonCreateOrganization() {
		removeAllComponents();

		final Button createOrg = new Button("Создание организации", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				removeAllComponents();
				final Form form = new Form();
				form.addField(NAME, new TextField(NAME));
				form.getField(NAME).setRequired(true);
				form.getField(NAME).setRequiredError("Введите название организации");
				form.getField(NAME).addValidator(
						new StringLengthValidator("Название организации должно быть не более 255 символов", 1, 255,
								false));
				form.getField(NAME).setWidth("500px");
				addComponent(form);
				Button create = new Button("Создать", new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						try {
							form.commit();
							String name = form.getField(NAME).getValue().toString();
							Organization org = AdminServiceProvider.get().createOrganization(name,
									getApplication().getUser().toString(), null);
							treetable.addItem(new Object[]{org.getName()}, org.getId());
							treetable.setChildrenAllowed(org.getId(), false);
                            treetable.setValue(org.getId());
							treetable.requestRepaint();
							showButtonCreateOrganization();
							getWindow().showNotification("Организация " + name + " создана");
						} catch (Exception e) {

						}
					}
				});
				final Button cancel = new Button("Отменить", new Button.ClickListener() {

					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						showButtonCreateOrganization();
					}
				});
				HorizontalLayout buttons = new HorizontalLayout();
				buttons.setSpacing(true);
				buttons.addComponent(create);
				buttons.addComponent(cancel);
				form.getFooter().addComponent(buttons);
			}
		});
		addComponent(createOrg);
	}
}

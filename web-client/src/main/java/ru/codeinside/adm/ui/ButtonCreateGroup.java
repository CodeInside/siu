/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import ru.codeinside.adm.AdminServiceProvider;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

public class ButtonCreateGroup extends VerticalLayout {

	private static final String NAME = "Код группы";
	private static final String TITLE = "Название группы";

	private static final long serialVersionUID = 1L;

	private String typeGroup;
	private TableGroup tableGroup;

	ButtonCreateGroup(final String typeGroup, TableGroup tableGroup) {
		this.typeGroup = typeGroup;
		this.tableGroup = tableGroup;
		setMargin(true);
		showButtonCreateGroup();
	}

	private void showButtonCreateGroup() {

		removeAllComponents();

		final Button buttonCreateGroup = new Button("Создание группы", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				removeAllComponents();
				final Form groupForm = new Form();
				groupForm.addField(NAME, new TextField(NAME));
				groupForm.getField(NAME).setRequired(true);
				groupForm.getField(NAME).setRequiredError("Введите код группы");
				groupForm.getField(NAME).addValidator(
						new StringLengthValidator("Код группы должен быть не более 255 символов", 1, 255, false));
				groupForm.getField(NAME).addValidator(new Validator() {

					private static final long serialVersionUID = 1L;

					public void validate(Object value) throws InvalidValueException {
						if (!isValid(value)) {
							if (value != null && value.toString().matches("[0-9].{0,}")) {
								throw new InvalidValueException("Код группы должен начинаться с латинской буквы");
							} else {
								throw new InvalidValueException("Код группы должен состоять из латинских букв и цифр");
							}
						}
					}

					public boolean isValid(Object value) {
						if (value == null || !(value instanceof String)) {
							return false;
						}

						return ((String) value).matches("[aA-zZ][aA0-zZ9]{0,}");
					}
				});

				groupForm.addField(TITLE, new TextField(TITLE));
				groupForm.getField(TITLE).setRequired(true);
				groupForm.getField(TITLE).setRequiredError("Введите название группы");
				groupForm.getField(TITLE).addValidator(
						new StringLengthValidator("Название группы должно быть не более 255 символов", 1, 255, false));
				groupForm.getField(TITLE).addValidator(new Validator() {

					private static final long serialVersionUID = 1L;

					public void validate(Object value) throws InvalidValueException {
						if (!isValid(value)) {
							throw new InvalidValueException("Название группы не должно состоять из пробелов");
						}
					}

					public boolean isValid(Object value) {
						if (value == null || !(value instanceof String)) {
							return false;
						}
						return (!((String) value).replace(" ", "").isEmpty());
					}
				});
				addComponent(groupForm);

				final Button apply = new Button("Создать", new Button.ClickListener() {

					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						try {
							groupForm.commit();
							String name = groupForm.getField(NAME).getValue().toString();
							String title = groupForm.getField(TITLE).getValue().toString();

							Boolean social = (typeGroup == GroupTab.EMPLOYEE);
							boolean gropIsExist = AdminServiceProvider.get().createGroup(name, title, social);
							if (gropIsExist) {
								tableGroup.addItem(name, title);
								getWindow().showNotification("Группа " + name + " создана");
							} else {
								getWindow().showNotification("Группа " + name + " уже существует",
										Notification.TYPE_WARNING_MESSAGE);
							}
							showButtonCreateGroup();
						} catch (Exception e) {

						}
					}
				});

				final Button cancel = new Button("Отменить", new Button.ClickListener() {

					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						showButtonCreateGroup();
					}
				});

				HorizontalLayout buttons = new HorizontalLayout();
				buttons.setSpacing(true);
				buttons.addComponent(apply);
				buttons.addComponent(cancel);
				groupForm.getFooter().addComponent(buttons);
			}

		});
		addComponent(buttonCreateGroup);
	}
}

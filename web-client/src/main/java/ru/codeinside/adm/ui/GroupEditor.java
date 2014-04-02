/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.vaadin.ui.*;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.Group;
import ru.codeinside.adm.database.Organization;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import ru.codeinside.gses.webui.supervisor.ConfirmWindow;

public class GroupEditor extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private final Group group;
	private String typeGroup;
    private final FilterTable table;

    GroupEditor(final String typeGroup, String nameGroup, FilterTable table) {
        this.table = table;
        group = getGroup(nameGroup);
		this.typeGroup = typeGroup;
		setSpacing(true);
		showEditor(nameGroup);
	}

	private void showEditor(String nameGroup) {
		List<Group> groups = AdminServiceProvider.get().findGroupByName(nameGroup);
		for (final Group g : groups) {
			Label header = new Label("Редактирование группы: " + g.getTitle() + " (" + nameGroup + ")");
			header.setStyleName("h2");
			addComponent(header);
            Button delete = new Button("Удалить", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    String confirmMessage = "Вы уверены, что хотите удалить группу?";
                    Window confirmWindow = new ConfirmWindow(confirmMessage);
                    confirmWindow.addListener(new Listener() {
                        @Override
                        public void componentEvent(Event event) {
                            if(event instanceof ConfirmWindow.ConfirmOkEvent){
                                AdminServiceProvider.get().deleteGroup(g.getId());
                                table.removeItem(g.getName());
                            }
                        }
                    });
                    getWindow().addWindow(confirmWindow);
                }
            });
            addComponent(delete);
		}
		final FilterTable all = new FilterTable();
		all.setCaption("Все доступные " + typeGroup.toLowerCase());
		table(all);
		final FilterTable current = new FilterTable();
		current.setCaption("Выбранные " + typeGroup.toLowerCase());
		table(current);
		addListener(all, current, current);
		addListener(current, all, current);
		addTableContent(all, current);
	}

	private void addTableContent(final FilterTable all, final FilterTable current) {
		if (typeGroup == GroupTab.ORGANIZATION) {
			List<Organization> allOrganizations = AdminServiceProvider.get().findAllOrganizations();
			Set<Organization> groupOrganizations = group.getOrganizations();

			for (Organization org : allOrganizations) {
				all.addItem(new Object[]{org.getName()}, org.getId());

			}
			for (Organization org : groupOrganizations) {
				all.removeItem(org.getId());
				current.addItem(new Object[]{org.getName()}, org.getId());
			}

		} else if (typeGroup == GroupTab.EMPLOYEE) {
			List<Employee> allEmployees = AdminServiceProvider.get().findAllEmployees();
			Set<Employee> groupEmployees = group.getEmployees();

			for (Employee emp : allEmployees) {
				all.addItem(new Object[]{emp.getLogin(), emp.getOrganization().getName()}, emp.getLogin());

			}

			for (Employee emp : groupEmployees) {
				all.removeItem(emp.getLogin());
				current.addItem(new Object[]{emp.getLogin(), emp.getOrganization().getName()}, emp.getLogin());
			}
		}
	}

	private void addListener(final FilterTable one, final FilterTable two, final FilterTable current) {
		one.addListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				Object value = event.getProperty().getValue();
				if (value != null) {
					if (typeGroup == GroupTab.ORGANIZATION) {
						one.removeItem(value);
						Organization org = AdminServiceProvider.get().findOrganizationById((Long) value);
						two.addItem(new Object[]{org.getName()}, org.getId());
					} else if (typeGroup == GroupTab.EMPLOYEE) {
						one.removeItem(value);
						Employee emp = AdminServiceProvider.get().findEmployeeByLogin(value.toString());
						two.addItem(new Object[]{emp.getLogin(), emp.getOrganization().getName()}, emp.getLogin());
					}

					saveChangesInDatabase(current);
				}
			}
		});
	}

	private void saveChangesInDatabase(final FilterTable current) {
		Collection<?> ids = current.getItemIds();
		TreeSet<String> setString = new TreeSet<String>();
		for (Object id : ids) {
			setString.add(id.toString());
		}
		if (typeGroup == GroupTab.ORGANIZATION) {
			AdminServiceProvider.get().setOrganizationInGroup(group, setString);
		} else if (typeGroup == GroupTab.EMPLOYEE) {
			AdminServiceProvider.get().setEmloyeeInGroup(group, setString);
		}
	}

	private void table(FilterTable table) {
		if (typeGroup == GroupTab.ORGANIZATION) {
			table.addContainerProperty("Организация", String.class, null);
		} else if (typeGroup == GroupTab.EMPLOYEE) {
			table.addContainerProperty("Логин", String.class, null);
			table.addContainerProperty("Организация", String.class, null);
		}
		table.setSizeFull();
		table.setHeight("250px");
		table.setSelectable(true);
		table.setMultiSelect(false);
		table.setImmediate(true);
        table.setFilterBarVisible(true);
        table.setFilterDecorator(new FilterDecorator_());
		addComponent(table);
	}

	private Group getGroup(String nameGroup) {
		List<Group> groups = AdminServiceProvider.get().findGroupByName(nameGroup);
		Group group = null;
		for (Group g : groups) {
			group = g;
		}
		return group;
	}

}

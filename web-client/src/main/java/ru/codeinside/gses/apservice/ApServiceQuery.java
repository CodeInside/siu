/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.apservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.codeinside.adm.database.Service;
import ru.codeinside.gses.manager.ManagerService;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.containers.LazyLoadingQuery;
import ru.codeinside.gses.webui.utils.Components;

import com.vaadin.data.Item;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ApServiceQuery implements LazyLoadingQuery {

	private static final long serialVersionUID = 1L;

	final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	String[] sortProps = {};
	boolean[] sortAsc = {};

	private final ApServiceForm procedureForm;
    private LazyLoadingContainer container;

    public ApServiceQuery(ApServiceForm procedureForm) {
		this.procedureForm = procedureForm;
	}

	@Override
	public int size() {
    int apServiceCount;
    if (container != null) {
      apServiceCount = ManagerService.get().getApServiceCount(container.sender);
    } else {
      apServiceCount = ManagerService.get().getApServiceCount(null);
    }
    return apServiceCount;
	}

	@Override
	public List<Item> loadItems(int start, int count) {
      ArrayList<Item> items = new ArrayList<Item>();
      List<Service> apServices;
      if (container != null){
        apServices = ManagerService.get().getApServices(start, count, sortProps, sortAsc, container.sender);
      } else {
        apServices = ManagerService.get().getApServices(start, count, sortProps, sortAsc, null);
      }
      for (Service service : apServices) {
          items.add(createItem(service));
      }
      return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		final Service service = ManagerService.get().getApService(id);
		return createItem(service);
	}

	PropertysetItem createItem(final Service s) {
		ClickListener l = new ClickListener() {

			private static final long serialVersionUID = -1469916073974570914L;

			@Override
			public void buttonClick(ClickEvent event) {
				procedureForm.showForm(s);
			}
		};

		PropertysetItem item = new PropertysetItem();
		item.addItemProperty("id", Components.buttonProperty(s.getId().toString(), l));
		item.addItemProperty("name", Components.stringProperty(s.getName()));
		item.addItemProperty("dateCreated", Components.stringProperty(formatter.format(s.getDateCreated())));

		return item;
	}

	@Override
	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		String[] props = new String[propertyIds.length];
		for (int i = 0; i < propertyIds.length; i++) {
			props[i] = propertyIds[i].toString();
		}
		sortProps = props;
		sortAsc = ascending;
	}

	@Override
	public void setLazyLoadingContainer(LazyLoadingContainer container) {
      this.container = container;
	}

}

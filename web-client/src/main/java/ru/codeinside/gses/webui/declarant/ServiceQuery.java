/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.declarant;

import java.io.Serializable;
import java.util.*;

import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.adm.database.Service;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.webui.Flash;

import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

final public class ServiceQuery implements Query, Serializable {

	private static final long serialVersionUID = 1L;

	final ProcedureType type;
    private boolean showActive;

    private LinkedHashSet<Service> services = new LinkedHashSet<Service>();

	public ServiceQuery(ProcedureType type, boolean  showActive) {
		this.type = type;
        this.showActive = showActive;
    }

	@Override
	public int size() {
        if(showActive){
            return Flash.flash().getDeclarantService().activeServicesCount(type);
        }
		services = new LinkedHashSet<Service>();
        for(Service s : Flash.flash().getDeclarantService().selectActiveServices(type, 0, DeclarantUtils.MAX_COUNT)){
            Set<Procedure> filtered = DeclarantUtils.filtered(Flash.login(), new ArrayList<Procedure>(s.getProcedures()));
            if(!filtered.isEmpty()){
                services.add(s);
            }
        }
        return services.size();
	}

	@Override
	public List<Item> loadItems(final int start, final int count) {
		final List<Item> items = Lists.newArrayListWithExpectedSize(services.size());
        List<Service> serviceList = showActive ? Flash.flash().getDeclarantService().selectActiveServices(type, start, count) : DeclarantUtils.sublist(services, start, count);
        for (Service s : serviceList) {
			final PropertysetItem item = new PropertysetItem();
			item.addItemProperty("id", new ObjectProperty<Long>(s.getId()));
			item.addItemProperty("name", new ObjectProperty<String>(s.getName()));
			items.add(item);
		}
		return items;
	}

	@Override
	public void saveItems(List<Item> addedItems, List<Item> modifiedItems, List<Item> removedItems) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean deleteAllItems() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Item constructItem() {
		throw new UnsupportedOperationException();
	}

}

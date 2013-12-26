/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.declarant;

import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.webui.Flash;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;

import static ru.codeinside.gses.webui.declarant.DeclarantUtils.sublist;

final public class ProcedureQuery implements Query, Serializable {

	private static final long serialVersionUID = 1L;

    private String employee;
    final ProcedureType type;
	final long serviceId;
    private final boolean showActive;
    private LinkedHashSet<Procedure> filtered = new LinkedHashSet<Procedure>();

    public ProcedureQuery(String employee, ProcedureType type, long serviceId, boolean showActive) {
        this.employee = employee;
        this.type = type;
        this.serviceId = serviceId;
        this.showActive = showActive;
    }

    @Override
	public int size() {
        if(showActive){
            return Flash.flash().getDeclarantService().activeProceduresCount(type, serviceId);
        }
        filtered = DeclarantUtils.filtered(employee, Flash.flash().getDeclarantService().selectDeclarantProcedures(type, serviceId, 0, DeclarantUtils.MAX_COUNT));
        return filtered.size();
	}

	@Override
	public List<Item> loadItems(final int start, final int count) {
		final List<Procedure> procs;
        if(showActive){
            procs = Flash.flash().getDeclarantService()
                    .selectActiveProcedures(type, serviceId, start, count);
        } else{
            procs = sublist(filtered, start, count);
        }
        final List<Item> items = Lists.newArrayListWithExpectedSize(procs.size());
		for (Procedure p : procs) {
            final PropertysetItem item = new PropertysetItem();
            item.addItemProperty("id", new ObjectProperty<Long>(Long.valueOf(p.getId())));
            item.addItemProperty("name", new ObjectProperty<String>(p.getName()));
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

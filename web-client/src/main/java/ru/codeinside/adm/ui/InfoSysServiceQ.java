/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import java.io.Serializable;
import java.util.List;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.InfoSystem;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.lazyquerycontainer.QueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.QueryFactory;

import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

public class InfoSysServiceQ extends LazyQueryDefinition {

	private static final long serialVersionUID = 1L;

	public InfoSysServiceQ() {
		super(false, 10);
		addProperty("id", String.class, null, true, true);
		addProperty("infosys", String.class, null, true, false);
		addProperty("address", String.class, null, true, true);
		addProperty("revision", String.class, null, true, true);
		addProperty("sname", String.class, null, true, true);
		addProperty("sversion", String.class, null, true, true);
		addProperty("name", String.class, null, true, true);
		addProperty("available", Boolean.class, null, true, true);
	}

	public Factory getFactory() {
		return new Factory();
	}

	public static class Factory implements QueryFactory, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public void setQueryDefinition(QueryDefinition queryDefinition) {
		}

		@Override
		public Query constructQuery(Object[] sortPropertyIds, boolean[] asc) {
			return new QueryImpl(convertTypes(sortPropertyIds), asc);
		}

		private String[] convertTypes(final Object[] objects) {
			boolean notEmpty = objects != null && objects.length > 0;
			String[] strings = null;
			if (notEmpty) {
				strings = new String[objects.length];
				for (int i = 0; i < objects.length; i++) {
					strings[i] = (String) objects[i];
				}
			}
			return strings;
		}
	}

	public static class QueryImpl implements Query, Serializable {

		private static final long serialVersionUID = 1L;

		final private String[] ids;
		final private boolean[] asc;

		public QueryImpl(String[] ids, boolean[] asc) {
			this.ids = ids;
			this.asc = asc;
		}

		@Override
		public int size() {
			return AdminServiceProvider.get().countInfoSystemServices();
		}

		@Override
		public List<Item> loadItems(final int start, final int count) {
			final List<InfoSystemService> systems = AdminServiceProvider.get().queryInfoSystemServices(ids, asc, start,
					count);
			final List<Item> items = Lists.newArrayListWithExpectedSize(systems.size());
			for (final InfoSystemService s : systems) {
				final PropertysetItem item = new PropertysetItem();
				InfoSystem infoSystem = s.getInfoSystem();
				item.addItemProperty("id", new ObjectProperty<String>(s.getId().toString()));
				item.addItemProperty("infosys", new ObjectProperty<String>(infoSystem == null ? "" : infoSystem.getCode()));
				item.addItemProperty("address", new ObjectProperty<String>(s.getAddress()));
				item.addItemProperty("revision", new ObjectProperty<String>(s.getRevision()));
				item.addItemProperty("sname", new ObjectProperty<String>(s.getSname()));
				item.addItemProperty("sversion", new ObjectProperty<String>(s.getSversion()));
				item.addItemProperty("name", new ObjectProperty<String>(s.getName()));
				item.addItemProperty("available", new ObjectProperty<Boolean>(s.isAvailable()));
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

}
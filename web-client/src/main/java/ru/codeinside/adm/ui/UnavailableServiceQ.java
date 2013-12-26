/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.ServiceUnavailable;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.lazyquerycontainer.QueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.QueryFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

public class UnavailableServiceQ extends LazyQueryDefinition {

	private static final long serialVersionUID = 1L;

	public UnavailableServiceQ() {
		super(false, 10);
		addProperty("infosys", String.class, null, true, false);
		addProperty("address", String.class, null, true, true);
		addProperty("date", String.class, null, true, true);
	}

	public Factory getFactory() {
		return new Factory();
	}

	public static class Factory implements QueryFactory, Serializable {
		private static final long serialVersionUID = 1L;


        private Long id = null;

        public void setInfoSystemId( Long id){
            this.id = id;
        }

		@Override
		public void setQueryDefinition(QueryDefinition queryDefinition) {
		}

		@Override
		public Query constructQuery(Object[] sortPropertyIds, boolean[] asc) {
			return new QueryImpl(id);
		}
	}

	public static class QueryImpl implements Query, Serializable {

		private static final long serialVersionUID = 1L;
        private final Long id;

        final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");

        public QueryImpl(Long id) {
			this.id = id;
		}

		@Override
		public int size() {
            if(id == null){
                return 0;
            }
			return AdminServiceProvider.get().countServiceUnavailableByInfoSystem(id);
		}

		@Override
		public List<Item> loadItems(final int start, final int count) {
            if(id == null){
                return Lists.newArrayList();
            }
			final List<ServiceUnavailable> systems = AdminServiceProvider.get().queryServiceUnavailableByInfoSystem(id, start, count);
			final List<Item> items = Lists.newArrayListWithExpectedSize(systems.size());
			for (final ServiceUnavailable s : systems) {
				final PropertysetItem item = new PropertysetItem();

                item.addItemProperty("infosys", new ObjectProperty<String>(s.getName()));
				item.addItemProperty("address", new ObjectProperty<String>(s.getAddress()));
				item.addItemProperty("date", new ObjectProperty<String>(formatter.format(s.getCreatedDate())));
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
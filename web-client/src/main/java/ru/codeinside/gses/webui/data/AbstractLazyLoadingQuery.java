/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import java.util.List;

import org.activiti.engine.query.Query;

import ru.codeinside.gses.webui.actions.ItemBuilder;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.containers.LazyLoadingQuery;

import com.google.common.collect.Lists;
import com.vaadin.data.Item;

public abstract class AbstractLazyLoadingQuery<T> implements LazyLoadingQuery {
	
	private static final long serialVersionUID = 4269788861279171943L;
	protected String orderBy = "name";
	private boolean asc = true;
	// private LazyLoadingContainer lazyLoadingContainer;
	private final ItemBuilder<T> itemBuilder;

	public AbstractLazyLoadingQuery(ItemBuilder<T> itemBuilder) {
		this.itemBuilder = itemBuilder;
	}

	public void setLazyLoadingContainer(LazyLoadingContainer lazyLoadingContainer) {
		// this.lazyLoadingContainer = lazyLoadingContainer;
	}

	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		for (Object o : propertyIds) {
			orderBy = (String) o;
		}
		for (boolean o : ascending) {
			asc = o;
		}
	}

	List<T> listPage(Query<?, T> query, int start, int count) {
		if (asc) {
			query.asc();
		} else {
			query.desc();
		}
		return query.listPage(start, count);
	}

	public List<Item> loadItems(int start, int count) {
		List<Item> items = Lists.newArrayList();
		for (T domainObject : items(start, count)) {
			items.add(itemBuilder.createItem(domainObject));
		}
		return items;
	}

	public Item loadSingleResult(String id) {
		T domainObject = singleResult(id);
		if (domainObject != null) {
			return itemBuilder.createItem(domainObject);
		}
		return null;
	}

	abstract List<T> items(int start, int count);

	abstract T singleResult(String id);

}
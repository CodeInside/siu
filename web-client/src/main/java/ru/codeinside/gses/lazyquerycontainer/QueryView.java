/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer;

import java.util.List;

import com.vaadin.data.Item;

/**
 * Interface for sorting and browsing data from a business service.
 * @author Tommi S.E. Laukkanen
 */
public interface QueryView {
	/**
	 * Returns the definition of properties provided by this view.
	 * @return the query definition
	 */
	QueryDefinition getQueryDefinition();
    /**
     * @return the maxCacheSize
     */
    int getMaxCacheSize();
    /**
     * @param maxCacheSize the maxCacheSize to set
     */
    void setMaxCacheSize(int maxCacheSize);
	/**
	 * Sorts the items according to the provided sort state.
	 * @param sortPropertyIds Properties participating in the sorting.
	 * @param ascendingStates List of sort order for the properties.
	 */
	void sort(Object[] sortPropertyIds, boolean[] ascendingStates);
	/**
	 * Refreshes data from business service and notifies listeners of changed
	 * item set.
	 */
	void refresh();
	/**
	 * Returns the number of items currently available through the view.
	 * @return Number of items available.
	 */
	int size();
	/**
	 * Gets item at the given index.
	 * @param index The index of the item.
	 * @return The item identified by the index.
	 */
	Item getItem(int index);
	/**
	 * Adds a new item to the end of the query result set.
	 * @return The index of the new item.
	 */
	int addItem();
	/**
	 * Removes item at given index.
	 * @param index Index of the Item to be removed.
	 */
	void removeItem(int index);
	/**
	 * Removes all items.
	 */
	void removeAllItems();
	/**
	 * Check if query view contains modifications.
	 * @return true if query has been modified.
	 */
	boolean isModified();
	/**
	 * Saves changes. Refresh has to be invoked after this method to clear the cache.
	 */
	void commit();
	/**
	 * Cancels changes. Refresh has to be invoked after this method to clear the cache.
	 */
	void discard();
    /**
     * Get list of added buffered items.
     * @return list of added buffered items
     */
    List<Item> getAddedItems();
    /**
     * Get list of modified buffered items.
     * @return list of modified buffered items
     */
    List<Item> getModifiedItems();
    /**
     * Get list of removed buffered items.
     * @return list of removed buffered items
     */
    List<Item> getRemovedItems();
}

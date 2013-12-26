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
 * Interface for loading data in batches and saving modifications.
 * @author Tommi S.E. Laukkanen
 */
public interface Query {
	/**
	 * Gets number of items available through this query.
	 * @return Number of items.
	 */
	int size();
	/**
	 * Load batch of items.
	 * @param startIndex Starting index of the item list.
	 * @param count Count of the items to be retrieved.
	 * @return List of items.
	 */
	List<Item> loadItems(int startIndex, int count);
	/**
	 * Saves the modifications done by container to the query result.
	 * Query will be discarded after changes have been saved
	 * and new query loaded so that changed items are sorted
	 * appropriately.
	 * @param addedItems Items to be inserted.
	 * @param modifiedItems Items to be updated.
	 * @param removedItems Items to be deleted.
	 */
	void saveItems(List<Item> addedItems, List<Item> modifiedItems, List<Item> removedItems);
	/**
	 * Removes all items.
	 * Query will be discarded after delete all items has been called.
	 * @return true if the operation succeeded or false in case of a failure.
	 */
	boolean deleteAllItems();
	/**
	 * Constructs new item to be used when adding items.
	 * @return The new item.
	 */
	Item constructItem();
}

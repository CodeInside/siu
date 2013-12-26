/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.containers;

import java.io.Serializable;
import java.util.List;

import com.vaadin.data.Item;

public interface LazyLoadingQuery extends Serializable {
	int size();

	List<Item> loadItems(int paramInt1, int paramInt2);

	Item loadSingleResult(String paramString);

	void setSorting(Object[] paramArrayOfObject, boolean[] paramArrayOfBoolean);

	void setLazyLoadingContainer(LazyLoadingContainer container);
}
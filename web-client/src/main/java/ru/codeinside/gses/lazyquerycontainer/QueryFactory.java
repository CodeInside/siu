/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer;

/**
 * Interface for constructing queries based on query definition and sort state.
 * @author Tommi S.E. Laukkanen
 */
public interface QueryFactory {
	/**
	 * Gets the definition of properties to be queried.
	 * @param queryDefinition The query definition.
	 */
	void setQueryDefinition(QueryDefinition queryDefinition);
	/**
	 * Constructs a new query according to the given sort state.
	 * @param sortPropertyIds Properties participating in the sorting.
	 * @param sortStates List of sort order for the properties. True corresponds ascending and false descending.
	 * @return A new query constructed according to the given sort state.
	 */
	Query constructQuery(Object[] sortPropertyIds, boolean[] sortStates);
}

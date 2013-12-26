/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer;

import java.io.Serializable;

/**
 * Query factory to be used with EntityQuery.
 * @author Tommi Laukkanen
 */
public final class EntityQueryFactory implements QueryFactory, Serializable {
    /** Java serialization version UID. */
    private static final long serialVersionUID = 1L;
    /** The query definition. */
    private EntityQueryDefinition entityQueryDefinition;

    /**
     * Gets the definition of properties to be queried.
     * @param queryDefinition The query definition.
     */
    @Override
    public void setQueryDefinition(final QueryDefinition queryDefinition) {
        entityQueryDefinition = (EntityQueryDefinition) queryDefinition;
    }

    /**
     * Constructs a new query according to the given sort state.
     * @param sortPropertyIds Properties participating in the sorting.
     * @param sortStates List of sort order for the properties. True corresponds
     *            ascending and false descending.
     * @return A new query constructed according to the given sort state.
     */
    @Override
    public Query constructQuery(final Object[] sortPropertyIds, final boolean[] sortStates) {
        entityQueryDefinition.setSortState(sortPropertyIds, sortStates);
        return new EntityQuery(entityQueryDefinition);
    }

}

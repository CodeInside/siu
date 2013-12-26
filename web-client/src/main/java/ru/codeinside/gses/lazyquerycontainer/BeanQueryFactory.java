/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("rawtypes")
public final class BeanQueryFactory<Q extends AbstractBeanQuery> implements QueryFactory, Serializable {
    /** Java serialization version UID. */
    private static final long serialVersionUID = 1L;
    /** QueryDefinition contains definition of the query properties. */
    private QueryDefinition queryDefinition;
    /** Query configuration contains implementation specific configuration. */
    private Class<Q> queryClass;
    /** The query implementation class. */
    private Map<String, Object> queryConfiguration;

    /**
     * Constructs BeanQuery and sets the user defined parameters.
     * 
     * @param queryClass
     *            The BeanQuery class;
     */
    public BeanQueryFactory(final Class<Q> queryClass) {
        super();
        this.queryClass = queryClass;
    }

    /**
     * Sets the query configuration for the custom query implementation.
     * @param queryConfiguration  The query configuration to be used by the custom query implementation.
     */
    public void setQueryConfiguration(final Map<String, Object> queryConfiguration) {
        this.queryConfiguration = queryConfiguration;
    }

    /**
     * Sets the query definition.
     * @param queryDefinition New query definition to be set.
     */
    public void setQueryDefinition(final QueryDefinition queryDefinition) {
        this.queryDefinition = queryDefinition;
    }

    /**
     * Constructs new query.
     * @param sortPropertyIds The properties participating in sort.
     * @param sortStates  The ascending or descending state of sort properties.
     * @return new instance of Query interface implementation.
     */
    public Query constructQuery(final Object[] sortPropertyIds, final boolean[] sortStates) {
        Q query;

        try {
            query = queryClass.getConstructor(
                    new Class[] { QueryDefinition.class, Map.class, Object[].class, boolean[].class }).newInstance(
                    new Object[] { queryDefinition, queryConfiguration, sortPropertyIds, sortStates });
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating query.", e);
        }

        return query;
    }

}

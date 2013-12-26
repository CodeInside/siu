/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer;

import java.util.Map;

import javax.persistence.EntityManager;

import com.vaadin.data.util.BeanItem;

/**
 * EntityContainer enables using JPA entities with lazy batch loading, filter, sort
 * and buffered writes.
 * @param <T> Entity class.
 * @author Tommi Laukkanen
 */
public final class EntityContainer<T extends Object> extends LazyQueryContainer {
    /** Java serialization version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor which configures query definition for accessing JPA entities.
     * @param entityManager The JPA EntityManager.
     * @param applicationManagedTransactions True if application manages transactions instead of container.
     * @param detachedEntities True if entities are detached from PersistenceContext.
     * @param compositeItems True f items are wrapped to CompositeItems.
     * @param entityClass The entity class.
     * @param batchSize The batch size.
     * @param nativeSortPropertyIds Properties participating in the native sort.
     * @param nativeSortPropertyAscendingStates List of property sort directions for the native sort.
     */
    public EntityContainer(final EntityManager entityManager, final boolean applicationManagedTransactions,
            final boolean detachedEntities, final boolean compositeItems,
            final Class<?> entityClass, final int batchSize,
            final Object[] nativeSortPropertyIds, final boolean[] nativeSortPropertyAscendingStates) {
        super(new EntityQueryDefinition(entityManager, applicationManagedTransactions,
                detachedEntities, compositeItems, 
                entityClass, batchSize, nativeSortPropertyIds, nativeSortPropertyAscendingStates),
                new EntityQueryFactory());
    }
    /**
     * Filters the container content by setting JPQL where criteria.  The entity expression
     * in generated JPQL queries is "e". Where keyword is not to be included. Refresh of container
     * is automatically invoked after this method is called.
     * Example:
     * whereCriteria = "beginDate<=:beginDate";
     * whereParameters.put("e.beginDate", new Date());
     * @param whereCriteria the where criteria to be included in JPA query or null to clear.
     * @param whereParameters the where parameters to set to JPA query or null to clear.
     */
    public void filter(final String whereCriteria, final Map<String, Object> whereParameters) {
        ((EntityQueryDefinition) getQueryView().getQueryDefinition()).setWhereCriteria(whereCriteria, whereParameters);
        refresh();
    }

    /**
     * Adds entity to the container as first item i.e. at index 0.
     * @return the new constructed entity.
     */
    public T addEntity() {
        final Object itemId = addItem();
        return getEntity((Integer) itemId);
    }

    /**
     * Removes given entity at given index and returns it.
     * @param index Index of the entity to be removed.
     * @return The removed entity.
     */
    public T removeEntity(final int index) {
        final T entityToRemove = getEntity(index);
        removeItem(new Integer(index));
        return entityToRemove;
    }
    
    /**
     * Gets entity at given index.
     * @param index The index of the entity.
     * @return the entity.
     */
    @SuppressWarnings("unchecked")
    public T getEntity(final int index) {
        if (getQueryView().getQueryDefinition().isCompositeItems()) {
            final CompositeItem compositeItem = (CompositeItem) getItem(new Integer(index));
            final BeanItem<T> beanItem = (BeanItem<T>) compositeItem.getItem("bean");
            return beanItem.getBean();
        } else { 
            return ((BeanItem<T>) getItem(new Integer(index))).getBean();
        }
    }

}

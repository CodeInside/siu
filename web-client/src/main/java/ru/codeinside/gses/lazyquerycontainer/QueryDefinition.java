/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer;

import java.util.Collection;

/**
 * Interface for defining properties for a query.
 * @author Tommi S.E. Laukkanen
 */
public interface QueryDefinition {
	/**
	 * Lists of the property IDs queried.
	 * @return A list of property IDs queried.
	 */
	Collection<?> getPropertyIds();
	/**
	 * List of the property IDs which can be sorted.
	 * @return A list of the property IDs which can be sorted.
	 */
	Collection<?> getSortablePropertyIds();
	/**
	 * Gets the property value class of the given property.
	 * @param propertyId If of the property of interest.
	 * @return The value class of the given property.
	 */
	Class<?> getPropertyType(Object propertyId);
	/**
	 * Gets the default value of the given property.
	 * @param propertyId If of the property of interest.
	 * @return The default value of the given property.
	 */
	Object getPropertyDefaultValue(Object propertyId);
	/**
	 * Returns true if the given property is read only.
	 * @param propertyId If of the property of interest.
	 * @return True if the given property is read only.
	 */
	boolean isPropertyReadOnly(Object propertyId);
	/**
	 * Returns true if the given property is sortable.
	 * @param propertyId If of the property of interest.
	 * @return True if the given property is sortable.
	 */
	boolean isPropertySortable(Object propertyId);
	/**
	 * Adds a new property to the definition.
	 * @param propertyId Id of the property.
	 * @param type Value class of the property.
	 * @param defaultValue Default value of the property.
	 * @param readOnly Read only state of the property.
	 * @param sortable Sortable state of the property.
	 */
	void addProperty(Object propertyId, Class<?> type, Object defaultValue, boolean readOnly, boolean sortable);
	/**
	 * Removes the given property from the definition.
	 * @param propertyId If of the property to be removed.
	 */
	void removeProperty(Object propertyId);	
	/**
	 * Gets the batch size.
	 * @return the batch size
	 */
	int getBatchSize();
    /**
     * Sets the query batch size.
     * After this method has been called the Query has to be discarded immediately.
     * @param batchSize the batchSize to set
     */
    void setBatchSize(final int batchSize);
    /**
     * True if query wraps items to CompositeItems.
     * @return the compositeItems
     */
    boolean isCompositeItems();
    /**
     * Sets whether query wraps items to CompositeItems.
     * After this method has been called the Query has to be discarded immediately.
     * @param compositeItems the compositeItems to set
     */
    void setCompositeItems(final boolean compositeItems);
}

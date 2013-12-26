/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package com.jensjansson.pagedtable;

import java.util.Collection;
import java.util.Collections;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

public class PagedTableContainer implements Container, Container.Indexed,
        Container.Sortable {
    private static final long serialVersionUID = -2134233618583099046L;

    private final Container.Indexed container;
    private int pageLength = 25;
    private int startIndex = 0;

    public PagedTableContainer(Container.Indexed container) {
        this.container = container;
    }

    public Container.Indexed getContainer() {
        return container;
    }

    public int getPageLength() {
        return pageLength;
    }

    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    /*
     * Overridden methods from the real container from here forward
     */

    @Override
    public int size() {
        int rowsLeft = container.size() - startIndex;
        if (rowsLeft > pageLength) {
            return pageLength;
        } else {
            return rowsLeft;
        }
    }

    public int getRealSize() {
        return container.size();
    }

    public Object getIdByIndex(int index) {
        return container.getIdByIndex(index + startIndex);
    }

    /*
     * Delegate methods to real container from here on
     */

    @Override
    public Item getItem(Object itemId) {
        return container.getItem(itemId);
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return container.getContainerPropertyIds();
    }

    @Override
    public Collection<?> getItemIds() {
        return container.getItemIds();
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        return container.getContainerProperty(itemId, propertyId);
    }

    @Override
    public Class<?> getType(Object propertyId) {
        return container.getType(propertyId);
    }

    @Override
    public boolean containsId(Object itemId) {
        return container.containsId(itemId);
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        return container.addItem(itemId);
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        return container.addItem();
    }

    @Override
    public boolean removeItem(Object itemId)
            throws UnsupportedOperationException {
        return container.removeItem(itemId);
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type,
            Object defaultValue) throws UnsupportedOperationException {
        return container.addContainerProperty(propertyId, type, defaultValue);
    }

    @Override
    public boolean removeContainerProperty(Object propertyId)
            throws UnsupportedOperationException {
        return container.removeContainerProperty(propertyId);
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        return container.removeAllItems();
    }

    public Object nextItemId(Object itemId) {
        return container.nextItemId(itemId);
    }

    public Object prevItemId(Object itemId) {
        return container.prevItemId(itemId);
    }

    public Object firstItemId() {
        return container.firstItemId();
    }

    public Object lastItemId() {
        return container.lastItemId();
    }

    public boolean isFirstId(Object itemId) {
        return container.isFirstId(itemId);
    }

    public boolean isLastId(Object itemId) {
        return container.isLastId(itemId);
    }

    public Object addItemAfter(Object previousItemId)
            throws UnsupportedOperationException {
        return container.addItemAfter(previousItemId);
    }

    public Item addItemAfter(Object previousItemId, Object newItemId)
            throws UnsupportedOperationException {
        return container.addItemAfter(previousItemId, newItemId);
    }

    public int indexOfId(Object itemId) {
        return container.indexOfId(itemId);
    }

    public Object addItemAt(int index) throws UnsupportedOperationException {
        return container.addItemAt(index);
    }

    public Item addItemAt(int index, Object newItemId)
            throws UnsupportedOperationException {
        return container.addItemAt(index, newItemId);
    }

    /*
     * Sorting interface from here on
     */

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        if (container instanceof Container.Sortable) {
            ((Container.Sortable) container).sort(propertyId, ascending);
        }
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        if (container instanceof Container.Sortable) {
            return ((Container.Sortable) container)
                    .getSortableContainerPropertyIds();
        }
        return Collections.EMPTY_LIST;
    }

}
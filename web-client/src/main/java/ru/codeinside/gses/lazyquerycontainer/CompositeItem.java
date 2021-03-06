/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.PropertysetItem;

/**
 * CompositeItem enables joining multiple items as single item. CompositeItem
 * contains PropertysetItem as default item to support adding and removing of
 * properties.
 * 
 * @author Tommi Laukkanen
 */
public final class CompositeItem implements Item {
    /** Serial version UID for this class. */
    private static final long serialVersionUID = 1L;
    /** Key for default item. */
    public static final String DEFAULT_ITEM_KEY = "default-item";
    /** List of item keys. */
    private List<String> itemKeys = new ArrayList<String>();
    /** Map of items. */
    private Map<String, Item> items = new HashMap<String, Item>();
    /** The default item. */
    private Item defaultItem = new PropertysetItem();

    /**
     * Default constructor initializes default Item.
     */
    public CompositeItem() {
        addItem(DEFAULT_ITEM_KEY, defaultItem);
    }

    /**
     * Adds new Item.
     * @param key Key of new Item.
     * @param item Item to be added.
     */
    public void addItem(final String key, final Item item) {
        itemKeys.add(key);
        items.put(key, item);
    }

    /**
     * Removes item.
     * @param key Key of the item to be removed.
     */
    public void removeItem(final String key) {
        itemKeys.remove(key);
        items.remove(key);
    }

    /**
     * Gets keys of Items.
     * @return List of keys.
     */
    public List<String> getItemKeys() {
        return Collections.unmodifiableList(itemKeys);
    }

    /**
     * Gets Item identified by Key.
     * @param key Key of the item to be retrieved.
     * @return Item corresponding to the given key.
     */
    public Item getItem(final String key) {
        return items.get(key);
    }

    /**
     * Lists IDs of the properties in the item.
     * @return Collection of property IDs.
     */
    public Collection<?> getItemPropertyIds() {
        List<Object> itemPropertyIds = new ArrayList<Object>();
        for (String itemKey : itemKeys) {
            Item item = items.get(itemKey);
            for (Object propertyId : item.getItemPropertyIds()) {
                itemPropertyIds.add(propertyId);
            }
        }
        return itemPropertyIds;
    }

    /**
     * Gets Item property by Item id.
     * @param id ID of the property to be retrieved.
     * @return property corresponding to the given ID or null if no matching property is found.
     */
    public Property getItemProperty(final Object id) {
        for (String itemKey : itemKeys) {
            Item item = items.get(itemKey);
            Property property = item.getItemProperty(id);
            if (property != null) {
                return property;
            }
        }
        return null;
    }

    /**
     * Adds property to default Item.
     * @param id ID of the property to be added.
     * @param property Property to be added.
     * @return true if Property was added successfully.
     */
    public boolean addItemProperty(final Object id, final Property property) {
        return defaultItem.addItemProperty(id, property);
    }

    /**
     * Removes item from default Item.
     * @param id ID of the property to be removed.
     * @return true if Property was removed successfully.
     */
    public boolean removeItemProperty(final Object id) {
        return defaultItem.removeItemProperty(id);
    }

}

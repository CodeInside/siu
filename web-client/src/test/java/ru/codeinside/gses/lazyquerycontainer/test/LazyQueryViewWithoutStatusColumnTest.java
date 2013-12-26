/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer.test;

import java.util.Collection;

import junit.framework.Assert;
import junit.framework.TestCase;

import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryView;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

public class LazyQueryViewWithoutStatusColumnTest extends TestCase {

    private final int viewSize = 100;
    private LazyQueryView view;
    private LazyQueryDefinition definition;

    protected void setUp() throws Exception {
        super.setUp();

        definition = new LazyQueryDefinition(true, this.viewSize);
        definition.addProperty("Index", Integer.class, 0, true, true);
        definition.addProperty("Reverse Index", Integer.class, 0, true, false);
        definition.addProperty("Editable", String.class, "", false, false);

        MockQueryFactory factory = new MockQueryFactory(viewSize, 0, 0);
        factory.setQueryDefinition(definition);
        view = new LazyQueryView(definition, factory);

    }

    protected void tearDown() throws Exception {
        super.tearDown();
        definition.removeProperty("Index");
        definition.removeProperty("Reverse Index");
        definition.removeProperty("Editable");
        Assert.assertEquals(0, definition.getPropertyIds().size());
    }

    public void testSize() {
        assertEquals(viewSize, view.size());
    }

    public void testGetItem() {
        for (int i = 0; i < viewSize; i++) {
            Item item = view.getItem(i);
            Property indexProperty = item.getItemProperty("Index");
            assertEquals(i, indexProperty.getValue());
            assertTrue(indexProperty.isReadOnly());
        }
    }

    public void testAscendingSort() {
        view.sort(new Object[] { "Index" }, new boolean[] { true });

        for (int i = 0; i < viewSize; i++) {
            Item item = view.getItem(i);
            Property indexProperty = item.getItemProperty("Index");
            assertEquals(i, indexProperty.getValue());
            assertTrue(indexProperty.isReadOnly());
        }
    }

    public void testDescendingSort() {
        view.sort(new Object[] { "Index" }, new boolean[] { false });

        for (int i = 0; i < viewSize; i++) {
            Item item = view.getItem(i);
            Property indexProperty = item.getItemProperty("Index");
            assertEquals(viewSize - i - 1, indexProperty.getValue());
            assertTrue(indexProperty.isReadOnly());
        }
    }

    public void testGetSortablePropertyIds() {
        Collection<?> sortablePropertyIds = view.getQueryDefinition().getSortablePropertyIds();
        assertEquals(1, sortablePropertyIds.size());
        assertEquals("Index", sortablePropertyIds.iterator().next());
    }

    public void testAddCommitItem() {
        int originalViewSize = view.size();
        assertFalse(view.isModified());
        int addIndex = view.addItem();
        assertEquals("Item must be added at the beginning", addIndex, 0);
        assertEquals(originalViewSize + 1, view.size());
        assertTrue(view.isModified());
        view.commit();
        view.refresh();
        assertFalse(view.isModified());
    }

    public void testAddTwiceCommitItem() {
        int originalViewSize = view.size();
        assertFalse(view.isModified());
        // Add the first Item
        int addIndex = (Integer) view.addItem();
        assertEquals("Item must be added at the beginning.", addIndex, 0);
        assertEquals(originalViewSize + 1, view.size());
        assertTrue(view.isModified());
        // Add a second Item
        addIndex = (Integer) view.addItem();
        assertEquals("Second item must be added at the beginning as well.", addIndex, 0);
        assertEquals(originalViewSize + 2, view.size());
        assertTrue(view.isModified());
        view.commit();
        view.refresh();
        assertFalse(view.isModified());
    }

    public void testAddDiscardItem() {
        int originalViewSize = view.size();
        assertFalse(view.isModified());
        int addIndex = view.addItem();
        assertEquals("Item must be added at the beginning", addIndex, 0);
        assertEquals(originalViewSize + 1, view.size());
        assertTrue(view.isModified());
        view.discard();
        view.refresh();
        assertFalse(view.isModified());
        assertEquals(originalViewSize, view.size());
    }

    public void testModifyCommitItem() {
        int modifyIndex = 0;
        assertFalse(view.isModified());
        view.getItem(modifyIndex).getItemProperty("Editable").setValue("test");
        assertTrue(view.isModified());
        view.commit();
        view.refresh();
        assertFalse(view.isModified());
        assertEquals("test", view.getItem(modifyIndex).getItemProperty("Editable").getValue());
    }

    public void testModifyDiscardItem() {
        int modifyIndex = 0;
        assertFalse(view.isModified());
        view.getItem(modifyIndex).getItemProperty("Editable").setValue("test");
        assertTrue(view.isModified());
        view.discard();
        view.refresh();
        assertFalse(view.isModified());
        assertEquals("", view.getItem(modifyIndex).getItemProperty("Editable").getValue());
    }

    public void testRemoveCommitItem() {
        int removeIndex = 0;
        int originalViewSize = view.size();
        assertFalse(view.isModified());
        assertFalse(view.getItem(removeIndex).getItemProperty("Editable").isReadOnly());
        view.removeItem(removeIndex);
        assertEquals(originalViewSize, view.size());
        assertTrue(view.getItem(removeIndex).getItemProperty("Editable").isReadOnly());
        assertTrue(view.isModified());
        view.commit();
        view.refresh();
        assertFalse(view.isModified());
        assertEquals(originalViewSize - 1, view.size());
        assertEquals(removeIndex + 1, view.getItem(removeIndex).getItemProperty("Index").getValue());
    }

    public void testRemoveDiscardItem() {
        int removeIndex = 0;
        int originalViewSize = view.size();
        assertFalse(view.isModified());
        assertFalse(view.getItem(removeIndex).getItemProperty("Editable").isReadOnly());
        view.removeItem(removeIndex);
        assertEquals(originalViewSize, view.size());
        assertTrue(view.getItem(removeIndex).getItemProperty("Editable").isReadOnly());
        assertTrue(view.isModified());
        view.discard();
        view.refresh();
        assertFalse(view.isModified());
        assertEquals(originalViewSize, view.size());
        assertEquals(removeIndex, view.getItem(removeIndex).getItemProperty("Index").getValue());
        assertFalse(view.getItem(removeIndex).getItemProperty("Editable").isReadOnly());
    }

}

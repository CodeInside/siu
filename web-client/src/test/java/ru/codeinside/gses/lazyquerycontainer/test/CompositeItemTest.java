/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.codeinside.gses.lazyquerycontainer.CompositeItem;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

/**
 * Unit tests for CompositeItem.
 * @author Tommi Laukkanen
 */
public class CompositeItemTest {

	private CompositeItem testItem;
	private static final String TEST_PROPERTY_ID="test-property-id";
	private static final Property testProperty=new ObjectProperty(new Object());
	private static final String TEST_PROPERTY_ID_2="test-property-id-2";
	private static final Property testProperty2=new ObjectProperty(new Object());
	private static final String TEST_ITEM_KEY="test-item-key";
	private static final PropertysetItem testCompositeItem=new PropertysetItem();
	
	@Before
	public void setUp() throws Exception {
		testItem=new CompositeItem();
		testItem.addItemProperty(TEST_PROPERTY_ID, testProperty);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link ru.codeinside.gses.lazyquerycontainer.CompositeItem#addItem(java.lang.String, com.vaadin.data.Item)}.
	 */
	@Test
	public void testAddItem() {
		testItem.addItem(TEST_ITEM_KEY, testCompositeItem);
		Assert.assertEquals("Does added test item key exists", 2,testItem.getItemKeys().size());
		Assert.assertEquals("Is added test item key correct?", TEST_ITEM_KEY,testItem.getItemKeys().get(1));
		testCompositeItem.addItemProperty(TEST_PROPERTY_ID_2, testProperty2);
		Assert.assertEquals("Does property id list contain the test property and new property", 2,testItem.getItemPropertyIds().size());
		Assert.assertEquals("Is the new property returned correctly?", testProperty2,testItem.getItemProperty(TEST_PROPERTY_ID_2));
	}

	/**
	 * Test method for {@link ru.codeinside.gses.lazyquerycontainer.CompositeItem#removeItem(java.lang.String, com.vaadin.data.Item)}.
	 */
	@Test
	public void testRemoveItem() {
		testItem.removeItem(CompositeItem.DEFAULT_ITEM_KEY);
		Assert.assertEquals("Is item key size 0 after removal of default item.?", 0,testItem.getItemKeys().size());
		Assert.assertNull("Does default item key return null after removal of default item?", testItem.getItem(CompositeItem.DEFAULT_ITEM_KEY));
	}

	/**
	 * Test method for {@link ru.codeinside.gses.lazyquerycontainer.CompositeItem#getItemKeys()}.
	 */
	@Test
	public void testGetItemKeys() {
		Assert.assertEquals("Does default item key exists?", 1,testItem.getItemKeys().size());
		Assert.assertEquals("Is default item key correct?", CompositeItem.DEFAULT_ITEM_KEY,testItem.getItemKeys().get(0));
	}
	
	/**
	 * Test method for {@link ru.codeinside.gses.lazyquerycontainer.CompositeItem#getItem(java.lang.String)}.
	 */
	@Test
	public void testGetItem() {
		Assert.assertNotNull("Is default item returned correctly?", testItem.getItem(CompositeItem.DEFAULT_ITEM_KEY));
	}

	/**
	 * Test method for {@link ru.codeinside.gses.lazyquerycontainer.CompositeItem#getItemPropertyIds()}.
	 */
	@Test
	public void testGetItemPropertyIds() {
		Assert.assertEquals("Does property id list contain the test property", 1,testItem.getItemPropertyIds().size());
		Assert.assertEquals("Does test property exists?", testProperty,testItem.getItemProperty(TEST_PROPERTY_ID));
	}

	/**
	 * Test method for {@link ru.codeinside.gses.lazyquerycontainer.CompositeItem#getItemProperty(java.lang.Object)}.
	 */
	@Test
	public void testGetItemProperty() {
		Assert.assertEquals("Does test property exists?", testProperty,testItem.getItemProperty(TEST_PROPERTY_ID));
	}

	/**
	 * Test method for {@link ru.codeinside.gses.lazyquerycontainer.CompositeItem#addItemProperty(java.lang.Object, com.vaadin.data.Property)}.
	 */
	@Test
	public void testAddItemProperty() {
		testItem.addItemProperty(TEST_PROPERTY_ID_2, testProperty2);
		Assert.assertEquals("Does property id list contain the test property and new property", 2,testItem.getItemPropertyIds().size());
		Assert.assertEquals("Is the new property returned correctly?", testProperty2,testItem.getItemProperty(TEST_PROPERTY_ID_2));
	}

	/**
	 * Test method for {@link ru.codeinside.gses.lazyquerycontainer.CompositeItem#removeItemProperty(java.lang.Object)}.
	 */
	@Test
	public void testRemoveItemProperty() {
		testItem.removeItemProperty(TEST_PROPERTY_ID);
		Assert.assertEquals("Is property ids length 0 after remove of the test property?", 0,testItem.getItemPropertyIds().size());
		Assert.assertNull("Does asking the property return null after remove", testItem.getItemProperty(TEST_PROPERTY_ID));
	}

}

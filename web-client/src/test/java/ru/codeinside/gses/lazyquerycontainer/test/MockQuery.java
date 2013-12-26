/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.codeinside.gses.lazyquerycontainer.Query;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

public class MockQuery implements Query {
	
	private MockQueryFactory queryFactory;
	private List<Item> items;
	private int batchQueryMinTime;
	private int batchQueryMaxTime;
	private Map<Item,Item> cloneMap=new HashMap<Item,Item>();
	
	public MockQuery(MockQueryFactory queryFactory,List<Item> items,int batchQueryMinTime, int batchQueryMaxTime) {
		this.queryFactory=queryFactory;
		this.items=items;
		this.batchQueryMinTime=batchQueryMinTime;
		this.batchQueryMaxTime=batchQueryMaxTime;
	}
	

	public List<Item> loadItems(int startIndex, int count) {
		List<Item> resultItems=new ArrayList<Item>();
		for(int i=0;i<count;i++) {
			// Returning clones to be able to control commit/discard of modifications.
			Item original=items.get(startIndex+i);
			Item clone=cloneItem(original);
			resultItems.add(clone);
			cloneMap.put(clone, original);
		}
		
		try {
			Thread.sleep(batchQueryMinTime+(int)(Math.random()*batchQueryMaxTime));
		} catch (InterruptedException e) {
		}
		
		return resultItems;
	}
	


	public int size() {
		return items.size();
	}


	public Item constructItem() {
		return queryFactory.constructItem(-1,-1);
	}


	public boolean deleteAllItems() {
		items.clear();
		return true;
	}

	public void saveItems(List<Item> addedItems, List<Item> modifiedItems,
			List<Item> removedItems) {
		items.addAll(0,addedItems);
		for(Item clone : removedItems) {
			Item original=cloneMap.get(clone);
			if(addedItems.contains(clone)) {
				// If item is new then it is not mapped through clone map.
				items.remove(clone);								
			}
			else {
				items.remove(original);				
			}
		}
		for(Item clone : modifiedItems) {
			Item original=cloneMap.get(clone);
			copyItemValues(original, clone);
		}
	}
	
	private Item cloneItem(Item originalItem) {
		PropertysetItem newItem=new PropertysetItem();
		for(Object propertyId : originalItem.getItemPropertyIds()) {
			Property originalProperty=originalItem.getItemProperty(propertyId);
			newItem.addItemProperty(propertyId, 
					new ObjectProperty(
					originalProperty.getValue(),
					originalProperty.getType(),
					originalProperty.isReadOnly()
					));
		}
		return newItem;
	}
	
	private void copyItemValues(Item target, Item source) {
		for(Object propertyId : source.getItemPropertyIds()) {
			Property sourceProperty=source.getItemProperty(propertyId);
			Property targetProperty=target.getItemProperty(propertyId);
			boolean readonlyState=targetProperty.isReadOnly();
			targetProperty.setReadOnly(false);
			target.getItemProperty(propertyId).setValue(sourceProperty.getValue());			
			targetProperty.setReadOnly(readonlyState);
		}
	}
	
}

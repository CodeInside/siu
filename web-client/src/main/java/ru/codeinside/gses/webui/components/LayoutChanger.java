/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import ru.codeinside.gses.webui.components.api.Changer;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class LayoutChanger extends VerticalLayout implements Changer {

	private HashMap<String, Component> components;
	private static final long serialVersionUID = 1L;
	private VerticalLayout layout;
	private Component current;
	private Component previous;
	
	public Component getCurrent(){
		return current;
	}
	
	public Component getPrevious(){
		return previous;
	}
	
	public LayoutChanger(VerticalLayout layout){
		this.layout = layout;
		components = new HashMap<String, Component>();
	}
	
	@Override
	public void set(Component newComponent, String name) {
		components.put(name, newComponent);
		layout.addComponent(newComponent);
		newComponent.setVisible(false);
	}

	@Override
	public void change(Component newComponent) {
		if(current == null){
			Iterator<Entry<String, Component>> i = components.entrySet().iterator();
			while(i.hasNext()){
				if(i.next().getValue().equals(newComponent)){
					current = newComponent;
					current.setVisible(true);
					return;
				}
			}
			throw new IllegalArgumentException("Set component to changer before change to it");
		}else{
			Iterator<Entry<String, Component>> i = components.entrySet().iterator();
			while(i.hasNext()){
				if(i.next().getValue().equals(newComponent)){
					previous = current;
					previous.setVisible(false);
					current = newComponent;
					current.setVisible(true);
					return;
				}
			}
			throw new IllegalArgumentException("Set component to changer before change to it");
		}
	}

	@Override
	public void back() {
		if(previous != null){
			previous.setVisible(true);
		}
		current.setVisible(false);
		Component tmp = current;
		current = previous;
		previous = tmp;
	}

	@Override
	public void clear() {
		layout.removeAllComponents();
	}
}

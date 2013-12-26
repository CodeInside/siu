/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import ru.codeinside.gses.webui.components.api.Changer;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Window;

public class ContentWindowChanger implements Changer {

	private static final long serialVersionUID = 3140438519838984935L;
	private Component current;
	private Component previos;
	private Window window;

	public ContentWindowChanger(Window window) {
		this.window = window;
		current = window.getContent();
	}

	@Override
	public void set(Component newComponent, String name) {
		window.setCaption(name);
		window.setContent((ComponentContainer)newComponent);
		current = newComponent;
	}

	@Override
	public void change(Component newComponent) {
		previos = current;
		current = newComponent;
		window.setContent((ComponentContainer) current);		
	}

	@Override
	public void back() {
		final Component newComponent = previos;
		previos = null;
		current = newComponent;
		window.setContent((ComponentContainer) current);
	}

	@Override
	public void clear() {
		current = null;
		previos = null;
		window.setContent((ComponentContainer) current);
	}
}

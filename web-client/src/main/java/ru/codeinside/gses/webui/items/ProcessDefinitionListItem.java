/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.items;

import static ru.codeinside.gses.webui.utils.Components.buttonProperty;
import static ru.codeinside.gses.webui.utils.Components.stringProperty;

import org.activiti.engine.repository.ProcessDefinition;

import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Button.ClickListener;

public class ProcessDefinitionListItem extends PropertysetItem implements Comparable<ProcessDefinitionListItem> {

	private static final long serialVersionUID = -6349169116789571646L;

	public ProcessDefinitionListItem(final ProcessDefinition processDefinition,//
	        ClickListener showListener) {
		addItemProperty("id", buttonProperty(processDefinition.getId(), showListener));
		addItemProperty("name", stringProperty(processDefinition.getName(), processDefinition.getKey()));
		addItemProperty("key", stringProperty(processDefinition.getKey(), ""));
	}

	public int compareTo(ProcessDefinitionListItem other) {
		String name = (String) getItemProperty("name").getValue();
		String otherName = (String) other.getItemProperty("name").getValue();
		int comparison = name.compareTo(otherName);

		if (comparison != 0) {
			return comparison;
		}
		String key = (String) getItemProperty("key").getValue();
		String otherKey = (String) other.getItemProperty("key").getValue();
		return key.compareTo(otherKey);
	}
}

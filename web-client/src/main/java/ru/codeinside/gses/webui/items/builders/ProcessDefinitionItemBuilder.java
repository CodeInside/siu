/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.items.builders;

import org.activiti.engine.repository.ProcessDefinition;

import ru.codeinside.gses.webui.data.ItemBuilder;
import ru.codeinside.gses.webui.actions.ShowProcessDefListener;
import ru.codeinside.gses.webui.items.ProcessDefinitionListItem;

import com.vaadin.Application;
import com.vaadin.data.Item;

public class ProcessDefinitionItemBuilder implements ItemBuilder<ProcessDefinition> {

	private static final long serialVersionUID = 5525566203220278809L;
	private final Application application;

	public ProcessDefinitionItemBuilder(Application application) {
		this.application = application;
	}

	@Override
	public Item createItem(ProcessDefinition processDef) {
		ShowProcessDefListener showListener = new ShowProcessDefListener(processDef, application);
		return new ProcessDefinitionListItem(processDef, showListener);
	}

}

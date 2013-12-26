/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.items.builders;

import org.activiti.engine.repository.Deployment;

import ru.codeinside.gses.webui.actions.ItemBuilder;
import ru.codeinside.gses.webui.items.DeploymentListitem;

import com.vaadin.data.Item;

public class DeploymentItemBuilder implements ItemBuilder<Deployment> {

	private static final long serialVersionUID = 33633280913068687L;

	@Override
	public Item createItem(Deployment deployment) {
		return new DeploymentListitem(deployment);
	}
}
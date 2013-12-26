/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.items;

import static ru.codeinside.gses.webui.utils.Components.stringProperty;

import org.activiti.engine.repository.Deployment;

import com.vaadin.data.util.PropertysetItem;

public class DeploymentListitem extends PropertysetItem implements Comparable<DeploymentListitem> {

	private static final long serialVersionUID = 5493926970174759411L;

	public DeploymentListitem(final Deployment deployment) {
		addItemProperty("id", stringProperty(deployment.getId()));
		addItemProperty("name", stringProperty(deployment.getName(), "Без имени"));
		addItemProperty("time", stringProperty(deployment.getDeploymentTime().toString()));
		
	}

	public int compareTo(DeploymentListitem other) {
		String name = (String) getItemProperty("name").getValue();
		String otherName = (String) other.getItemProperty("name").getValue();

		int comparison = name.compareTo(otherName);
		if (comparison != 0) {
			return comparison;
		}
		String id = (String) getItemProperty("id").getValue();
		String otherId = (String) other.getItemProperty("id").getValue();
		return id.compareTo(otherId);
	}
}
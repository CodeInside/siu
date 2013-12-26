/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;

import ru.codeinside.gses.webui.items.builders.DeploymentItemBuilder;

public class DeploymentListQuery extends AbstractLazyLoadingQuery<Deployment> {

	private static final long serialVersionUID = -1940368733305081634L;
	public static final Object[] NATURAL_COL_ORDER = new Object[] { "id", "name", "time" };
	public static final String[] COL_HEADERS_ENGLISH = new String[] { "Номер", "Название загруженного файла", "Время создания" };

	protected RepositoryService repositoryService;

	public DeploymentListQuery(ProcessEngine engine) {
		super(new DeploymentItemBuilder());
		this.repositoryService = engine.getRepositoryService();
	}

	public int size() {
		return (int) createQuery().count();
	}

	List<Deployment> items(int start, int count) {
		DeploymentQuery query = createQuery();

		if ("name".equals(orderBy)) {
			query.orderByDeploymentName();
		} else if ("id".equals(orderBy)) {
			query.orderByDeploymentId();
		} else if ("time".equals(orderBy)) {
			query.orderByDeploymenTime();
		}		
		return listPage(query, start, count);
	}

	Deployment singleResult(String id) {
		return createQuery().deploymentId(id).singleResult();
	}
	
	private DeploymentQuery createQuery() {
		return this.repositoryService.createDeploymentQuery();
	}

}
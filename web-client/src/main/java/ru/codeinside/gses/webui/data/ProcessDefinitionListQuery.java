/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import static ru.codeinside.gses.service.Functions.withRepository;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;

import ru.codeinside.gses.webui.actions.ItemBuilder;

import com.google.common.base.Function;

public class ProcessDefinitionListQuery extends AbstractLazyLoadingQuery<ProcessDefinition> {

	private static final long serialVersionUID = -4889120681002102040L;
	public static final Object[] NATURAL_COL_ORDER = new Object[] { "id", "name", "key", "run" };
	public static final String[] COL_HEADERS_RUS = new String[] { "Номер", "Название Процедуры", "Идентификатор", " " };

	public ProcessDefinitionListQuery(ItemBuilder<ProcessDefinition> itemBuilder) {
		super(itemBuilder);
	}

	List<ProcessDefinition> items(final int start, final int count) {
		return withRepository(new Function<RepositoryService, List<ProcessDefinition>>() {
			public List<ProcessDefinition> apply(RepositoryService srv) {
				ProcessDefinitionQuery query = srv.createProcessDefinitionQuery().latestVersion();
				if ("name".equals(orderBy)) {
					query.orderByProcessDefinitionName();
				} else if ("id".equals(orderBy)) {
					query.orderByProcessDefinitionId();
				} else if ("key".equals(orderBy)) {
					query.orderByProcessDefinitionKey();
				} else {
					query.orderByProcessDefinitionName();
				}
				return listPage(query, start, count);
			};
		});

	}

	public ProcessDefinition singleResult(final String id) {
		return withRepository(new Function<RepositoryService, ProcessDefinition>() {
			public ProcessDefinition apply(RepositoryService srv) {
				return srv.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
			};
		});
	}

	public int size() {
		return withRepository(new Function<RepositoryService, Integer>() {
			public Integer apply(RepositoryService srv) {
				return (int) srv.createProcessDefinitionQuery().latestVersion().count();
			};
		});
	}

}
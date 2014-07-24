/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import java.util.List;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;

import ru.codeinside.gses.webui.Flash;

public class ProcessListQuery extends AbstractLazyLoadingQuery<ProcessInstance> {

	private static final long serialVersionUID = -6592349959326582872L;

	public ProcessListQuery(ItemBuilder<ProcessInstance> itemBuilder) {
		super(itemBuilder);
	}

	List<ProcessInstance> items(int start, int count) {
		ProcessInstanceQuery query = createQuery();
		if ("name".equals(orderBy)) {
			query.orderByProcessDefinitionId();
		} else if ("id".equals(orderBy)) {
			query.orderByProcessDefinitionKey();
		} else {
			query.orderByProcessInstanceId();
		}
		return listPage(query, start, count);
	}

	public ProcessInstance singleResult(String id) {
		return createQuery().processInstanceId(id).singleResult();
	}

	public int size() {
		return (int) createQuery().count();
	}

	private ProcessInstanceQuery createQuery() {
		return Flash.flash().getProcessEngine().getRuntimeService().createProcessInstanceQuery();
	}
}
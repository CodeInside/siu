/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;

import ru.codeinside.gses.webui.Flash;

public class HistoryProcessListQuery extends AbstractLazyLoadingQuery<HistoricProcessInstance> {

	private static final long serialVersionUID = -6550330142184130254L;

	public HistoryProcessListQuery(ItemBuilder<HistoricProcessInstance> itemBuilder) {
		super(itemBuilder);
	}

	List<HistoricProcessInstance> items(int start, int count) {
		HistoricProcessInstanceQuery query = createQuery().finished();
		if ("name".equals(orderBy)) {
			query.orderByProcessInstanceStartTime();
		} else if ("id".equals(orderBy)) {
			query.orderByProcessInstanceEndTime();
		} else {
			query.orderByProcessInstanceId();
		}
		return listPage(query, start, count);
	}

	public HistoricProcessInstance singleResult(String id) {
		return createQuery().processInstanceId(id).singleResult();
	}

	public int size() {
		return (int) createQuery().finished().count();
	}

	private HistoricProcessInstanceQuery createQuery() {
		return Flash.flash().getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery();
	}
}
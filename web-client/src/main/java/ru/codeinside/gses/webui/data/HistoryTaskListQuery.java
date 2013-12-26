/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import java.util.List;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;

import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.actions.ItemBuilder;

public class HistoryTaskListQuery extends AbstractLazyLoadingQuery<HistoricTaskInstance> {

	private static final long serialVersionUID = 4802943613553785799L;
	public static final Object[] NATURAL_COL_ORDER = new Object[] { "id", "name", "exe" };
	public static final String[] COL_HEADERS_ENGLISH = new String[] { "Номер", "Название этапа", "Действие" };

	public HistoryTaskListQuery(ItemBuilder<HistoricTaskInstance> itemBuilder) {
		super(itemBuilder);
	}

	List<HistoricTaskInstance> items(int start, int count) {
		HistoricTaskInstanceQuery query = createQuery().finished();
		if ("name".equals(orderBy)) {
			query.orderByTaskName();
		} else if ("id".equals(orderBy)) {
			query.orderByTaskId();
		} else {
			query.orderByTaskId();
		}
		return listPage(query, start, count);
	}

	public HistoricTaskInstance singleResult(String id) {
		return createQuery().taskDefinitionKey(id).singleResult();
	}

	public int size() {
		return (int) createQuery().finished().count();
	}

	private HistoricTaskInstanceQuery createQuery() {
		return Flash.flash().getProcessEngine().getHistoryService().createHistoricTaskInstanceQuery();
	}
}
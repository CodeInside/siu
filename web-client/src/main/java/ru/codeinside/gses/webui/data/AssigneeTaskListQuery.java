/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import java.util.List;

import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.actions.ItemBuilder;

public class AssigneeTaskListQuery extends AbstractLazyLoadingQuery<Task> {

	private static final long serialVersionUID = 1L;
	public static final Object[] NATURAL_COL_ORDER = new Object[] { "id", "name", "exe", "assignee" };
	public static final String[] COL_HEADERS_RUS = new String[] { "Номер", "Название этапа", "Действие", "Исполнитель" };

	private final String assignee;

	public AssigneeTaskListQuery(ItemBuilder<Task> itemBuilder, String assignee) {
		super(itemBuilder);
		this.assignee = assignee;
	}

	List<Task> items(int start, int count) {
		TaskQuery query = createAssigneeTaskQuery();
		if ("name".equals(orderBy)) {
			query.orderByTaskName();
		} else if ("id".equals(orderBy)) {
			query.orderByTaskId();
		} else {
			query.orderByDueDate();
		}
		return listPage(query, start, count);
	}

	public Task singleResult(String id) {
		return createAssigneeTaskQuery().taskDefinitionKey(id).singleResult();
	}

	public int size() {
		return (int) createAssigneeTaskQuery().count();
	}

	private TaskQuery createAssigneeTaskQuery() {
		TaskQuery query = Flash.flash().getProcessEngine().getTaskService().createTaskQuery();
		query.taskAssignee(assignee);
		return query;
	}
}
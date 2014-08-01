/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import static ru.codeinside.gses.service.Functions.withTask;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import ru.codeinside.gses.webui.actions.ItemBuilder;

import com.google.common.base.Function;

public class TaskListQuery extends AbstractLazyLoadingQuery<Task> {

	private static final long serialVersionUID = 5827496216645211007L;
	public static final Object[] NATURAL_COL_ORDER = new Object[] { "id", "name", "exe", "assignee" };
	public static final String[] COL_HEADERS_RUS = new String[] { "Номер", "Название этапа", "Действие", "Исполнитель" };

	public TaskListQuery(ItemBuilder<Task> itemBuilder) {
		super(itemBuilder);
	}

	List<Task> items(final int start, final int count) {
		return withTask(new Function<TaskService, List<Task>>() {
			public List<Task> apply(TaskService srv) {
				TaskQuery query = srv.createTaskQuery();
				if ("name".equals(orderBy)) {
					query.orderByTaskName();
				} else if ("id".equals(orderBy)) {
					query.orderByTaskId();
				} else {
					query.orderByDueDate();
				}

				return listPage(query, start, count);
			};
		});

	}

	public Task singleResult(final String id) {
		return withTask(new Function<TaskService, Task>() {
			public Task apply(TaskService srv) {
				return srv.createTaskQuery().taskDefinitionKey(id).singleResult();
			};
		});
	}

	public int size() {
		return withTask(new Function<TaskService, Integer>() {
			public Integer apply(TaskService srv) {
				return (int) srv.createTaskQuery().count();
			};
		});
	}
}
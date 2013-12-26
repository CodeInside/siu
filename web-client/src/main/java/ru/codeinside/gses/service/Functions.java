/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.service;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

import ru.codeinside.gses.webui.Flash;

import com.google.common.base.Function;

final public class Functions {

	public static <T> T withRepository(String userId, Function<RepositoryService, T> fun) {
		return get().withRepository(userId, fun);
	}

	public static <T> T withRepository(Function<RepositoryService, T> fun) {
		return get().withRepository(fun);
	}

	public static <T> T withTask(Function<TaskService, T> fun) {
		return get().withTask(fun);
	}

	public static <T> T withHistory(Function<HistoryService, T> fun) {
		return get().withHistory(fun);
	}

	public static <T> T withRuntime(Function<RuntimeService, T> fun) {
		return get().withRuntime(fun);
	}

	public static <T> T withEngine(PF<T> fun) {
		return get().withEngine(fun);
	}

	public static ActivitiService get() {
		return Flash.flash().getActivitiService();
	}
}

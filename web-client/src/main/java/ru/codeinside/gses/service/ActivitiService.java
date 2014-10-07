/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.service;

import com.google.common.base.Function;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

import java.util.concurrent.atomic.AtomicReference;

public interface ActivitiService {

  /**
   * Пару методов используется во внешенй фоме.
   * Инициализация в {@link ru.codeinside.gses.webui.WebContext}
   */
  AtomicReference<ActivitiService> INSTANCE = new AtomicReference<ActivitiService>();

  <T> T withRepository(Function<RepositoryService, T> fun);

  <T> T withRepository(String userId, Function<RepositoryService, T> fun);

  <T> T withTask(Function<TaskService, T> fun);

  <T> T withHistory(Function<HistoryService, T> fun);

  <T> T withRuntime(Function<RuntimeService, T> fun);

  <T> T withEngine(Function<ProcessEngine, T> fun);

  <R> R withEngine(F0<R> f0);

  <R, A> R withEngine(F1<R, A> f1, A arg);

  <R, A1, A2> R withEngine(F2<R, A1, A2> f2, A1 arg1, A2 arg2);

  <R, A1, A2, A3> R withEngine(F3<R, A1, A2, A3> f3, A1 arg1, A2 arg2, A3 arg3);

}

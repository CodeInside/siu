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

import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Singleton
@TransactionManagement
@TransactionAttribute
@DependsOn("BaseBean")
public class ActivitiServiceImpl implements ActivitiService {

  @Inject
  Instance<ProcessEngine> processEngine;

  @Override
  public <T> T withRepository(final Function<RepositoryService, T> fun) {
    return fun.apply(engine().getRepositoryService());
  }

  private ProcessEngine engine() {
    return processEngine.get();
  }

  @Override
  public <T> T withTask(final Function<TaskService, T> fun) {
    return fun.apply(engine().getTaskService());
  }

  @Override
  public <T> T withRepository(String userId, Function<RepositoryService, T> fun) {
    engine().getIdentityService().setAuthenticatedUserId(userId);
    return withRepository(fun);
  }

  @Override
  public <T> T withHistory(Function<HistoryService, T> fun) {
    return fun.apply(engine().getHistoryService());
  }

  @Override
  public <T> T withRuntime(Function<RuntimeService, T> fun) {
    return fun.apply(engine().getRuntimeService());
  }

  @Override
  public <T> T withEngine(Function<ProcessEngine, T> fun) {
    return fun.apply(engine());
  }

  @Override
  public <R> R withEngine(final F0<R> f0) {
    return f0.apply(engine());
  }

  @Override
  public <R, A> R withEngine(final F1<R, A> f1, final A arg) {
    return f1.apply(engine(), arg);
  }

  @Override
  public <R, A1, A2> R withEngine(final F2<R, A1, A2> f2, final A1 arg1, final A2 arg2) {
    return f2.apply(engine(), arg1, arg2);
  }

  @Override
  public <R, A1, A2, A3> R withEngine(final F3<R, A1, A2, A3> f3, final A1 arg1, final A2 arg2, final A3 arg3) {
    return f3.apply(engine(), arg1, arg2, arg3);
  }

}

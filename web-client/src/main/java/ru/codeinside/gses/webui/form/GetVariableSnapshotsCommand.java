/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSession;
import ru.codeinside.gses.activiti.history.VariableSnapshot;

import java.util.List;
import java.util.Map;

@Deprecated
final public class GetVariableSnapshotsCommand implements Command<Map<String, VariableSnapshot>> {

  private final String taskId;
  private final List<FormPropertyHandler> handlers;

  public GetVariableSnapshotsCommand(String taskId, List<FormPropertyHandler> handlers) {
    this.taskId = taskId;
    this.handlers = handlers;
  }

  @Override
  public Map<String, VariableSnapshot> execute(final CommandContext commandContext) {
    final TaskEntity task = commandContext.getTaskManager().findTaskById(taskId);
    final HistoricDbSqlSession session = (HistoricDbSqlSession) commandContext.getSession(DbSqlSession.class);
    return session.getVariableSnapshots(task.getExecution(), handlers);
  }
}

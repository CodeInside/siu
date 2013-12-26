/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import ru.codeinside.gses.activiti.FormID;
import ru.codeinside.gses.activiti.forms.CloneSupport;
import ru.codeinside.gses.activiti.forms.CloneTree;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSession;
import ru.codeinside.gses.activiti.history.VariableSnapshot;

import java.util.Map;

public class CloneFormPropertiesCommand implements Command<FormPropertyClones> {

  final FormID id;
  final String pid;
  final String path;
  final String login;

  public CloneFormPropertiesCommand(FormID id, String pid, String path, String login) {
    this.id = id;
    this.pid = pid;
    this.path = path;
    this.login = login;
  }

  @Override
  public FormPropertyClones execute(final CommandContext commandContext) {
    final FullFormHandler fullFormHandler = new GetFormHandlerCommand(true, id.processDefinitionId, id.taskId, login).execute(commandContext);
    final CloneTree cloneTree = ((CloneSupport) fullFormHandler.formHandler).cloneTree(fullFormHandler.executionEntity, pid, path);
    final HistoricDbSqlSession session = (HistoricDbSqlSession) commandContext.getSession(DbSqlSession.class);

    final Map<String, VariableSnapshot> snapshots = fullFormHandler.executionEntity == null ? null : session.getVariableSnapshots(fullFormHandler.executionEntity, cloneTree.handlers);

    final FormPropertyClones clones = new FormPropertyClones();
    clones.properties = cloneTree.properties;
    clones.snapshots = snapshots;
    return clones;
  }

}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.jta;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;

final public class IdGenerator implements org.activiti.engine.impl.cfg.IdGenerator {

  @Override
  public String getNextId() {
    final CommandContext commandContext = Context.getCommandContext();
    final CustomDbSqlSession customDbSqlSession = (CustomDbSqlSession) commandContext.getDbSqlSession();
    return customDbSqlSession.getNextId();
  }
}

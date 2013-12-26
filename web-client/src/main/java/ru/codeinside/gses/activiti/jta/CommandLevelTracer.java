/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.jta;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandInterceptor;

import java.util.logging.Level;
import java.util.logging.Logger;

final public class CommandLevelTracer extends CommandInterceptor {

  final static private ThreadLocal<Integer> LEVEL = new InheritableThreadLocal<Integer>();

  final private Logger logger = Logger.getLogger(getClass().getName());
  final private Level logLevel;


  public CommandLevelTracer(final Level logLevel) {
    this.logLevel = logLevel;
  }

  public static int getLevel() {
    final Integer level = LEVEL.get();
    return level == null ? 0 : level;
  }

  public <T> T execute(final Command<T> command) {
    final Integer previous = LEVEL.get();
    if (previous == null) {
      LEVEL.set(1);
    } else {
      LEVEL.set(1 + previous);
    }
    if (isLogEnabled()) {
      logCommand(command, true);
    }
    try {
      return next.execute(command);
    } finally {
      if (isLogEnabled()) {
        logCommand(command, false);
      }
      if (previous == null) {
        LEVEL.remove();
      } else {
        LEVEL.set(previous);
      }
    }
  }

  private <T> void logCommand(final Command<T> command, boolean enter) {
    final String prefix = enter ? "start " : "stop  ";
    logger.log(logLevel, prefix + LEVEL.get() + "|" + Thread.currentThread().getName() + "|" + command.getClass().getSimpleName());
  }

  private boolean isLogEnabled() {
    return logger.isLoggable(logLevel);
  }

}

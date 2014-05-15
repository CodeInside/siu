package ru.codeinside.gses.webui.form;

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import ru.codeinside.gses.activiti.FormID;
import ru.codeinside.gses.service.F3;
import ru.codeinside.gses.webui.Flash;

final public class Fetcher implements F3<FormPropertyClones, FormID, String, String> {
  private final String login;

  public Fetcher(String login) {
    this.login = login;
  }
  public Fetcher() {
    this.login = Flash.login();
  }
  @Override
  public FormPropertyClones apply(ProcessEngine engine, FormID id, String pid, String path) {
    final CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
    return commandExecutor.execute(new CloneFormPropertiesCommand(
      id, pid, path, login
    ));
  }
}

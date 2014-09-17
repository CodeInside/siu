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
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.GetFormBlockCommand;
import ru.codeinside.gses.activiti.forms.api.definitions.BlockNode;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;
import ru.codeinside.gses.service.F3;
import ru.codeinside.gses.webui.Flash;

import java.util.List;

final public class Fetcher implements F3<List<PropertyValue<?>>, FormID, BlockNode, String> {
  private final String login;

  public Fetcher(String login) {
    this.login = login;
  }

  public Fetcher() {
    this.login = Flash.login();
  }

  @Override
  public List<PropertyValue<?>> apply(ProcessEngine engine, FormID id, BlockNode definition, String path) {
    final CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
    return commandExecutor.execute(new GetFormBlockCommand(
      id, login, definition, path
    ));
  }
}

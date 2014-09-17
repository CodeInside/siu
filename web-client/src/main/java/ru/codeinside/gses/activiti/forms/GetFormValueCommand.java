/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import ru.codeinside.gses.activiti.forms.api.values.FormValue;
import ru.codeinside.gses.activiti.forms.values.PropertyValuesBuilder;

public class GetFormValueCommand implements Command<FormValue> {
  final FormID id;
  final String login;

  public GetFormValueCommand(FormID id, String login) {
    this.id = id;
    this.login = login;
  }

  @Override
  public FormValue execute(CommandContext commandContext) {
    FormDefinition def = new GetFormDefinitionCommand(id, login).execute(commandContext);
    return new PropertyValuesBuilder(id.taskId, def.execution, null).build(def.propertyTree, def.task, def.processDefinition);
  }

}

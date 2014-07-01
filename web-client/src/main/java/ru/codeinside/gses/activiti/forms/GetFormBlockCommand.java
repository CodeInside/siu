/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import ru.codeinside.gses.activiti.forms.api.definitions.BlockNode;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;
import ru.codeinside.gses.activiti.forms.values.PropertyValuesBuilder;

import java.util.List;

public class GetFormBlockCommand implements Command<List<PropertyValue<?>>> {
  final FormID id;
  final String login;
  final BlockNode definition;
  final String path;

  public GetFormBlockCommand(FormID id, String login, BlockNode definition, String path) {
    this.id = id;
    this.login = login;
    this.definition = definition;
    this.path = path;
  }

  @Override
  public List<PropertyValue<?>> execute(CommandContext commandContext) {
    FormDefinition def = new GetFormDefinitionCommand(id, login).execute(commandContext);
    return new PropertyValuesBuilder(id.taskId, def.execution, null).block(definition, path);
  }

}

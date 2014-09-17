/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import ru.codeinside.gses.webui.Flash;

import java.util.Map;

public class SubmitFormCmd implements Command<Void> {

  final FormID formID;
  final Map<String, Object> properties;
  final Signatures signatures;

  public SubmitFormCmd(FormID formID, Map<String, Object> properties, Signatures signatures) {
    this.formID = formID;
    this.properties = properties;
    this.signatures = signatures;
  }

  @Override
  public Void execute(CommandContext commandContext) {
    FormDefinition def = new GetFormDefinitionCommand(formID, Flash.login()).execute(commandContext);
    new SubmitFormDataCmd(def.propertyTree, def.execution, properties, signatures).execute(commandContext);
    TaskEntity task = commandContext.getTaskManager().findTaskById(def.task.getId());
    task.complete();
    return null;
  }
}

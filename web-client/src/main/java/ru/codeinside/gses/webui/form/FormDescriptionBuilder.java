/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import org.activiti.engine.ProcessEngine;
import ru.codeinside.gses.activiti.FormID;
import ru.codeinside.gses.service.ExecutorService;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.Flash;

final public class FormDescriptionBuilder implements PF<FormDescription> {
  private static final long serialVersionUID = 1L;
  private final ExecutorService executorService;

  private final FormID id;

  public FormDescriptionBuilder(FormID id, ExecutorService executorService) {
    this.id = id;
    this.executorService = executorService;
  }

  public FormDescription apply(ProcessEngine engine) {
    final FullFormData fullFormData = new FullFormDataBuilder(id, Flash.login()).build(engine);
    final String procedureName = executorService.getProcedureNameByDefinitionId(fullFormData.processDefinition.getId());
    return new FormDescription(
      fullFormData.task,
      fullFormData.processDefinition,
      id,
      new FormSeqBuilder(fullFormData.decorator).build(),
      procedureName
    );
  }


}

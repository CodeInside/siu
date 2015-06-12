/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.collect.ImmutableList;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.GetFormValueCommand;
import ru.codeinside.gses.activiti.forms.api.values.FormValue;
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
    FormValue formValue = ((ServiceImpl) engine.getFormService())
      .getCommandExecutor()
      .execute(new GetFormValueCommand(id, Flash.login()));

    String procedureName = executorService == null
      ? formValue.getProcessDefinition().getName()
      : executorService.getProcedureNameByDefinitionId(formValue.getProcessDefinition().getId());

    return new FormDescription(
      formValue.getTask(),
      formValue.getProcessDefinition(),
      id,
      build(formValue),
      procedureName
    );
  }

  public ImmutableList<FormSeq> build(FormValue formValue) {
    ImmutableList.Builder<FormSeq> steps = ImmutableList.builder();
    steps.add(buildFormPage(formValue));
    if (formValue.getFormDefinition().isSignatureRequired()) {
      steps.add(new FormSignatureSeq());
    } else if (formValue.getFormDefinition().isDataFlow()) {
      String consumerName = formValue.getFormDefinition().getConsumerName();
      boolean needSp = formValue.getFormDefinition().needSp();
      boolean needOv = formValue.getFormDefinition().needOv();
      boolean needTep = formValue.getFormDefinition().needTep();
      boolean needSend = formValue.getFormDefinition().needSend();
      boolean isLazyWriter = formValue.getFormDefinition().isLazyWriter();

      DataAccumulator dataAccumulator = new DataAccumulator();
      dataAccumulator.setNeedOv(needOv);

      Task task = formValue.getTask();
      if (task != null) {
        dataAccumulator.setTaskId(task.getId());
      }
      dataAccumulator.setServiceName(consumerName);
      if (needSp) {
        steps.add(new FormSpSignatureSeq(dataAccumulator));
      }

      if (needOv) {
        steps.add(new FormOvSignatureSeq(dataAccumulator));
      }
    } else if (formValue.getFormDefinition().isResultDataFlow()) {
      boolean resultNeedSp = formValue.getFormDefinition().resultNeedSp();
      boolean resultNeedOv = formValue.getFormDefinition().resultNeedOv();

      DataAccumulator dataAccumulator = new DataAccumulator();
      dataAccumulator.setNeedOv(resultNeedOv);

      Task task = formValue.getTask();
      if (task != null) {
        dataAccumulator.setTaskId(task.getId());
      }

      if (resultNeedSp) {
        //TODO add SP sign step
      }

      if (resultNeedOv) {
        //TODO add OV sign step
      }
    }
    return steps.build();
  }

  FormSeq buildFormPage(FormValue formValue) {
    if (formValue.getFormDefinition().getFormKey() != null) {
      return new EFormBuilder(formValue, id);
    }

    // поддержка первого варианта внешних форм.
    if (formValue.getFormDefinition().getIndex().containsKey(API.JSON_FORM)) {
      return new JsonFormBuilder(formValue.getPropertyValues());
    }

    FieldTree fieldTree = new FieldTree(id);
    fieldTree.create(formValue);
    GridForm form = new GridForm(id, fieldTree);
    form.setImmediate(true);
    return new TrivialFormPage(fieldTree, form);
  }


}

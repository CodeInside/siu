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
  private final DataAccumulator accumulator;

  public FormDescriptionBuilder(FormID id, ExecutorService executorService, DataAccumulator accumulator) {
    this.id = id;
    this.executorService = executorService;
    this.accumulator = accumulator;
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
      steps.add(new FormSignatureSeq(accumulator));
    }
    if (formValue.getFormDefinition().isDataFlow()) {
      String consumerName = formValue.getFormDefinition().getConsumerName();
      boolean needSp = formValue.getFormDefinition().needSp();
      boolean needOv = formValue.getFormDefinition().needOv();
      boolean needTep = formValue.getFormDefinition().needTep();
      boolean needSend = formValue.getFormDefinition().needSend();
      boolean isLazyWriter = formValue.getFormDefinition().isLazyWriter();

      accumulator.setServiceName(consumerName);
      accumulator.setNeedOv(needOv);
      accumulator.setPropertyTree(formValue.getFormDefinition());

      Task task = formValue.getTask();
      if (task != null) {
        accumulator.setTaskId(task.getId());
      }

      if (needSp) {
        steps.add(new FormSpSignatureSeq(accumulator));
      }

      if (needOv) {
        steps.add(new FormOvSignatureSeq(accumulator));
      }
    } else if (formValue.getFormDefinition().isResultDataFlow()) {
      String requestType = formValue.getFormDefinition().getRequestType();
      String responseMessage = formValue.getFormDefinition().getResponseMessage();
      boolean resultNeedSp = formValue.getFormDefinition().resultNeedSp();
      boolean resultNeedOv = formValue.getFormDefinition().resultNeedOv();

      accumulator.setNeedOv(resultNeedOv);
      accumulator.setRequestType(requestType);
      accumulator.setResponseMessage(responseMessage);
      accumulator.setPropertyTree(formValue.getFormDefinition());

      Task task = formValue.getTask();
      if (task != null) {
        accumulator.setTaskId(task.getId());
      }

      if (resultNeedSp) {
        steps.add(new FormSpSignatureSeq(accumulator));
      }

      if (resultNeedOv) {
        steps.add(new FormOvSignatureSeq(accumulator));
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

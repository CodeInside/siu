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
import ru.codeinside.gses.webui.form.DataAccumulator;
import ru.codeinside.gses.webui.form.ProcessInstanceAttachmentConverter;
import ru.codeinside.gses.webui.form.SignatureType;

import java.util.Map;

public class SubmitFormCmd implements Command<String> {

    final FormID formID;
    final Map<String, Object> properties;
    final Map<SignatureType, Signatures> signatures;
    final boolean submitTask;
    final DataAccumulator accumulator;

    public SubmitFormCmd(FormID formID, Map<String, Object> properties, Map<SignatureType, Signatures> signatures,
                         DataAccumulator accumulator) {
        this(formID, properties, signatures, true, accumulator);
    }

    public SubmitFormCmd(FormID formID, Map<String, Object> properties,
                         Map<SignatureType, Signatures> signatures, boolean submitTask, DataAccumulator accumulator) {
        this.formID = formID;
        this.properties = properties;
        this.signatures = signatures;
        this.submitTask = submitTask;
        this.accumulator = accumulator;
    }

    @Override
    public String execute(CommandContext commandContext) {
        FormDefinition def = new GetFormDefinitionCommand(formID, Flash.login()).execute(commandContext);
        final String processInstanceId = def.execution.getProcessInstanceId();
        new SubmitFormDataCmd(
                def.propertyTree,
                def.execution,
                properties,
                signatures,
                new ProcessInstanceAttachmentConverter(processInstanceId),
                accumulator).execute(commandContext);
        if (submitTask) {
            TaskEntity task = commandContext.getTaskManager().findTaskById(def.task.getId());
            task.complete();
        }
        return processInstanceId;
    }
}

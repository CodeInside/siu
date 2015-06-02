/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.activiti.forms.SubmitFormCmd;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.form.api.FieldSignatureSource;
import ru.codeinside.gses.webui.form.api.FieldValuesSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TaskFormSubmitter implements PF<Boolean> {
  private static final long serialVersionUID = 1L;

  private final String taskId;
  private final List<Form> forms;

  TaskFormSubmitter(String taskId, List<Form> forms) {
    this.taskId = taskId;
    this.forms = forms;
  }

  public Boolean apply(ProcessEngine engine) {
    FieldValuesSource valuesSource = (FieldValuesSource) forms.get(0);
    Map<String, Object> fieldValues = valuesSource.getFieldValues();
    Map<SignatureType, Signatures> signatures = new HashMap<SignatureType, Signatures>();
    if (forms.size() > 1) {
      for (Form form : forms) {

        if (form instanceof FormSignatureSeq.SignatureForm) {
          FieldSignatureSource signatureSource = (FieldSignatureSource) form;
          signatures.put(SignatureType.FIELDS, signatureSource.getSignatures());
        } else if (form instanceof FormSpSignatureSeq.SpSignatureForm) {
          FieldSignatureSource signatureSource = (FieldSignatureSource) form;
          signatures.put(SignatureType.SP, signatureSource.getSignatures());
          fieldValues.put(FormSpSignatureSeq.SIGNED_DATA_ID, signatureSource.getSignedData());
        } else if (form instanceof FormOvSignatureSeq.OvSignatureForm) {
          FieldSignatureSource signatureSource = (FieldSignatureSource) form;
          signatures.put(SignatureType.OV, signatureSource.getSignatures());
          fieldValues.put(FormOvSignatureSeq.SIGNED_DATA_ID, signatureSource.getSignedData());
        }
      }
    } else {
      signatures = null;
    }

    CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
    commandExecutor.execute(new SubmitFormCmd(FormID.byTaskId(taskId), fieldValues, signatures));
    return true;
  }
}

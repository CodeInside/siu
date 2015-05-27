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
import ru.codeinside.gses.activiti.SubmitStartFormCommand;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.service.BidID;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.form.api.FieldSignatureSource;
import ru.codeinside.gses.webui.form.api.FieldValuesSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final public class StartTaskFormSubmiter implements PF<BidID> {
  private static final long serialVersionUID = 1L;

  private final String processDefinitionId;
  private final List<Form> forms;

  StartTaskFormSubmiter(String processDefinitionId, List<Form> forms) {
    this.processDefinitionId = processDefinitionId;
    this.forms = forms;
  }

  public BidID apply(ProcessEngine engine) {
    FieldValuesSource valuesSource = (FieldValuesSource) forms.get(0);
    Map<String, Object> fieldValues = valuesSource.getFieldValues();
    Map<String, Signatures> signatures = new HashMap<String, Signatures>();
    if (forms.size() > 1) { //TODO проверить взаимодействие с новой логикой
      for (Form form : forms) {
        if (form instanceof FormSignatureSeq.SignatureForm) {
          FieldSignatureSource signatureSource = (FieldSignatureSource) form;
          signatures.put("FieldsSignatures", signatureSource.getSignatures());
        } else if (form instanceof FormSpSignatureSeq.SpSignatureForm) {
          FieldSignatureSource signatureSource = (FieldSignatureSource) form;
          signatures.put("SpSignature", signatureSource.getSignatures());
        } else if (form instanceof FormOvSignatureSeq.OvSignatureForm) {
          FieldSignatureSource signatureSource = (FieldSignatureSource) form;
          signatures.put("OvSignature", signatureSource.getSignatures());
        }
      }
    } else {
      signatures = null;
    }

    // TODO пусть пока так, что б не ломалась старая логика. Потом надо педерелать, что б передавать мапу
    Signatures signature;
    if (signatures != null && signatures.containsKey("FieldsSignatures")) {
      signature = signatures.get("FieldsSignatures");
    } else {
      signature = null;
    }

    return ((ServiceImpl) engine.getFormService()).getCommandExecutor().execute(
      new SubmitStartFormCommand(null, null, processDefinitionId, fieldValues, signature, Flash.login(), null)
    );
  }
}

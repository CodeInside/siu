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

final public class StartTaskFormSubmitter implements PF<BidID> {
  private static final long serialVersionUID = 1L;

  private final String processDefinitionId;
  private final List<Form> forms;

  StartTaskFormSubmitter(String processDefinitionId, List<Form> forms) {
    this.processDefinitionId = processDefinitionId;
    this.forms = forms;
  }

  public BidID apply(ProcessEngine engine) {
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
          fieldValues.put(FormOvSignatureSeq.SOAP_MESSAGE, ((FormOvSignatureSeq.OvSignatureForm) form).getSoapMessage().getBytes());
          fieldValues.put(FormOvSignatureSeq.REQUEST_ID, ((FormOvSignatureSeq.OvSignatureForm) form).getRequestId());
        }
      }
    } else {
      signatures = null;
    }

    return ((ServiceImpl) engine.getFormService()).getCommandExecutor().execute(
      new SubmitStartFormCommand(null, null, processDefinitionId, fieldValues, signatures, Flash.login(), null)
    );
  }
}

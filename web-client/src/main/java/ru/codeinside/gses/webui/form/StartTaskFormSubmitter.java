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
import ru.codeinside.log.SignatureLogger;

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

    Signatures spSignatures = null;
    Signatures ovSignatures = null;
    String spData = null;
    String ovData = null;

    if (forms.size() > 1) {
      for (Form form : forms) {
        if (form instanceof FormSignatureSeq.SignatureForm) {
          FieldSignatureSource signatureSource = (FieldSignatureSource) form;
          signatures.put(SignatureType.FIELDS, signatureSource.getSignatures());
        } else if (form instanceof FormSpSignatureSeq.SpSignatureForm) {
          //TODO сохранять подписи СП и подписанные данные в базу. Например, в ByteArrayEntity. В контекст писать только ID
          FormSpSignatureSeq.SpSignatureForm spForm = (FormSpSignatureSeq.SpSignatureForm) form;
          if (!spForm.needOv()) {
            spSignatures = spForm.getSignatures();
            spData = spForm.getSignedData();
            fieldValues.put(spForm.getEntityFieldId(), spForm.getEntityId());
          }
        } else if (form instanceof FormOvSignatureSeq.OvSignatureForm) {
          //TODO сохранять подписи ОВ и подписанные данные в базу. Например, в ByteArrayEntity. В контекст писать только ID
          FormOvSignatureSeq.OvSignatureForm ovForm = (FormOvSignatureSeq.OvSignatureForm) form;
          ovSignatures = ovForm.getSignatures();
          ovData = ovForm.getSignedData();
          fieldValues.put(ovForm.getEntityFieldId(), ovForm.getEntityId());
        }
      }
    } else {
      signatures = null;
    }

    BidID bidID = ((ServiceImpl) engine.getFormService()).getCommandExecutor().execute(
            new SubmitStartFormCommand(null, null, processDefinitionId, fieldValues, signatures, Flash.login(), null));

    SignatureLogger signatureLogger = new SignatureLogger(bidID.bidId, null);
    if (spSignatures != null && spData != null) {
      signatureLogger.log(spData, spSignatures, SignatureType.SP);
    }
    if (ovSignatures != null && ovData != null) {
      signatureLogger.log(ovData, ovSignatures, SignatureType.OV);
    }

    return bidID;
  }
}

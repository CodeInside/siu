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
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.activiti.forms.SubmitFormCmd;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.form.api.FieldSignatureSource;
import ru.codeinside.gses.webui.form.api.FieldValuesSource;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.log.SignatureLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TaskFormSubmitter implements PF<Boolean> {
  private static final long serialVersionUID = 1L;

  private final String taskId;
  private final List<Form> forms;
  private DataAccumulator accumulator;

  TaskFormSubmitter(String taskId, List<Form> forms, DataAccumulator accumulator) {
    this.taskId = taskId;
    this.forms = forms;
    this.accumulator = accumulator;
  }

  public Boolean apply(ProcessEngine engine) {
    FieldValuesSource valuesSource = (FieldValuesSource) forms.get(0);
    Map<String, Object> fieldValues = valuesSource.getFieldValues();
    Map<SignatureType, Signatures> signatures = new HashMap<SignatureType, Signatures>();

    Signatures spSignatures = null;
    Signatures ovSignatures = null;
    String spData = null;
    String ovData = null;

    if (forms.size() > 1) {
      for (Form form : forms) {
        if (form instanceof FieldSignatureSource) {
          FieldSignatureSource signatureSource = (FieldSignatureSource) form;
          if (form instanceof FormSignatureSeq.SignatureForm) {
            signatures.put(SignatureType.FIELDS, signatureSource.getSignatures());
          } else if (form instanceof FormSpSignatureSeq.SpSignatureForm) {
            spSignatures = signatureSource.getSignatures();
            spData = signatureSource.getSignedData();
            signatures.put(SignatureType.SP, spSignatures);
            putEnclosures(fieldValues, signatureSource.getSignData().getEnclosures());
            //TODO сохранять signedAppData в ByteArrayEntity, а в контекст писать только ID
          } else if (form instanceof FormOvSignatureSeq.OvSignatureForm) {
            ovSignatures = signatureSource.getSignatures();
            ovData = signatureSource.getSignedData();
            signatures.put(SignatureType.OV, ovSignatures);
            putEnclosures(fieldValues, signatureSource.getSignData().getEnclosures());
            //TODO сохранять signedSoapBody в ByteArrayEntity, а в контекст писать только ID
          }
        }
      }
    } else {
      signatures = null;
    }

    CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
    String processInstanceId = commandExecutor.execute(
            new SubmitFormCmd(FormID.byTaskId(taskId), fieldValues, signatures, accumulator));

    Bid bid = AdminServiceProvider.get().getBidByProcessInstanceId(processInstanceId);
    SignatureLogger signatureLogger = new SignatureLogger(bid.getId(), taskId);
    if (spSignatures != null && spData != null) {
      signatureLogger.log(spData, spSignatures, SignatureType.SP, accumulator.getVirginAppData());
    }
    if (ovSignatures != null && ovData != null) {
      signatureLogger.log(ovData, ovSignatures, SignatureType.OV, accumulator.getVirginSoapMessage());
    }

    return true;
  }

  private void putEnclosures(Map<String, Object> fieldValues, List<Enclosure> enclosures) {
    for(Enclosure e : enclosures) {
      String key = e.code != null && !e.code.isEmpty() ? e.code : e.fileName;
      if (!fieldValues.containsKey(key)) {
        fieldValues.put(key, e);
      }
    }
  }
}

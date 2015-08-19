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
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.service.BidID;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.form.api.FieldSignatureSource;
import ru.codeinside.gses.webui.form.api.FieldValuesSource;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.log.SignatureLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final public class StartTaskFormSubmitter implements PF<BidID> {
  private static final long serialVersionUID = 1L;

  private final String processDefinitionId;
  private final List<Form> forms;
  private DataAccumulator accumulator;

  StartTaskFormSubmitter(String processDefinitionId, List<Form> forms, DataAccumulator accumulator) {
    this.processDefinitionId = processDefinitionId;
    this.forms = forms;
    this.accumulator = accumulator;
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
          FormSpSignatureSeq.SpSignatureForm spForm = (FormSpSignatureSeq.SpSignatureForm) form;
          spSignatures = spForm.getSignatures();
          spData = spForm.getSignedData();
          signatures.put(SignatureType.SP, spSignatures);
          putEnclosures(fieldValues, spForm.getSignData().getEnclosures());
        } else if (form instanceof FormOvSignatureSeq.OvSignatureForm) {
          FormOvSignatureSeq.OvSignatureForm ovForm = (FormOvSignatureSeq.OvSignatureForm) form;
          ovSignatures = ovForm.getSignatures();
          ovData = ovForm.getSignedData();
          putEnclosures(fieldValues, ovForm.getSignData().getEnclosures());
          signatures.put(SignatureType.OV, ovSignatures);
        }
      }
    } else {
      signatures = null;
    }

    updateFieldValuesFromTempContext(fieldValues);

    BidID bidID = ((ServiceImpl) engine.getFormService()).getCommandExecutor().execute(
            new SubmitStartFormCommand(null, null, processDefinitionId, fieldValues, signatures, Flash.login(), null,
                    accumulator));

    SignatureLogger signatureLogger = new SignatureLogger(bidID.bidId, null);
    if (spSignatures != null && spData != null) {
      signatureLogger.log(spData, spSignatures, SignatureType.SP, accumulator.getVirginAppData());
    }
    if (ovSignatures != null && ovData != null) {
      signatureLogger.log(ovData, ovSignatures, SignatureType.OV, accumulator.getVirginSoapMessage());
    }

    return bidID;
  }

  private void updateFieldValuesFromTempContext(Map<String, Object> fieldValues) {
    ExchangeContext tempContext = accumulator.getTempContext();
    if (tempContext != null) {
      for (String varName : tempContext.getVariableNames()) {
        Object oldValue = fieldValues.get(varName);
        Object newValue = tempContext.getVariable(varName);

        Map<String, PropertyNode> nodeMap = accumulator.getPropertyTree().getIndex();
        if (nodeMap.containsKey(varName) && !nodeMap.get(varName).isFieldWritable()) continue;

        if (newValue != null && !newValue.equals(oldValue)) {
          fieldValues.put(varName, newValue);
        }
      }
    }
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

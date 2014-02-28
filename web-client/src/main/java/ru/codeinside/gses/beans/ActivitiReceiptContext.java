/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.cmd.CreateAttachmentCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.task.Attachment;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.mime.MimeTypes;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
import ru.codeinside.gses.activiti.history.ExecutionId;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSession;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ReceiptContext;
import ru.codeinside.gws.api.Signature;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateEncodingException;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


//TODO переместить в другой пакет
public class ActivitiReceiptContext implements ReceiptContext {

  final static String VAR_PREFIX = "result_";
  final static int VAR_PREFIX_LENGTH = VAR_PREFIX.length();

  final Logger logger = Logger.getLogger(getClass().getName());
  final Map<String, Enclosure> nameToEnclosure = new LinkedHashMap<String, Enclosure>();
  final Map<Enclosure, String> enclosureToId = new IdentityHashMap<Enclosure, String>();
  final DelegateExecution execution;

  public ActivitiReceiptContext(DelegateExecution execution) {
    this.execution = execution;
  }

  @Override
  public Object getVariable(String name) {
    return ComplexValue.get(execution, VAR_PREFIX + name).getValue();
  }

  @Override
  public Object getVariableByFullName(String name) {
    return ComplexValue.get(execution, name).getValue();
  }


  @Override
  public Set<String> getPropertyNames() {
    return getVariableNames(false);
  }

  @Override
  public Set<String> getAllPropertyNames() {
    return getAllVariablesNames(true);
  }

  @Override
  public Set<String> getEnclosureNames() {
    return getVariableNames(true);
  }

  @Override
  public Set<String> getAllEnclosureNames() {
    return getAllVariablesNames(true);
  }

  @Override
  public Enclosure getEnclosure(String shortName) {
    return getAndCacheEnclosure(VAR_PREFIX + shortName);
  }

  @Override
  public Enclosure getEnclosureByFullName(String name) {
    return getAndCacheEnclosure(name);
  }

  @Override
  public void setEnclosure(String name, Enclosure enclosure) {
    if (name == null || enclosure == null || enclosure.content == null) {
      logger.info("invalid enclosure " + name + " = " + enclosure);
      return;
    }
    if (nameToEnclosure.get(name) != enclosure) {
      nameToEnclosure.put(name, enclosure);
      if (enclosureToId.containsKey(enclosure)) {
        execution.setVariable(name, enclosureToId.get(enclosure) + AttachmentFFT.SUFFIX);
      } else {
        Attachment attachment = createAttachment(name, enclosure);
        enclosureToId.put(enclosure, attachment.getId());
        execution.setVariable(name, enclosureToId.get(enclosure) + AttachmentFFT.SUFFIX);
        addSignature(name, enclosure.signature);
      }
    }
  }


  @Override
  public void setVariable(String name, Object value) {
    if (value instanceof Enclosure) {
      setEnclosure(name, (Enclosure) value);
    } else {
      nameToEnclosure.remove(name);
      execution.setVariable(name, value);
    }
  }

  public Map<Enclosure, String[]> getUsedEnclosures() {
    ImmutableMap.Builder<Enclosure, String[]> builder = ImmutableMap.builder();
    for (Enclosure enclosure : enclosureToId.keySet()) {
      String id = enclosureToId.get(enclosure);
      for (String name : nameToEnclosure.keySet()) {
        if (nameToEnclosure.get(name) == enclosure) {
          builder.put(enclosure, new String[]{id, name});
          break;
        }
      }
    }
    return builder.build();
  }


  private Set<String> getVariableNames(boolean attachment) {
    ImmutableSet.Builder<String> builder = ImmutableSet.builder();
    for (String varName : getAllVariablesNames(attachment)) {
      if (varName.startsWith(VAR_PREFIX)) {
        builder.add(varName.substring(VAR_PREFIX_LENGTH));
      }
    }
    return builder.build();
  }


  private Set<String> getAllVariablesNames(boolean attachment) {
    ImmutableSet.Builder<String> builder = ImmutableSet.builder();
    for (String varName : execution.getVariableNames()) {
      ComplexValue value = ComplexValue.get(execution, varName);
      if (attachment == value.isAttachmentId()) {
        builder.add(varName);
      }
    }
    return builder.build();
  }

  private Enclosure getAndCacheEnclosure(String name) {
    if (!nameToEnclosure.containsKey(name)) {
      ComplexValue value = ComplexValue.get(execution, name);
      if (value.isAttachmentId()) {
        Enclosure enclosure = Activiti.createEnclosureInCommandContext(value.getAttachmentId(), execution.getId(), name);
        if (enclosure != null) {
          nameToEnclosure.put(name, enclosure);
          enclosureToId.put(enclosure, value.getAttachmentId());
        }
      }
    }
    return nameToEnclosure.get(name);
  }

  private Attachment createAttachment(String name, Enclosure enclosure) {
    String mimeType = StringUtils.trimToNull(enclosure.mimeType);
    if (mimeType == null) {
      mimeType = new MimeTypes().getMimeType(enclosure.content).getName();
    }
    return new CreateAttachmentCmd(
      mimeType,
      execution.getId(),
      execution.getProcessInstanceId(),
      Activiti.getAttachmentName(enclosure, name),
      name,
      new ByteArrayInputStream(enclosure.content),
      null
    ).execute(Context.getCommandContext());
  }

  private void addSignature(String name, Signature signature) {
    if (signature != null && signature.certificate != null && signature.sign != null && signature.valid) {
      try {
        byte[] certificate = signature.certificate.getEncoded();
        byte[] sign = signature.sign;
        HistoricDbSqlSession session = (HistoricDbSqlSession) Context.getCommandContext().getDbSqlSession();
        ExecutionId executionId = new ExecutionId(execution.getProcessInstanceId(), execution.getId(), execution.getId());
        session.addSignature(executionId, name, certificate, sign, true);
      } catch (CertificateEncodingException e) {
        logger.log(Level.WARNING, "encode certificate fail", e);
      }
    }
  }

}

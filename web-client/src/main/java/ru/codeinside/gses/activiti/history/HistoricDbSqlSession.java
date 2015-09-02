/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.history;

import com.google.common.collect.Maps;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.impl.TaskQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSessionFactory;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableUpdateEntity;
import org.activiti.engine.impl.variable.EntityManagerSession;
import ru.codeinside.adm.database.AuditId;
import ru.codeinside.adm.database.AuditSnapshot;
import ru.codeinside.adm.database.AuditValue;
import ru.codeinside.gses.activiti.forms.PropertyContext;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.activiti.jta.CustomDbSqlSession;
import ru.codeinside.gses.beans.filevalues.SmevFileValue;
import ru.codeinside.gses.webui.data.TaskQueryImpl2;
import ru.codeinside.gses.webui.form.FormOvSignatureSeq;
import ru.codeinside.gses.webui.form.FormSpSignatureSeq;
import ru.codeinside.gses.webui.form.SignatureType;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.Signature;

import javax.persistence.EntityManager;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class HistoricDbSqlSession extends CustomDbSqlSession {

  /**
   * Уровень журналирования переменных.
   */
  final private Level varLogLevel = Level.FINE;
  final private Logger logger = Logger.getLogger(getClass().getName());
  final private ArrayList<AuditValue> insertAudits = new ArrayList<AuditValue>();
  final private ArrayList<VariableSignature> variableSignatures = new ArrayList<VariableSignature>();

  Map<SignatureType, Signatures> currentSignatures;

  HistoricDbSqlSession(DbSqlSessionFactory dbSqlSessionFactory) {
    super(dbSqlSessionFactory);
  }

  @Override
  public void insert(final PersistentObject o) {
    super.insert(o);
    if (o instanceof HistoricVariableUpdateEntity) {
      final HistoricVariableUpdateEntity var = (HistoricVariableUpdateEntity) o;
      final String user = getUser();
      logVar(user, var);

      final String writePath = PropertyContext.getWritePath();
      insertAudits.add(new AuditValue(user, Long.parseLong(var.getId()), new VarPath(writePath, var)));

      // упреждающее инициализируем сесиию,
      // чтобы не получить конкурентные модификацию на flush
      getEntityManagerSession();
    }
  }

  @Override
  public void flush() {
    linkPropertyValuesWithSignatures();
    super.flush();
  }

  @Override
  public TaskQueryImpl createTaskQuery() {
    return new TaskQueryImpl2();
  }

  public void linkPropertyValuesWithSignatures() {
    if (!insertAudits.isEmpty()) {
      final EntityManager em = getEntityManagerSession().getEntityManager();
      final Map<AuditId, AuditValue> map = Maps.newLinkedHashMap();
      for (final AuditValue audit : clearInsertAudits()) {
        final VarPath varPath = (VarPath) audit.getDetail();
        final HistoricVariableUpdate var = varPath.var;
        if (varPath.path != null && currentSignatures != null) {

          for (Signatures signatures : currentSignatures.values()) {

            final int index = signatures.findSign(varPath.path);
            if (index >= 0
                && !((VarPath) audit.getDetail()).var.getVariableName().equals(FormSpSignatureSeq.SIGNED_DATA_ID)
                && !((VarPath) audit.getDetail()).var.getVariableName().equals(FormOvSignatureSeq.SIGNED_DATA_ID)) {
              final byte[] sign = signatures.signs[index];
              final boolean isAttachment = signatures.files[index];
              audit.setSignature(signatures.certificate, sign, isAttachment);
            }

            // это вот с какой целью делается?
            final int spIndex = signatures.findSign(FormSpSignatureSeq.SP_SIGN);
            if (spIndex >= 0 && ((VarPath) audit.getDetail()).var.getVariableName().equals(FormSpSignatureSeq.SIGNED_DATA_ID)) {
              final byte[] sign = signatures.signs[spIndex];
              final boolean isAttachment = signatures.files[spIndex];
              audit.setSignature(signatures.certificate, sign, isAttachment);
            }

            // и это
            final int ovIndex = signatures.findSign(FormOvSignatureSeq.OV_SIGN);
            if (ovIndex >= 0 && ((VarPath) audit.getDetail()).var.getVariableName().equals(FormOvSignatureSeq.SIGNED_DATA_ID)) {
              final byte[] sign = signatures.signs[ovIndex];
              final boolean isAttachment = signatures.files[ovIndex];
              audit.setSignature(signatures.certificate, sign, isAttachment);
            }
          }
        } else {
          final VariableSignature signature = findVariableSignature(var);
          if (signature != null) {
            audit.setSignature(signature.cert, signature.sign, signature.file);
          }
        }
        map.put(new AuditId(getEid(var), var.getVariableName()), audit);
        em.persist(audit);
      }
      for (final AuditId auditId : map.keySet()) {
        AuditSnapshot snapshot = em.find(AuditSnapshot.class, auditId);
        if (snapshot != null) {
          snapshot.setValue(map.get(auditId));
        } else {
          snapshot = new AuditSnapshot(auditId, map.get(auditId));
        }
        em.persist(snapshot);
      }
    }

    variableSignatures.clear();

    currentSignatures = null;
  }

  private long getEid(final HistoricVariableUpdate h) {
    return Long.parseLong(getExecutionId(h));
  }

  private String getExecutionId(HistoricVariableUpdate h) {
    String id = h.getExecutionId();
    if (id == null) {
      id = h.getProcessInstanceId();
    }
    return id;
  }

  private List<AuditValue> clearInsertAudits() {
    final List<AuditValue> audits = new ArrayList<AuditValue>(insertAudits);
    insertAudits.clear();
    return audits;
  }

  private EntityManagerSession getEntityManagerSession() {
    return Context.getCommandContext().getSession(EntityManagerSession.class);
  }

  /**
   * Текущий пользователь из контекста аутентификации.
   */
  private String getUser() {
    return Authentication.getAuthenticatedUserId();
  }

  private void logVar(String user, HistoricVariableUpdateEntity var) {
    if (logger.isLoggable(varLogLevel)) {
      StringBuilder sb = new StringBuilder();
      sb.append("var:").append(var.getName());
      if (var.getRevision() > 0) {
        sb.append(", rev:").append(var.getRevision());
      }
      if (user != null) {
        sb.append(", user:").append(user);
      }
      sb.append(", value:").append(var.getValue());
      sb.append(",  pid:").append(var.getProcessInstanceId());
      if (!var.getProcessInstanceId().equals(var.getExecutionId())) {
        sb.append(",  eid:").append(var.getExecutionId());
      }
      if (var.getActivityInstanceId() != null) {
        sb.append(",  aid:").append(var.getActivityInstanceId());
      }
      if (var.getTaskId() != null) {
        sb.append(",  tid:").append(var.getTaskId());
      }
      logger.log(varLogLevel, sb.toString());
    }
  }

  /**
   * Метод вызывается в контексте команды Activiti.
   */
  public void addSignatures(Map<SignatureType, Signatures> signatures) {
    assert currentSignatures == null;
    assert signatures != null;
    currentSignatures = signatures;
  }

  public void addSignatures(SignatureType type, Signatures signatures) {
    if (currentSignatures == null) {
      currentSignatures = new HashMap<SignatureType, Signatures>();
    }
    currentSignatures.put(type, signatures);
  }

  public void addSignature(ExecutionId executionId, String varName, byte[] certificate, byte[] sign, boolean isAttachment) {
    variableSignatures.add(new VariableSignature(executionId, varName, certificate, sign, isAttachment));
  }

  //TODO: что тут является однозначным ключём?
  private VariableSignature findVariableSignature(HistoricVariableUpdate h) {
    String variableName = h.getVariableName();
    String taskId = h.getTaskId();
    if (taskId != null) {
      for (final VariableSignature v : variableSignatures) {
        if (variableName.equals(v.variable) && taskId.equals(v.executionId.taskId)) {
          return v;
        }
      }
    }
    final String executionId = h.getExecutionId();
    if (executionId != null) {
      for (final VariableSignature v : variableSignatures) {
        if (variableName.equals(v.variable) && executionId.equals(v.executionId.executionId)) {
          return v;
        }
      }
    }
    final String processDefinitionId = cacheGet(ExecutionEntity.class, getExecutionId(h)).getProcessDefinitionId();
    for (final VariableSignature v : variableSignatures) {
      if (variableName.equals(v.variable) && processDefinitionId.equals(v.executionId.processDefinitionId)) {
        return v;
      }
    }
    if (!variableSignatures.isEmpty()) {
      logger.log(varLogLevel, "can't find signature for variable " + variableName + " in " + variableSignatures);
    }
    return null;
  }


  public AuditValue getAuditSnapshotValue(long executionId, String varName) {
    EntityManager em = getEntityManagerSession().getEntityManager();
    AuditSnapshot snapshot = em.find(AuditSnapshot.class, new AuditId(executionId, varName));
    if (snapshot != null) {
      return snapshot.getValue();
    }
    logger.log(varLogLevel, "no history for " + executionId + ":" + varName);
    return null;
  }

  public AuditValue getTempAudit(long eid, String variableName) {
    for (AuditValue audit : new ArrayList<AuditValue>(insertAudits)) {
      VarPath varPath = (VarPath) audit.getDetail();
      HistoricVariableUpdate h = varPath.var;
      if (eid == getEid(h) && variableName.equals(h.getVariableName())) {
        final VariableSignature signature = findVariableSignature(h);
        if (signature != null) {
          audit.setSignature(signature.cert, signature.sign, signature.file);
        }
        return audit;
      }
    }
    return null;
  }

  public boolean addSignaturesBySmevFileValue(String processDefinitionId, String propertyId, SmevFileValue smev) {
    Enclosure enclosure = smev.getEnclosure();
    if (enclosure.signature == null || enclosure.signature.certificate == null) {
      return false;
    }
    Signature signature = enclosure.signature;
    try {
      variableSignatures.add(new VariableSignature(new ExecutionId(processDefinitionId), propertyId, signature.certificate.getEncoded(), signature.sign, true));
      return true;
    } catch (CertificateEncodingException e) {
      e.printStackTrace();
    }
    return false;
  }

}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.history;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.impl.TaskQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSessionFactory;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.juel.Builder;
import org.activiti.engine.impl.juel.IdentifierNode;
import org.activiti.engine.impl.juel.Tree;
import org.activiti.engine.impl.persistence.entity.AttachmentEntity;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableUpdateEntity;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.glassfish.osgicdi.ServiceUnavailableException;
import ru.codeinside.adm.database.AuditId;
import ru.codeinside.adm.database.AuditSnapshot;
import ru.codeinside.adm.database.AuditValue;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.Signatures;
import ru.codeinside.gses.activiti.VariableToBytes;
import ru.codeinside.gses.activiti.forms.PropertyContext;
import ru.codeinside.gses.activiti.jta.CustomDbSqlSession;
import ru.codeinside.gses.beans.filevalues.SmevFileValue;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.Flasher;
import ru.codeinside.gses.webui.data.TaskQueryImpl2;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.Signature;

import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class HistoricDbSqlSession extends CustomDbSqlSession {

  /**
   * Проверять ли целостность, сверяя значения аудита с текущими значением переменных.
   */
  private static final boolean CHECK_INTEGRITY = true;
  /**
   * Проверять ли целостность через equals при true либо через криптопровайдера при false.
   */
  private static final boolean CHECK_EQUALITY = true;
  /**
   * Уровень журналирования переменных.
   */
  final private Level varLogLevel = Level.FINE;
  final private Logger logger = Logger.getLogger(getClass().getName());
  final private ArrayList<AuditValue> insertAudits = new ArrayList<AuditValue>();
  final private ArrayList<VariableSignature> variableSignatures = new ArrayList<VariableSignature>();
  final private CryptoProvider cryptoProvider;

  Signatures currentSignatures;

  HistoricDbSqlSession(final CryptoProvider cryptoProvider, final DbSqlSessionFactory dbSqlSessionFactory) {
    super(dbSqlSessionFactory);
    this.cryptoProvider = cryptoProvider;
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
          final int index = currentSignatures.findSign(varPath.path);
          if (index >= 0) {
            final byte[] sign = currentSignatures.signs[index];
            final boolean isAttachment = currentSignatures.files[index];
            audit.setSignature(currentSignatures.certificate, sign, isAttachment);
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

  private String getUser() {
    final String user = Authentication.getAuthenticatedUserId();
    if (user != null) {
      return user;
    }
    final Flasher flash = Flash.flash();
    if (flash != null) {
      return flash.getLogin();
    }
    return null;
  }

  private void logVar(final String user, final HistoricVariableUpdateEntity var) {
    if (logger.isLoggable(varLogLevel)) {
      final StringBuilder sb = new StringBuilder();
      sb.append("name:").append(var.getName());
      if (var.getRevision() > 0) {
        // Пока не изменяется в Activiti
        sb.append(", rev:").append(var.getRevision());
      }
      if (user != null) {
        sb.append(", user:").append(user);
      }
      sb.append(", value:").append(var.getValue());
      sb.append(",  pid:").append(var.getProcessInstanceId());
      if (!var.getProcessInstanceId().equals(var.getExecutionId())) {
        // При параллельном исполнении - путь исполнения
        sb.append(",  eid:").append(var.getExecutionId());
      }
      if (var.getActivityInstanceId() != null) {
        // При передачи переменных между процессами
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
  public void addSignatures(final Signatures signatures) {
    assert currentSignatures == null;
    assert signatures != null;
    currentSignatures = signatures;
  }

  public void addSignature(ExecutionId executionId, String varName, byte[] certificate, byte[] sign, boolean isAttachment) {
    variableSignatures.add(new VariableSignature(executionId, varName, certificate, sign, isAttachment));
  }

  private String getWriteableVariableName(final FormPropertyHandler p) {
    final String varName;
    if (p.getVariableName() != null) {
      varName = p.getVariableName();
    } else if (p.getVariableExpression() != null) {
      varName = getSingleVariableName(p.getVariableExpression().getExpressionText());
    } else {
      varName = p.getId();
    }
    return varName;
  }

  //TODO: что тут является однозначным ключём?
  private VariableSignature findVariableSignature(final HistoricVariableUpdate h) {
    final String variableName = h.getVariableName();
    final String taskId = h.getTaskId();
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
      logger.log(Level.WARNING, "can't find signature for variable " + variableName + " in " + variableSignatures);
    }
    return null;
  }

  public Map<String, VariableSnapshot> getVariableSnapshots(ExecutionEntity execution, List<FormPropertyHandler> handlers) {
    final Map<String, VariableSnapshot> map = Maps.newLinkedHashMap();
    final long executionId = Long.parseLong(execution.getId());
    for (final FormPropertyHandler p : handlers) {
      if (!p.isReadable()) {
        continue;
      }
      final String varName;
      if (p.isWritable()) {
        varName = getWriteableVariableName(p);
      } else {
        varName = getReadableVarName(execution, p);
      }
      if (varName != null) {
        final AuditValue auditValue = getAuditSnapshotValue(executionId, varName);
        final VariableSnapshot propertyHistory;
        if (auditValue == null) {
          propertyHistory = VariableSnapshot.withName(varName);
        } else {
          final boolean verified = verifyValue(auditValue, execution, varName);
          propertyHistory = VariableSnapshot.withAudit(varName, auditValue, verified);
        }
        map.put(p.getId(), propertyHistory);
      }
    }
    return ImmutableMap.copyOf(map);
  }

  //TODO: упростить этот ахтунг
  private boolean verifyValue(final AuditValue auditValue, final ExecutionEntity execution, final String varName) {
    try {
      cryptoProvider.toString();
    } catch (ServiceUnavailableException e) {
      logger.info("Отсутсвует поддержка ГОСТ");
      return false;
    }

    boolean verified;
    final byte[] sign = auditValue.getSign();
    final byte[] cert = auditValue.getCert();
    if (sign == null && cert == null) {
      // ok
      verified = false;
    } else if (sign == null || cert == null) {
      logger.log(Level.WARNING, "Нарушение целостности: удалены элементы подписи для переменной " + execution.getId() + ":" + varName);
      verified = false;
    } else {
      try {
        X509Certificate certificate = X509.decode(cert);
        boolean attachment = auditValue.isAttachment();
        Object varValue = execution.getVariable(varName);
        verified = verifyValue(sign, certificate, attachment, varValue);
        if (!verified) {
          String type = varValue == null ? "null" : varValue.getClass().getName();
          logger.log(Level.WARNING, "Нарушение целостности: проверка подписи провалена для переменной " + execution.getId() + ":" + varName + " " + type);
        }
        if (verified && CHECK_INTEGRITY) {
          HistoricVariableUpdateEntity entity = selectById(HistoricVariableUpdateEntity.class, Long.toString(auditValue.getHid()));
          if (entity == null) {
            logger.log(Level.WARNING, "Нарушение целостности: не найдена запись истории " + auditValue.getHid() + " для переменной " + execution.getId() + ":" + varName);
            verified = false;
          } else {
            Object value = entity.getValue();
            if (CHECK_EQUALITY) {
              // либо через проверку эквивалентности значения
              if (varValue instanceof byte[]) {
                verified = value instanceof byte[] && Arrays.equals((byte[]) varValue, (byte[]) value);
              } else {
                verified = Objects.equal(varValue, value);
              }
              if (!verified) {
                logger.log(Level.WARNING, "Нарушение целостности: запись в истории " + auditValue.getHid() + " отличается для переменной " + execution.getId() + ":" + varName);
              }
            } else {
              // либо через проверку данных криптопровайдером. Это надежнее но и дольше.
              verified = verifyValue(sign, certificate, attachment, value);
              if (!verified) {
                logger.log(Level.WARNING, "Нарушение целостности: проверка подписи в истории провалена для переменной " + execution.getId() + ":" + varName);
              }
            }
          }
        }
      } catch (Exception e) {
        logger.log(Level.WARNING, "Ошибка при проверке подписи", e);
        verified = false;
      }
    }
    return verified;
  }

  private boolean verifyValue(byte[] sign, X509Certificate certificate, boolean attachment, Object value) {
    byte[] bytes;
    if (attachment) {
      bytes = getAttachmentContent("" + value);
    } else {
      bytes = VariableToBytes.toBytes(value);
    }
    return cryptoProvider.verifySignature(certificate, new ByteArrayInputStream(bytes), sign);
  }

  private byte[] getAttachmentContent(final String attachmentId) {
    // на самом деле attachmentId имеет формат, из которого надо взять id activiti attachment
    List<String> parts = ImmutableList.copyOf(Splitter.on(":").split(attachmentId).iterator());
    AttachmentEntity attachment = selectById(AttachmentEntity.class, parts.get(0));
    String contentId = attachment.getContentId();
    if (contentId == null) {
      return new byte[0];
    }
    ByteArrayEntity byteArray = selectById(ByteArrayEntity.class, contentId);
    return byteArray.getBytes();
  }

  //TODO: упростить это!
  private String getReadableVarName(final ExecutionEntity execution, final FormPropertyHandler p) {
    final String varName;
    if (p.getVariableName() != null || p.getVariableExpression() == null) {
      final String nameOrId = p.getVariableName() != null ? p.getVariableName() : p.getId();
      if (execution.hasVariable(nameOrId)) {
        varName = nameOrId;
      } else if (p.getDefaultExpression() != null) {
        varName = getSingleVariableName(p.getDefaultExpression().getExpressionText());
      } else {
        varName = null;
      }
    } else {
      varName = getSingleVariableName(p.getVariableExpression().getExpressionText());
    }
    return varName;
  }

  public AuditValue getAuditSnapshotValue(long executionId, String varName) {
    EntityManager em = getEntityManagerSession().getEntityManager();
    AuditSnapshot snapshot = em.find(AuditSnapshot.class, new AuditId(executionId, varName));
    if (snapshot != null) {
      return snapshot.getValue();
    }
    logger.log(Level.FINE, "no history for " + executionId + ":" + varName);
    return null;
  }

  private String getSingleVariableName(String expression) {
    final Tree tree = new Builder().build(expression);
    if (Iterables.isEmpty(tree.getFunctionNodes())) {
      if (Iterables.size(tree.getIdentifierNodes()) == 1) {
        final IdentifierNode node = Iterables.getOnlyElement(tree.getIdentifierNodes());
        return node.getName();
      }
    }
    return null;
  }

  public AuditValue getTempAudit(long eid, String variableName) {
    for (AuditValue audit : new ArrayList<AuditValue>(insertAudits)) {
      final VarPath varPath = (VarPath) audit.getDetail();
      final HistoricVariableUpdate h = varPath.var;
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

  public boolean addSignaturesBySmevFileValue(String processDefinitionId, String propertyId, FileValue fileValue) {
    if (!(fileValue instanceof SmevFileValue)) {
      return false;
    }
    SmevFileValue smev = (SmevFileValue) fileValue;
    Enclosure enclosure = smev.getEnclosure();
    if (enclosure.signature == null) {
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

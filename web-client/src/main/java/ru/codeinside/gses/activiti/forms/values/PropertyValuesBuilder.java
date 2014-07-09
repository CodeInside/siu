/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.values;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.form.StartFormVariableScope;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.juel.IdentifierNode;
import org.activiti.engine.impl.juel.Tree;
import org.activiti.engine.impl.persistence.entity.AttachmentEntity;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableUpdateEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.glassfish.osgicdi.ServiceUnavailableException;
import ru.codeinside.adm.database.AuditId;
import ru.codeinside.adm.database.AuditSnapshot;
import ru.codeinside.adm.database.AuditValue;
import ru.codeinside.adm.database.FieldBuffer;
import ru.codeinside.adm.database.FormBuffer;
import ru.codeinside.gses.activiti.VariableToBytes;
import ru.codeinside.gses.activiti.forms.api.definitions.BlockNode;
import ru.codeinside.gses.activiti.forms.api.definitions.EnclosureNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.api.definitions.ToggleNode;
import ru.codeinside.gses.activiti.forms.api.values.FormValue;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSession;
import ru.codeinside.gses.activiti.history.VariableSnapshot;
import ru.codeinside.gses.cert.NameParts;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gses.service.CryptoProviderAware;
import ru.codeinside.gses.service.Some;
import ru.codeinside.gws.api.CryptoProvider;

import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: аудит
// TODO: как строится archiveValues для истории ?!
// TODO: переключатели тоже должны клонироваться во втором проходе, когда собрано основное дерево!
final public class PropertyValuesBuilder {

  /**
   * Проверять ли целостность, сверяя значения аудита с текущими значением переменных.
   */
  private static final boolean CHECK_INTEGRITY = true;
  /**
   * Проверять ли целостность через equals при true либо через криптопровайдера при false.
   */
  private static final boolean CHECK_EQUALITY = true;
  final TaskEntity task;
  final HistoricDbSqlSession session;
  private final Logger logger = Logger.getLogger(getClass().getName());
  private final String taskId;
  private final ExecutionEntity execution;
  private final Map<String, String> archiveValues;
  private final boolean archiveMode;
  private final FormBuffer formBuffer;
  private final Map<String, ValueBuilder> valuesMap = new HashMap<String, ValueBuilder>();
  private CryptoProvider cryptoProvider;

  public PropertyValuesBuilder(String taskId, ExecutionEntity execution, Map<String, String> archiveValues) {
    this.taskId = taskId;
    this.execution = execution;
    this.archiveValues = archiveValues;
    archiveMode = execution == null && archiveValues != null;
    if (this.taskId != null) {
      CommandContext commandContext = Context.getCommandContext();
      EntityManager em = commandContext.getSession(EntityManagerSession.class).getEntityManager();
      List<FormBuffer> formBuffers = em.createQuery("select fb from FormBuffer fb where fb.taskId = :taskId", FormBuffer.class)
        .setParameter("taskId", this.taskId).getResultList();
      if (!formBuffers.isEmpty()) {
        formBuffer = formBuffers.get(0);
      } else {
        formBuffer = null;
      }
      task = commandContext.getTaskManager().findTaskById(taskId);
      session = (HistoricDbSqlSession) commandContext.getSession(DbSqlSession.class);
      if (Context.getProcessEngineConfiguration() instanceof CryptoProviderAware) {
        cryptoProvider = ((CryptoProviderAware) Context.getProcessEngineConfiguration()).getCryptoProviderProxy();
      }
    } else {
      formBuffer = null;
      task = null;
      session = null;
    }
  }

  public FormValue build(PropertyTree definition, Task task, ProcessDefinition processDefinition) {
    ValueBuilder valueBuilder = new ValueBuilder();
    valueBuilder.valueBuilders = new ArrayList<ValueBuilder>(definition.getNodes().length);
    for (PropertyNode node : definition.getNodes()) {
      build(valueBuilder, node, "");
    }
    return valueBuilder.toValues(task, processDefinition, definition, archiveMode);
  }

  public List<PropertyValue<?>> block(BlockNode definition, String path) {
    ValueBuilder valueBuilder = new ValueBuilder();
    valueBuilder.id = definition.getId();
    valueBuilder.valueBuilders = new ArrayList<ValueBuilder>(definition.getNodes().length);
    valuesMap.put(valueBuilder.id, valueBuilder);
    for (PropertyNode node : definition.getNodes()) {
      build(valueBuilder, node, path);
    }
    return valueBuilder.toCollection();
  }

  void build(ValueBuilder valueBuilderCollection, PropertyNode node, String suffix) {
    if (!node.isFieldReadable()) {
      return;
    }
    if (node instanceof ToggleNode) {
      return;
    }
    // 1. формируется id
    // 2. меняется variableName
    String id = node.getId() + suffix;

    if (valuesMap.containsKey(id)) {
      logger.info("duplicate value " + id);
      return;
    }

    // TODO: имя переменной должно быть согласовано с аудитом!
    String variableName = node.getVariableName() == null ? null : node.getVariableName() + suffix;

    Object modelValue = null;
    boolean useBuffer = false;
    Some<String> userVariable = Some.empty();
    if (archiveMode) {
      Object archiveValue = null;
      if (variableName != null || node.getVariableExpression() == null) {
        String varName = variableName != null ? variableName : id;
        if (archiveValues.containsKey(varName)) {
          archiveValue = archiveValues.get(varName);
          userVariable = Some.of(varName);
        } else if (node.getDefaultExpression() != null) {
          HistoryScope tracker = new HistoryScope(archiveValues);
          archiveValue = node.getDefaultExpression().getValue(tracker);
          userVariable = tracker.getUsedVariable();
        }
      } else {
        HistoryScope tracker = new HistoryScope(archiveValues);
        archiveValue = node.getVariableExpression().getValue(tracker);
        userVariable = tracker.getUsedVariable();
      }
      modelValue = node.getVariableType().convertFormValueToModelValue(archiveValue, node.getPattern(), node.getParams());
    } else {
      if (formBuffer != null) {
        for (FieldBuffer fieldBuffer : formBuffer.getFields()) {
          if (id.equals(fieldBuffer.getFieldId())) {
            useBuffer = true;
            modelValue = node.getVariableType().convertBufferToModelValue(fieldBuffer);
            break;
          }
        }
      }
      if (!useBuffer) {
        Object formValue = null;
        boolean readable = node.isVarReadable();
        if (!readable) {
          logger.info("skip variable for " + id + " by #read=false");
        }
        if (readable && execution != null) {
          if (variableName != null || node.getVariableExpression() == null) {
            String varName = variableName != null ? variableName : id;
            if (execution.hasVariable(varName)) {
              formValue = execution.getVariable(varName);
              userVariable = Some.of(varName);
            } else if (node.getDefaultExpression() != null) {
              VariableTracker tracker = new VariableTracker(execution);
              formValue = node.getDefaultExpression().getValue(tracker);
              userVariable = tracker.getUsedVariable();
            }
          } else {
            VariableTracker tracker = new VariableTracker(execution);
            formValue = node.getVariableExpression().getValue(tracker);
            userVariable = tracker.getUsedVariable();
          }
        } else {
          if (node.getDefaultExpression() != null) {
            formValue = node.getDefaultExpression().getValue(StartFormVariableScope.getSharedInstance());
          }
        }
        modelValue = node.getVariableType().convertFormValueToModelValue(formValue, node.getPattern(), node.getParams());
      }
    }

    ValueBuilder valueBuilder = new ValueBuilder();
    valuesMap.put(id, valueBuilder);
    valueBuilder.id = id;
    valueBuilder.value = modelValue;
    valueBuilder.node = node;
    valueBuilderCollection.valueBuilders.add(valueBuilder);

    //TODO для истории значения аудита пока не вытаскиваются
    if (userVariable.isPresent() && userVariable.get() != null && !archiveMode) {
      String varName = userVariable.get();
      long executionId = Long.parseLong(execution.getId());
      AuditValue auditValue = getAuditSnapshotValue(executionId, varName);
      if (auditValue != null) {
        AuditBuilder info = new AuditBuilder();
        info.login = auditValue.getLogin();
        info.verified = verifyValue(auditValue, execution, varName);
        if (auditValue.getSign() != null || auditValue.getCert() != null) {
          NameParts nameParts = X509.getSubjectParts(auditValue.getCert());
          info.ownerName = nameParts.getCommonName();
          info.organizationName = nameParts.getOrganization();
        }
        valueBuilder.auditBuilder = info;
      }
    }

    if (node instanceof BlockNode) {
      if (modelValue != null) {
        long n = (Long) modelValue;
        BlockNode block = (BlockNode) node;
        long items = n < block.getMinimum() ? block.getMinimum() : (n <= block.getMaximum() ? n : block.getMaximum());
        // коррекция значения по описателю блока
        if (n != items) {
          valueBuilder.value = items;
          if (valueBuilder.auditBuilder != null) {
            valueBuilder.auditBuilder.verified = false;
          }
        }
        valueBuilder.valueBuilders = new ArrayList<ValueBuilder>((int) items);
        for (long i = 1; i <= items; i++) {
          ValueBuilder clone = new ValueBuilder();
          clone.valueBuilders = new ArrayList<ValueBuilder>(block.getNodes().length);
          valueBuilder.valueBuilders.add(clone);
          for (PropertyNode child : block.getNodes()) {
            build(clone, child, suffix + '_' + i);
          }
        }
      }
    } else if (node instanceof EnclosureNode) {
      String attachments = (String) modelValue;
      EnclosureNode enclosureNode = (EnclosureNode) node;
      Iterable<String> varNamesForRefToAttachment = Splitter.on(';').omitEmptyStrings().trimResults().split(attachments);
      for (String varName : varNamesForRefToAttachment) {
        PropertyNode child = enclosureNode.createEnclosure(varName);
        build(valueBuilderCollection, child, suffix);
      }
    }
  }

  //TODO: упростить этот ахтунг
  private boolean verifyValue(final AuditValue auditValue, final ExecutionEntity execution, final String varName) {
    if (cryptoProvider == null) {
      return false;
    }
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
          HistoricVariableUpdateEntity entity = session.selectById(HistoricVariableUpdateEntity.class, Long.toString(auditValue.getHid()));
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
    AttachmentEntity attachment = session.selectById(AttachmentEntity.class, parts.get(0));
    String contentId = attachment.getContentId();
    if (contentId == null) {
      return new byte[0];
    }
    ByteArrayEntity byteArray = session.selectById(ByteArrayEntity.class, contentId);
    return byteArray.getBytes();
  }

  private AuditValue getAuditSnapshotValue(long executionId, String varName) {
    EntityManager em = Context.getCommandContext().getSession(EntityManagerSession.class).getEntityManager();
    AuditSnapshot snapshot = em.find(AuditSnapshot.class, new AuditId(executionId, varName));
    if (snapshot != null) {
      return snapshot.getValue();
    }
    logger.log(Level.FINE, "no history for " + executionId + ":" + varName);
    return null;
  }

}

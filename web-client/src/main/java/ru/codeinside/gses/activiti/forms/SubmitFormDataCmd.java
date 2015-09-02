/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import com.google.common.base.Splitter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.ClientRequestEntity;
import ru.codeinside.gses.activiti.forms.api.definitions.BlockNode;
import ru.codeinside.gses.activiti.forms.api.definitions.EnclosureNode;
import ru.codeinside.gses.activiti.forms.api.definitions.NullAction;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyType;
import ru.codeinside.gses.activiti.forms.api.definitions.ToggleNode;
import ru.codeinside.gses.activiti.forms.types.AttachmentType;
import ru.codeinside.gses.activiti.forms.values.VariableTracker;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSession;
import ru.codeinside.gses.beans.Smev;
import ru.codeinside.gses.beans.filevalues.SmevFileValue;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.service.Some;
import ru.codeinside.gses.webui.form.AttachmentConverter;
import ru.codeinside.gses.webui.form.DataAccumulator;
import ru.codeinside.gses.webui.form.FormOvSignatureSeq;
import ru.codeinside.gses.webui.form.ProtocolUtils;
import ru.codeinside.gses.webui.form.SignatureType;
import ru.codeinside.gses.webui.form.TmpAttachment;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.Packet;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class SubmitFormDataCmd implements Command<Void> {
  final PropertyTree propertyTree;
  final VariableScope variableScope;
  final DataAccumulator accumulator;
  final AttachmentConverter attachmentConverter;
  final Map<String, Object> properties;
  final Map<SignatureType, Signatures> signatures;
  final Map<String, Boolean> requiredMap = new HashMap<String, Boolean>();
  final Logger logger = Logger.getLogger(getClass().getName());
  final String tmpAttachmentIdPattern = "^(?i)[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}" + AttachmentType.SUFFIX + "$";

  public SubmitFormDataCmd(PropertyTree propertyTree, VariableScope variableScope,
                           Map<String, Object> properties,
                           Map<SignatureType, Signatures> signatures,
                           AttachmentConverter attachmentConverter,
                           DataAccumulator accumulator) {
    this.propertyTree = propertyTree;
    this.variableScope = variableScope;
    this.properties = new HashMap<String, Object>(properties);
    this.signatures = signatures;
    this.attachmentConverter = attachmentConverter;
    this.accumulator = accumulator;
  }

  @Override
  public Void execute(final CommandContext commandContext) {
    // TODO: нужно ли для СМЭВ переключатели видимости обрабатывать?

    // засейвить ServerResponse и ClientRequest
    saveServerResponse();
    saveClientRequest();

    restoreOriginalTmpAttachments();

    // Замена требований.
    for (final PropertyNode node : propertyTree.getNodes()) {
      if (node.getPropertyType() == PropertyType.TOGGLE) {
        ToggleNode toggle = (ToggleNode) node;
        boolean required = !toggle.getToggleTo();
        Object valueObject = properties.get(toggle.getToggler().getId());
        String value = valueObject == null ? null : valueObject.toString();
        if (toggle.getToggleValue().equals(value)) {
          required = !required;
        }
        for (PropertyNode target : toggle.getToggleNodes()) {
          if (required != target.isFieldRequired()) {
            requiredMap.put(target.getId(), required);
          }
        }
      }
    }

    for (PropertyNode node : propertyTree.getNodes()) {
      submit(node, "", commandContext);
    }

    for (final String propertyId : properties.keySet()) {
      PropertyContext.withWritePath(propertyId, new PropertyContext.Action() {
        @Override
        public void inContext() {
          Object originalValue = properties.get(propertyId);
          Object modelValue = attachmentConverter.convertAttachment(commandContext, originalValue);
          variableScope.setVariable(propertyId, modelValue);
          addSmevSignature(commandContext, originalValue, propertyId);
        }
      });
    }

    if (signatures != null) {
      HistoricDbSqlSession session = (HistoricDbSqlSession) commandContext.getSession(DbSqlSession.class);
      session.addSignatures(signatures);
      session.linkPropertyValuesWithSignatures();
    }

    return null;
  }

  /**
   * Заменить айдишники временных вложений на сами вложения
   */
  private void restoreOriginalTmpAttachments() {
    for (Map.Entry<String, Object> entry : properties.entrySet()) {
      String value = String.valueOf(entry.getValue());
      if (Pattern.matches(tmpAttachmentIdPattern, value)) {
        TmpAttachment attachment = accumulator.getAttachment(value.substring(0, value.length() - AttachmentType.SUFFIX.length()));
        if (attachment != null) {
          entry.setValue(attachment);
        }
      }
    }
  }

  private void saveClientRequest() {
    if (accumulator == null || accumulator.getClientRequest() == null) {
      return;
    }

    ClientRequestEntity clientRequestEntity = createClientRequestEntity(
        accumulator.getServiceName(),
        accumulator.getClientRequest()
    );

    long requestId = AdminServiceProvider.get().saveClientRequestEntity(clientRequestEntity);
    String key = accumulator.getServiceName() + FormOvSignatureSeq.REQUEST_ID;
    properties.put(key, requestId);
  }

  private void saveServerResponse() {
    if (accumulator == null || accumulator.getServerResponse() == null) {
      return;
    }

    long responseId = Fn.withEngine(
            new ProtocolUtils.CreateAndSaveServiceResponseEntity(),
            accumulator.getServerResponse(),
            accumulator.getTaskId(),
            accumulator.getUsedEnclosures()
    );
    properties.put(Smev.SERVER_RESPONSE_ID, responseId);
  }

  private ClientRequestEntity createClientRequestEntity(String serviceName, ClientRequest request) {
    final ClientRequestEntity entity = new ClientRequestEntity();
    entity.name = serviceName;
    if (request.action != null) {
      entity.action = request.action.getLocalPart();
      entity.actionNs = request.action.getNamespaceURI();
    }
    if (request.port != null) {
      entity.port = request.port.getLocalPart();
      entity.portNs = request.port.getNamespaceURI();
    }
    if (request.service != null) {
      entity.service = request.service.getLocalPart();
      entity.serviceNs = request.service.getNamespaceURI();
    }

    if (request.appData != null) {
      entity.appData = request.appData;
    }
    entity.portAddress = request.portAddress;
    entity.requestMessage = request.requestMessage;

    final Packet packet = request.packet;
    entity.gservice = packet.typeCode.name();
    entity.status = packet.status.name();
    entity.date = new Date();
    entity.exchangeType = packet.exchangeType;
    entity.requestIdRef = packet.requestIdRef;
    entity.originRequestIdRef = packet.originRequestIdRef;
    entity.serviceCode = packet.serviceCode;
    entity.caseNumber = packet.caseNumber;
    entity.testMsg = packet.testMsg;
    entity.signRequired = request.signRequired;
    entity.enclosureDescriptor = request.enclosureDescriptor;
    return entity;
  }

  void submit(final PropertyNode node, final String suffix, final CommandContext commandContext) {
    final PropertyType type = node.getPropertyType();
    if (type == PropertyType.TOGGLE || type == PropertyType.VISIBILITY_TOGGLE) {
      return;
    }
    final String id = node.getId() + suffix;

    Object blockValue = null;
    if (type == PropertyType.BLOCK) {
      // упрощение идентификации блоков, когда есть заначние без префикса '+'
      if (!properties.containsKey(id) && properties.containsKey(id.substring(1))) {
        properties.put(id, properties.remove(id.substring(1)));
      }
      if (properties.containsKey(id)) {
        blockValue = properties.get(id);
      } else {
        Expression expression = node.getDefaultExpression();
        if (expression != null) {
          blockValue = expression.getExpressionText();
        }
      }

    }

    PropertyContext.withWritePath(id, new PropertyContext.Action() {
      @Override
      public void inContext() {
        submitFormNode(node, id, commandContext, suffix);
      }
    });

    if (type == PropertyType.BLOCK) {
      int size = 0;
      if (blockValue != null) {
        try {
          size = Integer.parseInt(blockValue.toString());
        } catch (NumberFormatException e) {
          throw new ActivitiException("Size = '" + blockValue + "' for " + id + " is not integer!");
        }
      }
      BlockNode block = (BlockNode) node;
      if (size < block.getMinimum()) {
        throw new ActivitiException("Size = " + size + " for " + id + " is less then minimum " + block.getMinimum());
      }
      if (size > block.getMaximum()) {
        throw new ActivitiException("Size = " + size + " for " + id + " is greater then minimum " + block.getMaximum());
      }
      for (int i = 1; i <= size; i++) {
        for (PropertyNode child : block.getNodes()) {
          submit(child, suffix + "_" + i, commandContext);
        }
      }
    }

    if (node instanceof EnclosureNode) {
      String attachments = (String) variableScope.getVariable(id);
      if (attachments != null) {
        EnclosureNode enclosureNode = (EnclosureNode) node;
        Iterable<String> varNamesForRefToAttachment = Splitter.on(';').omitEmptyStrings().trimResults().split(attachments);
        for (String varName : varNamesForRefToAttachment) {
          PropertyNode child = enclosureNode.createEnclosure(varName);
          submit(child, suffix, commandContext);
        }
      }
    }

  }


  public void submitFormNode(PropertyNode node, String id, CommandContext commandContext, String suffix) {

    if (!node.isFieldWritable()) {
      if (properties.containsKey(id)) {
        throw new ActivitiException("form property '" + id + "' is not writable");
      }
    }

    boolean required;
    if (requiredMap.containsKey(id)) {
      required = requiredMap.get(id);
    } else {
      required = node.isFieldRequired() && node.isVisible();
    }

    if (required && !properties.containsKey(id) && node.getDefaultExpression() == null) {
      throw new ActivitiException("form property '" + id + "' is required");
    }

    if (!node.isVarWritable()) {
      logger.info("skip " + id + " by #write=false");
      return;
    }

    Object modelValue = null;
    if (properties.containsKey(id)) {
      Object propertyValue = properties.remove(id);
      modelValue = node.getVariableType().convertFormValueToModelValue(propertyValue, node.getPattern(), node.getParams());
    } else if (node.getDefaultExpression() != null) {
      Object expressionValue = node.getDefaultExpression().getValue(variableScope);
      if (expressionValue != null) {
        modelValue = node.getVariableType().convertFormValueToModelValue(expressionValue, node.getPattern(), node.getParams());
      } else if (required) {
        throw new ActivitiException("form property '" + id + "' is required");
      }
    }

    if (modelValue != null) {
      Object originalValue = modelValue;
      modelValue = attachmentConverter.convertAttachment(commandContext, modelValue);
      // TODO: разделить на трекер чтения и трекер записи!
      VariableTracker tracker = new VariableTracker(variableScope);
      if (node.getVariableName() != null) {
        tracker.setVariable(node.getVariableName() + suffix, modelValue);
      } else if (node.getVariableExpression() != null) {
        node.getVariableExpression().setValue(modelValue, tracker);
      } else {
        tracker.setVariable(id, modelValue);
      }
      Some<String> usedVariable = tracker.getUsedVariable();
      if (usedVariable.isPresent() && usedVariable.get() != null) {
        String varName = usedVariable.get();
        addSmevSignature(commandContext, originalValue, varName);
      }
    } else {
      applyNullValue(node, id, required);
    }
  }

  private void addSmevSignature(CommandContext commandContext, Object originalValue, String varName) {
    if (originalValue instanceof SmevFileValue &&
        variableScope instanceof ExecutionEntity) {
      HistoricDbSqlSession session = (HistoricDbSqlSession) commandContext.getSession(DbSqlSession.class);
      session.addSignaturesBySmevFileValue(((ExecutionEntity)variableScope).getProcessDefinitionId(), varName, (SmevFileValue) originalValue);
    }
  }

  //TODO: varTracker!
  private void applyNullValue(PropertyNode node, String id, boolean required) {
    if (!node.isFieldWritable() || NullAction.skip == node.getNullAction()) {
      return;
    }
    if (required) {
      throw new ActivitiException("Значение для свойства '" + id + "' обязательно!");
    }
    if (NullAction.set == node.getNullAction()) {
      if (node.getVariableName() != null) {
        variableScope.setVariable(node.getVariableName(), null);
      } else if (node.getVariableExpression() != null) {
        node.getVariableExpression().setValue(null, variableScope);
      } else {
        variableScope.setVariable(id, null);
      }
    } else if (NullAction.remove == node.getNullAction()) {
      if (node.getVariableName() != null) {
        variableScope.removeVariable(node.getVariableName());
      } else if (node.getVariableExpression() != null) {
        // в выражении не можем удалить, ставим NULL
        node.getVariableExpression().setValue(null, variableScope);
      } else {
        variableScope.removeVariable(id);
      }
    } else {
      throw new IllegalStateException("Не известное поведение для null");
    }
  }

}

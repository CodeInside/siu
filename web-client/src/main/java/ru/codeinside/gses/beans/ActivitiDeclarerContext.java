/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import org.activiti.engine.impl.ServiceImpl;
import ru.codeinside.adm.database.SmevChain;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.GetFormValueCommand;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyType;
import ru.codeinside.gses.activiti.forms.api.definitions.ToggleNode;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;
import ru.codeinside.gses.activiti.forms.api.values.BlockValue;
import ru.codeinside.gses.activiti.forms.api.values.FormValue;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;
import ru.codeinside.gses.activiti.forms.types.AttachmentType;
import ru.codeinside.gses.beans.filevalues.SmevFileValue;
import ru.codeinside.gses.service.BidID;
import ru.codeinside.gses.service.DeclarantServiceProvider;
import ru.codeinside.gses.webui.Configurator;
import ru.codeinside.gws.api.DeclarerContext;
import ru.codeinside.gws.api.Enclosure;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class ActivitiDeclarerContext implements DeclarerContext {

  final FormValue formValue;
  final private String componentName;
  final private SmevChain smevChain;
  final private String processDefinitionId;
  final private Logger logger = Logger.getLogger(getClass().getName());
  final private Map<String, Object> formPropertyValues = new LinkedHashMap<String, Object>();
  final private Map<String, ToggleNode> toggles;
  final private Map<String, Boolean> requiredFlags = new HashMap<String, Boolean>();

  final private AtomicLong gid;
  final private AtomicLong bidId = new AtomicLong(-1L);

  public ActivitiDeclarerContext(SmevChain smevChain,
                                 AtomicLong gid, String processDefinitionId, String componentName) {
    this.smevChain = smevChain;
    this.processDefinitionId = processDefinitionId;
    this.gid = gid;
    this.componentName = componentName;

    GetFormValueCommand cmd = new GetFormValueCommand(FormID.byProcessDefinitionId(processDefinitionId), null);
    formValue = ((ServiceImpl) Configurator.get().getFormService()).getCommandExecutor().execute(cmd);
    toggles = initializeRequireFlags(formValue.getFormDefinition());
  }

  @Override
  public void setValue(String propertyId, Object value) {
    if (value != null) {
      formPropertyValues.put(propertyId, value);
    } else {
      formPropertyValues.remove(propertyId);
    }
    processRequireFlag(propertyId);
  }

  @Override
  public boolean isRequired(String propertyId) {
    final PropertyNode property = getWritableNodeById(propertyId);
    if (requiredFlags.containsKey(propertyId)) {
      return requiredFlags.get(propertyId);
    }
    return property != null && property.isFieldRequired();
  }

  @Override
  public String getType(String propertyId) {
    final PropertyNode property = getWritableNodeById(propertyId);
    if (property == null) {
      return "";
    }
    VariableType variableType = property.getVariableType();
    return variableType == null ? "" : variableType.getName();
  }

  @Override
  public Set<String> getPropertyNames() {
    final Set<String> result = new LinkedHashSet<String>();
    collectId(formValue.getPropertyValues(), result);
    return result;
  }

  private void collectId(List<PropertyValue<?>> propertyValues, Set<String> result) {
    for (PropertyValue<?> pv : propertyValues) {
      if (pv.getNode().isFieldWritable()) {
        result.add(pv.getId());
        if (pv instanceof BlockValue) {
          BlockValue blockValue = (BlockValue) pv;
          for (List<PropertyValue<?>> clone : blockValue.getClones()) {
            collectId(clone, result);
          }
        }
      }
    }
  }

  @Override
  public String declare(String tag, String declarant) {
    if (bidId.compareAndSet(-1L, 0L)) {
      BidID bidID = DeclarantServiceProvider.get().smevDeclare(
        smevChain, componentName, Configurator.get(),
        processDefinitionId, formPropertyValues, declarant, tag
      );
      bidId.set(bidID.bidId);
      gid.compareAndSet(0L, bidID.bidId);
      return Long.toString(bidID.bidId);
    } else {
      // TODO: добавить в API тип исключений
      throw new IllegalStateException("Заявление для данного контекста уже подано");
    }
  }

  @Override
  public String declare() {
    return declare("", "smev");
  }

  public String getBidId() {
    long id = bidId.get();
    return (id == 0L || id == 1L) ? null : Long.toString(id);
  }

  private PropertyNode getWritableNodeById(String propertyId) {
    PropertyNode node = findById(formValue.getPropertyValues(), propertyId);
    if (node == null) {
      logger.warning("Не найдено записываемое свойство '" + propertyId + "'");
    }
    return null;
  }

  private PropertyNode findById(List<PropertyValue<?>> propertyValues, String id) {
    PropertyNode node = null;
    for (PropertyValue<?> pv : propertyValues) {
      if (pv.getId().equals(id)) {
        if (pv.getNode().isFieldWritable()) {
          node = pv.getNode();
        }
        break;
      }
      if (pv instanceof BlockValue) {
        BlockValue blockValue = (BlockValue) pv;
        for (List<PropertyValue<?>> clone : blockValue.getClones()) {
          node = findById(clone, id);
          if (node != null) {
            break;
          }
        }
        if (node != null) {
          break;
        }
      }
    }
    return node;
  }


  @Override
  public boolean isEnclosure(final String propertyId) {
    final PropertyNode fp = getWritableNodeById(propertyId);
    if (fp == null) {
      return false;
    }
    VariableType variableType = fp.getVariableType();
    if (variableType == null) {
      return false;
    }
    return variableType.getClass() == AttachmentType.class;
  }

  @Override
  public void addEnclosure(final String propertyId, final Enclosure enclosure) {
    if (checkDigest(enclosure)) {
      formPropertyValues.put(propertyId, new SmevFileValue(enclosure));
      processRequireFlag(propertyId);
    }
  }

  private boolean checkDigest(Enclosure enclosure) {
    if (enclosure.digest != null && enclosure.digest.length > 0) {
      if (!Arrays.equals(Activiti.createDigest(enclosure.content), enclosure.digest)) {
        logger.warning("Ошибка в свёртке GOST3411 для вложения '" + enclosure.zipPath + "'");
        return false; //TODO: исключительная ситуация?
      }
    }
    return true;
  }

  private Map<String, ToggleNode> initializeRequireFlags(final PropertyTree nodeMap) {
    final Map<String, ToggleNode> toggles = new HashMap<String, ToggleNode>();
    for (final PropertyNode node : nodeMap.getNodes()) {
      if (node.getPropertyType() == PropertyType.TOGGLE) {
        final ToggleNode toggle = (ToggleNode) node;
        toggles.put(toggle.getToggler().getId(), toggle);
        processRequireFlag(toggle);
      }
    }
    return toggles;
  }

  private void processRequireFlag(final ToggleNode toggle) {
    final String value;
    if (formPropertyValues.containsKey(toggle.getId())) {
      value = null;
    } else {
      value = formPropertyValues.get(toggle.getId()).toString();
    }
    boolean required = !toggle.getToggleTo();
    if (toggle.getToggleValue().equals(value)) {
      required = !required;
    }
    for (PropertyNode it : toggle.getToggleNodes()) {
      requiredFlags.put(it.getId(), required);
    }
  }

  private void processRequireFlag(final String propertyId) {
    final ToggleNode toggle = toggles.get(propertyId);
    if (toggle != null) {
      processRequireFlag(toggle);
    }
  }

  public Object getVariable(final String name) {
    return formPropertyValues.get(name);
  }


}
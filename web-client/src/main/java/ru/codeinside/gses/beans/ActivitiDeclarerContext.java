/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.StartFormData;
import ru.codeinside.adm.database.SmevChain;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.activiti.ActivitiFormProperties;
import ru.codeinside.gses.activiti.DelegateFormType;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.forms.PropertyNode;
import ru.codeinside.gses.activiti.forms.PropertyTree;
import ru.codeinside.gses.activiti.forms.PropertyTreeProvider;
import ru.codeinside.gses.activiti.forms.PropertyType;
import ru.codeinside.gses.activiti.forms.ToggleNode;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class ActivitiDeclarerContext implements DeclarerContext {

  final private StartFormData formData;
  final private String componentName;
  final private SmevChain smevChain;
  final private String processDefinitionId;
  final private Logger logger = Logger.getLogger(getClass().getName());
  final private Map<String, String> formPropertyValues = new LinkedHashMap<String, String>();
  final private Map<String, FileValue> files = new LinkedHashMap<String, FileValue>();
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
    formData = Configurator.get().getFormService().getStartFormData(processDefinitionId);
    toggles = initializeRequireFlags(((PropertyTreeProvider) formData).getPropertyTree());
  }

  @Override
  public void setValue(final String propertyId, final Object value) {
    String formValue = null;
    boolean hasValue = false;
    //TODO: обработать другие типы данных?
    for (FormProperty formProperty : formData.getFormProperties()) {
      if (!formProperty.getId().equals(propertyId)) {
        continue;
      }
      FormType type = formProperty.getType();
      if (type != null && "json".equals(type.getName())) {
        DelegateFormType dft = (DelegateFormType) formProperty.getType();
        formValue = dft.convertModelValueToFormValue(value);
        hasValue = true;
      }
      break;
    }
    if (!hasValue) {
      formValue = value == null ? null : value.toString();
    }
    if (formValue != null) {
      formPropertyValues.put(propertyId, formValue);
    } else {
      formPropertyValues.remove(propertyId);
    }
    files.remove(propertyId);
    processRequireFlag(propertyId);
  }

  @Override
  public boolean isRequired(String propertyId) {
    final FormProperty property = getWritablePropertyById(propertyId);
    if (requiredFlags.containsKey(propertyId)) {
      return requiredFlags.get(propertyId);
    }
    return property != null && property.isRequired();
  }

  @Override
  public String getType(String propertyId) {
    final FormProperty property = getWritablePropertyById(propertyId);
    if (property == null) {
      return "";
    }
    return property.getType().getName();
  }

  @Override
  public Set<String> getPropertyNames() {
    final Set<String> result = new LinkedHashSet<String>();
    for (final FormProperty fp : formData.getFormProperties()) {
      if (fp.isWritable()) {
        result.add(fp.getId());
      }
    }
    return result;
  }

  @Override
  public String declare(String tag, String declarant) {
    if (bidId.compareAndSet(-1L, 0L)) {
      BidID bidID = DeclarantServiceProvider.get().smevDeclare(
        smevChain, componentName, Configurator.get(),
        processDefinitionId, createDeclaredProperties(), declarant, tag
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

  private FormProperty getWritablePropertyById(final String propertyId) {
    for (final FormProperty fp : formData.getFormProperties()) {
      if (fp.isWritable() && fp.getId().equals(propertyId)) {
        return fp;
      }
    }
    logger.warning("Не найдено записываемое свойство '" + propertyId + "'");
    return null;
  }

  @Override
  public boolean isEnclosure(final String propertyId) {
    final FormProperty fp = getWritablePropertyById(propertyId);
    return fp != null && AttachmentFFT.isAttachment(fp);
  }

  @Override
  public void addEnclosure(final String propertyId, final Enclosure enclosure) {
    if (checkDigest(enclosure)) {
      files.put(propertyId, new SmevFileValue(enclosure));
      formPropertyValues.remove(propertyId);
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
    if (files.containsKey(toggle.getId())) {
      value = null;
    } else {
      value = formPropertyValues.get(toggle.getId());
    }
    boolean required = !toggle.getToggleTo();
    if (toggle.getToggleValue().equals(value)) {
      required = !required;
    }
    for (final PropertyNode it : toggle.getToggleNodes()) {
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


  // ---- internals ----

  private ActivitiFormProperties createDeclaredProperties() {
    // При создании ожидается смешанная карта значений и вложений
    Map<String, String> mixedValues = new LinkedHashMap<String, String>();
    mixedValues.putAll(formPropertyValues);
    for (String id : files.keySet()) {
      mixedValues.put(id, "вложение");
    }

    return ActivitiFormProperties.createWithFiles(mixedValues, files);
  }

}
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import ru.codeinside.gses.activiti.ftarchive.StringFFT;
import ru.codeinside.gses.activiti.history.VariableFormData;
import ru.codeinside.gses.activiti.history.VariableSnapshot;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;

import java.io.Serializable;
import java.util.Map;

/**
 * Какая то обёртка.
 * <p/>
 * Ответственность размыта и не понятная.
 */
final public class FormDecorator implements Serializable {

  private static final long serialVersionUID = 1L;

  final public FormID id;
  final public VariableFormData variableFormData;

  public FormDecorator(final FormID id, final VariableFormData variableFormData) {
    this.id = id;
    this.variableFormData = variableFormData;
  }

  // Для хранения в UI (без связи с переменными)
  public FormDecorator toSimple() {
    return new FormDecorator(id, null);
  }

  public ImmutableMap<String, FormProperty> getFormProperties() {
    final ImmutableMap.Builder<String, FormProperty> properties = ImmutableMap.builder();
    for (final FormProperty property : variableFormData.formData.getFormProperties()) {
      properties.put(property.getId(), property);
    }
    return properties.build();
  }


  public Map<String, FormProperty> getGeneral() {
    return Maps.filterValues(getFormProperties(), Predicates.not(new IsPostProcessedPredicate()));
  }

  public Map<String, FormProperty> gePostProcessed() {
    return Maps.filterValues(getFormProperties(), new IsPostProcessedPredicate());
  }


  public FieldConstructor getFieldConstructor(final FormProperty property) {
    final FormType formType = property.getType();
    if (formType instanceof DelegateFormType) {
      return ((DelegateFormType) formType).getFieldConstructor();
    }
    return new StringFFT().createConstructorOfField();
  }

  public FieldFormType getFieldType(final FormProperty property) {
    final FormType formType = property.getType();
    if (formType instanceof DelegateFormType) {
      return ((DelegateFormType) formType).getType();
    }
    return new StringFFT();
  }

  public FormPostProcessor getPostProcessor(final FormProperty property) {
    final FieldFormType fieldType = getFieldType(property);
    if (fieldType instanceof FormPostProcessor) {
      return (FormPostProcessor) fieldType;
    }
    return null;
  }

  public FormDecorator toComplex(Map<String, VariableSnapshot> snapshots) {
    VariableFormData formData = new VariableFormData(null, snapshots, null);
    return new FormDecorator(id, formData);
  }

  final class IsPostProcessedPredicate implements Predicate<FormProperty> {
    @Override
    public boolean apply(final FormProperty property) {
      final FormPostProcessor processor = getPostProcessor(property);
      return processor != null && processor.needStep(property);
    }

  }

  public static Form createForm() {
    final Form form = new Form();
    form.setSizeFull();
    form.getLayout().setStyleName("liquid1");
    return form;
  }


  public Field createField(final FormProperty property) {
    final boolean writable = property.isWritable();
    final boolean required = property.isRequired();
    return getFieldConstructor(property)
      .createField(property.getName(), property.getValue(), null, writable, required);
  }

  public VariableSnapshot getHistory(final FormProperty property) {
    if (variableFormData == null || variableFormData.variables == null) {
      return null; // стартовый блок
    }
    final VariableSnapshot variableSnapshot = variableFormData.variables.get(property.getId());
    final VariableSnapshot history;
    if (variableSnapshot != null && variableSnapshot.historyId != null) {
      history = variableSnapshot;
    } else {
      history = null;
    }
    return history;
  }

}

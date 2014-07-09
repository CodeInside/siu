/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.values;

import org.activiti.engine.delegate.VariableScope;
import ru.codeinside.gses.service.Some;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final public class HistoryScope implements VariableScope {

  final Set<String> usedVariables = new HashSet<String>();
  private final Map<String, String> archiveValues;

  public HistoryScope(Map<String, String> archiveValues) {
    this.archiveValues = archiveValues;
  }

  public Some<String> getUsedVariable() {
    if (usedVariables.isEmpty()) {
      return Some.empty();
    }
    if (usedVariables.size() > 1) {
      return Some.of(null);
    }
    return Some.of(usedVariables.iterator().next());
  }

  @Override
  public Map<String, Object> getVariables() {
    usedVariables.addAll(archiveValues.keySet());
    return new HashMap<String, Object>(archiveValues);
  }

  @Override
  public void setVariables(Map<String, ?> variables) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Map<String, Object> getVariablesLocal() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setVariablesLocal(Map<String, ?> variables) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object getVariable(String variableName) {
    usedVariables.add(variableName);
    return archiveValues.get(variableName);
  }

  @Override
  public Object getVariableLocal(Object variableName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Set<String> getVariableNames() {
    return archiveValues.keySet();
  }

  @Override
  public Set<String> getVariableNamesLocal() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setVariable(String variableName, Object value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object setVariableLocal(String variableName, Object value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean hasVariables() {
    return !archiveValues.isEmpty();
  }

  @Override
  public boolean hasVariablesLocal() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean hasVariable(String variableName) {
    return archiveValues.containsKey(variableName);
  }

  @Override
  public boolean hasVariableLocal(String variableName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void createVariableLocal(String variableName, Object value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void createVariablesLocal(Map<String, ?> variables) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeVariable(String variableName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeVariableLocal(String variableName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeVariables() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeVariablesLocal() {
    throw new UnsupportedOperationException();
  }
}

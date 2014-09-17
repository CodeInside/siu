/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.values;

import org.activiti.engine.delegate.VariableScope;
import ru.codeinside.gses.service.Some;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final public class VariableTracker implements VariableScope {

  final Set<String> usedVariables = new HashSet<String>();
  final VariableScope impl;

  public VariableTracker(VariableScope impl) {
    this.impl = impl;
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
    Map<String, Object> variables = impl.getVariables();
    usedVariables.addAll(variables.keySet());
    return variables;
  }

  @Override
  public void setVariables(Map<String, ? extends Object> variables) {
    usedVariables.addAll(variables.keySet());
    impl.setVariables(variables);
  }

  @Override
  public Map<String, Object> getVariablesLocal() {
    Map<String, Object> variablesLocal = impl.getVariablesLocal();
    usedVariables.addAll(variablesLocal.keySet());
    return variablesLocal;
  }

  @Override
  public void setVariablesLocal(Map<String, ? extends Object> variables) {
    usedVariables.addAll(variables.keySet());
    impl.setVariablesLocal(variables);
  }

  @Override
  public Object getVariable(String variableName) {
    usedVariables.add(variableName);
    return impl.getVariable(variableName);
  }

  @Override
  public Object getVariableLocal(Object variableName) {
    usedVariables.add(variableName.toString());
    return impl.getVariableLocal(variableName);
  }

  @Override
  public Set<String> getVariableNames() {
    return impl.getVariableNames();
  }

  @Override
  public Set<String> getVariableNamesLocal() {
    return impl.getVariableNamesLocal();
  }

  @Override
  public void setVariable(String variableName, Object value) {
    usedVariables.add(variableName);
    impl.setVariable(variableName, value);
  }

  @Override
  public Object setVariableLocal(String variableName, Object value) {
    usedVariables.add(variableName);
    return impl.setVariableLocal(variableName, value);
  }

  @Override
  public boolean hasVariables() {
    return impl.hasVariables();
  }

  @Override
  public boolean hasVariablesLocal() {
    return impl.hasVariablesLocal();
  }

  @Override
  public boolean hasVariable(String variableName) {
    return impl.hasVariable(variableName);
  }

  @Override
  public boolean hasVariableLocal(String variableName) {
    return impl.hasVariableLocal(variableName);
  }

  @Override
  public void createVariableLocal(String variableName, Object value) {
    usedVariables.add(variableName);
    impl.createVariableLocal(variableName, value);
  }

  @Override
  public void createVariablesLocal(Map<String, ? extends Object> variables) {
    usedVariables.addAll(variables.keySet());
    impl.createVariablesLocal(variables);
  }

  @Override
  public void removeVariable(String variableName) {
    usedVariables.add(variableName);
    impl.removeVariable(variableName);
  }

  @Override
  public void removeVariableLocal(String variableName) {
    usedVariables.add(variableName);
    impl.removeVariableLocal(variableName);
  }

  @Override
  public void removeVariables() {
    impl.removeVariables();
  }

  @Override
  public void removeVariablesLocal() {
    impl.removeVariablesLocal();
  }
}

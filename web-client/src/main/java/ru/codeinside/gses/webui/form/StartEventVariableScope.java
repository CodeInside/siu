package ru.codeinside.gses.webui.form;

import org.activiti.engine.delegate.VariableScope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StartEventVariableScope implements VariableScope {
  private final Map<String, Object> variables = new HashMap<String, Object>();

  @Override
  public Map<String, Object> getVariables() {
    return variables;
  }

  @Override
  public Map<String, Object> getVariablesLocal() {
    return variables;
  }

  @Override
  public Object getVariable(String variableName) {
    return variables.get(variableName);
  }

  @Override
  public Object getVariableLocal(Object variableName) {
    String name = (String) variableName;
    return variables.get(name);
  }

  @Override
  public Set<String> getVariableNames() {
    return variables.keySet();
  }

  @Override
  public Set<String> getVariableNamesLocal() {
    return variables.keySet();
  }

  @Override
  public void setVariable(String variableName, Object value) {
    variables.put(variableName, value);
  }

  @Override
  public Object setVariableLocal(String variableName, Object value) {
    return variables.put(variableName, value);
  }

  @Override
  public void setVariables(Map<String, ? extends Object> variables) {
    this.variables.putAll(variables);
  }

  @Override
  public void setVariablesLocal(Map<String, ? extends Object> variables) {
    this.variables.putAll(variables);
  }

  @Override
  public boolean hasVariables() {
    return !variables.isEmpty();
  }

  @Override
  public boolean hasVariablesLocal() {
    return !variables.isEmpty();
  }

  @Override
  public boolean hasVariable(String variableName) {
    return variables.containsKey(variableName);
  }

  @Override
  public boolean hasVariableLocal(String variableName) {
    return variables.containsKey(variableName);
  }

  @Override
  public void createVariableLocal(String variableName, Object value) {
    variables.put(variableName, value);
  }

  @Override
  public void createVariablesLocal(Map<String, ? extends Object> variables) {
    this.variables.putAll(variables);
  }

  @Override
  public void removeVariable(String variableName) {
    variables.remove(variableName);
  }

  @Override
  public void removeVariableLocal(String variableName) {
    variables.remove(variableName);
  }

  @Override
  public void removeVariables() {
    variables.clear();
  }

  @Override
  public void removeVariablesLocal() {
    variables.clear();
  }
}

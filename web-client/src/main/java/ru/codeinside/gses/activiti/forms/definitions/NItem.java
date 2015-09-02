/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.definitions;

import org.activiti.engine.delegate.Expression;
import ru.codeinside.gses.activiti.forms.api.definitions.NullAction;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyType;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

import java.util.Map;

final class NItem implements PropertyNode {

  final String id;
  final String underline;
  final String tip;
  final NullAction nullAction;
  final boolean readable;
  final boolean writable;
  final String name;
  final VariableType variableType;
  final boolean fieldReadable;
  final boolean fieldWritable;
  final boolean fieldRequired;
  final String variableName;
  final Expression variableExpression;
  final Expression defaultExpression;
  final String pattern;
  final Map<String, String> params;
  private boolean visible;

  NItem(final String id, String underline, String tip, NullAction nullAction, boolean readable, boolean writable,
        String name, boolean fieldReadable, boolean fieldRequired, String variableName,
        Expression variableExpression, Expression defaultExpression,
        VariableType variableType, boolean fieldWritable, String pattern, Map<String, String> params) {
    this.id = id;
    this.underline = underline;
    this.tip = tip;
    this.nullAction = nullAction;
    this.readable = readable;
    this.writable = writable;
    this.name = name;
    this.variableType = variableType;
    this.fieldReadable = fieldReadable;
    this.fieldWritable = fieldWritable;
    this.fieldRequired = fieldRequired;
    this.variableName = variableName;
    this.variableExpression = variableExpression;
    this.defaultExpression = defaultExpression;
    this.pattern = pattern;
    this.params = params;
  }

  @Override
  public PropertyNode getOwner() {
    return null;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public PropertyType getPropertyType() {
    return PropertyType.ITEM;
  }

  @Override
  public String getUnderline() {
    return underline;
  }

  @Override
  public String getTip() {
    return tip;
  }

  @Override
  public NullAction getNullAction() {
    return nullAction;
  }

  @Override
  public boolean isVarReadable() {
    return readable;
  }

  @Override
  public boolean isVarWritable() {
    return writable;
  }

  @Override
  public VariableType getVariableType() {
    return variableType;
  }

  @Override
  public boolean isFieldWritable() {
    return fieldWritable;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isFieldReadable() {
    return fieldReadable;
  }

  @Override
  public boolean isFieldRequired() {
    return fieldRequired;
  }

  @Override
  public String getVariableName() {
    return variableName;
  }

  @Override
  public Expression getVariableExpression() {
    return variableExpression;
  }

  @Override
  public Expression getDefaultExpression() {
    return defaultExpression;
  }

  @Override
  public String getPattern() {
    return pattern;
  }

  @Override
  public Map<String, String> getParams() {
    return params;
  }

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  @Override
  public String toString() {
    return "{" +
      "id='" + id + '\'' +
      ", underline='" + underline + '\'' +
      ", tip='" + tip + '\'' +
      ", nullAction=" + nullAction +
      ", readable=" + readable +
      ", writable=" + writable +
      ", name='" + name + '\'' +
      ", variableType=" + (variableType == null ? null : variableType.getName()) +
      ", fieldReadable=" + fieldReadable +
      ", fieldWritable=" + fieldWritable +
      ", fieldRequired=" + fieldRequired +
      ", variableName='" + variableName + '\'' +
      ", variableExpression=" + variableExpression +
      ", defaultExpression=" + defaultExpression +
      ", pattern='" + pattern + '\'' +
      ", params=" + params +
      '}';
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.definitions;

import org.activiti.engine.delegate.Expression;
import ru.codeinside.gses.activiti.forms.api.definitions.BlockNode;
import ru.codeinside.gses.activiti.forms.api.definitions.NullAction;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyType;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

import java.util.Arrays;
import java.util.Map;

final class NBlock implements BlockNode {

  final String id;
  final PropertyNode[] nodes;
  final int min;
  final int max;
  final String underline;
  final String tip;
  final NullAction nullAction;
  final VariableType variableType;
  final boolean fieldWritable;
  final String variableName;
  final boolean fieldRequired;
  final boolean fieldReadable;
  final String name;
  final Expression defaultExpression;
  final boolean varReadable;
  final boolean varWritable;
  private boolean visible;

  NBlock(String id, PropertyNode[] nodes, int min, int max, String underline, String tip, NullAction nullAction,
         VariableType variableType, boolean fieldWritable,
         String variableName, boolean fieldRequired, boolean fieldReadable, String name,
         Expression defaultExpression, boolean varReadable, boolean varWritable
  ) {
    this.id = id;
    this.nodes = nodes;
    this.min = min;
    this.max = max;
    this.underline = underline;
    this.tip = tip;
    this.nullAction = nullAction;
    this.variableType = variableType;
    this.fieldWritable = fieldWritable;
    this.variableName = variableName;
    this.fieldRequired = fieldRequired;
    this.fieldReadable = fieldReadable;
    this.name = name;
    this.defaultExpression = defaultExpression;
    this.varReadable = varReadable;
    this.varWritable = varWritable;
  }

  @Override
  public PropertyNode[] getNodes() {
    return nodes;
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
    return PropertyType.BLOCK;
  }

  @Override
  public int getMinimum() {
    return min;
  }

  @Override
  public int getMaximum() {
    return max;
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
    return varReadable;
  }

  @Override
  public boolean isVarWritable() {
    return varWritable;
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
    return null;
  }

  @Override
  public Expression getDefaultExpression() {
    return defaultExpression;
  }

  @Override
  public String getPattern() {
    return null;
  }

  @Override
  public Map<String, String> getParams() {
    return null;
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
      ", nodes=" + Arrays.toString(nodes) +
      ", min=" + min +
      ", max=" + max +
      ", underline='" + underline + '\'' +
      ", tip='" + tip + '\'' +
      ", nullAction=" + nullAction +
      ", variableType=" + variableType.getName() +
      ", fieldWritable=" + fieldWritable +
      ", variableName='" + variableName + '\'' +
      ", fieldRequired=" + fieldRequired +
      ", fieldReadable=" + fieldReadable +
      ", name='" + name + '\'' +
      ", defaultExpression=" + defaultExpression +
      ", varReadable=" + varReadable +
      ", varWritable=" + varWritable +
      '}';
  }
}

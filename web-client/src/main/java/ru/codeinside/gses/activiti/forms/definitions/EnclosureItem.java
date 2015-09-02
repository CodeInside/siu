/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.definitions;


import org.activiti.engine.delegate.Expression;
import ru.codeinside.gses.activiti.forms.api.definitions.EnclosureNode;
import ru.codeinside.gses.activiti.forms.api.definitions.NullAction;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyType;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

import java.util.Map;

final class EnclosureItem implements EnclosureNode {

  final String id;
  final String underline;
  final String tip;
  final NullAction nullAction;
  final VariableType variableType;
  final boolean fieldWritable;
  final VariableType encloseType;
  final String name;
  final String variableName;
  private boolean visible;

  EnclosureItem(final String id, String name, String variableName, String underline, String tip, NullAction nullAction, VariableType formType,
                boolean fieldWritable, VariableType encloseType) {
    this.id = id;
    this.name = name;
    this.variableName = variableName;
    this.underline = underline;
    this.tip = tip;
    this.nullAction = nullAction;
    this.variableType = formType;
    this.fieldWritable = fieldWritable; // TODO: remove? Не записываемое поле - лишь чтение
    this.encloseType = encloseType;
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
    return PropertyType.ENCLOSURE;
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
    return true;
  }

  @Override
  public boolean isVarWritable() {
    return true;
  }

  @Override
  public VariableType getVariableType() {
    return variableType;
  }

  @Override
  public boolean isFieldWritable() {
    return false;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isFieldReadable() {
    return true;
  }

  @Override
  public boolean isFieldRequired() {
    return false;
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
    return null;
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
  public PropertyNode createEnclosure(String id) {
    return new NItem(id, null, null, nullAction, true, true,
      "Данные для подписи", true, false, null, null, null,
      encloseType, true, null, null
    );
  }
}

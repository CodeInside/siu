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
import ru.codeinside.gses.activiti.forms.api.definitions.ToggleNode;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

import java.util.Map;

final class NToggle implements ToggleNode {

  final String id;
  final PropertyType propertyType;
  final PropertyNode propertyNode;
  final String toggleValue;
  final boolean toggleTo;
  final PropertyNode[] toggleNodes;
  private boolean visible;

  NToggle(final String id, final PropertyType propertyType,
          final PropertyNode propertyNode, final String toggleValue, final boolean toggleTo, final PropertyNode[] toggleNodes) {
    this.id = id;
    this.propertyType = propertyType;
    this.propertyNode = propertyNode;
    this.toggleValue = toggleValue;
    this.toggleTo = toggleTo;
    this.toggleNodes = toggleNodes;
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
    return propertyType;
  }


  @Override
  public PropertyNode getToggler() {
    return propertyNode;
  }

  @Override
  public String getToggleValue() {
    return toggleValue;
  }

  @Override
  public boolean getToggleTo() {
    return toggleTo;
  }

  @Override
  public PropertyNode[] getToggleNodes() {
    return toggleNodes;
  }

  @Override
  public String getUnderline() {
    return null;
  }

  @Override
  public String getTip() {
    return null;
  }

  @Override
  public NullAction getNullAction() {
    return NullAction.skip;
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
    return null;
  }

  @Override
  public boolean isFieldWritable() {
    return false;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public boolean isFieldReadable() {
    return false;
  }

  @Override
  public boolean isFieldRequired() {
    return false;
  }

  @Override
  public String getVariableName() {
    return null;
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

  /**
   * Видимость узла после переключения на форме
   *
   * @return видимость
   */
  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public void setVisible(boolean visible) {
    this.visible = visible;
  }
}

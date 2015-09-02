/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.api.definitions;

import org.activiti.engine.delegate.Expression;

import java.io.Serializable;
import java.util.Map;

/**
 * Узел дерева внутри формы.
 */
public interface PropertyNode extends Serializable {

  /**
   * @return узел-владелец, либо null
   */
  PropertyNode getOwner();

  String getId();

  PropertyType getPropertyType(); // пересекается с типом реализации

  String getUnderline();

  String getTip();

  NullAction getNullAction();

  /**
   * Читается ли значение ПЕРЕМЕННОЙ связанной со свойством
   * из маршрута в значение свойства, либо используется значние по умолчанию.
   * Отличается от визуального свойства {@link org.activiti.engine.form.FormProperty#isReadable()}!
   */
  boolean isVarReadable();

  /**
   * Записывается ли значение ПЕРЕМЕННОЙ связанной со свойством в маршрут.
   * Отличается от визуального свойства {@link org.activiti.engine.form.FormProperty#isWritable()}!
   */
  boolean isVarWritable();

  VariableType getVariableType();

  /**
   * @return является ли поле изменяемым.
   */
  boolean isFieldWritable();

  String getName();

  boolean isFieldReadable();

  boolean isFieldRequired();

  String getVariableName();

  Expression getVariableExpression();

  Expression getDefaultExpression();

  /**
   * @return шаблон. Трактовка звисит от типа свойства.
   */
  String getPattern();

  /**
   * @return карта дополнительных параметров. Трактовка зависит от типа свойства.
   */
  Map<String, String> getParams();

  /**
   * Видимость узла после переключения на форме
   * @return видимость
   */
  boolean isVisible();

  /**
   * Задать динамическую видимость узла
   *
   * @param visible видимость
   */
  void setVisible(boolean visible);

}

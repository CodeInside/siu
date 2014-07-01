/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.api.definitions;


import ru.codeinside.adm.database.FieldBuffer;

import java.io.Serializable;
import java.util.Map;

public interface VariableType<T> extends Serializable {

  /**
   * @param propertyValue выражение из описателя
   * @return значение
   */
  T convertFormValueToModelValue(Object propertyValue, String pattern, Map<String, String> values);

  /**
   * @param pattern шаблон.
   * @param values  значения.
   */
  void validateParams(String pattern, Map<String, String> values);

  Class<T> getJavaType();

  T convertBufferToModelValue(FieldBuffer fieldBuffer);

  // используется в СМЭВ
  String getName();
}
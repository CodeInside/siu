/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.api.values;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;

import java.io.Serializable;

/**
 * Значения элемента формы во время исполнения.
 */
public interface PropertyValue<T> extends Serializable {

  /**
   * Уникальный идентификатор
   */
  String getId();

  /**
   * Узел-описатель.
   */
  PropertyNode getNode();

  /**
   * Значение.
   */
  T getValue();

  /**
   * Аудит.
   */
  Audit getAudit();
}

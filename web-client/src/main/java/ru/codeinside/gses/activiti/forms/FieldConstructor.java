/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import com.vaadin.ui.Field;

/**
 * Сильная связность между типом движка (ValueType) и отображением (Field).
 */
public interface FieldConstructor extends ValueType {
  /**
   * @param taskId   идентификатор формы.
   * @param fieldId  идентификатор поля формы.
   * @param name     имя свойства.
   * @param value    значение свойства.
   * @param writable можно ли изменять значение.
   * @param required значение обязательно.
   * @return поле формы для отображения.
   */
  Field createField(String taskId, String fieldId, String name, String value, boolean writable, boolean required);
}

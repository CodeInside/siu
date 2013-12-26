/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import java.io.Serializable;

public interface PropertyNode extends Serializable {
  String getId();

  PropertyType getPropertyType();

  String getUnderline();

  String getTip();

  NullAction getNullAction();

  /**
   * Читается ли значение ПЕРЕМЕННОЙ связанной со свойством
   * из маршрута в значение свойства, либо используется значние по умолчанию.
   * Отличается от визуального свойства {@link org.activiti.engine.form.FormProperty#isReadable()}!
   */
  boolean isReadable();

  /**
   * Записывается ли значение ПЕРЕМЕННОЙ связанной со свойством в маршрут.
   * Отличается от визуального свойства {@link org.activiti.engine.form.FormProperty#isWritable()}!
   */
  boolean isWritable();
}

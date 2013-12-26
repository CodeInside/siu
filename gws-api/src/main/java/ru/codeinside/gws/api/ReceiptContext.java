/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.util.Set;

/**
 * Контекст квитанции для подготвки ответа на заявку.
 * Содержит методы работы с переменными процесса исполнения маршрута.
 */
public interface ReceiptContext {

  /**
   * Получить названия переменных, начинающихся на "result_". Вложения пропускаются.
   *
   * @return названия переменных без префикса "result_"
   */
  Set<String> getPropertyNames();

  /**
   * Получить названия вложений, начинающихся на "result_".
   *
   * @return названия вложений без префикса "result_".
   */
  Set<String> getEnclosureNames();

  /**
   * Получить значение переменной "result_{@code name}".
   *
   * @return значение переменной либо null.
   */
  Object getVariable(String name);

  /**
   * Получить вложение с именем "result_{@code name}".
   *
   * @return вложение либо null.
   */
  Enclosure getEnclosure(String name);

  /**
   * Установить в процесс исполнения маршрута переменную {@code name}
   * со значением указывающим на вложение, созданное по содержимому {@code enclosure}.
   *
   * @param name      название переменной.
   * @param enclosure вложение.
   */
  void setEnclosure(String name, Enclosure enclosure);

  /**
   * Установить в процесс исполнения маршрута перменную {@code name}
   * с заданным значением {@code value}.
   *
   * @param name
   * @param value
   */
  void setVariable(String name, Object value);
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

/**
 * Служба регистрации журналов СМЭВ.
 *
 * @author xeodon
 */
public interface LogService {

  /**
   * Включение/выключение журнала поставщиков.
   *
   * @param shouldWrite новое состояние журнала поставщиков.
   */
  void setShouldWriteServerLog(boolean shouldWrite);


  /**
   * Корневой каталог журнала.
   *
   * @return полный путь к каталогу.
   */
  String getPathInfo();

  /**
   * Создание журнала для экземпляра клиента (потребителя СМЭВ).
   *
   * @param componentName     имя компонента-реализации клиента.
   * @param processInstanceId идентификатор процесса BPMN, внутри которого происходит вызов.
   * @return
   */
  ClientLog createClientLog(String componentName, String processInstanceId);

  /**
   * Создание журнала для экземпляра услуги (поставщика СМЭВ).
   *
   * @param componentName имя компонента-реализации клиента.
   * @return
   */
  ServerLog createServerLog(String componentName);

}
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
   * Выключение действует на всех поставщиков.
   * Включение журнала учитывает настройку конкретного поставщика.
   *
   * @param enabled новое состояние журнала поставщиков.
   */
  void setServerLogEnabled(boolean enabled);

  /**
   * Включение/выключение журналирования ошибок поставщиков.
   * Логируются ошибки вне зависимости от настроек
   *
   * @param enabled новое состояние журнала поставщиков.
   */
  void setServerLogErrorsEnabled(boolean enabled);

  /**
   * Установка статуса запросов для логирования
   * Учитывает настройку конкреного поставщика
   *
   * @param status новое состояние журнала поставщиков.
   */
  void setServerLogStatus(String status);

  /**
   * Получить статус журнала для всех поставщиков.
   *
   * @return {@code true} если журнал включён.
   */
  boolean isServerLogEnabled();

  /**
   * Получить статус журнала ошибок для всех поставщиков.
   *
   * @return {@code true} если журнал включён.
   */
  boolean isServerLogErrorsEnabled();

  /**
   * Получить статус журнала ошибок для всех поставщиков.
   *
   * @return текущий статус журналируемых запросов.
   */
  String getServerLogStatus();

  /**
   * Включение/выключение журнала поставщика.
   *
   * @param componentName имя компонента
   * @param enabled       включение/выключение.
   */
  void setServerLogEnabled(String componentName, boolean enabled);


  /**
   * Получить статус журнала поставщика.
   *
   * @param componentName имя компонента.
   * @return {@code true} если журнал включен.
   */
  boolean isServerLogEnabled(String componentName);

  /**
   * Корневой каталог журнала.
   *
   * @return полный путь к каталогу.
   */
  String getPathInfo();

  /**
   * Создание журнала для экземпляра клиента (потребителя СМЭВ).
   *
   * @param bid               номер заявки.
   * @param componentName     имя компонента-реализации клиента.
   * @param processInstanceId идентификатор процесса BPMN, внутри которого происходит вызов.
   * @return журнал потребителя.
   */
  ClientLog createClientLog(long bid, String componentName, String processInstanceId, boolean isLogEnabled, boolean logErrors, String status);

  /**
   * Создание журнала для экземпляра услуги (поставщика СМЭВ).
   *
   * @param componentName имя компонента-реализации клиента.
   * @return журнал поставщика, либо {@code null} если журнал отключен.
   */
  ServerLog createServerLog(String componentName);

}
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

public interface LogService {

  String generateMarker();

  void log(String marker, boolean isClient, StackTraceElement[] traceElements);

  void log(String marker, String processInstanceId);

  void log(String marker, String msg, boolean isRequest, boolean isClient);

  void log(String marker, ServerRequest request);

  void log(String marker, ServerResponse response);

  boolean shouldWriteServerLog();

  void setShouldWriteServerLog(boolean should);

  String getPathInfo(); //нужно ли, используется для указания пути к файлу ?

  String generateMarker(boolean isClient);


  /**
   * Создание журнала для экземпляра клиента (потребителя СМЭВ).
   *
   * @param componentName     имя компонента-реализации клиента.
   * @param processInstanceId идентификатор процесса BPMN, внутри которого происходит вызов.
   * @return
   */
  ClientLog createClientLog(String componentName, String processInstanceId);
}
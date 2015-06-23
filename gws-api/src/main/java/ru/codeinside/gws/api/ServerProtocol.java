/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import java.io.OutputStream;

public interface ServerProtocol {

  /**
   * Поддерживаемвя ревизия.
   */
  Revision getRevision();

  /**
   * Сформировать запрос по входящему сообщению.
   *
   * @param message входящее SOAP сообщение.
   * @param service идентификатор сервиса.
   * @param port    идентификатор порта.
   * @return разобранный ответ.
   */
  ServerRequest processRequest(SOAPMessage message, QName service, ServiceDefinition.Port port);

  /**
   * Сформировать исходящее сообщение.
   *
   * @param request   запрос.
   * @param response  ответ.
   * @param service   идентификатор сервиса.
   * @param port      идентификатор порта.
   * @param serverLog журнал.
   * @return исходящее SOAP сообщение для транспортного уровня.
   */
  SOAPMessage processResponse(
      ServerRequest request,
      ServerResponse response,
      QName service, ServiceDefinition.Port port,
      ServerLog serverLog);

  /**
   * Подготовить SOAP-сообщение перед отправкой
   *
   * @param request        запрос
   * @param response       ответ
   * @param service        идентификатор сервиса
   * @param port           идентификатор порта
   * @param serverLog      журнал
   * @param normalizedBody нормализованный блок Body для получения подписи ОВ
   * @return предварительное сообщение для отправки
   */
  SOAPMessage createMessage(
      ServerResponse response,
      QName service, ServiceDefinition.Port port,
      ServerLog serverLog,
      OutputStream normalizedBody);
}

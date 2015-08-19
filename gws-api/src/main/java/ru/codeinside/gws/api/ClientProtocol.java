/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import javax.xml.soap.SOAPMessage;
import java.io.OutputStream;
import java.net.URL;

/**
 * Протокол взаимодействия потребителя с поставщиком через СМЭВ/SOAP/HTTP.
 */
public interface ClientProtocol {

  /**
   * Поддерживаемая ревизия СМЭВ.
   */
  Revision getRevision();

  /**
   * Отправить запрос поставщику и получить от него ответ.
   *
   * @param wsdlUrl ссылка на описание сервиса в формате WSDL.
   * @param request запрос от клиента к поствщику.
   * @param log     журнал клиента.
   * @return ответ от поставщика к клиенту.
   * @author xeodon
   * @since 1.0.7
   */
  ClientResponse send(URL wsdlUrl, ClientRequest request, ClientLog log);

  /**
   * Подготовить SOAP-сообщение перед отправкой
   *
   * @param wsdlUrl        ссылка на описание сервиса в формате WSDL.
   * @param request        запрос от клиента к поствщику.
   * @param log            журнал клиента.
   * @param normalizedBody нормализованный блок Body для получения подписи ОВ
   * @return предварительное сообщение для отправки
   */
  SOAPMessage createMessage(URL wsdlUrl, ClientRequest request, ClientLog log, OutputStream normalizedBody);

}

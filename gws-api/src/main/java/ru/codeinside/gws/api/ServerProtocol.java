/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;

public interface ServerProtocol {

  /**
   * Поддерживаемвя ревизия.
   */
  Revision getRevision();

  /**
   * Сформировать запрос по сообщению.
   */
  ServerRequest processRequest(SOAPMessage message, QName service, ServiceDefinition.Port port);

  /**
   * Сформировать сообщение по ответу.
   */
  SOAPMessage processResponse(ServerResponse response, QName service, ServiceDefinition.Port port);

}

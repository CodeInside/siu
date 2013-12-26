/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.net.URL;

public interface ClientProtocol {

  /**
   * Поддерживаемвя ревизия.
   */
  Revision getRevision();

  ClientResponse send(final URL wsdlUrl, final ClientRequest request);

  ClientResponse send(final URL wsdlUrl, final ClientRequest request, String processInstanceId);

}

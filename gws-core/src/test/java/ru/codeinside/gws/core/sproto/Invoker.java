/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.sproto;

import javax.xml.soap.SOAPMessage;
import javax.xml.ws.WebServiceContext;

public interface Invoker {
  SOAPMessage invoke(SOAPMessage in, WebServiceContext ctx);
}
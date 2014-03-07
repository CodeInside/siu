/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.adapter;

import com.sun.xml.ws.transport.http.servlet.ServletAdapter;
import ru.codeinside.gws.api.Declarant;
import ru.codeinside.gws.api.ServerProtocol;
import ru.codeinside.gws.api.ServiceDefinition;

import javax.xml.namespace.QName;
import java.net.URL;

final public class ProviderEntry {

  public ServerProtocol protocol;
  public Declarant declarant;
  public String name;
  public QName wsService;
  public ServiceDefinition.Port wsPortDef;

  public QName wsPort;
  public URL wsdl;
  public ServiceDefinition wsDef;
  public ServletAdapter servletAdapter;
}

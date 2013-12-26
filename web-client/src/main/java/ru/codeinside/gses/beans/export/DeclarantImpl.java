/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans.export;

import ru.codeinside.gses.beans.ActivitiRequestContext;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gses.webui.gws.TRefProvider;
import ru.codeinside.gws.api.Declarant;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerPipeline;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;

final public class DeclarantImpl implements Declarant {

  public ServerResponse processRequest(final ServerRequest serverRequest, final String name) {
    final TRef<Server> serverRef = TRefProvider.get().getServerByName(name);
    final Server server;
    if (serverRef == null) {
      server = null;
    } else {
      server = serverRef.getRef();
    }
    if (server == null) {
      throw new RuntimeException("Provider " + name + " not found");
    }
    if (server instanceof ServerPipeline) {
      return ((ServerPipeline) server).processRequest(serverRequest);
    }
    ActivitiRequestContext requestContext = new ActivitiRequestContext(serverRequest, name);
    return server.processRequest(requestContext);
  }
}
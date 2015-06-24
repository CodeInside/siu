/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans.export;

import ru.codeinside.adm.database.SmevChain;
import ru.codeinside.gses.beans.ActivitiRequestContext;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gses.webui.gws.TRefProvider;
import ru.codeinside.gws.api.Declarant;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerPipeline;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;

import java.util.UUID;

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

    SmevChain smevChain = createSmevChain(serverRequest);
    ServerResponse serverResponse;
    if (server instanceof ServerPipeline) {
      serverResponse = ((ServerPipeline) server).processRequest(serverRequest);
    } else {
      serverResponse = server.processRequest(new ActivitiRequestContext(smevChain, serverRequest, name));
    }

    // согласование цепочки
    {
      serverResponse.packet.originRequestIdRef = smevChain.originRequestIdRef;
      // дать возможность поставщику в режиме без СМЭВ сформировать идентификатор запроса в ответе
      if (smevChain.hasMessageId || serverResponse.packet.requestIdRef == null) {
        serverResponse.packet.requestIdRef = smevChain.requestIdRef;
      }
    }

    return serverResponse;

  }

  private SmevChain createSmevChain(ServerRequest request) {
    boolean hasMessageId = request.routerPacket != null && request.routerPacket.messageId != null;

    // Идентификатор локальной цепочки (между sender и recipient).
    // Если не указан, то формируем и обновляем в исходном запросе чтобы изменения были доступны поставщику:
    // - если есть СМЭВ, копируем из идентификатора роутера;
    // - если нет СМЭВ, генерируем.
    String originRequestIdRef = request.packet.originRequestIdRef;
    boolean originGenerated = false;
    if (originRequestIdRef == null) {
      if (hasMessageId) {
        originRequestIdRef = request.routerPacket.messageId;
      } else {
        originRequestIdRef = UUID.randomUUID().toString();
        originGenerated = true;
      }
      // изменяем запрос для использования внутри поставщика
      request.packet.originRequestIdRef = originRequestIdRef;
    }

    // Идентификатор текущего запроса.
    String requestIdRef;
    if (hasMessageId) {
      requestIdRef = request.routerPacket.messageId;
    } else if (originGenerated) {
      requestIdRef = originRequestIdRef; // согласованная генерация в начале цепочки
    } else {
      requestIdRef = UUID.randomUUID().toString();
    }
    return new SmevChain(hasMessageId, request.packet.originator, originRequestIdRef, request.packet.sender, requestIdRef, request.packet.recipient);
  }
}
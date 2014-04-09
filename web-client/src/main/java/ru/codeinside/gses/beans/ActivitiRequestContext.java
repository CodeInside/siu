/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.gses.service.DeclarantServiceProvider;
import ru.codeinside.gws.api.DeclarerContext;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.RequestContext;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ActivitiRequestContext implements RequestContext {

  private final ServerRequest serverRequest;
  private final String componentName;
  private final boolean first;
  private final AtomicLong gid;

  public ActivitiRequestContext(ServerRequest serverRequest, String componentName) {
    this.serverRequest = serverRequest;
    this.componentName = componentName;

    if (serverRequest.packet.originRequestIdRef == null) {
      if (serverRequest.routerPacket != null && serverRequest.routerPacket.messageId != null) {
        serverRequest.packet.originRequestIdRef = serverRequest.routerPacket.messageId;
      } else {
        serverRequest.packet.originRequestIdRef = UUID.randomUUID().toString();
      }
    }
    ExternalGlue glue = AdminServiceProvider.get().getGlueByRequestIdRef(serverRequest.packet.originRequestIdRef);
    first = glue == null;
    gid = new AtomicLong(first ? 0L : glue.getId());
  }

  @Override
  public boolean isFirst() {
    return first;
  }

  @Override
  public ServerResponse getState() {
    return findResponseByStatus(Packet.Status.STATE);
  }

  @Override
  public ServerResponse getResult() {
    ServerResponse response = findResponseByStatus(Packet.Status.RESULT);
    if (response == null) {
      response = findResponseByStatus(Packet.Status.REJECT);
    }
    return response;
  }

  @Override
  public List<String> getBids() {
    long activeGid = gid.get();
    if (activeGid == 0L) {
      return Collections.emptyList();
    }
    return DeclarantServiceProvider.get().getBids(activeGid);
  }

  @Override
  public ServerRequest getRequest() {
    return serverRequest;
  }

  @Override
  public DeclarerContext getDeclarerContext(long procedureCode) {
    ProcedureProcessDefinition active = AdminServiceProvider.get().getProcedureProcessDefinitionByProcedureCode(procedureCode);
    if (active == null) {
      // TODO: add exception to API!
      throw new RuntimeException("procedureCode " + procedureCode + " dont have active processDefinition");
    }
    return new ActivitiDeclarerContext(serverRequest.packet.originRequestIdRef, gid, active.getProcessDefinitionId(), componentName);
  }

  @Override
  public String getBid() {
    long activeGid = gid.get();
    if (activeGid == 0L) {
      return null;
    }
    return Long.toString(activeGid);
  }

  private ServerResponse findResponseByStatus(Packet.Status status) {
    return getServerResponseByBidAndStatus(gid.get(), status);
  }

  @Override
  public ServerResponse getState(String bid) {
    long bidId = Long.parseLong(bid);
    return getServerResponseByBidAndStatus(bidId, Packet.Status.STATE);
  }


  @Override
  public ServerResponse getResult(String bid) {
    Long bidId = Long.parseLong(bid);
    ServerResponse response = getServerResponseByBidAndStatus(bidId, Packet.Status.RESULT);
    if (response == null) {
      response = getServerResponseByBidAndStatus(bidId, Packet.Status.REJECT);
    }
    return response;
  }

  // ---- internals ----


  // TODO: проверка привязки bid к текущей цепочки запросов!
  private ServerResponse getServerResponseByBidAndStatus(long bid, Packet.Status state) {
    long activeGid = gid.get();
    if (activeGid == 0L) {
      return null;
    }
    return AdminServiceProvider.get().getServerResponseByBidIdAndStatus(activeGid, bid, state.name());
  }

}
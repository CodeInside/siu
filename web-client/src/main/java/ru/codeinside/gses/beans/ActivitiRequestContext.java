/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import commons.Exceptions;
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.SmevChain;
import ru.codeinside.gses.service.DeclarantService;
import ru.codeinside.gses.service.DeclarantServiceProvider;
import ru.codeinside.gws.api.DeclarerContext;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.RequestContext;
import ru.codeinside.gws.api.ServerException;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;

import javax.ejb.EJBException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ActivitiRequestContext implements RequestContext {

  private final ServerRequest serverRequest;
  private final String componentName;
  private final boolean first;
  private final AtomicLong gid;
  private final SmevChain smevChain;

  public ActivitiRequestContext(SmevChain smevChain, ServerRequest request, String componentName) {
    this.serverRequest = request;
    this.componentName = componentName;
    this.smevChain = smevChain;
    long id = declarantService().getGlueIdByRequestIdRef(smevChain.originRequestIdRef);
    gid = new AtomicLong(id);
    first = id == 0L;
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
    try {
      return declarantService().getBids(activeGid);
    } catch (EJBException e) {
      throw Exceptions.convertToApi(e);
    }
  }

  @Override
  public ServerRequest getRequest() {
    return serverRequest;
  }

  @Override
  public DeclarerContext getDeclarerContext(long procedureCode) {
    try {
      ProcedureProcessDefinition active = adminService().getProcedureProcessDefinitionByProcedureCode(procedureCode);
      if (active == null) {
        throw new ServerException("Не найдено процедруы с кодом '" + procedureCode + "'");
      }
      return new ActivitiDeclarerContext(smevChain, gid, active.getProcessDefinitionId(), componentName);
    } catch (EJBException e) {
      throw Exceptions.convertToApi(e);
    }
  }

  @Override
  public String getBid() {
    long activeGid = gid.get();
    if (activeGid == 0L) {
      return null;
    }
    return Long.toString(activeGid);
  }

  @Override
  public ServerResponse getState(String bid) {
    long bidId = parseLong(bid);
    if (bidId == 0L) {
      return null;
    }
    return getServerResponseByBidAndStatus(bidId, Packet.Status.STATE);
  }

  @Override
  public ServerResponse getResult(String bid) {
    long bidId = parseLong(bid);
    if (bidId == 0L) {
      return null;
    }
    ServerResponse response = getServerResponseByBidAndStatus(bidId, Packet.Status.RESULT);
    if (response == null) {
      response = getServerResponseByBidAndStatus(bidId, Packet.Status.REJECT);
    }
    return response;
  }

  // ---- internals ----

  private DeclarantService declarantService() {
    return DeclarantServiceProvider.forApi();
  }

  private AdminService adminService() {
    return AdminServiceProvider.forApi();
  }

  private ServerResponse getServerResponseByBidAndStatus(long bid, Packet.Status state) {
    long activeGid = gid.get();
    if (activeGid == 0L) {
      return null;
    }
    try {
      return adminService().getServerResponseByBidIdAndStatus(activeGid, bid, state.name());
    } catch (EJBException e) {
      throw Exceptions.convertToApi(e);
    }
  }

  private long parseLong(String bid) {
    if (bid == null) {
      return 0L;
    }
    try {
      return Long.parseLong(bid);
    } catch (NumberFormatException e) {
      return 0L;
    }
  }

  private ServerResponse findResponseByStatus(Packet.Status status) {
    return getServerResponseByBidAndStatus(gid.get(), status);
  }

}
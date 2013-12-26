/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.gws.api.DeclarerContext;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.RequestContext;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;

import java.util.UUID;

public class ActivitiRequestContext implements RequestContext {

    private final ServerRequest serverRequest;
    private final String name;
    private final String processInstanceId;
    private String bidId;
    private ActivitiDeclarerContext context;

    public ActivitiRequestContext(ServerRequest serverRequest, String name) {
        this.serverRequest = serverRequest;
        this.name = name;

        ExternalGlue glue = null;
        if (serverRequest.packet.originRequestIdRef == null) {
            if (serverRequest.routerPacket != null && serverRequest.routerPacket.messageId != null) {
                serverRequest.packet.originRequestIdRef = serverRequest.routerPacket.messageId;
            } else {
                serverRequest.packet.originRequestIdRef = UUID.randomUUID().toString();
            }
        } else {
            glue = AdminServiceProvider.get().getGlueByRequestIdRef(serverRequest.packet.originRequestIdRef);
        }
        this.bidId = glue == null ? null : glue.getBidId();
        processInstanceId = glue == null ? null : glue.getProcessInstanceId();
    }

    @Override
    public boolean isFirst() {
        return bidId == null;
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
    public ServerRequest getRequest() {
        return serverRequest;
    }

    @Override
    public DeclarerContext getDeclarerContext(long procedureCode) {
        if (bidId != null || context != null) {
            throw new RuntimeException("cant call many times");
        }
        ProcedureProcessDefinition active = AdminServiceProvider.get().getProcedureProcessDefinitionByProcedureCode(procedureCode);
        if (active == null) {
            throw new RuntimeException("procedureCode " + procedureCode + " dont have active processDefinition");
        }
        context = new ActivitiDeclarerContext(serverRequest.packet.originRequestIdRef, bidId, active.getProcessDefinitionId(), name);
        return context;
    }

    @Override
    public String getBid() {
        if (context != null) {
            bidId = context.getBidId();
        }
        return bidId;
    }

    private ServerResponse findResponseByStatus(Packet.Status status) {
        if (bidId == null) {
            return null;
        }
        return AdminServiceProvider.get().getServerResponseByBidIdAndStatus(bidId, status.name());
    }
}
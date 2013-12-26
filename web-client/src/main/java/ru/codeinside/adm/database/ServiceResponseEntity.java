/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.log.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.namespace.QName;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

//TODO перенести в другую бд
@Entity
@EntityListeners(Logger.class)
@Table(name = "service_response")
@SequenceGenerator(name = "service_response_seq", sequenceName = "service_response_seq")
public class ServiceResponseEntity {

    @Column(nullable = false)
    public String name;
    //TODO возможно можно удалить, и использовать requestIdRef
    public String bidId;
    public String actionNs;
    public String action;
    @Column(length = 1024 * 1024)
    public String appData;
    // TODO: нужно более рациональный тип
    @Column(nullable = false)
    public String gservice;
    // TODO: нужно более рациональный тип
    @Column(nullable = false)
    public String status;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date date;
    @Column(nullable = false)
    public String exchangeType;
    public String requestIdRef;
    public String originRequestIdRef;
    public String serviceCode;
    public String caseNumber;
    public String testMsg;
    public String docRequestCode;
    @Id
    @GeneratedValue(generator = "service_response_seq")
    private Long id;
    @OneToMany(mappedBy = "response", fetch = FetchType.LAZY)
    private Set<EnclosureEntity> enclosures;

    /**
     * Для JPA движка
     */
    protected ServiceResponseEntity() {

    }

    //TODO нужно ли различать для статус ответа и на его основе принемать решение об удаление предыдущих объектов в цепочке?
    public ServiceResponseEntity(String bidId, ServerResponse serverResponse) {
        this.bidId = bidId;

        appData = serverResponse.appData;

        if (serverResponse.action != null) {
            action = serverResponse.action.getLocalPart();
            actionNs = serverResponse.action.getNamespaceURI();
        }

        docRequestCode = serverResponse.docRequestCode;

        //TODO разбор пакета вынести
        Packet packet = serverResponse.packet;
        gservice = packet.typeCode.name();
        status = packet.status.name();
        date = packet.date != null ? packet.date : new Date();
        exchangeType = packet.exchangeType;
        requestIdRef = packet.requestIdRef;
        originRequestIdRef = packet.originRequestIdRef;
        serviceCode = packet.serviceCode;
        caseNumber = packet.caseNumber;
        testMsg = packet.testMsg;
        name = "TODO нужно ли оно?";
    }

    public Long getId() {
        return id;
    }

    public ServerResponse getServerResponse() {
        ServerResponse response = new ServerResponse();
        response.packet = new Packet();
        response.packet.serviceCode = serviceCode;
        response.appData = appData;
        response.packet.caseNumber = caseNumber;
        response.packet.typeCode = Packet.Type.valueOf(gservice);
        response.packet.status = Packet.Status.valueOf(status);
        response.packet.exchangeType = exchangeType;
        response.packet.requestIdRef = requestIdRef;
        response.packet.date = date;
        response.packet.originRequestIdRef = originRequestIdRef;
        response.packet.testMsg = testMsg;
        response.docRequestCode = docRequestCode;
        if (action != null || actionNs != null) {
            response.action = new QName(actionNs, action);
        }
        return response;
    }

    public Set<EnclosureEntity> getEnclosures() {
        if (enclosures == null) {
            enclosures = new LinkedHashSet<EnclosureEntity>();
        }
        return enclosures;
    }
}
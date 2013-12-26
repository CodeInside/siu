/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import org.eclipse.persistence.annotations.Index;

import javax.persistence.*;
import java.util.Date;

@Entity
@SequenceGenerator(name = "oep_log_seq", sequenceName = "oep_log_seq")
public class OepLog {

    //скоректировать длину полей

    @Id
    @GeneratedValue(generator = "oep_log_seq")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;                  // Дата
    private String bidId;               // Номер заявки

    @Index(name = "marker_idx")
    private String marker;               // Маркер

    private String infoSystem;          // Информационная система
    private boolean client;             // Тип запроса (клиент / поставщик)
    private String logDate;             // Время первого запроса

    @Column(columnDefinition = "text")
    private String error;             // стек трейс ошибки

    @AttributeOverrides({
        @AttributeOverride(name="sender",column=@Column(name="send_sender")),
        @AttributeOverride(name="recipient",column=@Column(name="send_recipient")),
        @AttributeOverride(name="originator",column=@Column(name="send_originator")),
        @AttributeOverride(name="service",column=@Column(name="send_service")),
        @AttributeOverride(name="typeCode",column=@Column(name="send_typeCode")),
        @AttributeOverride(name="status",column=@Column(name="send_status")),
        @AttributeOverride(name="date",column=@Column(name="send_date")),
        @AttributeOverride(name="requestIdRef",column=@Column(name="send_requestIdRef")),
        @AttributeOverride(name="originRequestIdRef",column=@Column(name="send_originRequestIdRef")),
        @AttributeOverride(name="serviceCode",column=@Column(name="send_serviceCode")),
        @AttributeOverride(name="caseNumber",column=@Column(name="send_caseNumber")),
        @AttributeOverride(name="exchangeType",column=@Column(name="send_exchangeType"))
    })
    @Embedded
    private SoapPackage sendPacket;

    @AttributeOverrides({
        @AttributeOverride(name="sender",column=@Column(name="receive_sender")),
        @AttributeOverride(name="recipient",column=@Column(name="receive_recipient")),
        @AttributeOverride(name="originator",column=@Column(name="receive_originator")),
        @AttributeOverride(name="service",column=@Column(name="receive_service")),
        @AttributeOverride(name="typeCode",column=@Column(name="receive_typeCode")),
        @AttributeOverride(name="status",column=@Column(name="receive_status")),
        @AttributeOverride(name="date",column=@Column(name="receive_date")),
        @AttributeOverride(name="requestIdRef",column=@Column(name="receive_requestIdRef")),
        @AttributeOverride(name="originRequestIdRef",column=@Column(name="receive_originRequestIdRef")),
        @AttributeOverride(name="serviceCode",column=@Column(name="receive_serviceCode")),
        @AttributeOverride(name="caseNumber",column=@Column(name="receive_caseNumber")),
        @AttributeOverride(name="exchangeType",column=@Column(name="receive_exchangeType"))
    })
    @Embedded
    private SoapPackage receivePacket;

    @AttributeOverrides({
        @AttributeOverride(name="data",column=@Column(name="send_data"))
    })
    @Embedded
    private HttpLog sendHttp;

    @AttributeOverrides({
        @AttributeOverride(name="data",column=@Column(name="receive_data"))
    })
    @Embedded
    private HttpLog receiveHttp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getInfoSystem() {
        return infoSystem;
    }

    public void setInfoSystem(String informationSystem) {
        this.infoSystem = informationSystem;
    }

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }

    public SoapPackage getSendPacket() {
        return sendPacket;
    }

    public void setSendPacket(SoapPackage sendPacket) {
        this.sendPacket = sendPacket;
    }

    public SoapPackage getReceivePacket() {
        return receivePacket;
    }

    public void setReceivePacket(SoapPackage receivePacket) {
        this.receivePacket = receivePacket;
    }

    public HttpLog getSendHttp() {
        return sendHttp;
    }

    public void setSendHttp(HttpLog sendSoap) {
        this.sendHttp = sendSoap;
    }

    public HttpLog getReceiveHttp() {
        return receiveHttp;
    }

    public void setReceiveHttp(HttpLog receiveSoap) {
        this.receiveHttp = receiveSoap;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLogDate() {
        return logDate;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
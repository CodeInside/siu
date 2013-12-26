/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api.impl;

import ru.codeinside.gws.api.*;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

final public class LogServiceFileImpl implements LogService {

    final private static Charset UTF8 = Charset.forName("UTF8");

    boolean shouldWriteServerLog = true;
    boolean shouldWriteClientLog = true;

    public String generateMarker() {
        String marker = UUID.randomUUID().toString();
        if(shouldWriteClientLog() || shouldWriteServerLog()){
            saveStringToFile(marker, "Date", new Date().toString());
        }
        return marker;
    }


    @Override
    public void log(String marker, boolean isClient, StackTraceElement[] traceElements) {
        if (isClient && !shouldWriteClientLog()) {
            return;
        }
        if (!isClient && !shouldWriteServerLog()) {
            return;
        }
        String result = "";
        for(StackTraceElement elements : traceElements){
            result += elements.toString() + " \n";
        }
        saveStringToFile(marker, "Error", result);
    }

    @Override
    public void log(String marker, String processInstanceId) {
        if(StringUtils.isEmpty(processInstanceId)){
            return;
        }
        if(shouldWriteClientLog()){
            saveStringToFile(marker, "ProcessInstanceId", processInstanceId);
        }
    }

    @Override
    public void log(String marker, String msg, boolean isRequest, boolean isClient) {
        if (isClient && !shouldWriteClientLog()) {
            return;
        }
        if (!isClient && !shouldWriteServerLog()) {
            return;
        }
        saveStringToFile(marker, "http-" + isRequest + "-" + isClient, msg);
    }

    @Override
    public void log(String marker, ClientRequest request) {
        if (!shouldWriteClientLog()) {
            return;
        }
        savePacketToFile(marker, request.packet, "ClientRequest");
    }

    @Override
    public void log(String marker, ClientResponse response) {
        if (!shouldWriteClientLog()) {
            return;
        }
        savePacketToFile(marker, response.packet, "ClientResponse");
    }

    @Override
    public void log(String marker, ServerRequest request) {
        if (!shouldWriteServerLog()) {
            return;
        }
        savePacketToFile(marker, request.packet, "ServerRequest");
    }

    @Override
    public void log(String marker, ServerResponse response) {
        if (!shouldWriteServerLog()) {
            return;
        }
        savePacketToFile(marker, response.packet, "ServerResponse");
    }

    //оптимизировать
    @Override
    public boolean shouldWriteClientLog() {
        return shouldWriteClientLog;
    }

    //оптимизировать
    @Override
    public boolean shouldWriteServerLog() {
        return shouldWriteServerLog;
    }

    @Override
    public void setShouldWriteClientLog(boolean should) {
        shouldWriteClientLog = should;
    }

    @Override
    public void setShouldWriteServerLog(boolean should) {
        shouldWriteServerLog = should;
    }

    @Override
    public String getPathInfo() {
        return LogSettings.getPath(false);
    }

    @Override
    public String generateMarker(boolean isClient) {
        String marker = UUID.randomUUID().toString();
        if (isClient && !shouldWriteClientLog()) {
            return marker;
        }
        if (!isClient && !shouldWriteServerLog()) {
            return marker;
        }
        saveStringToFile(marker, "Date", new Date().toString());
        return marker;

    }

    public void savePacketToFile(String marker, Packet packet, String name) {
        String soapPackage = createSoapPackage(packet);
        saveStringToFile(marker, name, soapPackage);
    }

    private void saveStringToFile(String marker, String name, String str) {
        try {
            saveBytesToFile(marker, name, str.getBytes(UTF8));
        } catch (RuntimeException e) {
            saveBytesToFile(marker, name, str.getBytes());
        }
    }

    private void saveBytesToFile(String marker, String name, byte[] bytes) {
        try {
            Files.cacheContentToFile(getPathInfo(), marker, bytes, name);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private String createSoapPackage(Packet packet) {
        String result = "";
        result += formattedString(infoSystem(packet.sender));//sender
        result += formattedString(infoSystem(packet.recipient)); //recipient
        result += formattedString(infoSystem(packet.originator)); //originator
        result += formattedString(packet.serviceName); //service
        result += formattedString((packet.typeCode != null ? packet.typeCode.toString() : "")); //typeCode
        result += formattedString((packet.status != null ? packet.status.toString() : "")); //status
        result += formattedString(packet.date != null ? packet.date.toString() : ""); //date
        result += formattedString(packet.requestIdRef); //requestIdRef
        result += formattedString(packet.originRequestIdRef); //originRequestIdRef
        result += formattedString(packet.serviceCode); //serviceCode
        result += formattedString(packet.caseNumber);//caseNumber
        result += formattedString(packet.exchangeType);//exchangeType
        return result;
    }

    static final String splitter = "!!;";

    private String formattedString(String str){
        return (StringUtils.isEmpty(str) ? "" : str) + splitter;
    }

    private String infoSystem(InfoSystem infoSystem) {
        if (infoSystem == null) {
            return "";
        }
        return "code: " + infoSystem.code + " ; name: " + infoSystem.name;
    }

}

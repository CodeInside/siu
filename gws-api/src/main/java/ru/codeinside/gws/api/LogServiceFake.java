/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.util.UUID;

public class LogServiceFake implements LogService {

    private boolean shouldWriteClientLog = true;
    private boolean shouldWriteServerLog = true;

    private static LogService instance = null;

    public static LogService fakeLog(){
        if(instance == null){
            instance = new LogServiceFake();
        }
        return instance;
    }

    @Override
    public String generateMarker() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String generateMarker(boolean isClient) {
        return generateMarker();
    }

    @Override
    public void log(String marker, boolean isClient, StackTraceElement[] traceElements) {
        new Exception().printStackTrace();
        for(StackTraceElement elements : traceElements){
            System.out.println("marker: " + marker + ":" +elements.toString());
        }
    }

    @Override
    public void log(String marker, String processInstanceId) {
        System.out.println("marker: " + marker + ":" +processInstanceId);
    }

    @Override
    public void log(String marker, String msg, boolean isRequest, boolean isClient) {
        System.out.println("marker: " + marker + ":" +msg);
    }

    @Override
    public void log(String marker, ClientRequest request) {
        System.out.println("marker: " + marker + ":" +request.toString());
    }

    @Override
    public void log(String marker, ClientResponse response) {
        System.out.println("marker: " + marker + ":" +response.toString());
    }

    @Override
    public void log(String marker, ServerRequest request) {
        System.out.println("marker: " + marker + ":" +request.toString());
    }

    @Override
    public void log(String marker, ServerResponse response) {
        System.out.println("marker: " + marker + ":" +response.toString());
    }

    @Override
    public boolean shouldWriteClientLog() {
        return shouldWriteClientLog;
    }

    @Override
    public boolean shouldWriteServerLog() {
        return shouldWriteServerLog;
    }

    @Override
    public void setShouldWriteClientLog(boolean should) {
        this.shouldWriteClientLog = should;
    }

    @Override
    public void setShouldWriteServerLog(boolean should) {
        this.shouldWriteServerLog = should;
    }

    @Override
    public String getPathInfo() {
        return null;
    }
}

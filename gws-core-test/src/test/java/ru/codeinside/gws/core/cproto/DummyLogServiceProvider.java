/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import ru.codeinside.gws.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DummyLogServiceProvider implements LogServiceProvider {
    private LogService logService;

    private Map<String, Boolean> checkLog = new HashMap<String, Boolean>();

    public boolean isUsed(){
        return  !checkLog.isEmpty();
    }

    @Override
    public LogService get() {
        if (logService == null) {
            logService = new LogService() {

                @Override
                public String generateMarker() {
                    return UUID.randomUUID().toString();
                }

                @Override
                public void log(String marker, boolean isClient, StackTraceElement[] traceElements) {
                    checkLog.put(marker, true);
                }

                @Override
                public void log(String marker, String processInstanceId) {
                    checkLog.put(marker, true);
                }

                @Override
                public void log(String marker, String msg, boolean isRequest, boolean isClient) {
                    checkLog.put(marker, true);
                }

                @Override
                public void log(String marker, ClientRequest request) {
                    checkLog.put(marker, true);
                }

                @Override
                public void log(String marker, ClientResponse response) {
                    checkLog.put(marker, true);
                }

                @Override
                public void log(String marker, ServerRequest request) {
                    checkLog.put(marker, true);
                }

                @Override
                public void log(String marker, ServerResponse response) {
                    checkLog.put(marker, true);
                }

                @Override
                public boolean shouldWriteClientLog() {
                    return true;
                }

                @Override
                public boolean shouldWriteServerLog() {
                    return true;
                }

                @Override
                public void setShouldWriteClientLog(boolean should) {

                }

                @Override
                public void setShouldWriteServerLog(boolean should) {

                }

                @Override
                public String getPathInfo() {
                    return null;
                }

                @Override
                public String generateMarker(boolean isClient) {
                    return UUID.randomUUID().toString();
                }
            };
        }
        return logService;
    }
}

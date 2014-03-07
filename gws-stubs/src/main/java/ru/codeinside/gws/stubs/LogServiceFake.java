/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.stubs;

import ru.codeinside.gws.api.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogServiceFake implements LogService {

  private boolean shouldWriteServerLog = true;

  @Override
  public String generateMarker() {
    return UUID.randomUUID().toString();
  }

  @Override
  public String generateMarker(boolean isClient) {
    return generateMarker();
  }

  @Override
  public ClientLog createClientLog(final String componentName, final String processInstanceId) {
    return new DebugClientLog(componentName, processInstanceId);
  }

  @Override
  public void log(String marker, boolean isClient, StackTraceElement[] traceElements) {
    new Exception().printStackTrace();
    for (StackTraceElement elements : traceElements) {
      System.out.println("marker: " + marker + ":" + elements.toString());
    }
  }

  @Override
  public void log(String marker, String processInstanceId) {
    System.out.println("marker: " + marker + ":" + processInstanceId);
  }

  @Override
  public void log(String marker, String msg, boolean isRequest, boolean isClient) {
    System.out.println("marker: " + marker + ":" + msg);
  }

  @Override
  public void log(String marker, ServerRequest request) {
    System.out.println("marker: " + marker + ":" + request.toString());
  }

  @Override
  public void log(String marker, ServerResponse response) {
    System.out.println("marker: " + marker + ":" + response.toString());
  }

  @Override
  public boolean shouldWriteServerLog() {
    return shouldWriteServerLog;
  }

  @Override
  public void setShouldWriteServerLog(boolean should) {
    this.shouldWriteServerLog = should;
  }

  @Override
  public String getPathInfo() {
    return null;
  }

  final public static class DebugClientLog implements ClientLog {
    private final Logger logger;
    private final String processInstanceId;

    public DebugClientLog(String componentName, String processInstanceId) {
      this.processInstanceId = processInstanceId;
      logger = Logger.getLogger("ru.codeinside.gws.api.client." + componentName);
      logger.info("create for " + processInstanceId);
    }

    @Override
    public void log(Throwable e) {
      logger.log(Level.INFO, processInstanceId, e);
    }

    @Override
    public OutputStream getHttpOutStream() {
      // TODO: блочный вывод
      return new OutputStream() {
        @Override
        public void write(int b) throws IOException {
          logger.log(Level.INFO, "out: " + ((char) b));
        }
      };
    }

    @Override
    public OutputStream getHttpInStream() {
      // TODO: блочный вывод
      return new OutputStream() {
        @Override
        public void write(int b) throws IOException {
          logger.log(Level.INFO, "in: " + ((char) b));
        }
      };
    }

    @Override
    public void logRequest(ClientRequest request) {
      logger.log(Level.INFO, "request: " + request);
    }

    @Override
    public void logResponse(ClientResponse response) {
      logger.log(Level.INFO, "response: " + response);
    }

    @Override
    public void close() {

    }
  }
}

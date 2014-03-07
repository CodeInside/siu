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

public class DebugLogService implements LogService {

  private boolean shouldWriteServerLog = true;

  @Override
  public ClientLog createClientLog(final String componentName, final String processInstanceId) {
    return new DebugClientLog(componentName, processInstanceId);
  }

  @Override
  public ServerLog createServerLog(String componentName) {
    return shouldWriteServerLog ? new DebugServerLog(componentName) : null;
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

    public DebugClientLog(String componentName, String processInstanceId) {
      logger = Logger.getLogger("ru.codeinside.gws.api.client." + componentName);
      logger.info("create for " + processInstanceId);
    }

    @Override
    public void log(Throwable e) {
      logger.log(Level.INFO, "failure", e);
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

  final public static class DebugServerLog implements ServerLog {
    private final Logger logger;

    public DebugServerLog(String componentName) {
      logger = Logger.getLogger("ru.codeinside.gws.api.client." + componentName);
    }

    @Override
    public void log(Throwable e) {
      logger.log(Level.INFO, "filure", e);
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
    public void logRequest(ServerRequest request) {
      logger.log(Level.INFO, "request: " + request);
    }

    @Override
    public void logResponse(ServerResponse response) {
      logger.log(Level.INFO, "response: " + response);
    }

    @Override
    public void close() {

    }
  }

}

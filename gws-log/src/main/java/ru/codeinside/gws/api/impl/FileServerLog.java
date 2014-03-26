/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gws.api.impl;

import ru.codeinside.gws.api.ServerLog;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.log.format.Metadata;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author xeodon
 * @since 1.0.2
 */
final class FileServerLog implements ServerLog {

  final Metadata metadata = new Metadata();
  final String dirName = UUID.randomUUID().toString().replace("-", "");

  OutputStream httpOut;
  OutputStream httpIn;

  public FileServerLog(String componentName) {
    metadata.componentName = componentName;
    metadata.date = new Date();
    Files.writeMetadataToSpool(metadata, dirName);
  }

  @Override
  public void log(Throwable e) {
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    metadata.error = sw.toString();
    Files.writeMetadataToSpool(metadata, dirName);
  }

  @Override
  public OutputStream getHttpOutStream() {
    if (httpOut == null) {
      try {
        File file = Files.createSpoolFile("http-" + true + "-" + false, dirName);
        httpOut = Files.fileOut(file);
      } catch (FileNotFoundException e) {
        LogServiceFileImpl.LOGGER.log(Level.WARNING, "create spool file fail", e);
        httpOut = new NullOutputStream();
      }
    }
    return httpOut;
  }

  @Override
  public OutputStream getHttpInStream() {
    if (httpIn == null) {
      try {
        File file = Files.createSpoolFile("http-" + false + "-" + false, dirName);
        httpIn = Files.fileOut(file);
      } catch (FileNotFoundException e) {
        LogServiceFileImpl.LOGGER.log(Level.WARNING, "create spool file fail", e);
        httpIn = new NullOutputStream();
      }
    }
    return httpIn;
  }

  @Override
  public void logRequest(ServerRequest request) {
    if (request != null && request.packet != null) {
      metadata.serverRequest = Files.getPack(request.packet);
      Files.writeMetadataToSpool(metadata, dirName);
    }
  }

  @Override
  public void logResponse(ServerResponse response) {
    if (response != null && response.packet != null) {
      metadata.serverResponse = Files.getPack(response.packet);
      Files.writeMetadataToSpool(metadata, dirName);
    }
  }

  @Override
  public void close() {
    Files.close(httpOut, httpIn);
    Files.moveFromSpool(dirName);
  }
}

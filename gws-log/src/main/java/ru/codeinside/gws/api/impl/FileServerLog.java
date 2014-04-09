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
final class FileServerLog extends FileLog implements ServerLog {

  public FileServerLog(String componentName) {
    metadata.componentName = componentName;
    metadata.date = new Date();
    Files.writeMetadataToSpool(metadata, dirName);
  }

  @Override
  public void logRequest(ServerRequest request) {
    if (request != null && request.packet != null) {
      logReceivePacket(request.packet);
    }
  }

  @Override
  public void logResponse(ServerResponse response) {
    if (response != null && response.packet != null) {
      logSendPacket(response.packet);
    }
  }
}

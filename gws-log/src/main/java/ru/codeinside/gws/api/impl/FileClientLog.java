/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gws.api.impl;

import ru.codeinside.gws.api.ClientLog;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.log.format.Metadata;
import ru.codeinside.gws.log.format.Pack;

import java.io.File;
import java.io.FileNotFoundException;
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
final class FileClientLog extends FileLog implements ClientLog {

  public FileClientLog(long bid, String componentName, String processInstanceId) {
    metadata.bid = bid;
    metadata.componentName = componentName;
    metadata.date = new Date();
    metadata.processInstanceId = processInstanceId;
    metadata.client = true;
    Files.writeMetadataToSpool(metadata, dirName);
  }

  @Override
  public void logRequest(ClientRequest request) {
    if (request != null && request.packet != null) {
      logSendPacket(request.packet);
    }
  }

  @Override
  public void logResponse(ClientResponse response) {
    if (response != null && response.packet != null) {
      logReceivePacket(response.packet);
    }
  }

}

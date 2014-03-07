/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api.impl;

import ru.codeinside.gws.api.ClientLog;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.LogService;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.ServerLog;

import java.nio.charset.Charset;
import java.util.logging.Logger;

final public class LogServiceFileImpl implements LogService {

  final static Charset UTF8 = Charset.forName("UTF8");
  final static Logger LOGGER = Logger.getLogger(LogServiceFileImpl.class.getName());
  final static String DELIMITER = "!!;";

  boolean shouldWriteServerLog = true;

  @Override
  public void setShouldWriteServerLog(boolean should) {
    shouldWriteServerLog = should;
  }

  @Override
  public String getPathInfo() {
    return LogSettings.getPath(false);
  }


  @Override
  public ClientLog createClientLog(String componentName, String processInstanceId) {
    return new FileClientLog(componentName, processInstanceId);
  }

  @Override
  public ServerLog createServerLog(String componentName) {
    return shouldWriteServerLog ? new FileServerLog(componentName) : null;
  }

  static String createSoapPackage(Packet packet) {
    return
      formattedString(infoSystem(packet.sender))
        + formattedString(infoSystem(packet.recipient))
        + formattedString(infoSystem(packet.originator))
        + formattedString(packet.serviceName)
        + formattedString((packet.typeCode != null ? packet.typeCode.toString() : ""))
        + formattedString((packet.status != null ? packet.status.toString() : ""))
        + formattedString(packet.date != null ? packet.date.toString() : "")
        + formattedString(packet.requestIdRef)
        + formattedString(packet.originRequestIdRef)
        + formattedString(packet.serviceCode)
        + formattedString(packet.caseNumber)
        + formattedString(packet.exchangeType);
  }


  private static String formattedString(String str) {
    return (StringUtils.isEmpty(str) ? "" : str) + DELIMITER;
  }

  private static String infoSystem(InfoSystem infoSystem) {
    if (infoSystem == null) {
      return "";
    }
    return "code: " + infoSystem.code + " ; name: " + infoSystem.name;
  }

}

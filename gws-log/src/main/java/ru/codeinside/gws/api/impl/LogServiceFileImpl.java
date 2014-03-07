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
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

final public class LogServiceFileImpl implements LogService {

  final static Charset UTF8 = Charset.forName("UTF8");
  final static Logger LOGGER = Logger.getLogger(LogServiceFileImpl.class.getName());
  final static String DELIMITER = "!!;";

  boolean shouldWriteServerLog = true;

  public String generateMarker() {
    String marker = UUID.randomUUID().toString();
    if (shouldWriteServerLog()) {
      saveStringToFile(marker, "Date", new Date().toString());
    }
    return marker;
  }


  @Override
  public void log(String marker, boolean isClient, StackTraceElement[] traceElements) {
    if (!isClient && !shouldWriteServerLog()) {
      return;
    }
    String result = "";
    for (StackTraceElement elements : traceElements) {
      result += elements.toString() + " \n";
    }
    saveStringToFile(marker, "Error", result);
  }

  @Override
  public void log(String marker, String processInstanceId) {
  }

  @Override
  public void log(String marker, String msg, boolean isRequest, boolean isClient) {
    if (!isClient && !shouldWriteServerLog()) {
      return;
    }
    saveStringToFile(marker, "http-" + isRequest + "-" + isClient, msg);
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

  @Override
  public boolean shouldWriteServerLog() {
    return shouldWriteServerLog;
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
    if (!isClient && !shouldWriteServerLog()) {
      return marker;
    }
    saveStringToFile(marker, "Date", new Date().toString());
    return marker;

  }

  @Override
  public ClientLog createClientLog(String componentName, String processInstanceId) {
    return new FileClientLog(componentName, processInstanceId);
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

  public void savePacketToFile(String marker, Packet packet, String name) {
    String soapPackage = createSoapPackage(packet);
    saveStringToFile(marker, name, soapPackage);
  }

  static String createSoapPackage(Packet packet) {
    return
      LogServiceFileImpl.formattedString(LogServiceFileImpl.infoSystem(packet.sender))
        + LogServiceFileImpl.formattedString(LogServiceFileImpl.infoSystem(packet.recipient))
        + LogServiceFileImpl.formattedString(LogServiceFileImpl.infoSystem(packet.originator))
        + LogServiceFileImpl.formattedString(packet.serviceName)
        + LogServiceFileImpl.formattedString((packet.typeCode != null ? packet.typeCode.toString() : ""))
        + LogServiceFileImpl.formattedString((packet.status != null ? packet.status.toString() : ""))
        + LogServiceFileImpl.formattedString(packet.date != null ? packet.date.toString() : "")
        + LogServiceFileImpl.formattedString(packet.requestIdRef)
        + LogServiceFileImpl.formattedString(packet.originRequestIdRef)
        + LogServiceFileImpl.formattedString(packet.serviceCode)
        + LogServiceFileImpl.formattedString(packet.caseNumber)
        + LogServiceFileImpl.formattedString(packet.exchangeType);
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

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api.impl;

import org.codehaus.jackson.map.ObjectMapper;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.log.format.Metadata;
import ru.codeinside.gws.log.format.Pack;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Утилитные функции по работе с файлами.
 */
final public class Files {

  /**
   * Создать абсолютный путь к кеш-файлу внутри временного каталога.
   * Тривиальная схема именования prefix-id-file
   */
  static File createCacheFileName(String path, String id, String prefix, String suffix) {
    String fileName = prefix + "-" + suffix.replace(File.separatorChar, '_');
    return new File(getAppTmpDir(path, id), fileName);
  }

  /**
   * Получить и кешировать в сессии временный каталог приложения.
   */
  static File getAppTmpDir(String path, String suf) {
    File tmpDir = new File(path + File.separator + suf);
    tmpDir.mkdirs();
    return tmpDir;
  }

  static void moveFromSpool(String marker) {
    Logger logger = LogServiceFileImpl.LOGGER;
    File source = Files.getAppTmpDir(LogSettings.getPath(true), marker);

//    Вринат разбиения по каталогам:
    File target0 = Files.getAppTmpDir(LogSettings.getPath(false), marker.substring(marker.length()-2, marker.length()-1));
    File target1 = new File(target0, marker.substring(marker.length()-1));
    if (!target1.exists() && !target1.mkdir()) {
      logger.log(Level.WARNING, "can't create " + target1);
      return;
    }
    File target = new File(target1, marker);

//    File target = Files.getAppTmpDir(LogSettings.getPath(false), UUID.randomUUID().toString().replace("-", ""));
    if (target.exists()) {
      target.delete();
    }

    if (!source.renameTo(target)) {
      logger.log(Level.INFO, "move from spool (override?)");
      File[] files = source.listFiles();
      if (files != null) {
        for (File file : files) {
          if (!file.renameTo(new File(target, file.getName()))) {
            logger.log(Level.INFO, "can't move " + file);
          }
        }
      }
      if (!source.delete()) {
        logger.log(Level.INFO, "can't delete " + source);
      }
    }
  }

  static File createSpoolFile(String name, String marker) {
    return createCacheFileName(LogSettings.getPath(true), marker, "log", name);
  }

  static void writeMetadataToSpool(Metadata metadata, String marker) {
    try {
      File spoolFile = Files.createSpoolFile("metadata", marker);
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.writeValue(new BufferedOutputStream(new FileOutputStream(spoolFile), 16*1024), metadata);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static Pack getPack (Packet packet) {
    Pack pack= new Pack();
    pack.caseNumber = packet.caseNumber;
    pack.date = packet.date;
    pack.exchangeType = packet.exchangeType;
    pack.oktmo = packet.oktmo;
    pack.originator = packet.originator.code;
    pack.originRequestIdRef = packet.originRequestIdRef;
    pack.recipient = packet.recipient.code;
    pack.requestIdRef = packet.requestIdRef;
    pack.sender = packet.sender.code;
    pack.serviceCode = packet.serviceCode;
    pack.serviceName = packet.serviceName;
    pack.status = String.valueOf(packet.status);
    pack.testMsg = packet.testMsg;
    pack.typeCode = String.valueOf(packet.typeCode);
    return pack;
  }

}

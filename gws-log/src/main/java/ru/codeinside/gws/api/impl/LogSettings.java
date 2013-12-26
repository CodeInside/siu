/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api.impl;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс-помощник для получения сведений о профиле под которым запущено приложение
 */
public class LogSettings {

  private static final String PRODUCTION_PATH = "/var/log";
  private static final String SPOOL_PATH = "/var/spool";

  public static String getPath(boolean isSpool) {
    String instanceRoot = System.getProperty("com.sun.aas.instanceRoot");
    if (instanceRoot != null) {
      return isSpool?
        new File(new File(new File(instanceRoot), "logs"), "spool").getAbsolutePath():
        new File(new File(new File(instanceRoot), "logs"), "smev").getAbsolutePath();
    }

    Properties properties = new Properties();
    String path = null;
    try {
      properties.load(LogSettings.class.getResourceAsStream("/profile/profile.properties"));
      path = isSpool?
        properties.getProperty("spool.path"):
        properties.getProperty("directory.path");
    } catch (IOException e) {
      Logger.getLogger(LogSettings.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
    return isSpool?
      StringUtils.defaultIfEmpty(path, SPOOL_PATH):
      StringUtils.defaultIfEmpty(path, PRODUCTION_PATH);
  }


}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.utils;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс-помощник для получения сведений о профиле под которым запущено приложение
 */
public class RunProfile {
  private static final String DEVELOPMENT_PROFILE = "development";
  private static final String PRODUCTION_PROFILE = "production";
  private static final String CURRENT_PROFILE_NAME = getRunProfileName();

  private static String getRunProfileName() {
    Properties properties = new Properties();
    String runProfileName = null;
    try {
      properties.load(RunProfile.class.getResourceAsStream("/profile/profile.properties"));
      runProfileName = properties.getProperty("run.profile");
    } catch (IOException e) {
      Logger.getLogger(RunProfile.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
    return StringUtils.defaultIfEmpty(runProfileName, PRODUCTION_PROFILE);
  }

  public static boolean isProduction() {
    // если не development, то считать production - вдруг сборка поломается
    return !DEVELOPMENT_PROFILE.equals(CURRENT_PROFILE_NAME);
  }
}

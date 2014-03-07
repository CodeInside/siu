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

enum LogSettings {

  ;

  static File logRoot;

  public synchronized static String getPath(boolean isSpool) {
    if (logRoot == null) {
      File asRoot = new File(System.getProperty("com.sun.aas.instanceRoot"));
      logRoot = new File(asRoot, "logs");
    }
    return new File(logRoot, isSpool ? "spool" : "smev").getAbsolutePath();
  }

  static synchronized void setLogRoot(File file) {
    logRoot = file;
  }

}

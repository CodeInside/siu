/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;

final class LogCleaner implements Runnable {

  final LogConverter converter;

  LogCleaner(LogConverter converter) {
    this.converter = converter;
  }

  public void run() {
    String logDepth = AdminServiceProvider.get().getSystemProperty(LogScheduler.LOG_DEPTH);
    if (logDepth != null && logDepth.matches("[1-9][0-9]*")) {
      converter.logToZip(Integer.valueOf(logDepth));
    } else {
      converter.logToZip(14);
    }
  }
}

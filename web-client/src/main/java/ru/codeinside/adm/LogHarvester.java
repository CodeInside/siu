/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

final class LogHarvester implements Runnable {

  final LogConverter converter;
  final ScheduledExecutorService executor;

  int delay = 500;

  LogHarvester(LogConverter converter, ScheduledExecutorService executor) {
    this.converter = converter;
    this.executor = executor;
  }

  @Override
  public void run() {

    if (converter.logToBd()) {
      delay = 500;
    } else {
      delay = Math.min(7000, (int) (delay * 1.5));
    }

    if (!executor.isShutdown()) {
      executor.schedule(this, delay, TimeUnit.MILLISECONDS);
    }
  }
}

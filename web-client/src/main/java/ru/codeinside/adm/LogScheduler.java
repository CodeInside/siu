/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Startup
@DependsOn("BaseBean")
@Lock(LockType.READ)
public class LogScheduler {

  transient static LogConverter logConverterInstance;
  transient static ScheduledExecutorService executorInstance;

  /**
   * используем аннотацию EJB, так как Weld(Inject) ведёт себя не адекватно с классами без реализации интерфейса.
   */
  @EJB
  LogConverter logConverter;

  ScheduledExecutorService executor;

  @PostConstruct
  public void init() {
    logConverterInstance = logConverter;
    executor = Executors.newSingleThreadScheduledExecutor();
    executorInstance = executor;
    int initialDelay = 24 - Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

    executor.scheduleAtFixedRate(new LogCleaner(logConverter), initialDelay, 24, TimeUnit.HOURS);
    executor.schedule(new LogHarvester(logConverter, executor), 10, TimeUnit.SECONDS);
    logConverter = null;
  }

  @PreDestroy
  public void shutdown() {
    executor.shutdownNow();
    executor = null;
  }

  public static void cleanLog() {
    executorInstance.schedule(new LogCleaner(logConverterInstance), 0, TimeUnit.SECONDS);
  }

}

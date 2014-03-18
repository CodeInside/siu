/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;

import org.apache.derby.iapi.tools.run;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Startup
@DependsOn("BaseBean")
public class LogScheduler {


  public static final int CLEAN_LOG_PERIOD_HOURS = 24;
  public static final int CLEAN_LOG_DEPTH_DAYS = 14;

  @Inject
  LogConverter logConverter;

  transient static LogConverter instance;

  private static ScheduledExecutorService executor;

  @PostConstruct
  public void init() {
    synchronized (LogConverter.class) {
      if (instance == null) {
        instance = logConverter;
      }
    }
    executor = Executors.newSingleThreadScheduledExecutor();

    /*executor.scheduleWithFixedDelay(new Runnable() {
      @Override
      public void run() {
        get().logToBd();
      }
    }, 10, 10, TimeUnit.SECONDS);*/
//    executor.schedule(new Runnable() {
//      @Override
//      public void run() {
//        get().logToBd();
//      }
//    }, 10, TimeUnit.SECONDS);
    scheduleToBd(10);

    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int initialDelay = 24 - hour;
    if (initialDelay > 0) {
      executor.schedule(new Runnable() {
        public void run() {
          get().logToZip(CLEAN_LOG_DEPTH_DAYS);
        }
      }, 20, TimeUnit.SECONDS);
    }
    executor.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        get().logToZip(CLEAN_LOG_DEPTH_DAYS);
      }
    }, initialDelay, CLEAN_LOG_PERIOD_HOURS, TimeUnit.HOURS);

  }

  @PreDestroy
  public void shutdown() {
    if (executor != null) {
      executor.shutdownNow();
      executor = null;
    }
    synchronized (LogConverter.class) {
      if (instance == logConverter) {
        instance = null;
      }
    }
  }

  public static LogConverter get() {
    if (instance == null) {
      throw new IllegalStateException("Сервис не зарегистрирован!");
    }
    return instance;
  }

  private void scheduleToBd(int floatDelay) {
    executor.schedule(new Runnable() {
      @Override
      public void run() {
        scheduleToBd(get().logToBd());
      }
    }, floatDelay, TimeUnit.SECONDS);
  }
}

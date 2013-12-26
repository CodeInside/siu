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
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Startup
@DependsOn("BaseBean")
public class LogScheduler {


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

        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                get().logToBd();
            }
        }, 1, 3, TimeUnit.MINUTES);
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
}

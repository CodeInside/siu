/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import ru.codeinside.gses.liquibase.api.MigrationService;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.osgi.Renovation;
import ru.codeinside.gses.webui.utils.RunProfile;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import static javax.ejb.TransactionManagementType.CONTAINER;

/**
 * Это должен быть начальный сервис, от которого зависят остальные.
 * <p/>
 * Из-за того, что в активаторе бандла не можем находится долго,
 * основная операция миграции происходит тут.
 */
@Singleton
@Startup
@TransactionManagement(CONTAINER)
public class BaseBean {

  final private Logger logger = Logger.getLogger(getClass().getName());

  @TransactionAttribute(REQUIRES_NEW)
  @PostConstruct
  public void migration() throws Exception {
    if (RunProfile.isProduction()) {
      final BundleContext context = Activator.getContext();
      if (context == null) {
        logger.warning("Режим WAR, активатор модуля не доступен!");
        return;
      }
      final String serviceName = MigrationService.class.getName();

      ServiceReference reference = context.getServiceReference(serviceName);
      if (reference == null) {
        logger.fine("Ждём инициализации сервиса миграции gses-liquibase...");
        final long timeout = 1000L * 30L + System.currentTimeMillis();
        while (reference == null) {
          Thread.sleep(100);
          reference = context.getServiceReference(serviceName);
          if (reference == null && timeout > System.currentTimeMillis()) {
            throw new IllegalStateException("Отсутсвует сервис миграции gses-liquibase!");
          }
        }
      }

      final long startAt = System.currentTimeMillis();
      try {
        final MigrationService service = (MigrationService) context.getService(reference);
        new Renovation().renovateUnderTx(service);
      } finally {
        context.ungetService(reference);
      }

      final long millis = System.currentTimeMillis() - startAt;
      final long seconds = TimeUnit.SECONDS.convert(millis, TimeUnit.MILLISECONDS);

      final long globalMillis = System.currentTimeMillis() - Activator.getStartTimeMillis();
      final long globalSeconds = TimeUnit.SECONDS.convert(globalMillis, TimeUnit.MILLISECONDS);

      logger.info("\n--- Миграция за " + seconds + " из " + globalSeconds + " секунд ---\n");
    }
  }

}

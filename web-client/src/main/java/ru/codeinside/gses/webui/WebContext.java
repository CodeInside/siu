/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import commons.Streams;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.utils.RunProfile;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@WebListener
@Singleton
public class WebContext implements ServletContextListener {

  final Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  ActivitiJobProvider activitiJobProvider;

  @Override
  public void contextInitialized(final ServletContextEvent event) {
    System.setProperty("ru.codeinside.gses.webui.productionMode", Boolean.toString(RunProfile.isProduction()));

    activitiJobProvider.startNow();

    File tmpDir = (File) event.getServletContext().getAttribute(ServletContext.TEMPDIR);
    Streams.init(tmpDir);

    final long millis = System.currentTimeMillis() - Activator.getStartTimeMillis();
    final long seconds = TimeUnit.SECONDS.convert(millis, TimeUnit.MILLISECONDS);
    logger.info("\n--- Запуск WEB клиента завершён за " + seconds + " секунд ---\n");
  }

  @Override
  public void contextDestroyed(final ServletContextEvent unused) {
    logger.info("\n--- Остановка WEB клиента ---\n");
  }
}

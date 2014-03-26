/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import ru.codeinside.gses.webui.utils.RunProfile;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.LogService;
import ru.codeinside.gws.api.Server;

/**
 * Варинат с активатором.
 */
final public class Activator implements BundleActivator {

  private ServiceTracker tracker;
  private ServiceTracker serverTracker;
  private ServiceTracker logTracker;

  private static long startTimeMillis;
  private static BundleContext CONTEXT;


  public static long getStartTimeMillis() {
    return startTimeMillis;
  }

  public static BundleContext getContext() {
    return CONTEXT;
  }

  @Override
  public void start(final BundleContext bundleContext) throws Exception {

    startTimeMillis = System.currentTimeMillis();
    CONTEXT = bundleContext;

    final Renovation renovation = new Renovation();
    if (RunProfile.isProduction()) {
      renovation.validateResources();
      renovation.validatePersistence();
    }

    tracker = new ServiceTracker(bundleContext, ru.codeinside.gws.api.Client.class.getName(), new TRefEvents<Client>(bundleContext));
    tracker.open();

    serverTracker = new ServiceTracker(bundleContext, ru.codeinside.gws.api.Server.class.getName(), new TRefEvents<Server>(bundleContext));
    serverTracker.open();

    logTracker = new ServiceTracker(bundleContext, LogService.class.getName(), new LogCustomizer(bundleContext));
    logTracker.open();
  }

  @Override
  public void stop(final BundleContext bundleContext) throws Exception {
    CONTEXT = null;

    if (serverTracker != null) {
      serverTracker.close();
    }
    if (tracker != null) {
      tracker.close();
    }
    if (logTracker != null) {
      logTracker.close();
    }
  }

}
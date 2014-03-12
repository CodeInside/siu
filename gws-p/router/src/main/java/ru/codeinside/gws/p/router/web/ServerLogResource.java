/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router.web;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import ru.codeinside.gws.api.LogService;
import ru.codeinside.gws.api.ServerLog;

import javax.servlet.http.HttpServletRequest;
import java.io.Closeable;

final public class ServerLogResource implements Closeable {

  final BundleContext bundleContext;
  final ServiceReference reference;

  public ServerLogResource(BundleContext bundleContext, ServiceReference reference) {
    this.bundleContext = bundleContext;
    this.reference = reference;
  }

  public ServerLog createLog(HttpServletRequest req) {
    if (reference == null) {
      return null;
    }
    String componentName = Chain.getService(req);
    LogService logService = (LogService) bundleContext.getService(reference);
    return logService.createServerLog(componentName);
  }

  @Override
  public void close() {
    if (reference != null) {
      bundleContext.ungetService(reference);
    }
  }
}

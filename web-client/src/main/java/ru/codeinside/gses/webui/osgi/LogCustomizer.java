/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gws.api.LogService;
import ru.codeinside.gws.api.LogServiceFake;

public class LogCustomizer implements ServiceTrackerCustomizer {

    final BundleContext bundleContext;
    private static LogService logService = null;

    LogCustomizer(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public Object addingService(ServiceReference serviceReference) {
        final Object service = bundleContext.getService(serviceReference);
        if(service instanceof LogService){
            logService = (LogService) service;
            initLogService();
        }
        return logService;
    }

    @Override
    public void modifiedService(ServiceReference serviceReference, Object o) {
        if(o instanceof LogService){
            logService = (LogService) o;
            initLogService();
        }
    }

    private void initLogService() {
        if(AdminServiceProvider.tryGet() != null){
            logService.setShouldWriteClientLog(AdminServiceProvider.getBoolProperty(LogService.httpTransportPipeDump));
            logService.setShouldWriteServerLog(AdminServiceProvider.getBoolProperty(LogService.httpAdapterDump));
        }
    }

    @Override
    public void removedService(ServiceReference serviceReference, Object o) {
        if(o instanceof LogService){
            logService = null;
        }
    }

    public static LogService getLogger(){
        if(logService == null){
            return LogServiceFake.fakeLog();
        }
        return logService;
    }
}

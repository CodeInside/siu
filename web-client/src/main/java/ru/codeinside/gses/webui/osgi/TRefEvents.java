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

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

final class TRefEvents<T> implements ServiceTrackerCustomizer {

    final BundleContext bundleContext;
    final Logger log = Logger.getLogger(getClass().getName());
    final Level logLevel = Level.FINE;

    TRefEvents(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public Object addingService(final ServiceReference serviceReference) {
        if (log.isLoggable(logLevel)) {
            log.log(logLevel, "Add SEMV serviceReference:\n" + getInfo(serviceReference));
        }
        final Object client = bundleContext.getService(serviceReference);
        final TRefImpl<T> serviceRef = new TRefImpl<T>(serviceReference, (T) client);
        TRefRegistryImpl.add(serviceReference, serviceRef);
        return client;
    }

    @Override
    public void modifiedService(final ServiceReference serviceReference, Object client) {
        if (log.isLoggable(logLevel)) {
            log.log(logLevel, "Modify SMEV serviceReference:\n" + getInfo(serviceReference));
        }
        TRefRegistryImpl.update(serviceReference, client);
    }

    @Override
    public void removedService(final ServiceReference serviceReference, Object client) {
        if (log.isLoggable(logLevel)) {
            log.log(logLevel, "Remove SMEV  serviceReference:\n" + getInfo(serviceReference));
        }
        TRefRegistryImpl.remove(serviceReference);
    }

    private String getInfo(ServiceReference serviceReference) {
        final StringBuilder sb = new StringBuilder();
        for (String key : serviceReference.getPropertyKeys()) {
            sb.append(key);
            sb.append('=');
            Object value = serviceReference.getProperty(key);
            if (value instanceof String[]) {
                sb.append(Arrays.asList((String[]) value));
            } else {
                sb.append(value);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

}

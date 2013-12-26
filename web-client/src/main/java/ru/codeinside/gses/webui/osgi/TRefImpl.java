/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.osgi;

import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import ru.codeinside.gses.webui.gws.TRef;

import java.io.Serializable;

final class TRefImpl<T> implements TRef<T> {

    private static final long serialVersionUID = -814154250062325413L;
    final String name;
    final String symbolicName;
    final Version version;
    final String location;
    final T server;

    TRefImpl(ServiceReference reference, T server) {
        Object name = reference.getProperty("component.name");
        this.name =  name != null ? name.toString() : getSymbolicName();
        symbolicName = reference.getBundle().getSymbolicName();
        version = reference.getBundle().getVersion();
        location = reference.getBundle().getLocation();
        this.server = server;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSymbolicName() {
        return symbolicName;
    }

    @Override
    public String getVersion() {
        return version != null ? version.toString() : "";
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public T getRef() {
        return server;
    }

    @Override
    public String toString() {
        return "{" + getSymbolicName() + ", " + getVersion() + "}";
    }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.osgi;

import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import ru.codeinside.gses.webui.gws.ClientRefRegistry;
import ru.codeinside.gses.webui.gws.ServiceRefRegistry;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.Server;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

//TODO Выстроить иерархию, шаблоны 
final public class TRefRegistryImpl implements ServiceRefRegistry, ClientRefRegistry {

    final static IdentityHashMap<ServiceReference, TRefImpl<Server>> services = new IdentityHashMap<ServiceReference, TRefImpl<Server>>();
    final static IdentityHashMap<ServiceReference, TRefImpl<Client>> clients = new IdentityHashMap<ServiceReference, TRefImpl<Client>>();

    static void add(final ServiceReference ref, final TRefImpl<?> serviceRef) {
        if (serviceRef.getRef() instanceof Server) {
            synchronized (services) {
                //System.out.println("Add SEMV services");
                services.put(ref, (TRefImpl<Server>) serviceRef);
                //System.out.println("ref " + ref.getPropertyKeys() + " " + ref.getBundle().toString());
            }
        }
        if (serviceRef.getRef() instanceof Client) {
            synchronized (clients) {
                //System.out.println("Add SEMV clients");
                clients.put(ref, (TRefImpl<Client>) serviceRef);
                //System.out.println("ref " + ref.getPropertyKeys() + " " + ref.getBundle().toString());
            }
        }
    }

    static void update(final ServiceReference ref, final Object obj) {
        boolean ok = false;
        if (obj instanceof Server) {
            synchronized (services) {
                TRefImpl<Server> clientRef = services.get(ref);
                ok = clientRef != null && clientRef.server == obj;
            }
        }
        if (obj instanceof Client) {
            synchronized (clients) {
                TRefImpl<Client> clientRef = clients.get(ref);
                ok = clientRef != null && clientRef.server == obj;
            }
        }
        if (!ok) {
            new Throwable("Something wrong").printStackTrace();
        }
    }

    static void remove(ServiceReference ref) {
        synchronized (services) {
            services.remove(ref);
        }
        synchronized (clients) {
            clients.remove(ref);
        }
    }

    public static List<TRef<Server>> getServerRefs() {
        return getRefs(services);
    }

    public static <T> List<TRef<T>> getRefs(IdentityHashMap<ServiceReference, TRefImpl<T>> maps) {
        final List<TRef<T>> result = new ArrayList<TRef<T>>();
        synchronized (maps) {
            result.addAll(maps.values());
        }
        return result;
    }

    @Override
    public List<TRef<Server>> getServiceRefs() {
        return getRefs(services);
    }

    @Override
    public List<TRef<Client>> getClientRefs() {
        return getRefs(clients);
    }

    @Override
    public TRef<Client> getClientByNameAndVersion(final String name, final String version) {
        return getByNameAndVersion(clients, name, version);
    }

    public <T> TRef<T> getByNameAndVersion(IdentityHashMap<ServiceReference, TRefImpl<T>> maps, final String name, final String version) {
        TRefImpl<T> found = null;
        synchronized (maps) {
            for (final TRefImpl<T> ref : maps.values()) {
                //System.out.println("name s " + ref.getName());
                //System.out.println("version s " + ref.getVersion());
                if (name.equals(ref.getName())) {
                    if (found == null) {
                        found = ref;
                    } else {
                        // ищем совпадение Major и Minor версии
                        String[] splitVersion = version.split("\\.");
                        Version bversion = ref.version;
                        int major = Integer.parseInt(splitVersion[0]);
                        int minor = Integer.parseInt(splitVersion[1]);
                        if (bversion.getMajor() == major && bversion.getMinor() == minor && bversion.getMicro() > found.version.getMicro()) {
                            found = ref;
                        }
                    }
                }
            }
        }
        return found;
    }

    public <T> TRef<T> getByName(IdentityHashMap<ServiceReference, TRefImpl<T>> maps, final String name) {
        TRefImpl<T> found = null;
        synchronized (maps) {
            int major = 0;
            int minor = 0;
            int micro = 0;
            for (final TRefImpl<T> ref : maps.values()) {
                //System.out.println("name s " + ref.getName());
                //System.out.println("version s " + ref.getVersion());
                if (name.equals(ref.getName())) {
                    if (found == null) {
                        found = ref;
                        Version bversion = ref.version;
                        major = bversion.getMajor();
                        minor = bversion.getMinor();
                        micro = bversion.getMinor();
                    } else {
                        // Максимальную версию
                        Version bversion = ref.version;
                        if (bversion.getMajor() >= major && bversion.getMinor() >= minor && bversion.getMicro() > micro) {
                            found = ref;
                            major = bversion.getMajor();
                            minor = bversion.getMinor();
                            micro = bversion.getMinor();
                        }
                    }
                }
            }
        }
        return found;
    }

    @Override
    public TRef<Server> getServerByName(String name) {
        return getByName(services, name);
    }

}
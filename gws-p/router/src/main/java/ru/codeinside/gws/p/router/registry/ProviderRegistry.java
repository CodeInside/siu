/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router.registry;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import ru.codeinside.gws.api.Declarant;
import ru.codeinside.gws.api.LogService;
import ru.codeinside.gws.api.LogServiceFake;
import ru.codeinside.gws.api.LogServiceProvider;
import ru.codeinside.gws.api.ProtocolFactory;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerProtocol;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.api.ServiceDefinitionParser;
import ru.codeinside.gws.p.adapter.ProviderEntry;
import ru.codeinside.gws.p.adapter.Registry;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class ProviderRegistry implements ru.codeinside.gws.p.registry.api.ProviderRegistry, Registry, LogServiceProvider {

  final Logger logger = Logger.getLogger(getClass().getName());

  private LogService logService;

  /**
   * Все события обрабатываются в одном потоке.
   */
  final ScheduledExecutorService eventDriver = Executors.newSingleThreadScheduledExecutor();

  final ProviderEntry EMPTY = new ProviderEntry();
  final boolean CLEAR_DEFINITION = false;

  final HashMap<String, ProviderEntry> entries = new LinkedHashMap<String, ProviderEntry>();
  final HashMap<String, ServiceReference> providerRefs = new LinkedHashMap<String, ServiceReference>();
  final Set<String> registry = new LinkedHashSet<String>();

  BundleContext bundleContext;
  Declarant declarant;
  ServiceDefinitionParser serviceDefinitionParser;
  ProtocolFactory protocolFactory;


  public void activate(final ComponentContext context) {
    REGISTRY.set(ProviderRegistry.this);
    logger.info("Активация маршрутизатора СМЭВ");
    eventDriver.submit(new Runnable() {
      @Override
      public void run() {
        bundleContext = context.getBundleContext();
      }
    });
  }

  public void deactivate(final ComponentContext context) {
    REGISTRY.set(null);
    eventDriver.shutdownNow();
    logger.info("Выключение маршрутизатора СМЭВ");
  }

  public void add(final ServiceReference serviceReference) {
    eventDriver.submit(new Runnable() {
      @Override
      public void run() {
        if (bundleContext == null) {
          // ждём инициализации
          eventDriver.schedule(this, 25, TimeUnit.MILLISECONDS);
        } else {
          logger.info("Регистрация поставщика: " + getInfo(serviceReference));
          final String name = (String) serviceReference.getProperty("component.name");
          if (name != null) {
            // саморегистрация поставщика
            entries.put(name, EMPTY);
            addProvider(serviceReference, name);
          }
        }
      }
    });
  }

  public void remove(final ServiceReference serviceReference) {
    execute(new Runnable() {
      @Override
      public void run() {
        logger.info("Удаление поставщика: " + getInfo(serviceReference));
        final String name = (String) serviceReference.getProperty("component.name");
        if (name != null) {
          removeProvider(name);
        }
      }
    });
  }

  public void addLogService(final LogService log) {
    eventDriver.submit(new Runnable() {
      @Override
      public void run() {
        if (bundleContext == null) {
          // ждём инициализации
          eventDriver.schedule(this, 25, TimeUnit.MILLISECONDS);
        } else {
          logger.info("Регистрация лога");
          if (log != null) {
            ProviderRegistry.this.logService = log;
          }
        }
      }
    });
  }

  public void removeLogService(final LogService log) {
    execute(new Runnable() {
      @Override
      public void run() {
        logger.info("Удаление лога");
        ProviderRegistry.this.logService = null;
      }
    });
  }

  public void addDefinitionParser(final ServiceDefinitionParser newServiceDefinitionParser) {
    eventDriver.submit(new Runnable() {
      @Override
      public void run() {
        serviceDefinitionParser = newServiceDefinitionParser;
        updateEntries();
      }
    });
  }

  public void removeDefinitionParser(final ServiceDefinitionParser oldServiceDefinitionParser) {
    execute(new Runnable() {
      @Override
      public void run() {
        serviceDefinitionParser = null;
        updateEntries();
      }
    });
  }

  public void addProtocolFactory(final ProtocolFactory newProtocolFactory) {
    eventDriver.submit(new Runnable() {
      @Override
      public void run() {
        protocolFactory = newProtocolFactory;
        updateEntries();
      }
    });
  }


  public void removeProtocolFactory(final ProtocolFactory oldProtocolFactory) {
    execute(new Runnable() {
      @Override
      public void run() {
        protocolFactory = null;
        updateEntries();
      }
    });
  }

  public void addDeclarant(final Declarant newDeclarant) {
    eventDriver.submit(new Runnable() {
      @Override
      public void run() {
        declarant = newDeclarant;
        updateEntries();
      }
    });
  }

  public void removeDeclarant(final Declarant oldDeclarant) {
    execute(new Runnable() {
      @Override
      public void run() {
        declarant = null;
        updateEntries();
      }
    });
  }


  @Override
  public void updateProviderNames(final Set<String> providerNames) {
    final Set<String> new_ = new HashSet<String>(providerNames);
    eventDriver.submit(new Runnable() {
      @Override
      public void run() {
        logger.info("Обновление реестра поставщиков: " + new_);
        registry.clear();
        registry.addAll(new_);

        final Set<String> old = new HashSet<String>(entries.keySet());

        final Set<String> removes = new HashSet<String>(old);
        removes.removeAll(new_);
        for (final String name : removes) {
          // удаляем лишь пустышки
          final ProviderEntry entry = entries.get(name);
          if (entry == EMPTY) {
            removeProvider(name);
          }
        }

        final Set<String> insertions = new HashSet<String>(new_);
        insertions.removeAll(old);
        for (final String name : insertions) {
          entries.put(name, EMPTY);
          final ServiceReference serviceReference = providerRefs.get(name);
          if (serviceReference != null) {
            addProvider(serviceReference, name);
          }
        }
      }
    });
  }

  @Override
  public ProviderEntry getProviderEntry(final String name) {
    final Future<ProviderEntry> f = eventDriver.submit(new Callable<ProviderEntry>() {
      @Override
      public ProviderEntry call() throws Exception {
        return entries.get(name);
      }
    });
    try {
      return f.get(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      logger.log(Level.INFO, "Прерывание", e);
      return null;
    } catch (ExecutionException e) {
      logger.log(Level.INFO, "Отказ", e.getCause());
      return null;
    } catch (TimeoutException e) {
      logger.log(Level.INFO, "Таймаут ожидания поставщика " + name);
      return null;
    }
  }

  @Override
  public void destroyPorts() {
    final Future<?> f = eventDriver.submit(new Runnable() {
      @Override
      public void run() {
        for (ProviderEntry entry : entries.values()) {
          disposeEndpoint(entry);
        }
      }
    });
    try {
      f.get();
    } catch (InterruptedException e) {
      logger.log(Level.INFO, "Перрывание", e);
    } catch (ExecutionException e) {
      logger.log(Level.INFO, "Отказ", e.getCause());
    }
  }

  @Override
  public Set<String> names() {
    final Future<Set<String>> f = eventDriver.submit(new Callable<Set<String>>() {
      @Override
      public Set<String> call() throws Exception {
        return entries.keySet();
      }
    });
    try {
      return f.get(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      logger.log(Level.INFO, "Прерывание", e);
      return Collections.emptySet();
    } catch (ExecutionException e) {
      logger.log(Level.INFO, "Отказ", e.getCause());
      return Collections.emptySet();
    } catch (TimeoutException e) {
      logger.log(Level.INFO, "Таймаут ожидания имён поставщиков");
      return Collections.emptySet();
    }
  }

  // --------------- internals --------------

  void addProvider(final ServiceReference serviceReference, final String name) {
    providerRefs.put(name, serviceReference);
    final ProviderEntry entry = entries.get(name);
    if (entry != null) {
      disposeEndpoint(entry);
      final ProviderEntry newEntry = new ProviderEntry();
      newEntry.name = name;
      newEntry.declarant = declarant;
      try {
        final Server provider = (Server) bundleContext.getService(serviceReference);
        if (provider != null) {
          newEntry.wsdl = provider.getWsdlUrl();
        }
      } catch (RuntimeException e) {
        logger.log(Level.WARNING, "fail getWsdlUrl for provider " + name, e);
      } finally {
        bundleContext.ungetService(serviceReference);
      }
      if (newEntry.wsdl != null) {
        updateEntry(newEntry);
        entries.put(name, newEntry);
      }
    }
  }

  void removeProvider(String name) {
    providerRefs.remove(name);
    final ProviderEntry entry = entries.get(name);
    if (entry != null) {
      disposeEndpoint(entry);
      if (registry.contains(name)) {
        // оставляем пустышку лишь если есть в реестре
        entries.put(name, EMPTY);
      } else {
        // поддержать саморегистрацию
        entries.remove(name);
      }
    }
  }

  String getInfo(ServiceReference serviceReference) {
    final StringBuilder sb = new StringBuilder();
    for (String key : serviceReference.getPropertyKeys()) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append(key);
      sb.append('=');
      Object value = serviceReference.getProperty(key);
      if (value instanceof String[]) {
        sb.append(Arrays.asList((String[]) value));
      } else {
        sb.append(value);
      }
    }
    return sb.toString();
  }

  /**
   * Обновить все параметры всех поставщиков.
   */
  void updateEntries() {
    for (final ProviderEntry entry : entries.values()) {
      if (entry != EMPTY) {
        updateEntry(entry);
      }
    }
  }

  /**
   * Обновить все параметры поставщика.
   */
  void updateEntry(final ProviderEntry entry) {
    disposeEndpoint(entry); // гаранировать применение новых свойств
    entry.declarant = declarant;
    updateWsDefinition(entry);
    updateProtocol(entry);
  }

  void updateWsDefinition(final ProviderEntry entry) {
    if (CLEAR_DEFINITION) {
      entry.wsService = null;
      entry.wsPort = null;
      entry.wsPortDef = null;
      entry.wsDef = null;
    }
    if (serviceDefinitionParser == null) {
      return;
    }
    final ServiceDefinition def;
    try {
      def = serviceDefinitionParser.parseServiceDefinition(entry.wsdl);
    } catch (RuntimeException e) {
      logger.log(Level.WARNING, "fail parseServiceDefinition for provider " + entry.name, e);
      return;
    }
    final QName serviceName;
    final ServiceDefinition.Service serviceDef;
    final QName portName;
    final ServiceDefinition.Port port;
    {
      if (def.services == null || def.services.size() != 1) {
        logger.log(Level.WARNING, "Invalid WSDL service definitions for " + entry.name + " : " + def.services);
        return;
      }
      final Map.Entry<QName, ServiceDefinition.Service> e = first(def.services);
      serviceDef = e.getValue();
      serviceName = e.getKey();
    }
    {
      if (serviceDef.ports == null || serviceDef.ports.size() != 1) {
        logger.log(Level.WARNING, "Invalid WSDL port definitions for " + entry.name + " : " + serviceDef.ports);
        return;
      }
      final Map.Entry<QName, ServiceDefinition.Port> e = first(serviceDef.ports);
      portName = e.getKey();
      port = e.getValue();
    }
    entry.wsDef = def;
    entry.wsService = serviceName;
    entry.wsPort = portName;
    entry.wsPortDef = port;
  }

  void updateProtocol(final ProviderEntry entry) {
    entry.protocol = createProtocol(entry.wsDef, entry.name);
  }

  ServerProtocol createProtocol(final ServiceDefinition def, String name) {
    if (def == null) {
      return null;
    }
    if (protocolFactory == null) {
      return null;
    }
    try {
      return protocolFactory.createServerProtocol(def);
    } catch (RuntimeException e) {
      logger.log(Level.WARNING, "fail createServerProtocol for " + name, e);
      return null;
    }
  }


  <K, V> Map.Entry<K, V> first(final Map<K, V> map) {
    return map.entrySet().iterator().next();
  }


  void disposeEndpoint(ProviderEntry entry) {
    if (entry != EMPTY) {
      if (entry.servletAdapter != null) {
        entry.servletAdapter.getEndpoint().dispose();
        entry.servletAdapter = null;
      }
    }
  }

  void execute(final Runnable runnable) {
    if (eventDriver.isShutdown()) {
      synchronized (this) {
        runnable.run();
      }
    } else {
      eventDriver.submit(runnable);
    }
  }

  @Override
  public LogService get() {
    if (logService == null) {
      return new LogServiceFake(); // сделать синглтон
    }
    return logService;
  }

}

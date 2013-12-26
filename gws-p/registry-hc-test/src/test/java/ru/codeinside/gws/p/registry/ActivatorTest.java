/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.registry;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import ru.codeinside.gws.p.registry.api.ProviderRegistry;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import org.junit.Ignore;

/*@RunWith(PaxExam.class)
public class ActivatorTest extends Assert {

  @Configuration
  public Option[] config() {
    return options(
      mavenBundle("org.apache.felix", "org.apache.felix.scr", "1.6.2"),
      mavenBundle("ru.codeinside", "gws-p-registry-api", "1.0.1"),
      mavenBundle("ru.codeinside", "gws-p-registry-hc", "1.0.1"),
      junitBundles()
    );
  }

  @Inject
  BundleContext bundleContext;

  @Test
  public void testInjecting() throws InterruptedException {
    final AtomicReference<Set<String>> names = new AtomicReference<Set<String>>();
    final ProviderRegistry registry = new ProviderRegistry() {
      @Override
      public void updateProviderNames(Set<String> providerNames) {
        names.set(providerNames);
      }
    };
    ServiceRegistration<ProviderRegistry> reg = bundleContext.registerService(ProviderRegistry.class, registry, null);
    reg.unregister();

    final Set<String> actual = new LinkedHashSet<String>(names.get());
    final Set<String> expected = new LinkedHashSet<String>(Arrays.asList("mvvact", "finance-women", "finance-veteran", "finance-pensioner"));
    assertEquals(expected, actual);
  }
}*/

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.registry.hc;

import ru.codeinside.gws.p.registry.api.ProviderRegistry;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

final public class Registrar {
  final static public Set<String> providers = Collections.unmodifiableSet(
    new LinkedHashSet<String>(
      Arrays.asList(
        "mvvact",
        "finance-women",
        "finance-veteran",
        "finance-pensioner"
      )
    )
  );

  public void add(final ProviderRegistry providerRegistry) {
    providerRegistry.updateProviderNames(providers);
  }

}

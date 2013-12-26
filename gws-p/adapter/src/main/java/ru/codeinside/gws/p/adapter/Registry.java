/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.adapter;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Реестр услуг.
 */
public interface Registry {

  AtomicReference<Registry> REGISTRY = new AtomicReference<Registry>();

  void destroyPorts();

  ProviderEntry getProviderEntry(String name);

  Set<String> names();
}

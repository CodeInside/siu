/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.util.Set;

public interface DeclarerContext {

  Set<String> getPropertyNames();

  boolean isRequired(String propertyName);

  boolean isEnclosure(String propertyName);

  String getType(String propertyName);

  void setValue(String propertyName, Object value);

  void addEnclosure(String propertyName, Enclosure enclosure);

  String declare();

  String declare(String tag);

  Object getVariable(String name);
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.migrations;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Set;

final public class Databases {

  final public static String ADMIN = "jdbc/adminka";
  final public static String TMP = "jdbc/gses_tmp";
  final public static Set<String> UNITS = ImmutableSet.of("myPU", "logPU");
  final public static Map<String, String> VERSIONS = ImmutableMap.of(ADMIN, Version.DB_VERSION);
  final public static String[] ALL = {ADMIN};

  private Databases() {
  }
}

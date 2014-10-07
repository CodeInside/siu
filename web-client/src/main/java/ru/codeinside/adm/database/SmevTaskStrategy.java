/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import commons.Named;

import java.io.Serializable;

public enum SmevTaskStrategy implements Named, Serializable {

  REQUEST("запрос"),

  PING("опрос");


  final public String name;

  SmevTaskStrategy(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}

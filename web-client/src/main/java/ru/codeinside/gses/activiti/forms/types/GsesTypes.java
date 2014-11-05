/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.types;

public enum GsesTypes {

  STRING("string"),
  MULTILINE("multiline"),
  LONG("long"),
  DATE("date"),
  MASKED("masked"),
  BOOLEAN("boolean"),
  ENUM("enum"),
  DICTIONARY("directory"),
  ATTACHMENT("attachment"),
  SIGNATURE("signature"),
  SMEV_REQUEST("smevRequest"),
  ENCLOSURE("enclosure"),
  SMEV_REQUEST_ENCLOSURE("smevRequestEnclosure"),
  SMEV_RESPONSE_ENCLOSURE("smevResponseEnclosure"),
  JSON("json"),
  REQUEST("request");

  public final String name;

  GsesTypes(String name) {
    this.name = name;
  }
}

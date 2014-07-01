/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.types;

import ru.codeinside.adm.database.ClientRequestEntity;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.forms.Signatures;

import java.util.Date;

public enum GsesTypes {

  STRING("string", String.class),
  MULTI_LINE("multiline", String.class),
  LONG("long", Long.class),
  DATE("date", Date.class),
  MASKED("masked", String.class),
  BOOLEAN("boolean", boolean.class),
  ENUM("enum", String.class),
  DICTIONARY("directory", String.class),
  ATTACHMENT("attachment", FileValue.class),
  SIGNATURE("signature", Signatures.class),
  SMEV_REQUEST("smevRequest", ClientRequestEntity.class),
  ENCLOSURE("enclosure", FileValue.class),
  SMEV_REQUEST_ENCLOSURE("smevRequestEnclosure", String.class),
  SMEV_RESPONSE_ENCLOSURE("smevResponseEnclosure", String.class),
  JSON("json", String.class);

  public final String name;
  public final Class type;

  GsesTypes(String name, Class type) {
    this.name = name;
    this.type = type;
  }
}

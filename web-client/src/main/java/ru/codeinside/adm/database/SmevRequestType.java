/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import commons.Named;
import ru.codeinside.gws.api.Packet;

import java.io.Serializable;

public enum SmevRequestType implements Named, Serializable {

  REQUEST("запрос"),

  PING("опрос"),

  CANCEL("отзыв заявления");


  final public String name;

  SmevRequestType(String name) {
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

  public static SmevRequestType fromStatus(Packet.Status status) {
    if (status == null) {
      throw new IllegalStateException("Отсуствует тип запроса СМЭВ");
    }
    for (SmevRequestType type : values()) {
      if (status.name().equals(type.name())) {
        return type;
      }
    }
    throw new IllegalStateException("Неизвестный тип запроса СМЭВ {" + status + "}");
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

public enum Role {

  Administrator("Администратор"),

  Manager("Менеджер"),

  Executor("Исполнитель"),

  Supervisor("Контролер"),

  SuperSupervisor("Общий контролер"),

  Declarant("Заявитель");

  final public String description;

  Role(String description) {
    this.description = description;
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import commons.Named;

import java.io.Serializable;

public enum SmevStage implements Named, Serializable {

  ENTER("Вход в блок"),
  REQUEST_PREPARE("Подготовка запроса"),
  REQUEST("Создание данных для передачи"),
  LOG("Создание журнала"),
  NETWORK("Передача и прём данных"),
  NETWORK_ERROR("Обработка ошибок передачи данных"),
  RESPONSE("Обработка принятых данных");


  final public String name;

  SmevStage(String name) {
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

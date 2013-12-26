/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.history;

import org.activiti.engine.impl.persistence.entity.HistoricVariableUpdateEntity;

/**
 * Переменная с идентификатром свойства.
 */
final public class VarPath {

  final String path;
  final HistoricVariableUpdateEntity var;

  public VarPath(final String path, final HistoricVariableUpdateEntity var) {
    this.path = path;
    this.var = var;
  }
}

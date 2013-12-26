/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import org.activiti.engine.impl.form.FormHandler;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

final public class FullFormHandler {

  final public FormHandler formHandler;
  final public ExecutionEntity executionEntity;

  public FullFormHandler(final FormHandler formHandler, final ExecutionEntity executionEntity) {
    this.formHandler = formHandler;
    this.executionEntity = executionEntity;
  }
}

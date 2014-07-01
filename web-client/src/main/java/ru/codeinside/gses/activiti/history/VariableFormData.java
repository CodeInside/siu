/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.history;

import org.activiti.engine.form.FormData;

import java.util.Map;

final public class VariableFormData {
  final public FormData formData;
  final public Map<String, VariableSnapshot> variables;

  public VariableFormData(FormData formData, Map<String, VariableSnapshot> variables) {
    this.formData = formData;
    this.variables = variables;
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import org.activiti.engine.form.FormProperty;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.history.VariableFormData;

import java.io.Serializable;
import java.util.Map;

/**
 * Какая то обёртка.
 * <p/>
 * Ответственность размыта и не понятная.
 */
@Deprecated
final public class FormDecorator implements Serializable {

  private static final long serialVersionUID = 1L;

  final public FormID id;
  final public VariableFormData variableFormData;

  public FormDecorator(FormID id, VariableFormData variableFormData) {
    this.id = id;
    this.variableFormData = variableFormData;
  }

  // Для хранения в UI (без связи с переменными)
  public FormDecorator toSimple() {
    return new FormDecorator(id, null);
  }

  public Map<String, FormProperty> getGeneral() {
    throw new UnsupportedOperationException();
  }



}

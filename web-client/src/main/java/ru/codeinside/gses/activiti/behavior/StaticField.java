/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import org.activiti.engine.delegate.VariableScope;

final public class StaticField<T> implements Field<T> {

  final T value;

  public StaticField(T value) {
    this.value = value;
  }

  public static <T> StaticField<T> of(T value) {
    return new StaticField<T>(value);
  }

  @Override
  public T getValue(VariableScope scope) {
    return value;
  }
}

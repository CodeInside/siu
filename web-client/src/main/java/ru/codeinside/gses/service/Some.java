/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.service;

public class Some<T> {

  final boolean present;
  final T value;

  private Some(boolean present, T value) {
    this.present = present;
    this.value = value;
  }

  public static <T> Some<T> empty() {
    return new Some<T>(false, null);
  }

  public static <T> Some<T> of(T value) {
    return new Some<T>(true, value);
  }

  public boolean isPresent() {
    return present;
  }

  public T get() {
    if (!present) {
      throw new IllegalStateException();
    }
    return value;
  }
}

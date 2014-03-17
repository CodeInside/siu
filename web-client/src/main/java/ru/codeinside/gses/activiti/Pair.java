/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;


public class Pair<K, V> {

  private K _1;
  private V _2;

  public Pair(K _1, V _2) {
    this._1 = _1;
    this._2 = _2;
  }

  public static <K, V> Pair<K, V> of(K k, V v) {
    return new Pair<K, V>(k, v);
  }

  public K get_1() {
    return _1;
  }

  public void set_1(K _1) {
    this._1 = _1;
  }

  public V get_2() {
    return _2;
  }

  public void set_2(V _2) {
    this._2 = _2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Pair pair = (Pair) o;

    if (_1 != null ? !_1.equals(pair._1) : pair._1 != null) return false;
    if (_2 != null ? !_2.equals(pair._2) : pair._2 != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = _1 != null ? _1.hashCode() : 0;
    result = 31 * result + (_2 != null ? _2.hashCode() : 0);
    return result;
  }
}

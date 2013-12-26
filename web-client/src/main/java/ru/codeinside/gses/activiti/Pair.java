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

  public Pair (K _1, V _2){
    this._1 = _1;
    this._2 = _2;
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
}

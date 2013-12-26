/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import java.io.Serializable;

final public class SmevRequestValue implements Serializable {
  final public String label;
  final public long id;

  public SmevRequestValue(String label, long id) {
    this.label = label;
    this.id = id;
  }

  @Override
  public String toString() {
    return label;
  }
}

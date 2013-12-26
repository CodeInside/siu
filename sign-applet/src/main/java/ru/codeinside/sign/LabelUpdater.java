/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

import java.awt.Label;

final class LabelUpdater implements Runnable {
  private final Label label;
  private final String type;


  public LabelUpdater(Label label, String type) {
    this.label = label;
    this.type = type;
  }

  @Override
  public void run() {
    label.setText(type);
  }
}

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.eventbus;

import java.io.IOException;
import java.io.OutputStream;

final class CountOutputStream extends OutputStream {

  private int count;

  @Override
  public void write(int b) throws IOException {
    count++;
  }

  public int getByteCount() {
    return count;
  }
}

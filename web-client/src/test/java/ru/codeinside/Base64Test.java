/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside;

import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gses.webui.executor.ArchiveFactory;

public class Base64Test {

  @Test
  public void padding() {
    byte[] bytes = new byte[1024];
    String s = ArchiveFactory.toBase64HumanString(bytes);
    // 21 ряд плюс хвостик
    Assert.assertEquals(65 * 21 + 24, s.length());
  }
}

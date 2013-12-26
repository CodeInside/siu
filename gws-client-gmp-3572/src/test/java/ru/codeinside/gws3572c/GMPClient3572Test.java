/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3572c;


import junit.framework.Assert;
import org.junit.Test;

public class GMPClient3572Test {
  @Test
  public void testPadString() throws Exception {
    GMPClient3572 client = new GMPClient3572();
    Assert.assertEquals("0000100", client.leftPad("100", 7, '0'));
    Assert.assertEquals("100", client.leftPad("100", 3, '0'));
    Assert.assertEquals("1000", client.leftPad("1000", 3, '0'));
  }
}
